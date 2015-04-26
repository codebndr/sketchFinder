import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

public class SketchFinderTest {
	// Change this to wherever your folder may be.
	private static final String currentLocation= "/home/waldo/Documents/codebender/SketchFinder/";

	private final ByteArrayOutputStream redirectedOutputStream = new ByteArrayOutputStream();

	private final PrintStream originalOutputStream = System.out;

	public void redirectOutput() {
		System.setOut(new PrintStream(redirectedOutputStream));
	}

	public void releaseOutput() {
		System.setOut(originalOutputStream);
	}

	@Test
	public void testMain() throws IOException {
	
		// Test the Projects.
		String[][] input = {
				{
						currentLocation + "test_sketchbooks/nate-sparkfun",
						"hideProjectFiles" },
				{
							currentLocation + "test_sketchbooks/seekerakos",
						"hideProjectFiles" },
				{
							currentLocation + "test_sketchbooks/seed studio",
						"hideProjectFiles" },
				{
							currentLocation + "test_sketchbooks/tzikis sketchbook",
						"hideProjectFiles" },

		};

		String[] unsplitExpectedOutput = {
				"Projects:\n> nate-sparkfun\n    > Alex Mouse\n        > Alex_Mouse\n    > Alpha_GPS_Clock\n    > AnnoyATron\n    > Battery_Tester\n        > Firmware\n            > Battery_Tester\n    > BigTime\n        > Firmware\n            > BigTime\n    > Boxing_Attendance\n    > ML8511_Breakout\n        > firmware\n            > MP8511_Read_Example\n    > MLX90620_Example\n        > MLX90620_alphaCalculator\n    > Money_Vacuum\n    > OpenLog\n        > firmware\n            > OpenLog_CommandTest\n            > OpenLog_ReadExample\n            > OpenLog_Test_Sketch\n            > OpenLog_Test_Sketch_Binary\n    > PicoDos\n        > firmware\n            > PicoDos\n    > SpeedBagCounter\n    > TopHat\n        > firmware\n            > TopHat_Basic\n    > Wimp_Weather_Station\n\n----------------------------------------------------------------------\n> libraries\n    readme.txt\n",
				"Projects:\n> seekerakos\n    > receiver\n    > transimiter\n    > transmiter_ino\n\n----------------------------------------------------------------------\n> libraries\n    readme.txt",
				"Projects:\n> seed studio\n\n----------------------------------------------------------------------\n> libraries\n    readme.txt",
			    "Projects:\n> tzikis sketchbook\n\n----------------------------------------------------------------------\n> libraries\n    readme.txt" };
		String[][] expected = new String[4][];

		for (int i = 0; i < unsplitExpectedOutput.length; i++) {
			String[] temp = unsplitExpectedOutput[i].split("[\n\r]");
			expected[i] = temp;
		}

		for (int i = 0; i < input.length; i++) {
			redirectOutput();
				
			redirectedOutputStream.reset();
			
			SketchFinder.main(input[i]);

			releaseOutput();
			
			String[] actualOutput = redirectedOutputStream.toString().split(
					"[\n\r]");

			// Check the projects first.
			boolean doneWithProjects = false;
			int j = 0;

			while (!doneWithProjects && j < actualOutput.length){
				
				System.out.println(expected[i][j]);
				System.out.println(actualOutput[j]);

				assertEquals(expected[i][j], actualOutput[j]);
				System.out.println(actualOutput[j]);
				
				doneWithProjects = expected[i][j]
						.equals("----------------------------------------------------------------------");
				j++;
			}

			// Then check the libraries.

			// TODO: check the libraries.
		}
	}
}
