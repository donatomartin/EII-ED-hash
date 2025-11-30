package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BasicSearchTest {

	static final Status E = Status.EMPTY;
	static final Status V = Status.VALID;
	static final Status D = Status.DELETED;
	static final Integer N = null;

	@Test
	void nullTableTest() {
		HashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Test null element.
		assertThrows(NullPointerException.class, () -> table.search(null));
	}

	@Test
	void emptyTableTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// The table is empty.

		// Should return false since there are no elements.
		assertFalse(table.search(10));
	}

	@Test
	void fullTableNoCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table (0 to 4, same indexes and values).
		Status[] statuses = { V, V, V, V, V };
		Integer[] elements = { 0, 1, 2, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table is full.

		// Test existing elements.
		assertTrue(table.search(0));
		assertTrue(table.search(1));
		assertTrue(table.search(2));
		assertTrue(table.search(3));
		assertTrue(table.search(4));

		// Test non-existing element (should return false).
		assertFalse(table.search(5));
	}

	@Test
	void deletedTableNoCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table with deleted elements.
		Status[] statuses = { D, D, D, D, D };
		Integer[] elements = { 0, 1, 2, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table has only deleted elements.

		// Test existing elements in deleted state.
		assertFalse(table.search(0));
		assertFalse(table.search(1));
		assertFalse(table.search(2));
		assertFalse(table.search(3));
		assertFalse(table.search(4));

		// Test non-existing element (should return false).
		assertFalse(table.search(5));
	}

	@Test
	void validNodesCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table with collisions.
		Status[] statuses = { E, V, V, V, V };
		Integer[] elements = { N, 6, 11, 16, 21 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table has collisions.

		// Test existing elements.
		// O collisions
		assertTrue(table.search(6));
		// Collision with index 1
		assertTrue(table.search(11));
		// Collision with indexes 1 and 2
		assertTrue(table.search(16));
		// Collision with indexes 1, 2 and 3
		assertTrue(table.search(21));

		// Test non-existing element (should return false).
		// Collision with indexes 1, 2, 3 and 4
		assertFalse(table.search(26));
	}

	@Test
	void deletedNodesCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table with deleted nodes and collisions.
		Status[] statuses = { E, D, D, V, D };
		Integer[] elements = { N, 6, 11, 16, 21 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table has deleted nodes with collisions.

		// Test existing elements in deleted state.
		// O collisions
		assertFalse(table.search(6));
		// Collision with index 1
		assertFalse(table.search(11));
		// Collision with indexes 1, 2 and 3
		assertFalse(table.search(21));

		// Test non-existing element (should return false).
		// Collision with indexes 1, 2, 3 and 4
		assertFalse(table.search(26));
	}

	@Test
	void wrapAroundTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table to force wrap-around.
		Status[] statuses = { V, E, E, V, V };
		Integer[] elements = { 18, N, N, 8, 13 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table has wrap-around scenarios.

		// Test existing elements.
		assertTrue(table.search(8));
		assertTrue(table.search(13));
		assertTrue(table.search(18));

		// Test non-existing element that requires wrap-around (should return false).
		assertFalse(table.search(5));
	}

	@Test
	void quadraticInfiniteLoopTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.QUADRATIC_PROBING);

		// Artificially fill the table to create a scenario that could cause infinite
		// loops.
		Status[] statuses = { V, V, V, E, E };
		Integer[] elements = { 0, 1, 2, N, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Quadratic probing does not guarantee that all indexes can be reached.
		// Trying to find 6 in the current state, will make the following collisions:
		// 0:1, 1:2, 2:0, 3:0, 4:2, 5:0, 6:1, 7:2, ... and so on.
		/*
		 * It should stop after making collisions equal to the capacity of the table. If
		 * it didn't find it by then, it does not exist in the table (it couldn't have
		 * been inserted either).
		 */

		// Test non-existing element (should return false without infinite loop).
		assertFalse(table.search(6));
	}
}
