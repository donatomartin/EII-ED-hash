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
        // TODO
        return 0.0;
    }

    protected abstract int hashFunction(T element, int attempts);

    protected static boolean isPrimeNumber(int n) {
        // TODO
        return false;
    }

    protected static int getNextPrimeNumber(int n) {
        // TODO
        return 0;
    }

    protected static int getPreviousPrimeNumber(int n) {
        // TODO
        return 0;
    }

}
