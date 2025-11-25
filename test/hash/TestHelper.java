package hash;

public class TestHelper {

	public static <T> String expectedHashTableNodes(Status[] statuses, T[] elements) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < statuses.length; i++) {
			String statusInitial = statuses[i].getStatusInitial();
			T element = elements[i];
			String elementString = element != null ? element.toString() : "-";
			sb.append(i).append(":").append("{")
					.append(statusInitial).append("|").append(elementString)
					.append("}").append("   ");
		}

		// Remove last space
		return sb.substring(0, sb.length() - 3);
	}

	public static <T> void setHashTableNodes(ClosedHashTable<T> hashTable, Status[] statuses, T[] elements) {
		// Reset element number and capacity
		hashTable.capacityB = statuses.length;
		hashTable.elementNumber = 0;
		for (int i = 0; i < hashTable.associativeArray.length; i++) {
			hashTable.associativeArray[i].setStatus(statuses[i]);
			hashTable.associativeArray[i].setElement(elements[i]);

			// If the status is VALID, increment the element number
			hashTable.elementNumber += statuses[i] == Status.VALID ? 1 : 0;
		}
	}

	public static <T> void addElements(HashTable<T> hashTable, T[] elements) {
		for (T element : elements) {
			hashTable.add(element);
		}
	}

	public static <T> void setAsDeletedHelper(ClosedHashTable<T> table, int index, T element) {
		// Set the node at the specified index as DELETED with the given element.
		HashNode<T> node = table.associativeArray[index];
		node.setElement(element);
		node.setStatus(Status.DELETED);
	}
}
