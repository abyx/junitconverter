package junitconverter;


import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SourcePosition;
import com.sun.tools.javadoc.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.junit.*;

/**
 * This class converts a file to TestNG if it's a JUnit test class.
 * 
 * @author <a href="mailto:aviv.by@gmail.com">Aviv Ben-Yosef</a>
 */
public class JUnitTestConverter extends Doclet {
  private static File[] m_fileNames = null;
  private static List<String> m_classNames = new ArrayList<String>();
  // Files and their test methods
  private static Map<File, List<MethodDoc>> m_files = 
	  new HashMap<File, List<MethodDoc>>();
  private static Map<File, List<String>> m_fileLines = 
	  new HashMap<File, List<String>>();
  private static Map<File, String> m_packageNames = new HashMap<File, String>();
  private static Map<File, Integer> m_typelines = new HashMap<File, Integer>();
  private File m_outDir = null;
  private boolean m_done;

  /**
   * @param fileNames
   */
  public JUnitTestConverter(File[] fileNames, File outDir) {
    m_fileNames = fileNames;
    m_outDir = outDir;

    Collection<String> argv = new ArrayList<String>();

    argv.add("-quiet");

    argv.add("-doclet");
    argv.add(getClass().getName());

    for (File fn : fileNames) {
      argv.add(fn.getAbsolutePath());
    }

    String[] newArgv = argv.toArray(new String[argv.size()]);

    m_done = 0 == Main.execute(newArgv);
  }

  private static File findFileName(ClassDoc cd) {
    for (File fn : m_fileNames) {
      if (fn.getAbsolutePath().endsWith(cd.name() + ".java")) {
        return fn;
      }
    }

    assert false : "COULDN'T FIND FILE " + cd.name();
    return null;
  }

  /**
   * This method is required for all doclets.
   * 
   * @return true on success.
   */
  public static boolean start(RootDoc root) {
    ClassDoc[] classes = root.classes();

    for (ClassDoc cd : classes) {
      if (!isJUnitTest(cd)) {
        continue;
      }

      if(!cd.isAbstract())
        m_classNames.add(cd.qualifiedTypeName());

      File file;
      if (null != cd.position().file()) {
        file = cd.position().file();
      } else {
        file = findFileName(cd);
      }

      String fqn = cd.qualifiedTypeName();
      int tn = fqn.indexOf(cd.typeName());
      if (tn > 0) {
        m_packageNames.put(file, fqn.substring(0, tn - 1));
      }
      m_typelines.put(file, new Integer(cd.position().line()));

      MethodDoc[] methods = cd.methods();
      List<MethodDoc> testMethods = new ArrayList<MethodDoc>();

      for (MethodDoc md : methods) {
        if (isTest(md) || isSetUp(md) || isTearDown(md)) {
          // Add the lines backward, so we can insert them without messing
          // up their order later
          testMethods.add(0, md);
        }
      }

      m_files.put(file, testMethods);
    }

    return true;
  }

  /**
   * Query the superclasses in order to find out if the current classdoc is from
   * a real <CODE>TestCase</CODE>.
   */
  private static boolean isJUnitTest(ClassDoc clsDoc) {
    if (clsDoc.isInterface()) {
      return false;
    }

    ClassDoc superCls = clsDoc.superclass();

    while (null != superCls
        && !"java.lang.Object".equals(superCls.qualifiedTypeName())) {
      if ("junit.framework.TestCase".equals(superCls.qualifiedTypeName())
          || "TestCase".equals(superCls.typeName())) {
        return true;
      }

      superCls = superCls.superclass();
    }

    return false;
  }

  private static boolean isTest(MethodDoc md) {
    return md.name().startsWith("test");
  }

  private static boolean isSetUp(MethodDoc md) {
    return "setUp".equals(md.name());
  }

  private static boolean isTearDown(MethodDoc md) {
    return "tearDown".equals(md.name());
  }

  public int convert() {
    if (!m_done) {
      return -1;
    }

    int converted = 0;
    for (File file : m_files.keySet()) {
      try {
        List<String> finalLines = 
        	insertAnnotations(m_files.get(file), fileToLines(file));

        m_fileLines.put(file, finalLines);

        writeFile(file);
        converted++;
      } catch (IOException ioe) {
        ppp("failed to process " + file);
        ioe.printStackTrace();
      }
    }

    return converted;
  }

  private void writeFile(File filePath) {
    String fileName = filePath.getName();
    File file = 
    	new File(getPackageOutputDir(m_outDir, m_packageNames.get(filePath)), 
    			 fileName);
    File parentDir = file.getParentFile();

    parentDir.mkdirs();

    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(file));

      List<String> lines = m_fileLines.get(filePath);
      assert null != lines : "NO LINES FOR " + filePath;

      for (String l : lines) {
        bw.write(l);
        bw.write('\n');
      }

      ppp("Wrote " + file.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != bw) {
          bw.close();
        }
      } catch (IOException ioe) {
      }
    }
  }

  private List<String> insertAnnotations(
		  List<MethodDoc> methodDocs, List<String> lines) {
    insertImports(lines);
    
    for (MethodDoc md : methodDocs) {
      SourcePosition sp = md.position();
      int line = sp.line() + 3;
      if (isTest(md)) {
        lines.add(line, "    @" + Test.class.getSimpleName());
      } 
      else if (isSetUp(md)) {
        ppp("ADDING NEW BEFORE AT " + line);
        lines.add(line, "    @" + Before.class.getSimpleName());
      } 
      else if (isTearDown(md)) {
        lines.add(line, "    @" + After.class.getSimpleName());
      }
    }

    return lines;
  }

  private void insertImports(List<String> lines) {
	  int lineCount = 0;
	  for (String line : lines) {
		  lineCount++;
		  line = line.trim();
		  if(line.startsWith("import")) {
			  lines.add(lineCount - 1, 
					  "import static " + Assert.class.getName() + ".*;");
			  insertAnnotationsImports(lines, lineCount - 1);
			  break;
		  }
	  }
  }

  private void insertAnnotationsImports(
		  List<String> lines, int lineToInsertAt) {
	  lines.add(lineToInsertAt, "import " + Test.class.getName() + ";");
	  lines.add(lineToInsertAt, "import " + Before.class.getName() + ";");
	  lines.add(lineToInsertAt, "import " + After.class.getName() + ";");
  }

  private File getPackageOutputDir(File outDir, String packageName) {
    if (packageName == null) packageName = "";
    return new File(outDir, packageName.replace('.', File.separatorChar));
  }

  /**
   * Convert a file into a list of its lines
   * 
   * @throws IOException
   */
  private List<String> fileToLines(File file) throws IOException {
    List<String> result = new ArrayList<String>();
    BufferedReader br = new BufferedReader(new FileReader(file));

    String line = br.readLine();
    while (null != line) {
      result.add(line);
      line = br.readLine();
    }

    return result;
  }

  static private void ppp(String s) {
    if (JUnitConverter.isVerbose()) {
      System.out.println("[JUnitTestConverter]" + s);
    }
  }

  public String[] getClassNames() {
    return m_classNames.toArray(new String[m_classNames.size()]);
  }
}
