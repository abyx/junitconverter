package junitconverter.stages;

import junitconverter.testcase.TestCaseClass;

import org.junit.Assert;

/**
 * Adds a static import for the {@link Assert} methods.
 *
 * @author abyx
 */
public class AssertsImportingStage extends AbstractTestConversionStage {

	public void convertClass(TestCaseClass testCase) {
		codeEditor.importStaticClass(testCase, Assert.class);
	}
}
