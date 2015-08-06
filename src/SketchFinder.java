/** 
 * @author Wen Zhu, Waldo Withers E-mail: wz246@cornell.edu ldw48@cornell.edu
 * @author Thodoris Bais | Email: thodoris.bais@gmail.com | Website: thodorisbais.com
 * 
 * Description: JSON format of included projects and libraries in the sketch folder
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SketchFinder {
	
	static FileTree tree; // Holds the files, when found.
    static private LibraryList libraries;
    static private List<File> librariesFolders;

	// The values of user arguments, with default values.
	static boolean help = false;
	static boolean useExtensions = true;
	static boolean showProjectFiles = false;
	static boolean showLibraryFiles = true;
	static boolean showHiddenFiles = false;
	static boolean showUnderscoreFiles = false;

	// Tracks whether the program is looking through the library. If so, handles
	// files differently.
	static boolean inLibrary = false;

	/**
	 * Parse the user arguments, including file name if possible. Do not check
	 * the file name.
	 */
	static private void parse(String[] args) {
		if (args == null || args.length == 0 || args[0].equals("--help")
				|| args[0].equals("-help") || args[0].equals("-h")
				|| args[0].equals("--h")) {
			// If a help argument or no argument is specified, calls for the
			// help menu.
			help = true;
		} else { // start the tree and parse the rest of the arguments.

			// Add the root to the tree.
			tree.addRoot(new File(args[0]));

			// Parse all the user options.
			for (int i = 1; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("hideExtensions")) {
					useExtensions = false;
				} else if (args[i].equalsIgnoreCase("showProjectFiles")) {
					showProjectFiles = true;
				} else if (args[i].equalsIgnoreCase("hideLibraryFiles")) {
					showLibraryFiles = false;
				} else if (args[i].equalsIgnoreCase("showHiddenFiles")) {
					showHiddenFiles = true;
				} else if (args[i].equalsIgnoreCase("showUnderscoreFiles")) {
					showUnderscoreFiles = true;
				}
			}
		}
	}

	/**
	 * Looks through the given folder and returns a string representation of the
	 * sketches and libraries therein.
	 */
	public static void main(String[] args) throws IOException {
		tree= new FileTree();

		parse(args);

		if (help) {
			System.out
					.print("usage: sketchFinder [-h --h --help -help] filepath [options] \n"
							+ "options include: \n"
							+ "   hideExtensions \n"
							+ "   showProjectFiles \n"
							+ "   hideLibraryFiles \n"
							+ "   showHiddenFiles \n"
							+ "   showUnderscoreFiles \n"); // TODO: Get help
			// message.
			return;
		}

		File folder = tree.getRoot();

		if (folder == null || !folder.exists()) {
			System.err.print("That folder does not exist!");
			return;
		}

		if (!folder.isDirectory()) {
			System.err.print("That is not a directory!");
			return;
		}

//		FileTree libraries = null; // will hold the all the libraries.

		// iterate through the folder and look for more folders.
		// Similar to getSketches(), but also looks for the libraries folder.
		for (File f : folder.listFiles()) {
			if (!showHiddenFiles && f.isHidden()) {
				continue; // Ignores hidden files.
			}

			if (!showUnderscoreFiles && f.getName().startsWith("_")) {
				continue; // Ignores file that start with underscores.
			}

			if (f.isDirectory()) {
				// if the subfolder name is libraries folder, get its libraries.
				// like they do in the Arduino code, things in the libraries
				// folders also count as projects.
//				if (f.getName().equalsIgnoreCase("libraries")) {
//					libraries = getLibraries(f);
//				}

				FileTree sub = getSketches(f);
				if (sub != null) {
					tree.addSubfolder(sub);
				}
			} else if (showProjectFiles) {
				tree.addFile(f);
			}
		}
		System.out.println("Projects:");
		System.out.println(tree);

		for (int i = 0; i < 70; i++) {
			System.out.print("-");
		}
		System.out.println();

//		if (libraries != null) {
//			System.out.println(libraries);
//		} else {
//			System.out.println("No libraries found");
//		}
        getLibraries(folder);
        System.out.print(libraries);

	}

	/**
	 * Find all the projects in the folder recursively, with project files if
	 * desired. Put them all in the tree.
	 */
	private static FileTree getSketches(File folder) {
		FileTree tree = new FileTree(folder);

		// check whether the folder is empty or not.
		boolean hasStuff = false;
		for (File f : folder.listFiles()) {
			hasStuff = hasStuff || processFiles(f, tree);
		}

		if (hasStuff) {
			return tree;
		} else {
			return null;
		}
	}

	private static boolean processFiles(File f, FileTree tree2) {

		if (f.isHidden() && !showHiddenFiles) {
			return false;
		}

		if (!showUnderscoreFiles && f.getName().startsWith("_")) {
			return false;
		}

		if (f.isDirectory()) {

			// don't create an extra subfolder for a folder named "examples"
			if (f.getName().equals("examples")) {
				
				boolean hasStuff = false;
				for (File f2 : f.listFiles()) {
					hasStuff = processFiles(f2,tree2);
				}
				return hasStuff;
			}

			FileTree sub = getSketches(f);
			if (sub != null) {
				tree2.addSubfolder(sub);
				return true;
			} else {
				return false;
			}

		} else {
			if (inLibrary && showLibraryFiles) {
				tree2.addFile(f);
			} else if (!inLibrary && showProjectFiles) {
				tree2.addFile(f);
			}
			return (inLibrary || isIno(f) || isPde(f));
		}
	}

	/**
	 * Find the libraries in folder, which is much like getting sketches but has
	 * different settings.
	 */
	private static void getLibraries(File folder) throws IOException {
        librariesFolders = new ArrayList<File>();
        librariesFolders.add(new File(folder, "libraries"));

//      Scan for libraries in each library folder.
//      Libraries located in the latest folders on the list can override
//      other libraries with the same name.
        scanAndUpdateLibraries(librariesFolders);
	}

	/** File f is a pde file, or at least has that extension */
	private static boolean isPde(File f) {
		String name = f.getName();
		return name.substring(name.lastIndexOf('.') + 1)
				.equalsIgnoreCase("pde");
	}

	/** File f is an ino file, or at least has that extension */
	private static boolean isIno(File f) {
		String name = f.getName();
		return name.substring(name.lastIndexOf('.') + 1)
				.equalsIgnoreCase("ino");
	}


    //scan libraries folders recursively
    static public void scanAndUpdateLibraries(List<File> folders) throws IOException {
        libraries = scanLibraries(folders);
    }

    static public LibraryList scanLibraries(List<File> folders) throws IOException {
        LibraryList res = new LibraryList();
        for (File folder : folders)
            res.addOrReplaceAll(scanLibraries(folder));
        return res;
    }

    static public LibraryList scanLibraries(File folder) throws IOException {
        LibraryList res = new LibraryList();

        String list[] = folder.list(new OnlyDirs());
        // if a bad folder or something like that, this might come back null
        if (list == null)
            return res;

        for (String libName : list) {
            File subfolder = new File(folder, libName);
            Library lib = Library.create(subfolder);
            // (also replace previously found libs with the same name)
            if (lib != null)
                res.addOrReplace(lib);
        }
        return res;
    }

	/**
	 * A data structure for holding files and folders, used for both the
	 * libraries and the projects. Includes a toString() which prints it as a
	 * tree with indented hierarchy.
	 */
	private static class FileTree {
		private File folder;
		private List<FileTree> subfolders;
		private List<File> files;

		/**
		 * Default constructor. A new FileTree with a null root and no
		 * subfolders or files.
		 */
		public FileTree() {
			this(null);
		}

		/** The root folder of this subtree. */
		public File getRoot() {
			return folder;
		}

		/**
		 * Constructor. A new FileTree with root root and no subfolders or
		 * files.
		 */
		public FileTree(File root) {
			folder = root;
			subfolders = new ArrayList<FileTree>();
			files = new ArrayList<File>();
		}

		/** Add the folder f as the root of the tree. */
		public void addRoot(File f) {
			folder = f;
		}

		/** Add the FileTree sf representing a subfolder. */
		public void addSubfolder(FileTree sf) {
			subfolders.add(sf);
		}

		/** Add the file f. */
		public void addFile(File f) {
			files.add(f);
		}

		/**
		 * A String representation of the FileTree. Assumes that the root is at
		 * depth 0.
		 *
		 * Prints first the files in the folder, then all it's subfolders.
		 */
		@Override
		public String toString() {
			return toString(0);
		}

		/**
		 * A String representation of the FileTree indented to the depth
		 * specified by level.
		 *
		 * Prints first the files in the folder, then all it's subfolders.
		 */
		public String toString(int level) {
			/* Increment the level variable for the indentation-building loop
			   and to pass on to the toString functions of the subtrees. */
			String indentation = "";
			for (int i = 0; i < level; i++) {
				indentation += "    ";
			}
			level++;
			
			// Shall contain all the output in a format easy to add onto.
			StringBuilder output = new StringBuilder();
				
			// Shall contain all the output in a format easy to add onto.
			output.append(indentation).append("{\"" + folder.getName()
					+ "\": [ \n");

			// Include all the files in the folder.
			for (File f : files) {
				output.append("    "+indentation).append("\"");
				if (useExtensions) { // Include extensions.
					output.append(f.getName());
				} else { // Hide the extensions.
					output.append(f.getName().substring(0,
									f.getName().lastIndexOf(".")));
				}
				output.append("\",\n");
			}
	
			// Include all the subfiles.
			for (FileTree sub : subfolders) {
				output.append(sub.toString(level));
				output.append(",\n");
			}
			
			output.setLength(output.length() - 2);
			output.append("\n"+indentation).append("]");
			output.append("}");
			
			// Convert output into a string and return.
			return output.toString();
		}
    }
}