/*
 * $Id$
 * $Date$
 */
package junitconverter;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Tool class scanning and converting the JUnit sources found in a directory.
 *
 * @author <a href="mailto:aviv.by@google.com">Aviv Ben-Yosef</a>
 */
public class JUnitDirectoryConverter {
   private File m_sourceDir = null;
   private File m_outDir    = null;

   private Map<File, File> m_fileNames = new HashMap<File, File>();

   /**
    * Sole constructor.
    *
    * @param srcDir path to directory containing JUnit tests
    * @param outDir path to output directory for the generated test sources
    */
   public JUnitDirectoryConverter(File srcDir, File outDir) 
   {
      m_sourceDir = srcDir;
      m_outDir    = outDir;
   }

   public int convert() {
      m_fileNames = convert(m_sourceDir);
      File[] files = m_fileNames.keySet().toArray(new File[m_fileNames.size()]);

      JUnitTestConverter fc = new JUnitTestConverter(files, m_outDir);

      int converted = fc.convert();

      if(-1 == converted) {
         return converted;
      }

      return converted;
   }

   private boolean isTestFile(File f) {
      return f.getName().endsWith(".java");
   }

   private Map<File, File> convert(File f) {
      Map<File, File> result = new HashMap<File, File>();
      if(f.isDirectory()) {
         File[] files = f.listFiles();
         for(File file : files) {
            File f2 = file.getAbsoluteFile();
            Map<File, File> others = convert(f2);
            result.putAll(others);
         }
      } else {
         if(isTestFile(f)) {
            result.put(f, f);
         }
      }

      return result;
   }
}
