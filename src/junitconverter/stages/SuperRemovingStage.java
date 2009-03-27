package junitconverter.stages;

import junit.framework.TestCase;
import junitconverter.testcase.TestCaseClass;

/**
 * Removes the superclass from classes that inherit from JUnit's TestCase.
 *
 * @author abyx
 */
public class SuperRemovingStage extends AbstractTestConversionStage {

	/**
	 * @see junitconverter.stages.TestConversionStage#convertClass(junitconverter.testcase.TestCaseClass)
	 */
	public void convertClass(TestCaseClass testCase) {
		if (TestCase.class.getSimpleName().equals(testCase.getSuperName())
				|| TestCase.class.getName().equals(testCase.getSuperName())) {
			codeEditor.removeSuper(testCase);
		}
	}
}
