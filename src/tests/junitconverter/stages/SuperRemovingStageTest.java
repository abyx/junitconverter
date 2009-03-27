package tests.junitconverter.stages;

import static org.easymock.EasyMock.*;

import java.util.Collections;

import junit.framework.TestCase;
import junitconverter.CodeEditor;
import junitconverter.stages.SuperRemovingStage;
import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestMethod;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link SuperRemovingStage} class.
 *
 * @author abyx
 */
public class SuperRemovingStageTest {

	private CodeEditor mock;
	private SuperRemovingStage testedStage;
	
	/* --- Tests Organization --- */
	
	@Before
	public void setUp() {
		mock = createMock(CodeEditor.class);
		testedStage = new SuperRemovingStage();
		testedStage.setCodeEditor(mock);
	}
	
	/* --- Tests --- */
	
	/**
	 * Makes sure that nothing is done if the class doesn't extend any type.
	 */
	@Test
	public void noSuperRemovedWhenNotExtended() throws Exception {
		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList(), 
					Collections.<TestMethod>emptyList());
	
		// We expect nothing to be done
		replay(mock);
		
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	/**
	 * Makes sure that nothing is done if the class extends some arbitrary type.
	 */
	@Test
	public void noSuperRemovedWhenExtendsRandomClass() throws Exception {
		TestCaseClass testCaseClass = 
			new TestCaseClass(Collections.<String>emptyList(), null, null, 
					Collections.<TestMethod>emptyList(), 
					Object.class.getName());
		
		// We expect nothing to be done
		replay(mock);
		
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	/**
	 * Makes sure that the super is removed if the extends clause specifies
	 * TestCase.
	 */
	@Test
	public void removesSuperWhenExtendsTestCase() throws Exception {
		TestCaseClass testCaseClass =
			new TestCaseClass(Collections.<String>emptyList(),
					null, null, Collections.<TestMethod>emptyList(), 
					TestCase.class.getSimpleName());
		
		mock.removeSuper(testCaseClass);
		replay(mock);
		
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	/**
	 * Makes sure that the super is removed if the extends clause specifies
	 * TestCase with a fully-qualified name.
	 */
	@Test
	public void removesSuperWhenExtendsTestCaseWithFullName() throws Exception {
		TestCaseClass testCaseClass =
			new TestCaseClass(Collections.<String>emptyList(),
					null, null, Collections.<TestMethod>emptyList(), 
					TestCase.class.getName());
		
		mock.removeSuper(testCaseClass);
		replay(mock);
		
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
}
