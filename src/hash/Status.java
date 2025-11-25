package hash;

public enum Status {
	EMPTY,
	VALID,
	DELETED;
	
	public String getStatusInitial() {
		switch (this) {
			case EMPTY:
				return "E";
			case VALID:
				return "V";
			case DELETED:
				return "D";
			default:
				return "?";
		}
	}
}
