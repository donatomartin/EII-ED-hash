package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InitializationTests {

	@Test
	void testCapacityAndSize() {
		// Test with a prime number capacity.
		HashTable<Integer> table = new ClosedHashTable<Integer>(7, HashStrategy.LINEAR_PROBING);
		// The table keeps the same capacity.
		assertEquals(7, table.getCapacityB());
		assertEquals(0, table.getElementNumber());

		// Test with non-prime capacities.
		table = new ClosedHashTable<Integer>(10, HashStrategy.LINEAR_PROBING);

		// The table gets the next prime number as capacity.
		assertEquals(11, table.getCapacityB());

		// Initially, the table is empty.
		assertEquals(0, table.getElementNumber());

		table = new ClosedHashTable<Integer>(50, HashStrategy.LINEAR_PROBING);

		// The table gets the next prime number as capacity.
		assertEquals(53, table.getCapacityB());

		// Initially, the table is empty.
		assertEquals(0, table.getElementNumber());

		// Test with minimum accepted size (3).
		table = new ClosedHashTable<Integer>(3, HashStrategy.LINEAR_PROBING);

		// Test with capacity 2.
		assertThrows(IllegalArgumentException.class,
				() -> new ClosedHashTable<Integer>(2, HashStrategy.LINEAR_PROBING));

		// Test with capacity of zero.
		assertThrows(IllegalArgumentException.class,
				() -> new ClosedHashTable<Integer>(0, HashStrategy.LINEAR_PROBING));

		// Test with negative capacity.
		assertThrows(IllegalArgumentException.class,
				() -> new ClosedHashTable<Integer>(-5, HashStrategy.LINEAR_PROBING));

		// Test with null collisions strategy.
		assertThrows(NullPointerException.class, () -> new ClosedHashTable<Integer>(20, null));

	}

	@Test
	void testAssociativeArray() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(53, HashStrategy.LINEAR_PROBING);
		HashNode<Integer>[] associativeArray = table.associativeArray;

		// The length of the array is equal to the capacity of the table.
		assertEquals(53, associativeArray.length);
		assertEquals(table.getCapacityB(), associativeArray.length);

		// The array does not contain null nodes, all nodes are initialized.
		for (HashNode<Integer> node : associativeArray) {
			// The array does not contain null nodes, all nodes are initialized.
			assertNotNull(node);
			// All T node elements are null initially.
			assertNull(node.getElement());
			// All nodes status are EMPTY initially.
			node.getStatus().equals(Status.EMPTY);
		}
	}

}
