package hash;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FindMatchingNodeTests {

	final static Status E = Status.EMPTY;
	final static Status V = Status.VALID;
	final static Status D = Status.DELETED;
	final static Integer N = null;

	@Test
	void nullElementTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Test null element input.
		assertThrows(NullPointerException.class, () -> table.findMatchingNode(null));
	}

	@Test
	void emptyTableTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// The table is empty.

		// Should return null since there are no nodes.
		HashNode<Integer> node = table.findMatchingNode(10);
		assertNull(node);
	}

	@Test
	void fullTableTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table (0 to 4, same indexes and values).
		Status[] statuses = { V, V, V, V, V };
		Integer[] elements = { 0, 1, 2, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table is full.

		// Test existing elements.
		HashNode<Integer> node = table.findMatchingNode(3);
		assertTrue(node == table.associativeArray[3]);

		node = table.findMatchingNode(0);
		assertTrue(node == table.associativeArray[0]);

		node = table.findMatchingNode(4);
		assertTrue(node == table.associativeArray[4]);

		// Test non-existing element (should return null).
		node = table.findMatchingNode(10);
		assertNull(node);

	}

	@Test
	void deletedTableTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table with DELETED nodes.
		Status[] statuses = { D, D, D, D, D };
		Integer[] elements = { 0, 1, 2, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table is full of DELETED nodes.

		// Test existing elements (should return the DELETED nodes).
		HashNode<Integer> node = table.findMatchingNode(3);
		assertTrue(node == table.associativeArray[3]);

		node = table.findMatchingNode(0);
		assertTrue(node == table.associativeArray[0]);

		node = table.findMatchingNode(4);
		assertTrue(node == table.associativeArray[4]);

		// Test non-existing element (should return null).
		node = table.findMatchingNode(10);
		assertNull(node);

	}

	@Test
	void validNodesNoCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { E, V, V, V, E };
		Integer[] elements = { N, 1, 2, 3, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Each node should be found directly with no collisions.
		HashNode<Integer> availableNode = table.findMatchingNode(1);
		assertTrue(availableNode == table.associativeArray[1]);

		availableNode = table.findMatchingNode(2);
		assertTrue(availableNode == table.associativeArray[2]);

		availableNode = table.findMatchingNode(3);
		assertTrue(availableNode == table.associativeArray[3]);
	}

	@Test
	void validNodesCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { E, V, V, V, V };
		Integer[] elements = { N, 1, 6, 11, 8 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// The returned methods are found after collisions.

		// It first will collide with index 1.
		HashNode<Integer> availableNode = table.findMatchingNode(6);
		assertTrue(availableNode == table.associativeArray[2]);

		// It first will collide with indexes 1 and 2.
		availableNode = table.findMatchingNode(11);
		assertTrue(availableNode == table.associativeArray[3]);

		// It first will collide with index 3.
		availableNode = table.findMatchingNode(8);
		assertTrue(availableNode == table.associativeArray[4]);
	}

	@Test
	void deletedNodesTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { E, V, D, D, V };
		Integer[] elements = { N, 6, 11, 16, 21 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// The method returns any matching node, regardless of status.

		// Test deleted elements.
		HashNode<Integer> node = table.findMatchingNode(11);
		assertTrue(node == table.associativeArray[2]);

		node = table.findMatchingNode(16);
		assertTrue(node == table.associativeArray[3]);

		// Test non-existing element (should return null).
		node = table.findMatchingNode(0);
		assertNull(node);
	}

	@Test
	void wrapAroundTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { V, V, E, E, V };
		Integer[] elements = { 9, 14, N, N, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Try to find the matching node for an element that hashes to index 4.
		// It should wrap around and return the node at index 1.
		HashNode<Integer> matchingNode = table.findMatchingNode(14);
		assertTrue(matchingNode == table.associativeArray[1]);
	}

	@Test
	void quadraticInfiniteLoopTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.QUADRATIC_PROBING);

		// Populate the table artificially.
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
		HashNode<Integer> matchingNode = table.findMatchingNode(6);
		assertNull(matchingNode);
	}

}
