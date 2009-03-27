package junitconverter.testcase;

/**
 * 
 *
 * @author abyx
 */
public class TestCaseMethod {

	protected final int methodLine;

	/**
	 * @param methodLine2
	 */
	public TestCaseMethod(int methodLine) {
		this.methodLine = methodLine - 1;
	}

	/**
	 * @return the methodLine
	 */
	public int getLine() {
		return methodLine;
	}

}
