package tests.junitconverter.stages;


import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import junitconverter.CodeEditor;
import junitconverter.stages.AssertsImportingStage;
import junitconverter.testcase.TestCaseClass;

import static org.easymock.EasyMock.*;

/**
 * Tests the {@link AssertsImportingStage} class.
 *
 * @author abyx
 */
public class AssertImportingStageTest {

	/* --- Tests --- */

	@Test
	public void assertStaticImportInesrtion() {
		CodeEditor mock = createMock(CodeEditor.class);

		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList());
		mock.importStaticClass(testCaseClass, Assert.class);
		replay(mock);
		
		AssertsImportingStage stage = new AssertsImportingStage();
		stage.setCodeEditor(mock);
		stage.convertClass(testCaseClass);
		verify(mock);
	}
}
