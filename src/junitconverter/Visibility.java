package junitconverter;

public enum Visibility {
	PUBLIC,
	PROTECTED,
	PRIVATE;
	
	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
