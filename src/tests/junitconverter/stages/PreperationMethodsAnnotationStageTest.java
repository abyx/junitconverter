package tests.junitconverter.stages;

import java.util.Collections;

import junitconverter.*;
import junitconverter.stages.PreperationMethodsAnnotationStage;
import junitconverter.testcase.SetUpMethod;
import junitconverter.testcase.TearDownMethod;
import junitconverter.testcase.TestCaseClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;


/**
 * Tests the {@link PreperationMethodsAnnotationStage} class.
 *
 * @author abyx
 */
public class PreperationMethodsAnnotationStageTest {

	/* --- Tests Organization --- */
	
	private CodeEditor mock;
	private PreperationMethodsAnnotationStage testedStage;

	@Before
	public void setUp() {
		mock = createMock(CodeEditor.class);
		testedStage = new PreperationMethodsAnnotationStage();
		testedStage.setCodeEditor(mock);
	}
	
	/* --- Tests --- */
	
	@Test
	public void handlingNoSetUp() {
		// We do not expect anything
		replay(mock);
		testedStage.convertClass(new TestCaseClass(null));
		verify(mock);
	}
	
	@Test
	public void annotatingSetUpMethod() {
		SetUpMethod setUpMethod = new SetUpMethod(0);
		TestCaseClass testCaseClass = 
			new TestCaseClass(
					Collections.<String>emptyList(), setUpMethod, null);
		mock.annotateMethod(testCaseClass, setUpMethod, Before.class);
		replay(mock);
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void annotatingTearDownMethod() {
		TearDownMethod tearDownMethod = new TearDownMethod(0);
		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList(), null, 
					tearDownMethod, null, null);
		mock.annotateMethod(testCaseClass, tearDownMethod, After.class);
		replay(mock);
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
}
