package junitconverter;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

/**
 * Convert JUnit files into TestNG by annotating them.
 *
 * @author <a href="mailto:aviv.by@gmail.com">Aviv Ben-Yosef</a>
 */
public class JUnitConverter {
   private static final String SRC_DIR_OPT = "-srcdir";
   private static final String OUT_DIR_OPT = "-d";
   private static final String OVERWRITE_OPT = "-overwrite";
   private static final String QUIET_OPT = "-quiet";
   
   public static void main(String[] args) {
      if(args.length == 0) {
         usage();

         return;
      }

      Map params = extractOptions(args);
      
      if(null != params.get(QUIET_OPT)) {
    	  m_verbose = true;
      }
      
      if(null == params.get(SRC_DIR_OPT)) {
         exitWithError("The source directory cannot be null");
      }

      String srcPath = (String) params.get(SRC_DIR_OPT);
      File src = new File(srcPath);

      if(!src.exists() || !src.isDirectory()) {
         exitWithError("Invalid source directory: " + src.getAbsolutePath());
      }

      boolean overwrite = null != params.get(OVERWRITE_OPT);

      if(null != params.get(OUT_DIR_OPT) && overwrite) {
         exitWithError("Cannot use both -d and --overwrite options");
      }

      if(null == params.get(OUT_DIR_OPT) && !overwrite) {
         exitWithError("One of -d and --overwrite options is required");
      }

      String outPath = overwrite ? srcPath : (String) params.get(OUT_DIR_OPT);
      
      JUnitDirectoryConverter convertor = 
    	  new JUnitDirectoryConverter(new File(srcPath), new File(outPath));

      int result = convertor.convert();

      switch(result) {
         case -1:
            log("Generation failed. Consult messages.");
            break;
         case 0:
            log("No tests were generated");
         default:
            log(result + " tests were generated");
      }
   }

   public static boolean isVerbose() {
	   return m_verbose;
   }
   
   /**
    * @param message
    */
   private static void exitWithError(String message) {
	  System.err.println(message); 
	  System.exit(1);
   }

   /**
    * Extract command line options.
    */
   private static Map extractOptions(String[] args) {
      Map options = new HashMap();

      for (int i = 0; i < args.length; i++) {
      if (SRC_DIR_OPT.equals(args[i])) {
        if (i + 1 < args.length) {
          options.put(SRC_DIR_OPT, args[i + 1]);
          i++;
        }
      } else if (OUT_DIR_OPT.equals(args[i])) {
        if (i + 1 < args.length) {
          options.put(OUT_DIR_OPT, args[i + 1]);
          i++;
        }
      } else if (OVERWRITE_OPT.equals(args[i])) {
    	  options.put(OVERWRITE_OPT, Boolean.TRUE);
      } else if (QUIET_OPT.equals(args[i])) {
    	  options.put(QUIET_OPT, Boolean.TRUE);
      }
    }

      return options;
   }

   /**
     * Prints usage info to console.
     */
   private static void usage() {
      System.out.println("Converts JUnit 3 test cases to JUnit 4.");
      System.out.println("Usage: java -cp <> " + JUnitConverter.class.getName()
            + " -srcdir <source_dir> "
            + " (-d <output_dir> -overwrite)");
      System.out.println("");
      System.out.println("-srcdir\t Source directory containing JUnit tests");
      System.out.println("-d\t\t Output directory for resulting TestNG tests and configuration xml");
      System.out.println("-overwrite\t Overwrite the original JUnit files with the new ones"
            + "\n\t\t The flag cannot be used when -d is used.");
   }
   
   private static void log(String message) {
	   if (m_verbose) {
		   System.out.println(message);
	   }
   }
   
   private static boolean m_verbose;
}
