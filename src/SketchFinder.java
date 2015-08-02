/** 
 * @author Wen Zhu, Waldo Withers E-mail: wz246@cornell.edu ldw48@cornell.edu
 * @author Thodoris Bais | Email: thodoris.bais@gmail.com | Website: thodorisbais.com
 * 
 * Description: JSON format of included projects and libraries in the sketch folder.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SketchFinder {
	
	static FileTree tree; // Holds the files, when found.
    static private LibraryList libraries;
    static private List<File> librariesFolders;
	static private File sketchFolder;

	// The values of user arguments, with default values.
	static boolean help = false;
    static boolean importZipLib = false;
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
		}
        else if(args[0].equals("-import")){
            importZipLib = true;
        }
        else { // start the tree and parse the rest of the arguments.

			// Add the root to the tree.
			tree.addRoot(new File(args[0]));
			sketchFolder = new File(args[0]);

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
     * set the sketch folder
	 */
    public static void setSketchFolder(String path){
        File sketchFolderTmp = new File(path);
        if (!sketchFolderTmp.exists()){
            System.out.println("The sketch folder doesn't exsit!!!");
            return;
        }
        if (!sketchFolderTmp.isDirectory()){
            System.out.println("The sketch folder is not a directory!!!");
        }
        sketchFolder = sketchFolderTmp;
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
        if (importZipLib){
            String zipPath = args[1];
            String sketchPath = args[2];
            setSketchFolder(sketchPath);
            handleAddLibrary(zipPath);
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

        //add the libraries to the librarylist
        getLibraries(folder);

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
	 * The method to deal with importing .zip library
	 */

	static public void handleAddLibrary(String path) {

		File sourceFile = new File(path);
		File tmpFolder = null;

		try {
			// unpack ZIP
			if (!sourceFile.isDirectory()) {
				try {
					tmpFolder = FileUtils.createTempFolder();
					ZipDeflater zipDeflater = new ZipDeflater(sourceFile, tmpFolder);
					zipDeflater.deflate();
					File[] foldersInTmpFolder = tmpFolder.listFiles(new OnlyDirs());
					if (foldersInTmpFolder.length != 1) {
						throw new IOException("Zip doesn't contain a library");
					}
					sourceFile = foldersInTmpFolder[0];
				} catch (IOException e) {
					System.out.println(e);
					return;
				}
			}

			// is there a valid library?
			File libFolder = sourceFile;
			String libName = libFolder.getName();
			if (!isSanitaryName(libName)) {
				String mess = "The library \"{0}\" cannot be used.\n"
								+ "Library names must contain only basic letters and numbers.\n"
								+ "(ASCII only and no spaces, and it cannot start with a number)";
				System.out.println(mess);
				return;
			}

			// copy folder
			File destinationFolder = new File(getSketchbookLibrariesFolder(), sourceFile.getName());
			if (!destinationFolder.mkdir()) {
				System.out.printf("A library named {0} already exists", sourceFile.getName());
				return;
			}
			try {
				FileUtils.copy(sourceFile, destinationFolder);
			} catch (IOException e) {
				System.out.println(e);
				return;
			}
			System.out.println("Library added to your libraries. Check \"Include library\" menu");
		} finally {
			// delete zip created temp folder, if exists
			FileUtils.recursiveDelete(tmpFolder);
		}
	}


	static public File getSketchbookLibrariesFolder() {
		File libdir = new File(sketchFolder, "libraries");
		if (!libdir.exists()) {
			try {
				libdir.mkdirs();
				File readme = new File(libdir, "readme.txt");
				FileWriter freadme = new FileWriter(readme);
				freadme.write("For information on installing libraries, see: " +
						"http://arduino.cc/en/Guide/Libraries\n");
				freadme.close();
			} catch (Exception e) {
			}
		}
		return libdir;
	}


	/**
	 * Return true if the name is valid for a Processing sketch.
	 */
	static public boolean isSanitaryName(String name) {
		return sanitizeName(name).equals(name);
	}


	/**
	 * Produce a sanitized name that fits our standards for likely to work.
	 * <p/>
	 * Java classes have a wider range of names that are technically allowed
	 * (supposedly any Unicode name) than what we support. The reason for
	 * going more narrow is to avoid situations with text encodings and
	 * converting during the process of moving files between operating
	 * systems, i.e. uploading from a Windows machine to a Linux server,
	 * or reading a FAT32 partition in OS X and using a thumb drive.
	 * <p/>
	 * This helper function replaces everything but A-Z, a-z, and 0-9 with
	 * underscores. Also disallows starting the sketch name with a digit.
	 */
	static public String sanitizeName(String origName) {
		char c[] = origName.toCharArray();
		StringBuffer buffer = new StringBuffer();

		// can't lead with a digit, so start with an underscore
		if ((c[0] >= '0') && (c[0] <= '9')) {
			buffer.append('_');
		}
		for (int i = 0; i < c.length; i++) {
			if (((c[i] >= '0') && (c[i] <= '9')) ||
					((c[i] >= 'a') && (c[i] <= 'z')) ||
					((c[i] >= 'A') && (c[i] <= 'Z')) ||
					((i > 0) && (c[i] == '-')) ||
					((i > 0) && (c[i] == '.'))) {
				buffer.append(c[i]);
			} else {
				buffer.append('_');
			}
		}
		// let's not be ridiculous about the length of filenames.
		// in fact, Mac OS 9 can handle 255 chars, though it can't really
		// deal with filenames longer than 31 chars in the Finder.
		// but limiting to that for sketches would mean setting the
		// upper-bound on the character limit here to 25 characters
		// (to handle the base name + ".class")
		if (buffer.length() > 63) {
			buffer.setLength(63);
		}
		return buffer.toString();
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
			// Sort the files and subfolders.
			files.sort(new Comparator<File>() {
				@Override
				public int compare(File file, File file2) {
					return file.getName().compareToIgnoreCase(file2.getName());
				}
			});
			subfolders.sort(new Comparator<FileTree>() {
				@Override
				public int compare(FileTree ft, FileTree ft2) {
					return ft.folder.getName().compareToIgnoreCase(
							ft2.folder.getName());
				}
			});

			// increment the level variable for the indentation-building loop
			// and to pass on to the toString functions of the subtrees.
			level++;
			String indentation = "";
			for (int i = 0; i < level; i++) {
				indentation += "    ";
			}

			// Shall contain all the output in a format easy to add onto.
			StringBuffer output = new StringBuffer();

			boolean hasSubfolders = false;
			if (subfolders.size() > 0) {
				hasSubfolders = true;
			}
			if (hasSubfolders) {
				// Shall contain all the output in a format easy to add onto.
				output.append("{\"" + folder.getName() + "\"");
			} else {
				// Shall contain all the output in a format easy to add onto.
				output.append("\"" + folder.getName() + "\"");
			}

			// Include all the files in the folder.
			for (File f : files) {
				if (useExtensions) { // Include extensions.
					output.append(indentation).append(f.getName()).append("\n");
				} else { // Hide the extensions.
					output.append(indentation).append(f.getName().substring(0, f.getName().lastIndexOf(".")))
							.append("\n");
				}
			}

			// Include all the subfiles.
			boolean hasSubfiles = false;
			output.append(": [");
			for (FileTree sub : subfolders) {
				output.append(sub.toString(level));
				output.append(",");
				hasSubfiles = true;
			}
			if (hasSubfiles) {
				output.setLength(output.length() - 1);
				output.append("]");
				output.append("}\n");
			} else {
				output.setLength(output.length() - 3);
			}

			// Convert output into a string and return.
			return output.toString();
		}
    }
}