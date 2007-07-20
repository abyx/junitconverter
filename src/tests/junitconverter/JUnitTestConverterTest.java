/*
 * Created by abyx on Jul 6, 2007.
 */
package tests.junitconverter;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import junit.framework.TestCase;
import junitconverter.JUnitTestConverter;

import org.junit.*;


/**
 * Tests {@link JUnitTestConverter}.
 *
 * @author abyx
 */
public class JUnitTestConverterTest {

	private File destDir;
	private File sourceDir;

	@Before
	public void setUp() {
		destDir = createTempDir(System.getProperty("java.io.tmpdir"),
				"dest-dir-tests" + System.currentTimeMillis());
		sourceDir = createTempDir(
				System.getProperty("java.io.tmpdir"), 
				"source-dir-tests" + System.currentTimeMillis());
	}
	
	@After
	public void tearDown() {
		for (File file : destDir.listFiles()) {
			file.delete();
		}
		destDir.delete();
		for (File file : sourceDir.listFiles()) {
			file.delete();
		}
		sourceDir.delete();
	}

	@Test
	public void testAnnotationsImportsAdded() throws IOException {
		File testCase = File.createTempFile("Test", ".java", sourceDir);
		createRandomTestCase(testCase);
		
		JUnitTestConverter converter = 
			new JUnitTestConverter(new File[] {testCase}, destDir);
		assertEquals("Wrong number of converted tests", 1, converter.convert());
		BufferedReader reader = createTestCaseReader(testCase);
		
		String line = reader.readLine();
		List<String> lines = new LinkedList<String>();
		while (line != null && !line.startsWith("public class")) {
			lines.add(line);
			line = reader.readLine();
		}
		reader.close();
		
		assertTrue("Test import not found" + lines, 
				lines.contains("import " + Test.class.getName() + ";"));
		assertTrue("Before import not found" + lines, 
				lines.contains("import " + Before.class.getName() + ";"));
		assertTrue("After import not found" + lines, 
				lines.contains("import " + After.class.getName() + ";"));
	}

	@Test
	public void testStaticImportAdded() throws IOException {
		File testCase = File.createTempFile("Test", ".java", sourceDir);
		createRandomTestCase(testCase);
		
		JUnitTestConverter converter = 
			new JUnitTestConverter(new File[] { testCase }, destDir);
		assertEquals("Wrong number of tests converted", 1, converter.convert());
		
		BufferedReader reader = createTestCaseReader(testCase);
		String line = reader.readLine();
		boolean importFound = false;
		while (line != null && !line.startsWith("public class")) {
			if (line.equals(
					"import static " + Assert.class.getName() + ".*;")) {
				importFound = true;
				break;
			}
			line = reader.readLine();
		}
		reader.close();
		assertTrue("Import not found", importFound);
	}
	
	@Test
	public void testAnnotationsAdded() throws IOException {
		File testCase = File.createTempFile("Test", ".java", sourceDir);
		TestCaseLinesToAnnotate linesToAnnotate = 
			createRandomTestCase(testCase);
		List<String> trimmedTestsLines = new LinkedList<String>();
		for (String string : linesToAnnotate.tests) {
			trimmedTestsLines.add(string.trim());
		}
		JUnitTestConverter converter = 
			new JUnitTestConverter(new File[] { testCase }, destDir);
		assertEquals("Wrong number of tests converted", 1, converter.convert());
		
		BufferedReader reader = createTestCaseReader(testCase);
		String prevLine = null;
		String currLine = reader.readLine();
		while (currLine != null) {
			currLine = currLine.trim();
			if (linesToAnnotate.setUp.trim().equals(currLine)) {
				assertEquals("Set up method wasn't annotated", 
						"@" + Before.class.getSimpleName(), prevLine);
			} else if (linesToAnnotate.tearDown.trim().equals(currLine)) {
				assertEquals("Tear down method wasn't annotated", 
						"@" + After.class.getSimpleName(), prevLine);
			} else if (trimmedTestsLines.contains(currLine)) {
				assertEquals("A test wasn't annotated",
						"@" + Test.class.getSimpleName(), prevLine);
			}
			prevLine = currLine;
			currLine = reader.readLine();
		}
		reader.close();
	}

	private BufferedReader createTestCaseReader(File testCase) throws FileNotFoundException {
		return new BufferedReader(new FileReader(
				new File(destDir, testCase.getName())));
	}
	
	private TestCaseLinesToAnnotate createRandomTestCase(File testCase) throws IOException {
		TestCaseLinesToAnnotate linesToAnnotate = new TestCaseLinesToAnnotate();
		testCase.deleteOnExit();
		FileWriter writer = new FileWriter(testCase);
		writeImports(writer);
		String testCaseClassName = 
			testCase.getName().substring(0, testCase.getName().length() - 5);
		
		writer.write("public class " + testCaseClassName 
				+ " extends TestCase {\n");
		
		String setUp = "    protected void setUp() {}\n";
		linesToAnnotate.setUp = setUp;
		writer.write(setUp);
		String tearDown = "    protected void tearDown() {}\n";
		writer.write(tearDown);
		linesToAnnotate.tearDown = tearDown;
		
		linesToAnnotate.tests = new LinkedList<String>();
		int testMethods = new Random().nextInt(5) + 1;
		for (int i = 0; i < testMethods; i++) {
			String test = "    public void testNum" + i + "(){}\n";
			writer.write(test);
			linesToAnnotate.tests.add(test);
		}
		
		writer.write("}\n");
		
		writer.close();
		
		return linesToAnnotate;
	}

	private void writeImports(FileWriter writer) throws IOException {
		List<Class<? extends Object>> importClasses = 
			Arrays.asList(TestCase.class, String.class, Integer.class);
		Collections.shuffle(importClasses);
		for (Class<? extends Object> importClass : importClasses) {
			writeImport(writer, importClass);
		}
	}

	private void writeImport(Writer writer, Class clazz) throws IOException {
		writer.write("import " + clazz.getName() + ";\n");
	}
	
	private File createTempDir(String parent, String child) {
		File newDir = new File(parent, child);
		newDir.mkdirs();
		newDir.deleteOnExit();
		return newDir;
	}
	
	private static class TestCaseLinesToAnnotate {
		public String setUp;
		public String tearDown;
		public List<String> tests;
	}
}
