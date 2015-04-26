import java.io.File;

/**
 * Created by kevin on 4/26/15.
 */
public class ImportLibTest {
    public static void main(String args[]){
        SketchFinder SF = new SketchFinder();
        String destFolderPath = "/Users/kevin/Desktop/sketchtest";
        String zipPath = "/Users/kevin/Desktop/Archive.zip";
        SF.setSketchFolder(destFolderPath);
        SF.handleAddLibrary(zipPath);
    }
}
