package hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ToStringTests {

	final static Status E = Status.EMPTY;
	final static Status V = Status.VALID;
	final static Status D = Status.DELETED;
	final static Integer N = null;

	@Test
	void test() {
		ClosedHashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING);
		Status[] statuses = { E, E, E, E, E };
		Integer[] elements = { N, N, N, N, N };
		TestHelper.setHashTableNodes(table, statuses, elements);

		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		statuses = new Status[] { V, D, E, V, D };
		elements = new Integer[] { 10, 6, N, 3, 4 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		statuses = new Status[] { V, V, V, V, V };
		elements = new Integer[] { 1, 2, 3, 4, 5 };
		TestHelper.setHashTableNodes(table, statuses, elements);

		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		statuses = new Status[] { D, D, D, D, D };
		elements = new Integer[] { 7, 8, 9, 10, 11 };
		TestHelper.setHashTableNodes(table, statuses, elements);
	}

}
