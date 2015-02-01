/** 
 * @author Kevin Zhu, Waldo Withers E-mail: wz246@cornell.edu ldw48@cornell.edu
 * @version ：Feb 1, 2015 10:43:45 AM 
 * Despcription: This program is to find the projects and libraries in the sketch folder
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SketchFinder {
	public static void main(String[] args) {

		File folder = parse(args);
		
		if (!folder.exists()) {
			System.err.print("The folder doesn't exist!");
			return;
		}
		
		if (!folder.isDirectory()) {
			System.err.print("The folder is not a directory!");
			return;
		}

		FileTree tree = new FileTree(folder); //the tree will hold the entire file structures.
		FileTree libraries; //the libraries tree will hold the all the libraries in the folder.
		
		//iterate the folder, add all the files with extension .ino or .pde to the files child.
		//add all the subfolders to the subfolder.
		for (File f : folder.listFiles()) {
			//if the subfolder name is libraries folder, we will scan the folder and return a library tree.
			if (f.getName() == "libraries") {
				libraries = getLibraries(f);
			}
			
			if (f.isDirectory()) {
				FileTree sub = getSketches(f);
				if (sub != null) {
					tree.addSubfolder(sub);
				}
			} else if (isIno(f) || isPde(f)) {
				tree.addFile(f);
			}
		}
		System.out.println(tree.getFolder());
	}
	
	//the method is to get the file structure recursively.
	private static FileTree getSketches(File folder) {
		FileTree tree = new FileTree(folder);
		
		//check whether the folder is empty or not.
		boolean hasStuff = false;
		for (File f : folder.listFiles()) {

			if (f.isDirectory()) {
				FileTree sub = getSketches(f);
				if (sub != null) {
					tree.addSubfolder(sub);
					hasStuff = true;
				}
			} else if (isIno(f) || isPde(f)) {
				tree.addFile(f);
				hasStuff = true;
			}
		}

		if (hasStuff) {
			return tree;
		} else {
			return null;
		}
	}

	
	//this method is to get the libraries in the libraries folder.
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

		public FileTree(File root) {
			folder = root;
			subfolders = new ArrayList<FileTree>();
			files = new ArrayList<File>();
		}

		public void addSubfolder(FileTree sf) {
			subfolders.add(sf);
		}

		public void addFile(File f) {
			files.add(f);
		}

//		@Override
//		public String toString() {
//			return null;
//		}
	}

	static private File parse(String[] args) {
		return new File(args[0]);
	}
}