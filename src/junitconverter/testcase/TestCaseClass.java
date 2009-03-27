package junitconverter.testcase;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * This class represents an old {@link TestCase} class that's being converted.
 *
 * @author abyx
 */
public class TestCaseClass {
	
	private final List<TestMethod> testMethods;
	private final SetUpMethod setUpMethod;
	private final TearDownMethod tearDownMethod;
	private final List<String> lines;
	private final String superName;
	private int extendsLine;
	private final List<Integer> superMethodInvocations = new ArrayList<Integer>();
	private final List<Integer> superConstructorInvocations = 
		new ArrayList<Integer>();
	private final List<Integer> overrideAnnotationsLines = 
		new ArrayList<Integer>();

	/**
	 * @param lines The lines of code of this test case.
	 */
	public TestCaseClass(List<String> lines) {
		this(lines, null);
	}
	
	/**
	 * @param lines The lines of code of this test case.
	 * @param testMethods The test methods in this test case (null if none).
	 */
	public TestCaseClass(List<String> lines, List<TestMethod> testMethods) {
		this(lines, null, testMethods);
	}

	/**
	 * @param lines The lines of code of this test case.
	 * @param setUpMethod The set up method of this test case (null if none).
	 * @param testMethods The test methods in this test case (null if none).
	 */
	public TestCaseClass(List<String> lines, SetUpMethod setUpMethod,
			List<TestMethod> testMethods) {
		this(lines, setUpMethod, null, testMethods, null);
	}

	/**
	 * @param lines
	 *            The lines of code of this test case.
	 * @param setUpMethod
	 *            The set up method of this test case (null if none).
	 * @param tearDownMethod
	 *            The tear down method of this test case (null if none).
	 * @param testMethods
	 *            The test methods in this test case (null if none).
	 * @param superName
	 *            The name of the super class, as seen in the extends clause
	 *            (null if none).
	 */
	public TestCaseClass(List<String> lines, SetUpMethod setUpMethod, 
			TearDownMethod tearDownMethod, List<TestMethod> testMethods, 
			String superName) {
		this.lines = lines;
		this.setUpMethod = setUpMethod;
		this.tearDownMethod = tearDownMethod;
		this.testMethods = testMethods;
		this.superName = superName;
	}

	public List<TestMethod> getTestMethods() {
		return testMethods;
	}
	
	public SetUpMethod getSetUpMethod() {
		return setUpMethod;
	}
	
	public TearDownMethod getTearDownMethod() {
		return tearDownMethod;
	}
	
	public List<String> getLines() {
		return lines;
	}
	
	public String getSuperName() {
		return superName;
	}

	public int getExtendsLine() {
		return extendsLine;
	}

	public void setExtendsLine(int extendsLine) {
		this.extendsLine = extendsLine - 1;
	}

	public List<Integer> getSuperMethodInvocations() {
		return superMethodInvocations;
	}

	public void setSuperMethodInvocations(List<Integer> superMethodInvocations) {
		this.superMethodInvocations.clear();
		for (int line : superMethodInvocations) {
			this.superMethodInvocations.add(line - 1);
		}
	}

	public List<Integer> getSuperConstructorInvocations() {
		return superConstructorInvocations;
	}

	public void setSuperConstructorInvocations(
			List<Integer> superConstructorInvocations) {
		this.superConstructorInvocations.clear();
		for (int line : superConstructorInvocations) {
			this.superConstructorInvocations.add(line - 1);
		}
	}
	
	public List<Integer> getOverrideAnnotationsLines() {
		return overrideAnnotationsLines;
	}
	
	public void setOverrideAnnotationsLines(
			List<Integer> overrideAnnotationsLines) {
		this.overrideAnnotationsLines.clear();
		for (int line : overrideAnnotationsLines) {
			this.overrideAnnotationsLines.add(line - 1);
		}
	}
}
