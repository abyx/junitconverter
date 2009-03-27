package tests.junitconverter.stages;

import java.util.Collections;

import junitconverter.CodeEditor;
import junitconverter.stages.AnnotationsImportingStage;
import junitconverter.testcase.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * 
 *
 * @author abyx
 */
public class AnnotationsImportingStageTest {

	private CodeEditor mock;
	private AnnotationsImportingStage testedStage;
	
	/* --- Tests Organization --- */
	
	@Before
	public void setUp() {
		mock = createMock(CodeEditor.class);
		testedStage = new AnnotationsImportingStage();
		testedStage.setCodeEditor(mock);
	}
	
	/* --- Tests --- */
	
	@Test
	public void noImportingWhenEmpty() {
		// We expect no changes
		replay(mock);
		testedStage.convertClass(new TestCaseClass(null));
		verify(mock);
	}
	
	@Test
	public void importingBeforeAnnotation() {
		TestCaseClass testCaseClass = 
			new TestCaseClass(
					Collections.<String>emptyList(), new SetUpMethod(0), null);
		mock.importClass(testCaseClass, Before.class);
		replay(mock);
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void importingTestAnnotation() {
		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList(),
					Collections.singletonList(new TestMethod(0)));
		mock.importClass(testCaseClass, Test.class);
		replay(mock);
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void importingAfterAnnotation() {
		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList(), null, 
					new TearDownMethod(0), null, null);
		mock.importClass(testCaseClass, After.class);
		replay(mock);
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
}
