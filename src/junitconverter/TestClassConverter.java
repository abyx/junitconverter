package junitconverter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import junit.framework.TestCase;
import junitconverter.stages.*;
import junitconverter.testcase.*;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class TestClassConverter {

	private static final String STATS_URL = "http://codelord.net/stats.php";
	private static final String NOSTATS_FLAG = "--nostats";
	private final List<TestConversionStage> stages = 
		new ArrayList<TestConversionStage>();
	
	public TestClassConverter() {
		stages.add(new SuperRemovingStage());
		stages.add(new VisibilityAdaptionStage());
		stages.add(new PreperationMethodsAnnotationStage());
		stages.add(new TestMethodsAnnotationStage());
		stages.add(new AssertsImportingStage());
		stages.add(new AnnotationsImportingStage());
	}
	
	public void convert(File inputFile, File outputFile) 
			throws IOException, RecognitionException {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRFileStream(inputFile.getAbsolutePath()))));
		parser.compilationUnit();
		
		if (!isTestCase(parser, inputFile)) {
			return;
		}
		
		List<String> newLines = runConversion(inputFile, parser);
		writeChanges(outputFile, newLines);
	}

	private void writeChanges(File outputFile, List<String> newLines)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		try {
			for (String line : newLines) {
				writer.write(line);
				writer.write("\n");
			}
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}

	private List<String> runConversion(File inputFile, JavaParser parser)
			throws FileNotFoundException, IOException {
		List<String> lines = readLines(inputFile);
		
		TestCaseClass testCaseClass = buildTestCaseClass(parser, lines);

		ClassWriter classWriter = new SimpleClassWriter(lines);
		CodeEditor codeEditor = new SimpleCoderEditor(classWriter);
		
		for (TestConversionStage stage : stages) {
			stage.setCodeEditor(codeEditor);
		}
		
		for (TestConversionStage stage : stages) {
			stage.convertClass(testCaseClass);
		}
		
		List<String> newLines = classWriter.result();
		return newLines;
	}

	private boolean isTestCase(JavaParser parser, File inputFile) {
		if (!TestCase.class.getSimpleName().equals(parser.getSuperName()) &&
				!TestCase.class.getName().equals(parser.getSuperName())) {
			try {
				if (!TestCase.class.isAssignableFrom(
						Class.forName(parser.getFullName()))) {
					return false;
				}
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
		
		return true;
	}

	private TestCaseClass buildTestCaseClass(JavaParser parser,
			List<String> lines) {
		SetUpMethod setUpMethod = null;
		if (parser.getMethodsWithLines().containsKey("setUp")) {
			setUpMethod = 
				new SetUpMethod(parser.getMethodsWithLines().get("setUp"),
						parser.getAnnotations("setUp"));
		}
		
		TearDownMethod tearDownMethod = null;
		if (parser.getMethodsWithLines().containsKey("tearDown")) {
			tearDownMethod = new TearDownMethod(
					parser.getMethodsWithLines().get("tearDown"),
					parser.getAnnotations("tearDown"));
		}
		
		List<TestMethod> testMethods = new ArrayList<TestMethod>();
		for (String methodName : parser.getMethods()) {
			if (methodName.startsWith("test") 
					&& isVisibleEnough(parser, methodName)) {
				testMethods.add(new TestMethod(
						parser.getMethodsWithLines().get(methodName),
						parser.getAnnotations(methodName)));
			}
		}
		
		String superName = parser.getSuperName();
		TestCaseClass testCaseClass = new TestCaseClass(
				lines, setUpMethod, tearDownMethod, testMethods, superName);
		
		testCaseClass.setExtendsLine(parser.getSuperLine());
		testCaseClass.setOverrideAnnotationsLines(
				new LinkedList<Integer>(parser.getOverrideAnnotationsLines()));
		testCaseClass.setSuperConstructorInvocations(
				new LinkedList<Integer>(parser.getSuperConstructorInvocations()));
		testCaseClass.setSuperMethodInvocations(
				new LinkedList<Integer>(parser.getSuperMethodInvocations()));
		return testCaseClass;
	}

	private boolean isVisibleEnough(JavaParser parser, String methodName) {
		return (parser.getVisibility(methodName).equals(Visibility.PUBLIC.toString())
				|| parser.getVisibility(methodName).equals(Visibility.PROTECTED.toString()));
	}

	private List<String> readLines(File inputFile)
			throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<String>(); 
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		try {
			while (reader.ready()) {
				lines.add(reader.readLine());
			}
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// Ignore
			}
		}
		return lines;
	}
	
	private static void listJavaFilesRecursively(File dir, List<File> files) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				listJavaFilesRecursively(file, files);
			} else {
				if (file.getName().endsWith(".java")) {
					files.add(file);
				}
			}
		}
	}
	
	public static void main(String[] args) 
			throws IOException, RecognitionException {
		boolean sendStats = true;
		File rootFile;
		
		if (args.length < 1 || args.length > 2) {
			usage();
		}
		if (args.length == 2) {
			if (! NOSTATS_FLAG.equals(args[0])) {
				usage();
			}
			
			sendStats = false;
			rootFile = new File(args[1]);
		} else {
			rootFile = new File(args[0]);
		}
		
		if (sendStats) {
			sendStats();
		}
		
		
		List<File> files = new LinkedList<File>();
		if (rootFile.isDirectory()) {
			listJavaFilesRecursively(rootFile, files);
		} else {
			files.add(rootFile);
		}
		TestClassConverter testClassConverter = new TestClassConverter();
		for (File file : files) {
			testClassConverter.convert(file, file);
		}
	}

	private static void sendStats() {
		try {
			URLConnection connection = new URL(STATS_URL).openConnection();
			connection.setReadTimeout(1000);
			connection.setConnectTimeout(1000);
			connection.connect();
		} catch (IOException e) {
			// Too bad.
		}
	}

	private static void usage() {
		System.err.println("Usage: java " + 
				TestClassConverter.class.getName() + 
				" [" + NOSTATS_FLAG + "] <src dir>");
		System.exit(1);
	}
}
