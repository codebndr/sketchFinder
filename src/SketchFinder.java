/** 
 * @author Kevin Zhu, Waldo Withers E-mail: wz246@cornell.edu ldw48@cornell.edu
 * @version ：Feb 1, 2015 10:43:45 AM 
 * Despcription: This program is to find the projects and libraries in the sketch folder
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SketchFinder {
	static FileTree tree = new FileTree(); // the tree will hold the entire
	// file structures.
	static AtomicBoolean useExtensions = new AtomicBoolean(true);
	static AtomicBoolean showProjectFiles = new AtomicBoolean(false);
	static AtomicBoolean showLibraryFiles = new AtomicBoolean(true);
	static boolean inLibrary = false;

	static private void parse(String[] args) {
		tree.addRoot(new File(args[0]));
		for (int i = 1; i < args.length; i++) {
			if (args[i].equals("hideExtensions")) {
				useExtensions.set(false);
			} else if (args[i].equals("showProjectFiles")) {
				showProjectFiles.set(true);
			}
			if (args[i].equals("hideLibraryFiles")) {
				showLibraryFiles.set(false);
			}
		}
	}

	public static void main(String[] args) {

		parse(args);

		File folder = tree.getRoot();

		System.out.println(folder.getName());

		if (folder == null || !folder.exists()) {
			System.err.print("The folder doesn't exist!");
			return;
		}

		if (!folder.isDirectory()) {
			System.err.print("The folder is not a directory!");
			return;
		}
		FileTree libraries = null; // the libraries tree will hold the all the
		// libraries in the folder.

		// iterate the folder, add all the files with extension .ino or .pde to
		// the files child.
		// add all the subfolders to the subfolder.
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				// if the subfolder name is libraries folder, we will scan the
				// folder and return a library tree.
				if (f.getName().equals("libraries")) {
					inLibrary = true;
					libraries = getLibraries(f);
					inLibrary = false;
					continue;
				}

				FileTree sub = getSketches(f);
				if (sub != null) {
					tree.addSubfolder(sub);
				}
			} else if (showProjectFiles.get()) {
				tree.addFile(f);
			}
		}
		System.out.println("Projects:");
		System.out.println(tree);

		for (int i = 0; i < 70; i++) {
			System.out.print("-");
		}
		System.out.println();
		System.out.println(libraries);

	}

	// the method is to get the file structure recursively.
	private static FileTree getSketches(File folder) {
		FileTree tree = new FileTree(folder);

		// check whether the folder is empty or not.
		boolean hasStuff = false;
		for (File f : folder.listFiles()) {

			if (f.isDirectory()) {
				FileTree sub = getSketches(f);
				if (sub != null) {
					tree.addSubfolder(sub);
					hasStuff = true;
				}
			} else {
				if (inLibrary && showLibraryFiles.get()) {
					tree.addFile(f);
				} else if (!inLibrary && showProjectFiles.get()) {
					tree.addFile(f);
				}
				if (isIno(f) || isPde(f)) {
					hasStuff = true;
				}
			}
		}

		if (hasStuff) {
			return tree;
		} else {
			return null;
		}
	}

	// this method is to get the libraries in the libraries folder.
	private static FileTree getLibraries(File folder) {
		return getSketches(folder);
	}

	private static boolean isPde(File f) {
		String name = f.getName();
		return name.substring(name.lastIndexOf('.') + 1)
				.equalsIgnoreCase("pde");
	}

	private static boolean isIno(File f) {
		String name = f.getName();
		return name.substring(name.lastIndexOf('.') + 1)
				.equalsIgnoreCase("ino");
	}

	private static class FileTree {
		private File folder;
		private List<FileTree> subfolders;
		private List<File> files;

		public FileTree() {
			this(null);
		}

		public File getRoot() {
			return folder;
		}

		public FileTree(File root) {
			folder = root;
			subfolders = new ArrayList<FileTree>();
			files = new ArrayList<File>();
		}

		public void addRoot(File f) {
			folder = f;
		}

		public void addSubfolder(FileTree sf) {
			subfolders.add(sf);
		}

		public void addFile(File f) {
			files.add(f);
		}

		@Override
		public String toString() {
			return toString(0);
		}

		// sort the files and subfolders respectively and print them.
		public String toString(int level) {
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

			level++;
			String indentation = "";
			for (int i = 0; i < level; i++) {
				indentation += "    ";
			}

			StringBuffer output = new StringBuffer("> " + folder.getName()
					+ "\n");
			for (File f : files) {
				if (useExtensions.get()) {
					output.append(indentation).append(f.getName()).append("\n");
				} else {
					output.append(indentation)
							.append(f.getName().substring(0,
									f.getName().lastIndexOf("."))).append("\n");
				}
			}
			for (FileTree sub : subfolders) {
				output.append(indentation).append(sub.toString(level));
			}
			return output.toString();
		}
	}
}