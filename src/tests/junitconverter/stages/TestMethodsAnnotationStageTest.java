package tests.junitconverter.stages;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import junitconverter.*;
import junitconverter.stages.TestMethodsAnnotationStage;
import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestMethod;

import static org.easymock.EasyMock.*;

/**
 * Tests the {@link TestMethodsAnnotationStage} class.
 *
 * @author abyx
 */
public class TestMethodsAnnotationStageTest {

	private CodeEditor mock;
	private TestMethodsAnnotationStage testedStage;
	
	/* --- Test Organization --- */

	@Before
	public void setUp() {
		mock = createMock(CodeEditor.class);
		testedStage = new TestMethodsAnnotationStage();
		testedStage.setCodeEditor(mock);
	}
	
	/* --- Tests --- */
	
	@Test
	public void noChangesWhenNoMethods() {
		// We do not expect any editing
		replay(mock);
		testedStage.convertClass(new TestCaseClass(null));
		verify(mock);
	}
	
	@Test
	public void annotatingTestMethod() {
		TestMethod testMethod = new TestMethod(0);
		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList(),
					Collections.singletonList(testMethod));
		mock.annotateMethod(testCaseClass, testMethod, Test.class);
		replay(mock);
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
}
