package junitconverter.stages;

import junitconverter.testcase.TestCaseClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This stage is responsible for adding imports for the {@link Before}, 
 * {@link After} and {@link Test} annotations, if needed.
 *
 * @author abyx
 */
public class AnnotationsImportingStage extends AbstractTestConversionStage {

	/**
	 * @see junitconverter.stages.TestConversionStage#convertClass(junitconverter.testcase.TestCaseClass)
	 */
	public void convertClass(TestCaseClass testCase) {
		if (testCase.getSetUpMethod() != null) {
			codeEditor.importClass(testCase, Before.class);
		}
		
		if (testCase.getTestMethods() != null
				&& !(testCase.getTestMethods().isEmpty())) {
			codeEditor.importClass(testCase, Test.class);
		}
		
		if (testCase.getTearDownMethod() != null) {
			codeEditor.importClass(testCase, After.class);
		}
	}

}
