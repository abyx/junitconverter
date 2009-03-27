package tests.junitconverter.stages;

import static org.easymock.EasyMock.*;

import java.util.LinkedList;
import java.util.List;

import junitconverter.CodeEditor;
import junitconverter.Visibility;
import junitconverter.stages.VisibilityAdaptionStage;
import junitconverter.testcase.SetUpMethod;
import junitconverter.testcase.TearDownMethod;
import junitconverter.testcase.TestCaseClass;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link VisibilityAdaptionStage} class.
 *
 * @author abyx
 */
public class VisibilityAdaptionStageTest {

	private CodeEditor mock;
	private VisibilityAdaptionStage testedStage;
	
	/* --- Tests Organization --- */

	@Before
	public void setUp() {
		mock = createMock(CodeEditor.class);
		testedStage = new VisibilityAdaptionStage();
		testedStage.setCodeEditor(mock);
	}
	
	/* --- Tests --- */
	
	@Test
	public void setUpAdapter() {
		List<String> lines = new LinkedList<String>();
		lines.add("    " + Visibility.PROTECTED + " void setUp() {");
		SetUpMethod setUpMethod = new SetUpMethod(1);
		TestCaseClass testCaseClass = 
			new TestCaseClass(lines, setUpMethod, null);
		mock.changeVisiblity(testCaseClass, setUpMethod, Visibility.PUBLIC);
		replay(mock);
		
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void tearDownAdapter() {
		List<String> lines = new LinkedList<String>();
		lines.add("    " + Visibility.PROTECTED + " void tearDown() {");
		TearDownMethod tearDownMethod = new TearDownMethod(1);
		TestCaseClass testCaseClass = 
			new TestCaseClass(lines, null, tearDownMethod, null, null);
		
		mock.changeVisiblity(testCaseClass, tearDownMethod, Visibility.PUBLIC);
		replay(mock);
		
		testedStage.convertClass(testCaseClass);
		verify(mock);
	}
}
