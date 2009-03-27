package junitconverter;

import java.util.*;

import junitconverter.testcase.TestCaseClass;

/**
 * 
 *
 * @author abyx
 */
public class SimpleClassWriter implements ClassWriter {

	private final List<String> lines;

	private final LineOffsets linesChanges = new LineOffsets();

	
	/**
	 * @param lines The source of the class we're writing.
	 */
	public SimpleClassWriter(List<String> lines) {
		this.lines = new ArrayList<String>(lines);
	}

	/**
	 * @see junitconverter.ClassWriter#insertLine(TestCaseClass, int, String)
	 */
	public void insertLine(TestCaseClass testCaseClass, int lineNumber,
			String line) {
		int realLine = linesChanges.getRealLinePosition(lineNumber);
		lines.add(realLine, line);
		linesChanges.addLineAt(lineNumber);
	}

	/**
	 * @see junitconverter.ClassWriter#replaceLine(TestCaseClass, int, String)
	 */
	public void replaceLine(TestCaseClass testCaseClass, int lineNumber,
			String line) {
		int realLine = linesChanges.getRealLinePosition(lineNumber);
		lines.remove(realLine);
		lines.add(realLine, line);
	}

	public List<String> result() {
		return new ArrayList<String>(lines);
	}
	
	private static class LineOffsets {

		private final List<LineOffset> offsets = new ArrayList<LineOffset>();
		
		public int getRealLinePosition(int origLine) {
			int realLine = origLine;
			for (LineOffset offset : offsets) {
				if (offset.getLine() <= origLine) {
					realLine += offset.getOffset();
				}
			}
			return realLine;
		}
		
		public void addLineAt(int origLine) {
			offsets.add(new LineOffset(getRealLinePosition(origLine), 1));
			Collections.sort(offsets);
		}
	}
	
	private static class LineOffset implements Comparable<LineOffset> {
		private int line;
		private int offset;

		public LineOffset(int line, int offset) {
			this.line = line;
			this.offset = offset;
		}
		
		/**
		 * @return the line
		 */
		public int getLine() {
			return line;
		}
		
		/**
		 * @return the offset
		 */
		public int getOffset() {
			return offset;
		}

		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(LineOffset o) {
			if (getLine() > o.getLine()) {
				return 1;
			} else if (getLine() < o.getLine()) {
				return -1;
			} else {
				return 0;
			}
		}
		
		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("[Line: %d Offset %d]", line, offset);
		}
	}
}
