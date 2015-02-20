import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class SketchFinderTest {

	private final ByteArrayOutputStream redirectedOutputStream = new ByteArrayOutputStream();

	private final PrintStream originalOutputStream = System.out;

	public void redirectOutput() {
		System.setOut(new PrintStream(redirectedOutputStream));
	}

	public void releaseOutput() {
		System.setOut(originalOutputStream);
	}

	@Test
	public void testMain() {

		// Test the Projects.
		String[] nateSparkfunInput = {
				"/home/waldo/Documents/codebender/sketchFinder/test_sketchbooks/nate-sparkfun",
				"hideProjectFiles" };
		String temp = "Projects:\n> nate-sparkfun\n    > Alex Mouse\n        > Alex_Mouse\n    > Alpha_GPS_Clock\n    > AnnoyATron\n    > Battery_Tester\n        > Firmware\n            > Battery_Tester\n    > BigTime\n        > Firmware\n            > BigTime\n    > Boxing_Attendance\n    > ML8511_Breakout\n        > firmware\n            > MP8511_Read_Example\n    > MLX90620_Example\n        > MLX90620_alphaCalculator\n    > Money_Vacuum\n    > OpenLog\n        > firmware\n            > OpenLog_CommandTest\n            > OpenLog_ReadExample\n            > OpenLog_Test_Sketch\n            > OpenLog_Test_Sketch_Binary\n    > PicoDos\n        > firmware\n            > PicoDos\n    > SpeedBagCounter\n    > TopHat\n        > firmware\n            > TopHat_Basic\n    > Wimp_Weather_Station\n\n----------------------------------------------------------------------\n> libraries\n    readme.txt\n\n";
		String[] nateSparkfunExpectedOutput = temp.split("[\n\r]");

		redirectOutput();

		SketchFinder.main(nateSparkfunInput);

		releaseOutput();

		System.out.println();

		String[] nateSparkfunActualOutput = redirectedOutputStream.toString()
				.split("[\n\r]");

		
		
		// Check the projects first.
		boolean doneWithProjects = false;
		int i = 0;

		while (!doneWithProjects && i < nateSparkfunActualOutput.length) {
			assertEquals(nateSparkfunExpectedOutput[i],
					nateSparkfunActualOutput[i]);

			doneWithProjects = nateSparkfunExpectedOutput[i]
					.equals("----------------------------------------------------------------------");
			i++;
		}
		
		//Then check the libraries.
		
		//TODO: check the libraries.
	}
}
