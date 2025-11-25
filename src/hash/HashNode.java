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
		sb.append("{").append(status.getStatusInitial()).append("|");
		if (element == null)
			sb.append("-");
		else
			sb.append(element.toString());
		sb.append("}");
		return sb.toString();
	}

	void setElement(T element) {
		this.element = element;
	}

	void setStatus(Status status) {
		this.status = status;
	}

	public T getElement() {
		return element;
	}

	public Status getStatus() {
		return status;
	}
	
}
