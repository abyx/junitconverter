package junitconverter.stages;

import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestCaseMethod;

import org.junit.Test;

/**
 * This stage is responsible for adding the {@link Test} annotation to test
 * methods.
 *
 * @author abyx
 */
public class TestMethodsAnnotationStage extends AbstractTestConversionStage {

	/**
	 * @see junitconverter.stages.TestConversionStage#convertClass(junitconverter.testcase.TestCaseClass)
	 */
	public void convertClass(TestCaseClass testCase) {
		if (testCase.getTestMethods() == null) {
			return;
		}
		
		for (TestCaseMethod testMethod : testCase.getTestMethods()) {
			codeEditor.annotateMethod(testCase, testMethod, Test.class);
		}
	}
}
