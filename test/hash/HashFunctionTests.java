package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HashFunctionTests {

	@Test
	void linearProbingTest() {
		ClosedHashTable<Integer> linearTable = new ClosedHashTable<Integer>(11, HashStrategy.LINEAR_PROBING);

		assertEquals(5, linearTable.linearProbing(5, 0)); // (5 + 0) % 11 = 5 % 11 = 5
		assertEquals(6, linearTable.linearProbing(5, 1)); // (5 + 1) % 11 = 6 % 11 = 6
		assertEquals(7, linearTable.linearProbing(5, 2)); // (5 + 2) % 11 = 7 % 11 = 7
		assertEquals(8, linearTable.linearProbing(5, 3)); // (5 + 3) % 11 = 8 % 11 = 8

		assertEquals(10, linearTable.linearProbing(10, 0)); // (10 + 0) % 11 = 10 % 11 = 10
		assertEquals(0, linearTable.linearProbing(10, 1)); // (10 + 1) % 11 = 11 % 11 = 0
		assertEquals(1, linearTable.linearProbing(10, 2)); // (10 + 2) % 11 = 12 % 11 = 1
		assertEquals(2, linearTable.linearProbing(10, 3)); // (10 + 3) % 11 = 13 % 11 = 2

		// Test negative hash code
		assertThrows(IllegalArgumentException.class, () -> linearTable.linearProbing(-5, 1));

		// Test negative attempt number
		assertThrows(IllegalArgumentException.class, () -> linearTable.linearProbing(5, -1));
		// Test attempt number greater than capacity
		assertThrows(IllegalArgumentException.class, () -> linearTable.linearProbing(5, 12));
		// Test attempt number equal to capacity
		assertEquals(0, linearTable.linearProbing(11, 11));
	}

	@Test
	void quadraticProbingTest() {
		ClosedHashTable<Integer> quadraticTable = new ClosedHashTable<Integer>(11, HashStrategy.QUADRATIC_PROBING);

		assertEquals(5, quadraticTable.quadraticProbing(5, 0)); // (5 + 0^2) % 11 = 5 % 11 = 5
		assertEquals(6, quadraticTable.quadraticProbing(5, 1)); // (5 + 1^2) % 11 = 6 % 11 = 6
		assertEquals(9, quadraticTable.quadraticProbing(5, 2)); // (5 + 2^2) % 11 = 9 % 11 = 9
		assertEquals(3, quadraticTable.quadraticProbing(5, 3)); // (5 + 3^2) % 11 = 14 % 11 = 3

		assertEquals(10, quadraticTable.quadraticProbing(10, 0)); // (10 + 0^2) % 11 = 10 % 11 = 10
		assertEquals(0, quadraticTable.quadraticProbing(10, 1)); // (10 + 1^2) % 11 = 11 % 11 = 0
		assertEquals(3, quadraticTable.quadraticProbing(10, 2)); // (10 + 2^2) % 11 = 14 % 11 = 3
		assertEquals(8, quadraticTable.quadraticProbing(10, 3)); // (10 + 3^2) % 11 = 19 % 11 = 8

		// Test negative hash code
		assertThrows(IllegalArgumentException.class, () -> quadraticTable.quadraticProbing(-5, 1));
		// Test negative attempt number
		assertThrows(IllegalArgumentException.class, () -> quadraticTable.quadraticProbing(5, -1));
		// Test attempt number greater than capacity
		assertThrows(IllegalArgumentException.class, () -> quadraticTable.linearProbing(5, 12));
		// Test attempt number equal to capacity
		assertEquals(0, quadraticTable.quadraticProbing(11, 11));
	}

	@Test
	void jumpFunctionHTest() {
		ClosedHashTable<Integer> doubleHashTable = new ClosedHashTable<Integer>(11, HashStrategy.DOUBLE_HASHING);

		// previous prime to 11 is 7
		assertEquals(7, doubleHashTable.jumpFunctionH(0)); // 7 - (0 % 7) = 7 - 0 = 7
		assertEquals(6, doubleHashTable.jumpFunctionH(1)); // 7 - (1 % 7) = 7 - 1 = 6
		assertEquals(5, doubleHashTable.jumpFunctionH(2)); // 7 - (2 % 7) = 7 - 2 = 5
		assertEquals(4, doubleHashTable.jumpFunctionH(3)); // 7 - (3 % 7) = 7 - 3 = 4
		assertEquals(3, doubleHashTable.jumpFunctionH(4)); // 7 - (4 % 7) = 7 - 4 = 3
		assertEquals(1, doubleHashTable.jumpFunctionH(6)); // 7 - (6 % 7) = 7 - 6 = 1
		assertEquals(7, doubleHashTable.jumpFunctionH(7)); // 7 - (7 % 7) = 7 - 0 = 7
		assertEquals(3, doubleHashTable.jumpFunctionH(25)); // 7 - (25 % 7) = 7 - 4 = 3

		// Test negative hash code
		assertThrows(IllegalArgumentException.class, () -> doubleHashTable.jumpFunctionH(-1));
	}

	@Test
	void doubleHashingTest() {
		ClosedHashTable<Integer> doubleHashTable = new ClosedHashTable<Integer>(11, HashStrategy.DOUBLE_HASHING);

		// previous prime to 11 is 7
		assertEquals(5, doubleHashTable.doubleHashing(5, 0)); // (5 + 0 * (7 - (5 % 7))) % 11 = (5 + 0) % 11 = 5
		assertEquals(7, doubleHashTable.doubleHashing(5, 1)); // (5 + 1 * (7 - (5 % 7))) % 11 = (5 + 2) % 11 = 7
		assertEquals(9, doubleHashTable.doubleHashing(5, 2)); // (5 + 2 * (7 - (5 % 7))) % 11 = (5 + 4) % 11 = 9
		assertEquals(0, doubleHashTable.doubleHashing(5, 3)); // (5 + 3 * (7 - (5 % 7))) % 11 = (5 + 6) % 11 = 0

		assertEquals(10, doubleHashTable.doubleHashing(10, 0)); // 10 + 0 * (7 - (10 % 7)) % 11 = (10 + 0) % 11 = 10
		assertEquals(3, doubleHashTable.doubleHashing(10, 1)); // 10 + 1 * (7 - (10 % 7)) % 11 = (10 + 4) % 11 = 3
		assertEquals(7, doubleHashTable.doubleHashing(10, 2)); // 10 + 2 * (7 - (10 % 7)) % 11 = (10 + 8) % 11 = 7
		assertEquals(0, doubleHashTable.doubleHashing(10, 3)); // 10 + 3 * (7 - (10 % 7)) % 11 = (10 + 12) % 11 = 0

		// Test negative hash code
		assertThrows(IllegalArgumentException.class, () -> doubleHashTable.doubleHashing(-5, 1));
		// Test negative attempt number
		assertThrows(IllegalArgumentException.class, () -> doubleHashTable.doubleHashing(5, -1));
		// Test attempt number greater than capacity
		assertThrows(IllegalArgumentException.class, () -> doubleHashTable.doubleHashing(5, 12));
		// Test attempt number equal to capacity
		assertEquals(0, doubleHashTable.doubleHashing(11, 11));
	}

	@Test
	void hashFunctionLinearTest() {
		ClosedHashTable<String> linearTable = new ClosedHashTable<String>(11, HashStrategy.LINEAR_PROBING);
		String element = "test";
		int hashCode = Math.abs(element.hashCode());

		// Test negative attempt number
		assertThrows(IllegalArgumentException.class, () -> linearTable.hashFunction(element, -1));
		// Test attempt number greater than capacity
		assertThrows(IllegalArgumentException.class, () -> linearTable.hashFunction(element, 12));
		// Test attempt number equal to capacity
		assertEquals(0, linearTable.hashFunction(element, 11));

		// Direct values
		assertEquals(0, linearTable.hashFunction(element, 0));
		assertEquals(1, linearTable.hashFunction(element, 1));
		assertEquals(2, linearTable.hashFunction(element, 2));
		// Check methods match
		assertEquals(linearTable.linearProbing(hashCode, 0), linearTable.hashFunction(element, 0));
		assertEquals(linearTable.linearProbing(hashCode, 1), linearTable.hashFunction(element, 1));
		assertEquals(linearTable.linearProbing(hashCode, 2), linearTable.hashFunction(element, 2));
	}

	@Test
	void hashFunctionQuadraticTest() {
		ClosedHashTable<String> quadraticTable = new ClosedHashTable<String>(11, HashStrategy.QUADRATIC_PROBING);
		String element = "test";
		int hashCode = Math.abs(element.hashCode());

		// Test negative attempt number
		assertThrows(IllegalArgumentException.class, () -> quadraticTable.hashFunction(element, -1));
		// Test attempt number greater than capacity
		assertThrows(IllegalArgumentException.class, () -> quadraticTable.hashFunction(element, 12));
		// Test attempt number equal to capacity
		assertEquals(0, quadraticTable.hashFunction(element, 11));

		// Direct values
		assertEquals(0, quadraticTable.hashFunction(element, 0));
		assertEquals(1, quadraticTable.hashFunction(element, 1));
		assertEquals(4, quadraticTable.hashFunction(element, 2));
		// Check methods match
		assertEquals(quadraticTable.quadraticProbing(hashCode, 0), quadraticTable.hashFunction(element, 0));
		assertEquals(quadraticTable.quadraticProbing(hashCode, 1), quadraticTable.hashFunction(element, 1));
		assertEquals(quadraticTable.quadraticProbing(hashCode, 2), quadraticTable.hashFunction(element, 2));
	}

	@Test
	void hashFunctionDoubleHashingTest() {
		ClosedHashTable<String> doubleHashTable = new ClosedHashTable<String>(11, HashStrategy.DOUBLE_HASHING);
		String element = "test";
		int hashCode = Math.abs(element.hashCode());

		// Test negative attempt number
		assertThrows(IllegalArgumentException.class, () -> doubleHashTable.hashFunction(element, -1));
		// Test attempt number greater than capacity
		assertThrows(IllegalArgumentException.class, () -> doubleHashTable.hashFunction(element, 12));
		// Test attempt number equal to capacity
		assertEquals(0, doubleHashTable.hashFunction(element, 11));

		// Direct values
		assertEquals(0, doubleHashTable.hashFunction(element, 0));
		assertEquals(6, doubleHashTable.hashFunction(element, 1));
		assertEquals(1, doubleHashTable.hashFunction(element, 2));
		// Check methods match
		assertEquals(doubleHashTable.doubleHashing(hashCode, 0), doubleHashTable.hashFunction(element, 0));
		assertEquals(doubleHashTable.doubleHashing(hashCode, 1), doubleHashTable.hashFunction(element, 1));
		assertEquals(doubleHashTable.doubleHashing(hashCode, 2), doubleHashTable.hashFunction(element, 2));
	}

}
