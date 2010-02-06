package tests.junitconverter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.junitconverter.stages.*;

/**
 * A test suite.
 *
 * @author abyx
 */
@RunWith(Suite.class)
@SuiteClasses({ AnnotationsImportingStageTest.class, 
	PreperationMethodsAnnotationStageTest.class, 
	TestMethodsAnnotationStageTest.class, SimpleCodeEditorTest.class,
	VisibilityAdaptionStageTest.class, AssertImportingStageTest.class,
	SuperRemovingStageTest.class, JavaParserTest.class,
	SimpleClassWriterTest.class })
public class AllTests {
	// Blank
}
