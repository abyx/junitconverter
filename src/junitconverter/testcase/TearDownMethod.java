package junitconverter.testcase;

import java.util.List;

import junit.framework.TestCase;

/**
 * This class represents a {@link TestCase#tearDown()} method.
 *
 * @author abyx
 */
public class TearDownMethod extends TestCaseMethod {

	public TearDownMethod(int methodLine) {
		super(methodLine);
	}
	
	public TearDownMethod(int methodLine, List<String> annotations) {
		super(methodLine, annotations);
	}
}
