package hash;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FindAvailableNodeForTests {

	final static Status E = Status.EMPTY;
	final static Status V = Status.VALID;
	final static Status D = Status.DELETED;
	final static Integer N = null;

	@Test
	void nullElementTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Test null element input.
		assertThrows(NullPointerException.class, () -> table.findAvailableNodeFor(null));
	}

	@Test
	void fullTableTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Artificially fill the table (0 to 4, same indexes and values).
		Status[] statuses = { V, V, V, V, V };
		Integer[] elements = { 0, 1, 2, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Now the table is full.

		// Should return null since there are no available nodes.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(10);
		assertNull(availableNode);

	}

	@Test
	void noCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// There are no collisions since the table is empty.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(2);
		assertTrue(availableNode == table.associativeArray[2]);

		// Different value but with same hash index as before.
		availableNode = table.findAvailableNodeFor(7);
		assertTrue(availableNode == table.associativeArray[2]);

		// Populate the table artificially.
		Status[] statuses = { E, E, V, V, E };
		Integer[] elements = { N, N, 2, 3, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// The returned node should be at index 4 now.
		// It first will collide with indexes 2 and 3.
		availableNode = table.findAvailableNodeFor(9);
		assertTrue(availableNode == table.associativeArray[4]);
	}

	@Test
	void collisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { E, E, V, V, E };
		Integer[] elements = { N, N, 2, 3, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// It should insert at index 4 after colliding with indexes 2 and 3.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(7);
		assertTrue(availableNode == table.associativeArray[4]);
	}

	@Test
	void existingElementTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { E, V, V, V, E };
		Integer[] elements = { N, 1, 2, 6, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// It should return null since the element already exists in the table.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(2);
		assertNull(availableNode);

		// It should return null since the element already exists in the table.
		// It should collide first with indexes 1 and 2.
		availableNode = table.findAvailableNodeFor(6);
		assertNull(availableNode);
	}

	@Test
	void deletedNodeNoCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { V, D, V, D, E };
		Integer[] elements = { 0, 1, 2, 3, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// It should return the DELETED node at index 1.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(6);
		assertTrue(availableNode == table.associativeArray[1]);

		// It should return the DELETED node at index 3.
		availableNode = table.findAvailableNodeFor(8);
		assertTrue(availableNode == table.associativeArray[3]);
	}

	@Test
	void deletedNodeWithCollisionsTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		// Some DELETED nodes exist.
		Status[] statuses = { V, D, V, D, E };
		Integer[] elements = { 0, 1, 6, 3, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Try to find an available node for a new element that hashes to index 0.
		// It should colide with index 0 first.
		// Then, it should find the first DELETED node at index 1.
		// Then, it should check element isn't there by colliding until reaching EMPTY.
		// It should return the first DELETED node at index 1.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(5);
		assertTrue(availableNode == table.associativeArray[1]);

		// Try to find an available node for element 6, which already exists.
		// First, it will collide with index 1, finding the first DELETED.
		// Then, it will collide with index 2, finding the existing element.
		// It should return null.
		availableNode = table.findAvailableNodeFor(6);
		assertNull(availableNode);
	}

	@Test
	void noEmptyNodesTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { V, D, V, D, V };
		Integer[] elements = { 0, 1, 2, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Try to find an available node for a new element that hashes to index 0.
		// It should find the first DELETED node at index 1.
		// Then it should collide with all other nodes.
		// Then it should collide a number of times equal to the capacity of the table.
		// After that, it should return the first DELETED node found.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(5);
		assertTrue(availableNode == table.associativeArray[1]);
	}

	@Test
	void wrapAroundTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Populate the table artificially.
		Status[] statuses = { V, V, E, E, E };
		Integer[] elements = { 0, 1, N, N, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Try to find an available node for an element that hashes to index 1.
		// It should wrap around and return the node at index 2.
		HashNode<Integer> availableNode = table.findAvailableNodeFor(6);
		assertTrue(availableNode == table.associativeArray[2]);
	}

	@Test
	void quadraticInfiniteLoopTest() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.QUADRATIC_PROBING);

		// Populate the table artificially.
		Status[] statuses = { V, V, V, E, E };
		Integer[] elements = { 0, 1, 2, N, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Quadratic probing does not guarantee that all indexes can be reached.
		// Trying to find an available node for 6 in the current state, will make the
		// following collisions:
		// 0:1, 1:2, 2:0, 3:0, 4:2, 5:0, 6:1, 7:2, ... and so on.
		/*
		 * It should stop after making collisions equal to the capacity of the table. If
		 * it didn't find an available node by then, it just can't find a valid node.
		 */
		HashNode<Integer> availableNode = table.findAvailableNodeFor(6);
		assertNull(availableNode);

		// Populate the table artificially.
		statuses = new Status[] { D, V, V, E, E };
		elements = new Integer[] { 0, 1, 2, N, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// In this case, it should find the DELETED node at index 0.
		// After that, it will try to check if 6 exists elsewhere.
		// It will enter a loop, but it should exit after colliding more than the size.
		// After exiting, should return the first DELETED node found.
		availableNode = table.findAvailableNodeFor(6);
		assertTrue(availableNode == table.associativeArray[0]);
	}

}
