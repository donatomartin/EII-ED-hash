package hashtables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InverseResizingTests {

	final static Status E = Status.EMPTY;
	final static Status V = Status.VALID;
	final static Status D = Status.DELETED;
	final static Integer N = null;

	final static double MIN_LOAD_FACTOR = 0.15;
	final static double MAX_LOAD_FACTOR = 0.5;
	final double DELTA = 0.0001;


	@Test
	void inverseResizingTests() {
		// Both upper and inverse resizing.
		HashTable<Integer> table = new ClosedHashTable<Integer>(5, HashStrategy.LINEAR_PROBING, MAX_LOAD_FACTOR,
				MIN_LOAD_FACTOR);

		// Add one element.
		table.add(5);
		table.add(10);
		table.add(15);
		table.add(11);
		table.add(22);
		table.add(33);

		// By no should be at size 23
		Status[] statuses = new Status[] { E, E, E, E, E, V, E, E, E, E, V, V, V, E, E, V, E, E, E, E, E, E, V };
		Integer[] elements = new Integer[] { N, N, N, N, N, 5, N, N, N, N, 33, 11, 10, N, N, 15, N, N, N, N, N, N, 22 };

		assertEquals(6, table.getElementNumber());
		assertEquals(23, table.getCapacityB());
		assertEquals(6 / 23.0, table.getLoadFactor(), DELTA);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		// Remove 2. No inverse resizing shouldn't occur yet.
		table.remove(5);
		table.remove(10);

		// Should keep same size.
		statuses = new Status[] { E, E, E, E, E, D, E, E, E, E, V, V, D, E, E, V, E, E, E, E, E, E, V };
		elements = new Integer[] { N, N, N, N, N, 5, N, N, N, N, 33, 11, 10, N, N, 15, N, N, N, N, N, N, 22 };
		assertEquals(4, table.getElementNumber());
		assertEquals(23, table.getCapacityB());
		assertEquals(4 / 23.0, table.getLoadFactor(), DELTA);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		// Remove one more to trigger inverse resizing.
		table.remove(15);

		// Should have resized to next prime down from 23 / 2 = 11 -> 7
		statuses = new Status[] { E, V, E, E, V, V, E };
		elements = new Integer[] { N, 22, N, N, 11, 33, N };
		assertEquals(3, table.getElementNumber());
		assertEquals(7, table.getCapacityB());
		assertEquals(3 / 7.0, table.getLoadFactor(), DELTA);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		// Remove one more element.
		table.remove(11);
		// Should keep same size.
		statuses = new Status[] { E, V, E, E, D, V, E };
		elements = new Integer[] { N, 22, N, N, 11, 33, N };
		assertEquals(2, table.getElementNumber());
		assertEquals(7, table.getCapacityB());
		assertEquals(2 / 7.0, table.getLoadFactor(), DELTA);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		// Remove one more element. Should trigger inverse resizing.
		table.remove(22);
		// Should have resized to next prime down from 7 / 2 = 3 -> 3
		// Since 3 is the minimum capacity allowed, it should stay at 3.
		statuses = new Status[] { V, E, E, };
		elements = new Integer[] { 33, N, N };
		assertEquals(1, table.getElementNumber());
		assertEquals(3, table.getCapacityB());
		assertEquals(1 / 3.0, table.getLoadFactor(), DELTA);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());

		// Remove last element. It should trigger resizing.
		table.remove(33);
		// Should have resized to next prime down from 3 / 2 = 1 -> 3
		// Since 3 is the minimum capacity allowed, it should stay at 3.
		// The resizing happens, it's just to the same size. The deleted nodes are
		// cleared.
		statuses = new Status[] { E, E, E, };
		elements = new Integer[] { N, N, N };
		assertEquals(0, table.getElementNumber());
		assertEquals(3, table.getCapacityB());
		assertEquals(0.0, table.getLoadFactor(), DELTA);
		assertEquals(TestHelper.expectedHashTableNodes(statuses, elements), table.toString());
	}

}
