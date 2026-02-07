package hash.propios.closed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hash.ClosedHashTable;
import hash.HashStrategy;

class ClosedHashTableExtraTests {

	@Test
	void doubleHashingUsesPreviousPrimeJump() {
		ClosedHashExposer table = new ClosedHashExposer(11, HashStrategy.DOUBLE_HASHING);
		int hashCode = 14; // jump should be previous prime (7) minus hash % 7.
		assertEquals(7, table.jumpPublic(hashCode));
		assertEquals((hashCode + table.jumpPublic(hashCode)) % table.getCapacityB(), table.doubleHashPublic(hashCode, 1));
	}

	@Test
	void inverseDynamicResizeShrinksToPreviousPrime() {
		ClosedHashTable<Integer> table = new ClosedHashTable<>(11, HashStrategy.LINEAR_PROBING, 2.0, 0.25);

		table.add(0);
		table.add(1);
		table.add(2);
		table.add(3);
		table.add(4);
		assertEquals(11, table.getCapacityB());

		table.remove(4);
		table.remove(3);
		table.remove(2); // Now 2 elements left, load factor below 0.25 triggers shrink.

		assertEquals(3, table.getCapacityB());
		assertTrue(table.search(0));
		assertTrue(table.search(1));
	}

	@Test
	void noInverseResizeWhenMinLoadFactorDisabled() {
		ClosedHashTable<Integer> table = new ClosedHashTable<>(11, HashStrategy.LINEAR_PROBING, 2.0);

		table.add(1);
		table.add(2);
		table.remove(1);
		table.remove(2);

		assertEquals(11, table.getCapacityB());
	}

	static class ClosedHashExposer extends ClosedHashTable<Integer> {
		ClosedHashExposer(int capacity, HashStrategy strategy) {
			super(capacity, strategy);
		}

		int doubleHashPublic(int hash, int attempts) {
			return doubleHashing(hash, attempts);
		}

		int jumpPublic(int hash) {
			return jumpFunctionH(hash);
		}
	}
}
