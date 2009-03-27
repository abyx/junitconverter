package junitconverter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestCaseMethod;

/**
 * A simple implementation of {@link CodeEditor}.
 *
 * @author abyx
 */
public class SimpleCoderEditor implements CodeEditor {

	private static final String IMPORT_PREFIX = "import ";
	
	private final ClassWriter classWriter;
	
	public SimpleCoderEditor(ClassWriter classWriter) {
		this.classWriter = classWriter;
	}
	
	/**
	 * @see junitconverter.CodeEditor#annotateMethod(junitconverter.testcase.TestCaseClass, junitconverter.testcase.TestCaseMethod, java.lang.Class)
	 */
	public void annotateMethod(TestCaseClass testCaseClass,
			TestCaseMethod method, Class<? extends Annotation> annotation) {
		String methodLine = testCaseClass.getLines().get(method.getLine());
		String indent = extractIndent(methodLine);
		classWriter.insertLine(testCaseClass, method.getLine(), 
				indent + "@" + annotation.getSimpleName());

	}
	
	/**
	 * @see junitconverter.CodeEditor#importClass(junitconverter.testcase.TestCaseClass, java.lang.Class)
	 */
	public void importClass(TestCaseClass testCaseClass, Class<?> klass) {
		classWriter.insertLine(testCaseClass, 
				findFirstImport(testCaseClass.getLines()), 
				IMPORT_PREFIX + klass.getName() + ";");
	}

	/**
	 * @see junitconverter.CodeEditor#changeVisiblity(junitconverter.testcase.TestCaseClass, junitconverter.testcase.TestCaseMethod, junitconverter.Visibility)
	 */
	public void changeVisiblity(TestCaseClass testCaseClass,
			TestCaseMethod method, Visibility visibility) {
		String line = testCaseClass.getLines().get(method.getLine());

		Visibility oldVisibility = extractVisibility(line);
		
		classWriter.replaceLine(testCaseClass, method.getLine(), 
				line.replaceFirst(
						oldVisibility.toString(), visibility.toString()));
	}

	/**
	 * @see junitconverter.CodeEditor#importStaticClass(junitconverter.testcase.TestCaseClass, java.lang.Class)
	 */
	public void importStaticClass(TestCaseClass testCaseClass, Class<?> klass) {
		classWriter.insertLine(testCaseClass, 
				findFirstImport(testCaseClass.getLines()), 
				IMPORT_PREFIX + "static " + klass.getName() + ".*;");
	}

	/**
	 * @see junitconverter.CodeEditor#removeSuper(junitconverter.testcase.TestCaseClass)
	 */
	public void removeSuper(TestCaseClass testCaseClass) {
		if (hasExtendsClause(testCaseClass)) {
			removeExtendsClause(testCaseClass);
		}
		
		removeSuperCtorInvocations(testCaseClass);
		
		removeSuperMethodInvocations(testCaseClass);
		
		removeOverrideAnnotations(testCaseClass);
	}

	/* --- Helper Methods --- */
	
	private int findFirstImport(List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).startsWith(IMPORT_PREFIX)) {
				return i;
			}
		}
		
		throw new IllegalArgumentException("Class doesn't contain imports");
	}
	
	/**
	 * @param line The line.
	 * @return The indentation at the beginning of the line.
	 */
	private String extractIndent(String line) {
		StringBuilder indent = new StringBuilder();
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
				indent.append(line.charAt(i));
			} else {
				break;
			}
		}
		return indent.toString();
	}
	
	/**
	 * Used to find the current visibility of a method.
	 * We search for the first occurrence of a visibility in the line and return
	 * it (in case someone decides to name his method 'privateTest' or 
	 * whatever).
	 * 
	 * @param line The line to get the visibility of.
	 * @return The visibility in the line.
	 */
	private Visibility extractVisibility(String line) {
		Pattern pattern = Pattern.compile(visibilityPattern());
		Matcher matcher = pattern.matcher(line);
		matcher.find();
		
		String stringVisibility = 
			line.substring(matcher.start(), matcher.end());
		for (Visibility visibility : Visibility.values()) {
			if (stringVisibility.equals(visibility.toString())) {
				return visibility;
			}
		}

		throw new IllegalArgumentException("Couldn't find a visibility");
	}

	/**
	 * @return The pattern for matching a visibility.
	 */
	private String visibilityPattern() {
		StringBuilder patternBuilder = new StringBuilder();
		for (Visibility visibility : Visibility.values()) {
			patternBuilder.append(visibility);
			patternBuilder.append('|');
		}
		// Remove the extra pipe
		patternBuilder.setLength(patternBuilder.length() - 1);
		return patternBuilder.toString();
	}
	
	private void removeExtendsClause(TestCaseClass testCaseClass) {
		String definitionWithExtends = 
			testCaseClass.getLines().get(testCaseClass.getExtendsLine());
		String definitionWithoutExtends = 
			definitionWithExtends.replaceFirst(
					"\\s*extends\\s+" 
						+ Pattern.quote(testCaseClass.getSuperName()), "");
		classWriter.replaceLine(testCaseClass, testCaseClass.getExtendsLine(), 
				definitionWithoutExtends);
	}

	private boolean hasExtendsClause(TestCaseClass testCaseClass) {
		return testCaseClass.getSuperName() != null;
	}
	
	private void removeSuperMethodInvocations(TestCaseClass testCaseClass) {
		commentOutLines(testCaseClass, 
				testCaseClass.getSuperMethodInvocations());
	}

	private void removeSuperCtorInvocations(TestCaseClass testCaseClass) {
		commentOutLines(testCaseClass, 
				testCaseClass.getSuperConstructorInvocations());
	}

	private void removeOverrideAnnotations(TestCaseClass testCaseClass) {
		commentOutLines(testCaseClass, testCaseClass.getOverrideAnnotationsLines());
	}

	private void commentOutLines(
			TestCaseClass testCaseClass, List<Integer> lines) {
		for (int line : lines) {
			classWriter.replaceLine(testCaseClass, line, 
					"//" + testCaseClass.getLines().get(line));
		}
	}
}
