package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BasicRemoveTests {

	static final Status E = Status.EMPTY;
	static final Status V = Status.VALID;
	static final Status D = Status.DELETED;
	static final Integer N = null;

	@Test
	void removeFromEmptyTableTest() {
		HashTable<String> table = new ClosedHashTable<String>(5, HashStrategy.LINEAR_PROBING);

		// Test null element removal.
		assertThrows(IllegalStateException.class, () -> table.remove("Something"));
	}

	@Test
	void removeNullElementTest() {
		ClosedHashTable<String> table = new ClosedHashTable<String>(5, HashStrategy.LINEAR_PROBING);

		// Artificially set an element as valid to avoid empty table scenario.
		table.elementNumber = 1;

		// Test null element removal.
		assertThrows(NullPointerException.class, () -> table.remove(null));
	}

	@Test
	void removeElementCountTest() {
		// No collisions, just adding elements until full.
		ClosedHashTable<String> table = new ClosedHashTable<String>(5, HashStrategy.LINEAR_PROBING);

		Status[] statuses = { V, V, V, V, V };
		String[] elements = { "apple", "banana", "cherry", "date", "elderberry" };
		TestHelper.setHashTableNodes(table, statuses, elements);
		assertEquals(5, table.getElementNumber());

		// Add elements and verify they are added correctly.
		assertTrue(table.remove("apple"));
		assertEquals(4, table.getElementNumber());

		assertTrue(table.remove("banana"));
		assertEquals(3, table.getElementNumber());

		assertTrue(table.remove("cherry"));
		assertEquals(2, table.getElementNumber());

		assertTrue(table.remove("date"));
		assertEquals(1, table.getElementNumber());

		assertTrue(table.remove("elderberry"));
		assertEquals(0, table.getElementNumber());

		// If we try to add another element, it should throw an exception.
		// The number of collitions will exceed capacity.
		assertThrows(IllegalStateException.class, () -> table.remove("orange"));
	}

	@Test
	void removeLinearCollisionsTest() {
		// No collisions, just adding elements until full.
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);

		// All elements have initial hash 1, causing collisions.
		Status[] statuses = { V, V, V, V, V };
		Integer[] elements = new Integer[] { 16, 21, 1, 6, 11 };
		TestHelper.setHashTableNodes(table, statuses, elements);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(5, table.getElementNumber());

		// Remove elements and verify they are removed correctly.
		assertTrue(table.remove(6));
		statuses = new Status[] { V, V, V, D, V };
		elements = new Integer[] { 16, 21, 1, 6, 11 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		assertTrue(table.remove(21));
		statuses = new Status[] { V, D, V, D, V };
		elements = new Integer[] { 16, 21, 1, 6, 11 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		assertTrue(table.remove(16));
		statuses = new Status[] { D, D, V, D, V };
		elements = new Integer[] { 16, 21, 1, 6, 11 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove non-existing element
		assertFalse(table.remove(100));
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove already deleted element
		assertFalse(table.remove(6));
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove remaining elements
		assertTrue(table.remove(1));
		statuses = new Status[] { D, D, D, D, V };
		elements = new Integer[] { 16, 21, 1, 6, 11 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(1, table.getElementNumber());

		assertTrue(table.remove(11));
		statuses = new Status[] { D, D, D, D, D };
		elements = new Integer[] { 16, 21, 1, 6, 11 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(0, table.getElementNumber());

		// Since the table is empty, removing any element should throw an exception.
		assertThrows(IllegalStateException.class, () -> table.remove(50));

	}

	@Test
	void removeQuadraticCollitionsTest() {
		// No collisions, just adding elements until full.
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.QUADRATIC_PROBING);

		Status[] statuses = new Status[] { V, V, V, V, V };
		Integer[] elements = new Integer[] { 11, 1, 6, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(5, table.getElementNumber());

		// Remove elements and verify they are removed correctly.
		assertTrue(table.remove(6));
		statuses = new Status[] { V, V, D, V, V };
		elements = new Integer[] { 11, 1, 6, 3, 4 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		assertTrue(table.remove(1));
		statuses = new Status[] { V, D, D, V, V };
		elements = new Integer[] { 11, 1, 6, 3, 4 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		assertTrue(table.remove(11));
		statuses = new Status[] { D, D, D, V, V };
		elements = new Integer[] { 11, 1, 6, 3, 4 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove non-existing element
		assertFalse(table.remove(100));
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove already deleted element
		assertFalse(table.remove(6));
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		assertTrue(table.remove(3));
		statuses = new Status[] { D, D, D, D, V };
		elements = new Integer[] { 11, 1, 6, 3, 4 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(1, table.getElementNumber());

		assertTrue(table.remove(4));
		statuses = new Status[] { D, D, D, D, D };
		elements = new Integer[] { 11, 1, 6, 3, 4 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(0, table.getElementNumber());

		// Since the table is empty, removing any element should throw an exception.
		assertThrows(IllegalStateException.class, () -> table.remove(50));

	}

	@Test
	void addDoubleHashingTest() {
		// No collisions, just adding elements until full.
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(7, HashStrategy.DOUBLE_HASHING);

		Status[] statuses = new Status[] { V, V, V, V, V, V, V };
		Integer[] elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		TestHelper.setHashTableNodes(table, statuses, elements);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(7, table.getElementNumber());

		// Remove elements and verify they are removed correctly.
		assertTrue(table.remove(10));
		statuses = new Status[] { V, V, V, D, V, V, V };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(6, table.getElementNumber());

		assertTrue(table.remove(17));
		statuses = new Status[] { V, V, V, D, V, V, D };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(5, table.getElementNumber());

		assertTrue(table.remove(24));
		statuses = new Status[] { V, V, V, D, D, V, D };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		assertTrue(table.remove(31));
		statuses = new Status[] { D, V, V, D, D, V, D };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());
		assertTrue(table.remove(45));

		statuses = new Status[] { D, D, V, D, D, V, D };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove non-existing element
		assertFalse(table.remove(100));
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove already deleted element
		assertFalse(table.remove(10));
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		// Remove remaining elements
		assertTrue(table.remove(52));
		statuses = new Status[] { D, D, D, D, D, V, D };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(1, table.getElementNumber());

		assertTrue(table.remove(38));
		statuses = new Status[] { D, D, D, D, D, D, D };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(0, table.getElementNumber());

		// Since the table is empty, removing any element should throw an exception.
		assertThrows(IllegalStateException.class, () -> table.remove(50));

	}

}
