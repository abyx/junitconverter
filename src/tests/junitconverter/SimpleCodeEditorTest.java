package tests.junitconverter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.*;

import junitconverter.*;
import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestMethod;

import org.junit.*;

public class SimpleCodeEditorTest {

	private static final String IMPORT_PREFIX = "import ";
	
	private static final String PACKAGE_PREFIX = "package ";

	private static final Class<?>[] classes = 
		{ Test.class, Before.class, After.class };
	
	private Random rand = new Random();
	private ClassWriter mock;
	private CodeEditor codeEditor;
	
	/* --- Tests Organization --- */
	
	@Before
	public void setUp() {
		mock = createMock(ClassWriter.class);
		codeEditor = new SimpleCoderEditor(mock);
	}
	
	/* --- Tests --- */

	@Test
	public void methodAnnotation() {
		// Create a random test class
		List<String> lines = getEmptyLines();
		int methodLine = rand.nextInt(lines.size());
		String indent = randomWhiteSpaces();
		lines.add(methodLine, indent + "bla bla");
		TestMethod testMethod = new TestMethod(methodLine + 1);
		TestCaseClass testCaseClass = 
			new TestCaseClass(lines, Collections.singletonList(testMethod));
		Class<Test> annotation = Test.class;
		// Annotate a method
		mock.insertLine(methodLine, indent + "@" + annotation.getSimpleName());
		replay(mock);
		
		codeEditor.annotateMethod(testCaseClass, testMethod, annotation);
		verify(mock);
	}
	
	@Test
	public void doesNotAddAnnotationsTwice() throws Exception {
		// Create a random test class
		List<String> lines = getEmptyLines();
		int methodLine = rand.nextInt(lines.size());
		String indent = randomWhiteSpaces();
		lines.add(methodLine, indent + "bla bla");
		
		Class<Test> annotation = Test.class;
		TestMethod testMethod = new TestMethod(methodLine + 1, 
				Collections.singletonList(annotation.getSimpleName()));
		TestCaseClass testCaseClass = 
			new TestCaseClass(lines, Collections.singletonList(testMethod));
		
		// Nothing should happen to the method
		replay(mock);
		
		codeEditor.annotateMethod(testCaseClass, testMethod, annotation);
		verify(mock);
	}
	
	@Test
	public void doesNotAddAnnotationsTwiceWithFullAnnotationName() 
			throws Exception {
		// Create a random test class
		List<String> lines = getEmptyLines();
		int methodLine = rand.nextInt(lines.size());
		String indent = randomWhiteSpaces();
		lines.add(methodLine, indent + "bla bla");
		
		Class<Test> annotation = Test.class;
		TestMethod testMethod = new TestMethod(methodLine + 1, 
				Collections.singletonList(annotation.getName()));
		TestCaseClass testCaseClass = 
			new TestCaseClass(lines, Collections.singletonList(testMethod));
		
		// Nothing should happen to the method
		replay(mock);
		
		codeEditor.annotateMethod(testCaseClass, testMethod, annotation);
		verify(mock);
	}
	
	@Test
	public void addingImport() {
		List<String> lines = getEmptyLines();

		// We insert a few imports, as there needs to be at least one
		int firstImport = insertImports(lines);
		
		TestCaseClass testCaseClass = new TestCaseClass(lines);
		
		Class<?> importedClass = randomClass();
		mock.insertLine(firstImport, IMPORT_PREFIX + importedClass.getName() + ";");
		replay(mock);
		
		codeEditor.importClass(testCaseClass, importedClass);
		verify(mock);
	}
	
	@Test
	public void addingImportWithNoOtherImports() {
		List<String> lines = getEmptyLines();

		TestCaseClass testCaseClass = new TestCaseClass(lines);
		
		Class<?> importedClass = randomClass();
		mock.insertLine(0, IMPORT_PREFIX + importedClass.getName() + ";");
		replay(mock);
		
		codeEditor.importClass(testCaseClass, importedClass);
		verify(mock);
	}
	
	@Test
	public void addingStaticImport() {
		List<String> lines = getEmptyLines();
		int firstImport = insertImports(lines);
		
		TestCaseClass testCaseClass = new TestCaseClass(lines);
		Class<?> importedClass = Assert.class;
		mock.insertLine(firstImport, IMPORT_PREFIX + "static " + importedClass.getName() + ".*;");
		
		replay(mock);
		
		codeEditor.importStaticClass(testCaseClass, importedClass);
		verify(mock);
	}
	
	@Test
	public void addingStaticImportWithNoOtherImports() {
		List<String> lines = getEmptyLines();
		
		TestCaseClass testCaseClass = new TestCaseClass(lines);
		Class<?> importedClass = Assert.class;
		mock.insertLine(0, IMPORT_PREFIX + "static " + importedClass.getName() + ".*;");
		
		replay(mock);
		
		codeEditor.importStaticClass(testCaseClass, importedClass);
		verify(mock);
	}

	@Test
	public void addingStaticImportWithNoOtherImportsAndPackage() {
		List<String> lines = getEmptyLines();

		lines.add(0, PACKAGE_PREFIX + "something;");
		TestCaseClass testCaseClass = new TestCaseClass(lines);
		Class<?> importedClass = Assert.class;
		mock.insertLine(1, IMPORT_PREFIX + "static " + importedClass.getName() + ".*;");
		
		replay(mock);
		
		codeEditor.importStaticClass(testCaseClass, importedClass);
		verify(mock);
	}
	
	@Test
	public void changingVisibility() {
		List<String> lines = getEmptyLines();
		int methodLine = rand.nextInt(lines.size());
		String indent = randomWhiteSpaces();
		Visibility newVisibility = randomVisibility();
		Visibility oldVisibility = newVisibility;
		String restOfLine = " void method() {";
		
		lines.add(methodLine, indent + oldVisibility + restOfLine);
		
		TestMethod testMethod = new TestMethod(methodLine + 1);
		TestCaseClass testCaseClass = 
			new TestCaseClass(lines, Collections.singletonList(testMethod));
		mock.replaceLine(methodLine, indent + newVisibility + restOfLine);
		replay(mock);
		
		codeEditor.changeVisiblity(testCaseClass, testMethod, newVisibility);
		verify(mock);
	}
	
	@Test
	public void removesExtendsClauseWhenAsked() throws Exception {
		List<String> lines = getEmptyLines();
		int extendsLine = 1;
		lines.add(extendsLine - 1, "public class Something extends TestCase {}");
		
		TestCaseClass testCaseClass = new TestCaseClass(lines, null, null, 
				Collections.<TestMethod>emptyList(), "TestCase");
		testCaseClass.setExtendsLine(extendsLine);
		
		mock.replaceLine(extendsLine - 1, "public class Something {}");
		replay(mock);
		
		codeEditor.removeSuper(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void removesSuperConstructorInvocations() throws Exception {
		List<String> lines = getEmptyLines();
		lines.add(2, "super(1);");
		lines.add(5, "super(4, 4);");
		
		TestCaseClass testCaseClass = new TestCaseClass(lines, null, null, 
				Collections.<TestMethod>emptyList(), null);
		testCaseClass.setSuperConstructorInvocations(Arrays.asList(2+1, 5+1));
		
		mock.replaceLine(2, "//" + lines.get(2));
		mock.replaceLine(5, "//" + lines.get(5));
		replay(mock);
		
		codeEditor.removeSuper(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void removesSuperMethodInvocations() throws Exception {
		List<String> lines = getEmptyLines();
		lines.add(2, "super.a(1);");
		lines.add(5, "super.a(4, 4);");
		
		TestCaseClass testCaseClass = new TestCaseClass(lines, null, null, 
				Collections.<TestMethod>emptyList(), null);
		testCaseClass.setSuperMethodInvocations(Arrays.asList(2+1, 5+1));
		
		mock.replaceLine(2, "//" + lines.get(2));
		mock.replaceLine(5, "//" + lines.get(5));
		replay(mock);
		
		codeEditor.removeSuper(testCaseClass);
		verify(mock);
	}
	
	@Test
	public void removesOverrideAnnotations() throws Exception {
		List<String> lines = getEmptyLines();
		lines.add(2, "@Override");
		lines.add(5, "@Override");
		
		TestCaseClass testCaseClass = new TestCaseClass(lines, null, null, 
				Collections.<TestMethod>emptyList(), null);
		testCaseClass.setOverrideAnnotationsLines(Arrays.asList(2+1, 5+1));
		
		mock.replaceLine(2, "//" + lines.get(2));
		mock.replaceLine(5, "//" + lines.get(5));
		replay(mock);
		
		codeEditor.removeSuper(testCaseClass);
		verify(mock);
	}
	
	/* --- Helper Methods --- */
	
	private int insertImports(List<String> lines) {
		int firstImport = rand.nextInt(lines.size());
		lines.add(firstImport, IMPORT_PREFIX + String.class.getName());
		lines.add(firstImport, IMPORT_PREFIX + Integer.class.getName());
		return firstImport;
	}
	
	private Visibility randomVisibility() {
		return Visibility.values()[rand.nextInt(Visibility.values().length)];
	}
	
	/**
	 * @return A random string of spaces or tabs.
	 */
	private String randomWhiteSpaces() {
		int amount = rand.nextInt(5);
		StringBuilder spaces = new StringBuilder();
		for (int i = 0; i < amount; i++) {
			if (rand.nextBoolean()) {
				spaces.append(' ');
			} else {
				spaces.append('\t');
			}
		}
		return spaces.toString();
	}

	/**
	 * @return A list of random size with empty lines.
	 */
	private List<String> getEmptyLines() {
		List<String> lines = new LinkedList<String>();
		int totalLines = rand.nextInt(30) + 10;
		for (int i = 0; i < totalLines; i++) {
			lines.add(new String());
		}
		return lines;
	}
	
	private Class<?> randomClass() {
		return classes[rand.nextInt(classes.length)];
	}
}
