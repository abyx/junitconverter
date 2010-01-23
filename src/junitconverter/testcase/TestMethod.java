package junitconverter.testcase;

import java.util.Collections;
import java.util.List;

public class TestMethod extends TestCaseMethod {

	public TestMethod(int methodLine) {
		this(methodLine, Collections.<String>emptyList());
	}

	public TestMethod(int methodLine, List<String> annotations) {
		super(methodLine, annotations);
	}
}
