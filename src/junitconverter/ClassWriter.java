package junitconverter;

import java.util.List;

import junitconverter.testcase.TestCaseClass;

/**
 * The actual interface that writes back a class after it has been changed.
 *
 * @author abyx
 */
public interface ClassWriter {

	/**
	 * Inserts a line of code at the given index of the class.
	 * 
	 * @param testCaseClass The class we're editing.
	 * @param lineNumber Where to insert the line (first is one).
	 * @param line The line to insert.
	 */
	public void insertLine(
			TestCaseClass testCaseClass, int lineNumber, String line);
	
	/**
	 * Replaces a line of code at the given index with a new line.
	 * 
	 * @param testCaseClass The class we're editing.
	 * @param lineNumber The line to replace.
	 * @param line The new line.
	 */
	public void replaceLine(
			TestCaseClass testCaseClass, int lineNumber, String line);
	
	/**
	 * @return The resulting lines of code.
	 */
	public List<String> result();
}
