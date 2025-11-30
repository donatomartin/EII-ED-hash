package hash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class AddHelperTests {

	final static Status E = Status.EMPTY;
	final static Status V = Status.VALID;
	final static Status D = Status.DELETED;
	final static Integer N = null;

	<T> void addElementToNodeTestHelper(ClosedHashTable<T> table, int index, T element) {
		// Get the number of elements before addition.
		int currentElementNumber = table.getElementNumber();

		// Get the node at the specified index.
		HashNode<T> node = table.associativeArray[index];

		// Add the element to the node.
		// This is the method we really want to test here.
		table.addElementToNode(node, element);

		// Verify that the node has been updated correctly.
		String message = "Element " + element + " at index " + index + " should be updated.";
		assertEquals(element, table.associativeArray[index].getElement(), message);

		message = "Status at index " + index + " should be VALID after addition of element " + element + ".";
		assertEquals(Status.VALID, table.associativeArray[index].getStatus(), message);

		message = "Element number should increase from " + currentElementNumber + " to " + (currentElementNumber + 1)
				+ " after addition of " + element + " at index " + index + ".";
		assertEquals(currentElementNumber + 1, table.getElementNumber(), message);
	}

	@Test
	void addElementToNodeTest() {
		// Tests the helper method that adds an element to a node.
		// This method is not public, but used internally when adding elements.
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// Get a node and check its initial state.
		assertEquals(0, table.getElementNumber());

		// Test adding elements to various nodes.
		addElementToNodeTestHelper(table, 1, 111);
		addElementToNodeTestHelper(table, 2, 222);
		addElementToNodeTestHelper(table, 4, 444);

		// We artificially set a node as DELETED.
		TestHelper.setAsDeletedHelper(table, 3, 333);

		// Now we add an element to that DELETED node.
		addElementToNodeTestHelper(table, 3, 333);

		// Now test error conditions for addElementToNode.

		// Test null node input.
		assertThrows(NullPointerException.class, () -> table.addElementToNode(null, 123));
		// Test null element input.
		HashNode<Integer> node1 = table.associativeArray[1];
		assertThrows(NullPointerException.class, () -> table.addElementToNode(node1, null));
		// Test adding an existing element.
		assertThrows(IllegalArgumentException.class, () -> table.addElementToNode(node1, 111));

		// Fill the last spot.
		addElementToNodeTestHelper(table, 0, 0);
		// Now it's full, so adding should throw an exception.
		// Because the table is full, the state exception should be thrown before
		// any other exception
		assertThrows(IllegalStateException.class, () -> table.addElementToNode(node1, 111));
		assertThrows(IllegalStateException.class, () -> table.addElementToNode(null, 999));
		assertThrows(IllegalStateException.class, () -> table.addElementToNode(node1, null));
	}

}
