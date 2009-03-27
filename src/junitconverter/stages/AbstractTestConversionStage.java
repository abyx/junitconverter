package junitconverter.stages;

import junitconverter.CodeEditor;


public abstract class AbstractTestConversionStage 
		implements TestConversionStage {

	protected CodeEditor codeEditor;

	public void setCodeEditor(CodeEditor codeEditor) {
		this.codeEditor = codeEditor;
	}
}
