package tests.junitconverter;

import static org.junit.Assert.assertEquals;

import java.util.*;

import junitconverter.SimpleClassWriter;

import org.junit.Test;


/**
 * Tests the {@link SimpleClassWriter} class.
 *
 * @author abyx
 */
public class SimpleClassWriterTest {

	private static final String chars = "abcdefghijklmnopqrstuvwxyz" +
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=+_";

	private Random rand = new Random();
	
	@Test
	public void keepsTrackOfMultipleLineInsertions() throws Exception {
		List<String> lines = new ArrayList<String>();
		lines.add("public void testFirst() {");
		lines.add("}");
		lines.add("public void testSecond() {");
		lines.add("}");
		lines.add("public void testThird() {");
		lines.add("}");
		
		SimpleClassWriter writer = new SimpleClassWriter(lines);
		writer.insertLine(2, "A"); // Add before second
		writer.insertLine(0, "A"); // Add before first
		writer.insertLine(4, "A"); // Add before third
		
		// Add it at the expected locations
		lines.add(0, "A");
		lines.add(3, "A");
		lines.add(6, "A");
		
		assertEquals("Line changes weren't handled correctly", 
				lines, writer.result());
	}

	@Test
	public void writingNothing() {
		List<String> lines = nextLines();
		
		assertEquals("Lines changed needlessly", lines, 
				new SimpleClassWriter(lines).result());
	}
	
	@Test
	public void oneValidInsertion() {
		List<String> lines = nextLines();
		
		SimpleClassWriter writer = new SimpleClassWriter(lines);
		String lineToInsert = "Hello";
		int lineNumber = 1;
		writer.insertLine(lineNumber, lineToInsert);
		lines.add(lineNumber, lineToInsert);
		assertEquals(lines, writer.result());
	}

	@Test
	public void oneValidReplacement() {
		List<String> lines = nextLines();
		SimpleClassWriter writer = new SimpleClassWriter(lines);
		
		int lineNumber = 1;
		String newLine = lines.get(lineNumber) + "Hello";
		
		lines.remove(lineNumber);
		lines.add(lineNumber, newLine);
		
		writer.replaceLine(lineNumber, newLine);
		
		assertEquals(lines, writer.result());
	}
	
	private List<String> nextLines() {
		int numLines = rand.nextInt(200) + 1;
		List<String> lines = new LinkedList<String>();
		
		for (int i = 0; i < numLines; i++) {
			lines.add(nextLine());
		}
		return lines;
	}

	private String nextLine() {
		int length = rand.nextInt(80) + 1;
		StringBuilder randLine = new StringBuilder();
		for (int i = 0; i < length; i++) {
			randLine.append(chars.charAt(rand.nextInt(chars.length())));
		}
		return randLine.toString();
	}
}
