package hash.propios.open;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hash.OpenHashTable;
import hash.HashTable;

class OpenHashTableTests {

	@Test
	void constructorRejectsCapacityBelowOne() {
		assertThrows(IllegalArgumentException.class, () -> new OpenHashTable<String>(0));
	}

	@Test
	void constructorAdjustsToNextPrime() {
		OpenHashTable<Integer> table = new OpenHashTable<>(6);
		assertEquals(7, table.getCapacityB());
		assertEquals(0, table.getElementNumber());
	}

	@Test
	void defaultConstructorUsesPrimeCapacity() {
		OpenHashTable<Integer> table = new OpenHashTable<>();
		assertEquals(11, table.getCapacityB());
	}

	@Test
	void addSearchAndDuplicateTest() {
		HashTable<String> table = new OpenHashTable<>(5);

		assertThrows(NullPointerException.class, () -> table.add(null));
		assertTrue(table.add("apple"));
		assertEquals(1, table.getElementNumber());
		assertTrue(table.search("apple"));

		assertFalse(table.add("apple"));
		assertEquals(1, table.getElementNumber());

		assertThrows(NullPointerException.class, () -> table.search(null));
	}

	@Test
	void removeValidationAndStateChanges() {
		HashTable<Integer> table = new OpenHashTable<>(3);

		assertThrows(IllegalStateException.class, () -> table.remove(1));
		assertThrows(NullPointerException.class, () -> table.remove(null));

		assertTrue(table.add(1));
		assertTrue(table.add(4)); // Same tree as 1 with capacity 3.
		assertTrue(table.add(7)); // Same tree as 1 with capacity 3.
		assertEquals(3, table.getElementNumber());

		assertTrue(table.search(4));
		assertFalse(table.remove(10));
		assertTrue(table.remove(4));
		assertFalse(table.search(4));
		assertEquals(2, table.getElementNumber());
	}

	@Test
	void searchOnEmptyTableReturnsFalse() {
		HashTable<Integer> table = new OpenHashTable<>(3);
		assertFalse(table.search(99));
	}

	@Test
	void removeReturnsFalseWhenElementMissing() {
		HashTable<Integer> table = new OpenHashTable<>(7);
		table.add(5);
		table.add(12);
		assertFalse(table.remove(99));
		assertEquals(2, table.getElementNumber());
	}

	@Test
	void hashFunctionValidations() {
		OpenHashTableExposer<Integer> table = new OpenHashTableExposer<>(5);

		assertThrows(NullPointerException.class, () -> table.callHash(null, 0));
		assertThrows(IllegalArgumentException.class, () -> table.callHash(1, -1));
		assertThrows(IllegalArgumentException.class, () -> table.callHash(1, 6));
		assertEquals(Math.abs(11) % table.getCapacityB(), table.callHash(11, 0));
		assertEquals((Math.abs(11) + 1) % table.getCapacityB(), table.callHash(11, 1));
	}

	static class OpenHashTableExposer<E extends Comparable<E>> extends OpenHashTable<E> {
		OpenHashTableExposer(int capacity) {
			super(capacity);
		}

		int callHash(E element, int attempts) {
			return hashFunction(element, attempts);
		}
	}
}
