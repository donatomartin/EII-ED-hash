package hash;

public class HashNode<T> {
	private T element;
	private Status status;
	
	public HashNode() {
		status = Status.EMPTY;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(status.getInitialStatus()).append("|");
		if (element == null)
			sb.append("-");
		else
			sb.append(element.toString());
		sb.append("}");
		return sb.toString();
	}

	public T getElement() {
		return element;
	}

	public Status getStatus() {
		return status;
	}
	
}
