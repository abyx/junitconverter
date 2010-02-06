package tests.junitconverter;

import static org.junit.Assert.*;

import java.util.*;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import junitconverter.JavaLexer;
import junitconverter.JavaParser;
import junitconverter.Visibility;


/**
 * Tests the java parser.
 *
 * @author abyx
 */
public class JavaParserTest {

	/**
	 * A simple test for checking whether the parser finds simple extends 
	 * clauses.
	 */
	@Test
	public void findsSimpleExtendsClause() throws Exception {
		JavaParser parser = createParser(
				"import junit.framework.TestCase;\n"
				+ "public class MyTest extends TestCase {\n"
				+ " public void testThat() {}}");
		assertEquals("TestCase", parser.getSuperName());
		assertEquals(2, parser.getSuperLine());
		assertEquals(28, parser.getSuperPos());
	}

	/**
	 * Tests that the java parser also identifies correctly extends clauses 
	 * where the type's full name is given and not simply its name.
	 */
	@Test
	public void findsFullyQualifiedExtendsClause() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase {"
				+ "\npublic void testThat() {}}");
		assertEquals("junit.framework.TestCase", parser.getSuperName());
		assertEquals(1, parser.getSuperLine());
		assertEquals(28, parser.getSuperPos());
	}
	
	/**
	 * Tests that the java parser also identifies the extends clause correctly
	 * when followed by an implements clause.
	 */
	@Test
	public void findsExtendsClauseWithImplements() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase implements Runnable,Serializable {"
				+ "\npublic void testThat() {}}");
		assertEquals("junit.framework.TestCase", parser.getSuperName());
		assertEquals(1, parser.getSuperLine());
		assertEquals(28, parser.getSuperPos());
	}
	
	/**
	 * Tests that the parser returns the right extends clause when an inner
	 * class exists.
	 */
	@Test
	public void findsFirstExtendsClauseWhenInnerClassExists() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase {"
				+ "\npublic void testThat() {}" 
				+ " private final static class Inner {}"
				+ "}");
		assertEquals("junit.framework.TestCase", parser.getSuperName());
		assertEquals(1, parser.getSuperLine());
		assertEquals(28, parser.getSuperPos());
	}
	
	/**
	 * Checks that when there's no extends clause nothing is returned as a super
	 * even if inner classes with extends clauses are there.
	 */
	@Test
	public void findsNoSuperWhenNoExtendsClause() throws Exception {
		JavaParser parser = createParser(
				"public class NotATest {" +
				"\n public void justAMethod() {}" +
				"\n public static class Inner extends Object {}" +
				"\n}");
		assertNull("Expected no super to be found", parser.getSuperName());
	}
	
	/**
	 * Tests that the parser returns the right method names.
	 */
	@Test
	public void findsMethods() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase {"
				+ "\npublic void testThat() {}" 
				+ "\npublic void testThat2() {}" 
				+ "\npublic void testThat3() {}" 
				+ "}");
		assertEquals(asSet("testThat", "testThat2", "testThat3"), 
				parser.getMethods());
	}

	/**
	 * Tests that the parser returns the right method names even when inner
	 * classes with methods exist.
	 */
	@Test
	public void findsMethodsWithoutInnerClassMethods() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase {"
				+ "\npublic void testThat() {}" 
				+ "\npublic void testThat2() {}" 
				+ "\npublic void testThat3() {}"
				+ "\nprivate static class Inner {"
				+ "\n public void testInner1() {}"
				+ "\n public void testInner2() {}"
				+ "}"
				+ "}");
		assertEquals(asSet("testThat", "testThat2", "testThat3"), 
				parser.getMethods());
	}
	
	/**
	 * Tests that the parser returns the right method names even when the are
	 * anonymous classes with methods present.
	 */
	@Test
	public void findsMethodsWithoutAnonymousClassMethods() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase {" +
				"\npublic void testThis() {}" +
				"\npublic void testThat() {" +
				"\nnew Runnable() {" +
				"\npublic void run() {}};}" +
				"\npublic void testThose() {}" +
				"}");
		assertEquals(asSet("testThis", "testThat", "testThose"),
				parser.getMethods());
	}
	
	/**
	 * Tests that the parser returns a list of super constructor invocations.
	 */
	@Test
	public void findsSuperConstructorInvocations() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends junit.framework.TestCase {" +
				"\npublic MyTest() {" +
				"\n super(\"test\");" +
				"\n}" +
				"\npublic MyTest(int a) {" +
				"\n new Runnable() {" +
				"\n public Runnable() { super(); }};" +
				"\n}}");
		assertEquals(asSet(3), parser.getSuperConstructorInvocations());
	}
	
	/**
	 * Tests that the parser returns a list of super methods invocations.
	 */
	@Test
	public void findsSuperMethodInvocations() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {" +
						"\npublic void myTest() {" +
						"\n super.someMehthod(\"test\");" +
						"\n}" +
						"\npublic void anotherTest() {" +
						"\n new Runnable() {" +
						"\n  public Runnable() { super.test(); }" +
						"\n};}" +
						"\n}"))));
		parser.compilationUnit();
		assertEquals(asSet(3), parser.getSuperMethodInvocations());
	}
	
	@Test
	public void findsOverrideAnnotations() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest extends TestCase {" +
				"\n@Override" +
				"\npublic void setUp() {}" +
				"\n@Override" +
				"\npublic void tearDown() {}" +
				"\nclass Test {" +
				"\n @Override" +
				"\n private void a() {}" +
				"\n}}");
		assertEquals(asSet(2, 4), parser.getOverrideAnnotationsLines());
	}
	
	@Test
	public void findClassNameWithDefaultPackage() throws Exception {
		JavaParser parser = createParser("public class MyTest {}");
		assertEquals("MyTest", parser.getFullName());
	}
	
	@Test
	public void findClassNameWithPackage() throws Exception {
		JavaParser parser = createParser(
				"package test;\n" +
				"public class MyTest {}");
		assertEquals("test.MyTest", parser.getFullName());
	}
	
	@Test
	public void findClassNameWithInnerClasses() throws Exception {
		JavaParser parser = createParser(
				"package test;\n" +
				"public class MyTest {\n" +
				"    public static class WrongClass {}\n" +
				"}");
		assertEquals("test.MyTest", parser.getFullName());
	}
	
	@Test
	public void findClassNameWithOtherClassesInFile() throws Exception {
		JavaParser parser = createParser(
				"package test;\n" +
				"public class MyTest {}\n" +
				"public class WrongClass {}");
		assertEquals("test.MyTest", parser.getFullName());
	}
	
	@Test
	public void savesSingleAnnotation() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest {\n" +
				"    @Annotation\n" +
				"    public void testAnnotations() {}\n" +
				"}");
		assertEquals(Collections.singletonList("Annotation"),
				parser.getAnnotations("testAnnotations"));
	}
	
	@Test
	public void savesMultipleAnnotationsOnMethod() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest {\n" +
				"    @Annotation1\n" +
				"    @Annotation2\n" +
				"    public void testAnnotations() {}\n" +
				"}");
		assertEquals(
				Arrays.asList("Annotation1", "Annotation2"), 
				parser.getAnnotations("testAnnotations"));
	}
	
	@Test
	public void savesAnnotationsOfMultipleMethods() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest {\n" +
				"    @Annotation1\n" +
				"    public void testFirst() {}\n" +
				"    @Annotation2\n" +
				"    public void testSecond() {}\n" +
				"}");
		assertEquals(Collections.singletonList("Annotation1"),
				parser.getAnnotations("testFirst"));
		assertEquals(Collections.singletonList("Annotation2"),
				parser.getAnnotations("testSecond"));
	}
	
	@Test
	public void savesMethodAnnotationOnly() throws Exception {
		JavaParser parser = createParser(
				"@Something\n" +
				"public class MyTest {\n" +
				"    @Annotation\n" +
				"    public void testAnnotations() {}\n" +
				"}");
		assertEquals(Collections.singletonList("Annotation"), 
				parser.getAnnotations("testAnnotations"));
	}
	
	@Test
	public void handlesNoAnnotations() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest {\n" +
				"    public void testNoAnnotations() {}\n" +
				"}");
		assertEquals(new ArrayList<String>(),
				parser.getAnnotations("testNoAnnotations"));
	}
	
	@Test
	public void savesMethodVisibility() throws Exception {
		JavaParser parser = createParser(
				"public class MyTest {\n" +
				"    private void testSomething() {}\n" +
				"}");
		assertEquals(Visibility.PRIVATE.toString(), 
				parser.getVisibility("testSomething"));
	}
	
	@Test
	public void savesMethodsVisibilityEvenWithAnonClass() throws Exception {
		JavaParser parser = createParser("public class A {\n" +
			"		public void testSomething() {\n" +
			"	    Delta delta = new Delta() {\n" +
			"	      public int somethingThatReturnsValue() {\n" +
			"	        return 0;\n" +
			"	      }\n" +
			"	    };\n" +
			"}}");
		assertEquals(Visibility.PUBLIC.toString(), 
				parser.getVisibility("testSomething"));
	}
	
	/* --- Helper Methods --- */
	
	/**
	 * @return
	 */
	private <T> Set<T> asSet(T ... values) {
		return new HashSet<T>(Arrays.asList(values));
	}
	
	private JavaParser createParser(String code) throws RecognitionException {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						code))));
		parser.compilationUnit();
		return parser;
	}
}
