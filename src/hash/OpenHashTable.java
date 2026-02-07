package hash;

import arboles.AVLTree;

public class OpenHashTable<T extends Comparable<T>> extends AbstractHashTable<T> {

	AVLTree<T>[] associativeArray;

	public OpenHashTable(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException();

		this.capacityB = isPrimeNumber(capacity) ? capacity : getNextPrimeNumber(capacity);

		@SuppressWarnings("unchecked")
		AVLTree<T>[] forest = new AVLTree[capacityB];
		this.associativeArray = forest;
		this.elementNumber = 0;
	}

	public OpenHashTable() {
		this(11);
	}

	@Override
	public boolean add(T element) {
		if (element == null)
			throw new NullPointerException();

		int index = hashFunction(element, 0);
		AVLTree<T> tree = associativeArray[index];
		if (tree == null) {
			tree = new AVLTree<>();
			associativeArray[index] = tree;
		}

		if (tree.search(element))
			return false;

		tree.add(element);
		elementNumber++;
		return true;
	}

	@Override
	public boolean search(T element) {
		if (element == null)
			throw new NullPointerException();
		if (elementNumber == 0)
			return false;

		int index = hashFunction(element, 0);
		AVLTree<T> tree = associativeArray[index];
		return tree != null && tree.search(element);
	}

	@Override
	public boolean remove(T element) {
		if (element == null)
			throw new NullPointerException();
		if (elementNumber == 0)
			throw new IllegalStateException();

		int index = hashFunction(element, 0);
		AVLTree<T> tree = associativeArray[index];
		if (tree == null || !tree.search(element))
			return false;

		tree.remove(element);
		elementNumber--;
		return true;
	}

	@Override
	protected int hashFunction(T element, int attempts) {
		if (element == null)
			throw new NullPointerException();
		if (attempts < 0 || attempts > capacityB)
			throw new IllegalArgumentException();

		int hash = Math.abs(element.hashCode());
		return (hash + attempts) % capacityB;
	}

}
