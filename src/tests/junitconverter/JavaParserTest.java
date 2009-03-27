package tests.junitconverter;

import static org.junit.Assert.*;

import java.util.*;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Test;

import junitconverter.JavaLexer;
import junitconverter.JavaParser;


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
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"import junit.framework.TestCase;\n"
						+ "public class MyTest extends TestCase {\n"
						+ " public void testThat() {}}"))));
		parser.compilationUnit();
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
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {"
						+ "\npublic void testThat() {}}"))));
		parser.compilationUnit();
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
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase implements Runnable,Serializable {"
						+ "\npublic void testThat() {}}"))));
		parser.compilationUnit();
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
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {"
						+ "\npublic void testThat() {}" 
						+ " private final static class Inner {}"
						+ "}"))));
		parser.compilationUnit();
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
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class NotATest {" +
						"\n public void justAMethod() {}" +
						"\n public static class Inner extends Object {}" +
						"\n}"))));
		parser.compilationUnit();
		assertNull("Expected no super to be found", parser.getSuperName());
	}
	
	/**
	 * Tests that the parser returns the right method names.
	 */
	@Test
	public void findsMethods() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {"
						+ "\npublic void testThat() {}" 
						+ "\npublic void testThat2() {}" 
						+ "\npublic void testThat3() {}" 
						+ "}"))));
		parser.compilationUnit();
		assertEquals(asSet("testThat", "testThat2", "testThat3"), 
				parser.getMethods());
	}

	/**
	 * Tests that the parser returns the right method names even when inner
	 * classes with methods exist.
	 */
	@Test
	public void findsMethodsWithoutInnerClassMethods() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {"
						+ "\npublic void testThat() {}" 
						+ "\npublic void testThat2() {}" 
						+ "\npublic void testThat3() {}"
						+ "\nprivate static class Inner {"
						+ "\n public void testInner1() {}"
						+ "\n public void testInner2() {}"
						+ "}"
						+ "}"))));
		parser.compilationUnit();
		assertEquals(asSet("testThat", "testThat2", "testThat3"), 
				parser.getMethods());
	}
	
	/**
	 * Tests that the parser returns the right method names even when the are
	 * anonymous classes with methods present.
	 */
	@Test
	public void findsMethodsWithoutAnonymousClassMethods() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {" +
						"\npublic void testThis() {}" +
						"\npublic void testThat() {" +
						"\nnew Runnable() {" +
						"\npublic void run() {}};}" +
						"\npublic void testThose() {}" +
						"}"))));
		parser.compilationUnit();
		assertEquals(asSet("testThis", "testThat", "testThose"),
				parser.getMethods());
	}
	
	/**
	 * Tests that the parser returns a list of super constructor invocations.
	 */
	@Test
	public void findsSuperConstructorInvocations() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends junit.framework.TestCase {" +
						"\npublic MyTest() {" +
						"\n super(\"test\");" +
						"\n}" +
						"\npublic MyTest(int a) {" +
						"\n new Runnable() {" +
						"\n public Runnable() { super(); }};" +
						"\n}}"))));
		parser.compilationUnit();
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
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest extends TestCase {" +
						"\n@Override" +
						"\npublic void setUp() {}" +
						"\n@Override" +
						"\npublic void tearDown() {}" +
						"\nclass Test {" +
						"\n @Override" +
						"\n private void a() {}" +
						"\n}}"))));
		parser.compilationUnit();
		assertEquals(asSet(2, 4), parser.getOverrideAnnotationsLines());
	}
	
	@Test
	public void findClassNameWithDefaultPackage() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
						"public class MyTest {}"))));
		parser.compilationUnit();
		assertEquals("MyTest", parser.getFullName());
	}
	
	@Test
	public void findClassNameWithPackage() throws Exception {
		JavaParser parser = new JavaParser(new CommonTokenStream(new JavaLexer(
				new ANTLRStringStream(
				"package test;\n" +
				"public class MyTest {}"))));
		parser.compilationUnit();
		assertEquals("test.MyTest", parser.getFullName());
	}
	
	/* --- Helper Methods --- */
	
	/**
	 * @return
	 */
	private <T> Set<T> asSet(T ... values) {
		return new HashSet<T>(Arrays.asList(values));
	}
}
