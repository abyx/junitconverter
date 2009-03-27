package junitconverter.stages;

import junitconverter.CodeEditor;
import junitconverter.testcase.TestCaseClass;

/**
 * This is a stage in the conversion of a test case.
 *
 * @author abyx
 */
public interface TestConversionStage {

	public void setCodeEditor(CodeEditor editor);
	
	public void convertClass(TestCaseClass testCase);
}
