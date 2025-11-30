package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BasicAddTests {

	static final Status E = Status.EMPTY;
	static final Status V = Status.VALID;
	static final Status D = Status.DELETED;
	static final Integer N = null;

	@Test
	void addNullElementTest() {
		HashTable<String> table = new ClosedHashTable<String>(5, HashStrategy.LINEAR_PROBING);

		// Test null element addition.
		assertThrows(NullPointerException.class, () -> table.add(null));
	}

	@Test
	void addToFullTableTest() {
		HashTable<String> table = new ClosedHashTable<String>(3, HashStrategy.LINEAR_PROBING);

		String[] elements = { "apple", "banana", "cherry" };
		TestHelper.addElements(table, elements);

		// Try to add another element to the full table.
		assertThrows(IllegalStateException.class, () -> table.add("date"));
	}

	@Test
	void addElementCountTest() {
		// No collisions, just adding elements until full.
		HashTable<String> table = new ClosedHashTable<String>(5, HashStrategy.LINEAR_PROBING);

		// Add elements and verify they are added correctly.
		assertTrue(table.add("apple"));
		assertEquals(1, table.getElementNumber());

		assertTrue(table.add("banana"));
		assertEquals(2, table.getElementNumber());

		assertTrue(table.add("cherry"));
		assertEquals(3, table.getElementNumber());

		assertTrue(table.add("date"));
		assertEquals(4, table.getElementNumber());

		assertTrue(table.add("elderberry"));
		assertEquals(5, table.getElementNumber());

		// If we try to add another element, it should throw an exception.
		// The number of collitions will exceed capacity.
		assertThrows(IllegalStateException.class, () -> table.add("orange"));
	}

	@Test
	void addLinearCollisionsTest() {
		// No collisions, just adding elements until full.
		HashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);
		Status[] statuses = { E, E, E, E, E };
		Integer[] elements = { N, N, N, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(0, table.getElementNumber());

		// Add elements and verify they are added correctly.
		assertTrue(table.add(1));
		statuses = new Status[] { E, V, E, E, E };
		elements = new Integer[] { N, 1, N, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(1, table.getElementNumber());

		assertTrue(table.add(6));
		statuses = new Status[] { E, V, V, E, E };
		elements = new Integer[] { N, 1, 6, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		assertTrue(table.add(11));
		statuses = new Status[] { E, V, V, V, E };
		elements = new Integer[] { N, 1, 6, 11, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		assertTrue(table.add(16));
		statuses = new Status[] { E, V, V, V, V };
		elements = new Integer[] { N, 1, 6, 11, 16 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		// Add an already existing element, should return false.
		assertFalse(table.add(6));
		statuses = new Status[] { E, V, V, V, V };
		elements = new Integer[] { N, 1, 6, 11, 16 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		// Fill the table.
		assertTrue(table.add(21));
		statuses = new Status[] { V, V, V, V, V };
		elements = new Integer[] { 21, 1, 6, 11, 16 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(5, table.getElementNumber());

		// If we try to add another element, it should throw an exception.
		// The number of collitions will exceed capacity.
		assertThrows(IllegalStateException.class, () -> table.add(26));
	}

	@Test
	void addQuadraticCollitionsTest() {
		// No collisions, just adding elements until full.
		HashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.QUADRATIC_PROBING);
		Status[] statuses = { E, E, E, E, E };
		Integer[] elements = { N, N, N, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(0, table.getElementNumber());

		// Add elements and verify they are added correctly.
		assertTrue(table.add(1));
		statuses = new Status[] { E, V, E, E, E };
		elements = new Integer[] { N, 1, N, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(1, table.getElementNumber());

		assertTrue(table.add(6));
		statuses = new Status[] { E, V, V, E, E };
		elements = new Integer[] { N, 1, 6, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		assertTrue(table.add(11));
		statuses = new Status[] { V, V, V, E, E };
		elements = new Integer[] { 11, 1, 6, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		// Add an already existing element, should return false.
		assertFalse(table.add(6));
		statuses = new Status[] { V, V, V, E, E };
		elements = new Integer[] { 11, 1, 6, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		// With this table capacity and quadratic probing, 16 will collide without
		// finding an empty spot.
		// This can only be fixed by resizing the table, for this test is not active.
		// So adding 16 should return false.
		assertFalse(table.add(16));

		// But if we add an element that does not collide, it should work.
		assertTrue(table.add(3));
		statuses = new Status[] { V, V, V, V, E };
		elements = new Integer[] { 11, 1, 6, 3, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		// But if we add an element that does not collide, it should work.
		assertTrue(table.add(4));
		statuses = new Status[] { V, V, V, V, V };
		elements = new Integer[] { 11, 1, 6, 3, 4 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(5, table.getElementNumber());
	}

	@Test
	void addDoubleHashingTest() {
		// No collisions, just adding elements until full.
		HashTable<Integer> table = new ClosedHashTable<Integer>(7, HashStrategy.DOUBLE_HASHING);
		Status[] statuses = { E, E, E, E, E, E, E };
		Integer[] elements = { N, N, N, N, N, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(0, table.getElementNumber());

		// Add elements and verify they are added correctly.
		assertTrue(table.add(10));
		statuses = new Status[] { E, E, E, V, E, E, E };
		elements = new Integer[] { N, N, N, 10, N, N, N };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(1, table.getElementNumber());

		assertTrue(table.add(17));
		statuses = new Status[] { E, E, E, V, E, E, V };
		elements = new Integer[] { N, N, N, 10, N, N, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(2, table.getElementNumber());

		assertTrue(table.add(24));
		statuses = new Status[] { E, E, E, V, V, E, V };
		elements = new Integer[] { N, N, N, 10, 24, N, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		// Add an already existing element, should return false.
		assertFalse(table.add(17));
		statuses = new Status[] { E, E, E, V, V, E, V };
		elements = new Integer[] { N, N, N, 10, 24, N, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(3, table.getElementNumber());

		assertTrue(table.add(31));
		statuses = new Status[] { V, E, E, V, V, E, V };
		elements = new Integer[] { 31, N, N, 10, 24, N, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(4, table.getElementNumber());

		assertTrue(table.add(38));
		statuses = new Status[] { V, E, E, V, V, V, V };
		elements = new Integer[] { 31, N, N, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(5, table.getElementNumber());

		assertTrue(table.add(45));
		statuses = new Status[] { V, V, E, V, V, V, V };
		elements = new Integer[] { 31, 45, N, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(6, table.getElementNumber());

		assertTrue(table.add(52));
		statuses = new Status[] { V, V, V, V, V, V, V };
		elements = new Integer[] { 31, 45, 52, 10, 24, 38, 17 };
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
		assertEquals(7, table.getElementNumber());

		// If we try to add another element, it should throw an exception.
		// The number of collisions will exceed capacity.
		assertThrows(IllegalStateException.class, () -> table.add(59));

	}

}
