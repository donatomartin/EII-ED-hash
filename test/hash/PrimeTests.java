package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrimeTests {

	@Test
	void isPrimeTest() {
		assertTrue(ClosedHashTable.isPrimeNumber(2));
		assertTrue(ClosedHashTable.isPrimeNumber(3));
		assertTrue(ClosedHashTable.isPrimeNumber(13));
		assertTrue(ClosedHashTable.isPrimeNumber(17));
		assertTrue(ClosedHashTable.isPrimeNumber(23));
		assertTrue(ClosedHashTable.isPrimeNumber(29));
		assertTrue(ClosedHashTable.isPrimeNumber(53));
		assertTrue(ClosedHashTable.isPrimeNumber(71));
		assertTrue(ClosedHashTable.isPrimeNumber(79));
		assertTrue(ClosedHashTable.isPrimeNumber(97));
		assertTrue(ClosedHashTable.isPrimeNumber(101));
		assertTrue(ClosedHashTable.isPrimeNumber(113));
		assertTrue(ClosedHashTable.isPrimeNumber(199));

		assertFalse(ClosedHashTable.isPrimeNumber(0));
		assertFalse(ClosedHashTable.isPrimeNumber(1));
		assertFalse(ClosedHashTable.isPrimeNumber(4));
		assertFalse(ClosedHashTable.isPrimeNumber(15));
		assertFalse(ClosedHashTable.isPrimeNumber(20));
		assertFalse(ClosedHashTable.isPrimeNumber(25));

		// Negative numbers are not prime.
		assertFalse(ClosedHashTable.isPrimeNumber(-1));
	}

	@Test
	void previousPrimeTest() {
		assertEquals(2, ClosedHashTable.getPreviousPrimeNumber(3));
		assertEquals(7, ClosedHashTable.getPreviousPrimeNumber(10));
		assertEquals(13, ClosedHashTable.getPreviousPrimeNumber(14));
		assertEquals(19, ClosedHashTable.getPreviousPrimeNumber(20));
		assertEquals(23, ClosedHashTable.getPreviousPrimeNumber(25));
		assertEquals(59, ClosedHashTable.getPreviousPrimeNumber(60));
		assertEquals(73, ClosedHashTable.getPreviousPrimeNumber(75));
		assertEquals(97, ClosedHashTable.getPreviousPrimeNumber(100));

		// Test exception for input less than or equal to 2 (lowest prime number).
		assertThrows(IllegalArgumentException.class, () -> ClosedHashTable.getPreviousPrimeNumber(2));
		assertThrows(IllegalArgumentException.class, () -> ClosedHashTable.getPreviousPrimeNumber(-1));
	}

	@Test
	void nextPrimeTest() {
		assertEquals(2, ClosedHashTable.getNextPrimeNumber(1));
		assertEquals(3, ClosedHashTable.getNextPrimeNumber(2));
		assertEquals(5, ClosedHashTable.getNextPrimeNumber(3));
		assertEquals(11, ClosedHashTable.getNextPrimeNumber(9));
		assertEquals(17, ClosedHashTable.getNextPrimeNumber(14));
		assertEquals(19, ClosedHashTable.getNextPrimeNumber(18));
		assertEquals(29, ClosedHashTable.getNextPrimeNumber(25));
		assertEquals(59, ClosedHashTable.getNextPrimeNumber(55));
		assertEquals(71, ClosedHashTable.getNextPrimeNumber(70));
		assertEquals(101, ClosedHashTable.getNextPrimeNumber(100));

		// Test for negative input, should return 2 (lowest prime number).
		assertEquals(2, ClosedHashTable.getNextPrimeNumber(-10));
	}

}
