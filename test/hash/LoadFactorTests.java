package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoadFactorTests {

	final double DELTA = 0.001;

	@Test
	void balanceFactorTest() {
		int capacity = 293; // A prime number capacity
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(capacity, HashStrategy.LINEAR_PROBING);

		assertEquals(0.0, table.getLoadFactor(), DELTA);

		// Fill the table and check load factor at each step
		for (int i = 1; i <= capacity; i++) {
			table.add(i);
			assertEquals(i / (double) capacity, table.getLoadFactor(), DELTA, "Load factor after adding " + i);
		}

		for (int i = 1; i <= capacity; i++) {
			table.remove(i);
			assertEquals((capacity - i) / (double) capacity, table.getLoadFactor(), DELTA,
					"Load factor after removing " + i);
		}
	}

	@Test
	void balanceFactorWithRepeatedAdditionsTest() {
		int capacity = 293; // A prime number capacity
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(capacity, HashStrategy.LINEAR_PROBING);

		assertEquals(0.0, table.getLoadFactor(), DELTA);

		// Add the same element multiple times and check load factor
		for (int i = 1; i <= capacity; i++) {
			table.add(99); // Adding the same element
			assertEquals(1 / (double) capacity, table.getLoadFactor(), DELTA,
					"Load factor after adding the same element " + i + " times");
		}

		table.remove(99);
		assertEquals(0.0, table.getLoadFactor(), DELTA, "Load factor after removing the element");
	}

	@Test
	void loadFactorWithRepeatedRemovalsTest() {
		int capacity = 293; // A prime number capacity
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(capacity, HashStrategy.LINEAR_PROBING);

		// Fill the table
		for (int i = 1; i <= capacity; i++) {
			table.add(i);
		}
		assertEquals(1.0, table.getLoadFactor(), DELTA, "Load factor after filling the table");

		// Remove the same element multiple times and check load factor
		for (int i = 1; i <= capacity; i++) {
			table.remove(99); // Removing the same element
			assertEquals((capacity - 1) / (double) capacity, table.getLoadFactor(), DELTA,
					"Load factor after removing the same element " + i + " times");
		}

	}
}
