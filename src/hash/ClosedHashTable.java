package hash;

public class ClosedHashTable<T> extends AbstractHashTable<T> implements HashTable<T> {
	
	HashNode<T>[] associativeArray;
	private HashStrategy hashStrategy;

		public ClosedHashTable(int capacity, HashStrategy hashStrategy) {
			
		if (hashStrategy == null)
			throw new NullPointerException();
		if (capacity < 3) 
			throw new IllegalArgumentException();

		this.associativeArray = new HashNode[capacity];
		this.hashStrategy = hashStrategy;
	}

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
