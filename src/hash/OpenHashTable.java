package hash;

import arboles.AVLTree;

public class OpenHashTable<T extends Comparable<T>> extends AbstractHashTable<T> {

	AVLTree<T>[] associativeArray;

	@Override
	public boolean add(T element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean search(T element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(T element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int hashFunction(T element, int attempts) {
		// TODO Auto-generated method stub
		return 0;
	}

}
