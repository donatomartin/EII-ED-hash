package hash;

public abstract class AbstractHashTable<T> implements HashTable<T> {
	protected int capacityB;
	protected int elementNumber;

	@Override
	public int getCapacityB() {
		return capacityB;
	}

	@Override
	public int getElementNumber() {
		return elementNumber;
	}

	@Override
	public double getLoadFactor() {
		return capacityB == 0 ? 0.0 : (double) elementNumber / capacityB;
	}

	protected abstract int hashFunction(T element, int attempts);

	protected static boolean isPrimeNumber(int n) {
		if (n < 2) return false;
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	protected static int getNextPrimeNumber(int n) {
		n++;
		while (!isPrimeNumber(n))
			n++;
		return n;
	}

	protected static int getPreviousPrimeNumber(int n) {
		if (n < 3)
			throw new IllegalArgumentException();
		
		n--;
		while (!isPrimeNumber(n))
			n--;
		return n;
	}

}
