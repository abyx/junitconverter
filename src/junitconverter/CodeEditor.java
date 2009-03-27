package junitconverter;

import java.lang.annotation.Annotation;

import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestCaseMethod;

/**
 * A <code>CodeEditor</code> is used for manipulating the test cases during
 * conversion.
 *
 * @author abyx
 */
public interface CodeEditor {

	/**
	 * Adds an annotation to a method.
	 * Doesn't check whether the annotation is already there or not.
	 * 
	 * @param testCaseClass The class we're manipulating.
	 * @param testMethod The method to be annotated.
	 * @param annotation The annotation class.
	 */
	void annotateMethod(TestCaseClass testCaseClass, TestCaseMethod method,
			Class<? extends Annotation> annotation);

	/**
	 * Adds an <code>import</code> statement to the requested class.
	 * Does not check whether the import is already there.
	 * The new import is added where the first import is currently (we assume
	 * there is always at least one import line at the beginning of a class). 
	 * 
	 * @param testCaseClass The class to add the import to.
	 * @param klass The class to be imported.
	 */
	void importClass(TestCaseClass testCaseClass, Class<?> klass);

	/**
	 * Changes the visibility of the given method to the given visibility.
	 * 
	 * @param testCaseClass The class we're editing.
	 * @param method The method to change.
	 * @param visibility The new visibility.
	 */
	void changeVisiblity(TestCaseClass testCaseClass, TestCaseMethod method,
			Visibility visibility);

	/**
	 * Adds a static import for all of a class' static data at the same location
	 * as {@link #importClass(TestCaseClass, Class)}.
	 * 
	 * @param testCaseClass The class to insert the import to.
	 * @param klass The class to statically import.
	 */
	void importStaticClass(TestCaseClass testCaseClass, Class<?> klass);
	
	/**
	 * Removes the superclass from the class. This means removing:
	 * <ul>
	 * 	<li><code>extends</code> clause
	 *  <li><code>super()</code> constructor invocations
	 *  <li>super method invocations
	 * </ul>
	 * 
	 * @param testCaseClass The class to modify.
	 */
	void removeSuper(TestCaseClass testCaseClass);
}
