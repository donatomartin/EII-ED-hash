package hash;

import org.junit.jupiter.api.DisplayNameGenerator.IndicativeSentences;

public class ClosedHashTable<T> extends AbstractHashTable<T> implements HashTable<T> {
	
	HashNode<T>[] associativeArray;
	private HashStrategy hashStrategy;

	public ClosedHashTable(int capacity, HashStrategy hashStrategy) {
			
		if (hashStrategy == null)
			throw new NullPointerException();
		if (capacity < 3) 
			throw new IllegalArgumentException();

		this.capacityB = capacity;
		if (!isPrimeNumber(capacity))
			this.capacityB = getNextPrimeNumber(capacity);

		this.associativeArray = new HashNode[capacity];
		for (int i = 0; i < capacity; i++)
			associativeArray[i] = new HashNode<T>();

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
		int hash = element.hashCode();
		switch (hashStrategy) {
			case LINEAR_PROBING -> {return linearProbing(hash, attempts);}
			case QUADRATIC_PROBING -> {return quadraticProbing(hash, attempts);}
			case DOUBLE_HASHING -> {return doubleHashing(hash, attempts);}
		}
		return -1;
	}
	
	protected int linearProbing(int hash, int attempts) {
		return (hash+attempts)%capacityB;
	}
	
	protected int quadraticProbing(int hash, int attempts) {
		return (hash+attempts * attempts)%capacityB;
	}
	
	protected int doubleHashing(int hash, int attempts) {
		return (hash + attempts * jumpFunction(hash)) % capacityB;		
	}
	
	protected int jumpFunction(int hashCode) {
		return 1 + Math.abs(hashCode) % (capacityB - 1);
	}
	
	protected HashNode<T> findMatchingNodeT element) {
		
		for (int attempts = 0; attempts < capacityB; attempts++) {
			int index = hashFunction(element, attempts);
			HashNode<T> node = associativeArray[index];

			if (node.getStatus().equals(Status.EMPTY))
				return null;
			if (node.getElement().equals(element))
				return node;
		}

		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < associativeArray.length; i++) {
			sb.append("%d:%s".formatted(i, associativeArray[i]));			
			if (i != associativeArray.length-1)
				sb.append("   ");
		}
		
		return sb.toString();
	}

}
