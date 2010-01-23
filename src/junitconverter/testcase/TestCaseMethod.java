package junitconverter.testcase;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * 
 *
 * @author abyx
 */
public class TestCaseMethod {

	protected final int methodLine;
	private List<String> annotations;

	public TestCaseMethod(int methodLine) {
		this(methodLine, Collections.<String>emptyList());
	}

	public TestCaseMethod(int methodLine, List<String> annotations) {
		this.methodLine = methodLine - 1;
		this.annotations = annotations;
	}

	/**
	 * @return the methodLine
	 */
	public int getLine() {
		return methodLine;
	}

	public boolean hasAnnotation(Class<? extends Annotation> annotation) {
		return annotations.contains(annotation.getSimpleName())
			|| annotations.contains(annotation.getName());
	}

}
