package hash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class RemoveHelperTests {

	final static Status E = Status.EMPTY;
	final static Status V = Status.VALID;
	final static Status D = Status.DELETED;
	final static Integer N = null;

	<T> void removeElementFromNodeTestHelper(ClosedHashTable<T> table, int index) {
		// Get the number of elements before removal.
		int currentElementNumber = table.getElementNumber();

		// Get the node at the specified index.
		HashNode<T> node = table.associativeArray[index];
		T originalElement = node.getElement();

		// Remove the element from the node.
		// This is the method we really want to test here.
		table.removeElementFromNode(node);

		// Verify that the node has been updated correctly.
		// The element should remain the same, but status should be DELETED.
		String message = "Element " + originalElement + " at index " + index + " should remain after removal.";
		assertEquals(originalElement, table.associativeArray[index].getElement(), message);

		message = "Status at index " + index + " should be DELETED after removal of element " + originalElement + ".";
		assertEquals(Status.DELETED, table.associativeArray[index].getStatus(), message);

		message = "Element number should decrease from " + currentElementNumber + " to " + (currentElementNumber - 1)
				+ " after removal of element " + originalElement + " at index " + index + ".";
		assertEquals(currentElementNumber - 1, table.getElementNumber(), message);
	}

	@Test
	void removeElementFromNodeTest() {
		// Tests the helper method that removes an element from a node.
		// This method is not public, but used internally when removing elements.
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		Status[] statuses = { E, V, V, V, E, };
		Integer[] elements = { N, 10, 20, 30, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		// Test removing elements from various nodes.
		removeElementFromNodeTestHelper(table, 1);
		removeElementFromNodeTestHelper(table, 2);
		assertEquals(1, table.getElementNumber());

		// Test null node input.
		assertThrows(NullPointerException.class, () -> table.removeElementFromNode(null));

		// Test removing already removed element.
		HashNode<Integer> node1 = table.associativeArray[1];
		assertThrows(IllegalArgumentException.class, () -> table.removeElementFromNode(node1));

		// Test removing from an EMPTY table.
		removeElementFromNodeTestHelper(table, 3);
		// Because the table is empty, the state exception should be thrown before
		// any other exception.
		assertThrows(IllegalStateException.class, () -> table.removeElementFromNode(node1));
		assertThrows(IllegalStateException.class, () -> table.removeElementFromNode(null));

	}

}
