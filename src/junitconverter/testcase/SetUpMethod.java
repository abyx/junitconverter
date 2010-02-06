package junitconverter.testcase;

import java.util.List;

import junit.framework.TestCase;

/**
 * This class represents a {@link TestCase#setUp()} method.
 *
 * @author abyx
 */
public class SetUpMethod extends TestCaseMethod {

	public SetUpMethod(int methodLine) {
		super(methodLine);
	}
	
	public SetUpMethod(int methodLine, List<String> annotations) {
		super(methodLine, annotations);
	}
}
