package hash;

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

		this.associativeArray = new HashNode[capacityB];
		for (int i = 0; i < capacityB; i++)
			associativeArray[i] = new HashNode<T>();

		this.hashStrategy = hashStrategy;
	}

	@Override
	public boolean add(T element) {
		if (element == null)
			throw new NullPointerException();
		if (elementNumber == capacityB)
			throw new IllegalStateException();

		HashNode<T> availableNode = findAvailableNodeFor(element);
		if (availableNode == null)
			return false;

		addElementToNode(availableNode, element);
		return true;
	}

	@Override
	public boolean search(T element) {
		if (element == null)
			throw new NullPointerException();
		if (elementNumber == 0)
			return false;

		HashNode<T> node = findMatchingNode(element);
		return node != null && node.getStatus() == Status.VALID;
	}

	@Override
	public boolean remove(T element) {
		if (element == null)
			throw new NullPointerException();
		if (elementNumber == 0)
			throw new IllegalStateException();

		HashNode<T> node = findMatchingNode(element);
		if (node == null || node.getStatus() != Status.VALID)
			return false;

		removeElementFromNode(node);
		return true;
	}

	@Override
	protected int hashFunction(T element, int attempts) {
		if (element == null)
			throw new NullPointerException();
		if (attempts < 0 || attempts > capacityB)
			throw new IllegalArgumentException();

		int hash = Math.abs(element.hashCode());
		switch (hashStrategy) {
			case LINEAR_PROBING -> {return linearProbing(hash, attempts);}
			case QUADRATIC_PROBING -> {return quadraticProbing(hash, attempts);}
			case DOUBLE_HASHING -> {return doubleHashing(hash, attempts);}
		}
		return -1;
	}
	
	protected int linearProbing(int hash, int attempts) {
		if (hash < 0 || attempts < 0 || attempts > capacityB)
			throw new IllegalArgumentException();
		return (Math.abs(hash) + attempts) % capacityB;
	}
	
	protected int quadraticProbing(int hash, int attempts) {
		if (hash < 0 || attempts < 0 || attempts > capacityB)
			throw new IllegalArgumentException();
		return (Math.abs(hash) + attempts * attempts) % capacityB;
	}
	
	protected int doubleHashing(int hash, int attempts) {
		if (hash < 0 || attempts < 0 || attempts > capacityB)
			throw new IllegalArgumentException();

		int jump = jumpFunctionH(hash);
		return (Math.abs(hash) + attempts * jump) % capacityB;		
	}
	
	protected int jumpFunctionH(int hashCode) {
		if (hashCode < 0)
			throw new IllegalArgumentException();

		int previousPrime = getPreviousPrimeNumber(capacityB);
		return previousPrime - (Math.abs(hashCode) % previousPrime);
	}
	
	protected HashNode<T> findMatchingNode(T element) {
		if (element == null)
			throw new NullPointerException();

		for (int attempts = 0; attempts < capacityB; attempts++) {
			int index = hashFunction(element, attempts);
			HashNode<T> node = associativeArray[index];

			if (node.getStatus().equals(Status.EMPTY))
				return null;
			if (node.getElement() != null && node.getElement().equals(element))
				return node;
		}
		return null;
	}

	protected HashNode<T> findAvailableNodeFor(T element) {
		if (element == null)
			throw new NullPointerException();

		HashNode<T> firstDeleted = null;

		for (int attempts = 0; attempts < capacityB; attempts++) {
			int index = hashFunction(element, attempts);
			HashNode<T> node = associativeArray[index];

			if (node.getStatus() == Status.EMPTY) {
				return firstDeleted != null ? firstDeleted : node;
			}

			if (node.getStatus() == Status.VALID && node.getElement() != null && node.getElement().equals(element))
				return null;

			if (node.getStatus() == Status.DELETED && firstDeleted == null)
				firstDeleted = node;
		}

		return firstDeleted;
	}

	protected void addElementToNode(HashNode<T> node, T element) {
		if (elementNumber == capacityB)
			throw new IllegalStateException();
		if (node == null)
			throw new NullPointerException();
		if (element == null)
			throw new NullPointerException();
		if (node.getStatus() == Status.VALID && element.equals(node.getElement()))
			throw new IllegalArgumentException();

		node.setElement(element);
		node.setStatus(Status.VALID);
		elementNumber++;
	}

	protected void removeElementFromNode(HashNode<T> node) {
		if (elementNumber == 0)
			throw new IllegalStateException();
		if (node == null)
			throw new NullPointerException();
		if (node.getStatus() != Status.VALID)
			throw new IllegalArgumentException();

		node.setStatus(Status.DELETED);
		elementNumber--;
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
