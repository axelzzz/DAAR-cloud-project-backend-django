package IndexTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class EgrepIndexTable {

	private static String toSearch;
	private static String whichBook;

	public static boolean contientDejaInteger(ArrayList<Integer> list, Integer toTest) {

		for (Integer i : list)
			if (i.equals(toTest))
				return true;
		return false;
	}

	public static ArrayList<Integer> supprimerDoublons(ArrayList<Integer> list) {

		ArrayList<Integer> res = new ArrayList<>();

		for (Integer i : list)
			if (contientDejaInteger(res, i))
				continue;
			else
				res.add(i);

		return res;
	}

	public static ArrayList<String> Recherche(String keyword, String folder_path) throws Exception {
		long startTime = System.currentTimeMillis();
		ArrayList<String> result = new ArrayList<String>();

		// S = ' ' + S + ' ';

		File file = new File(folder_path);
		File[] list_files = file.listFiles();
		for (int i = 0; i < list_files.length; i++) {
			if (list_files[i].isFile()) {
				if (IndexTable(keyword, folder_path, list_files[i].getPath())) {
					result.add(list_files[i].getPath());
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Index Table Temps utilise : " + (endTime - startTime) + "ms");
		return result;
	}

	public static boolean IndexTable(String keyword, String folder_path, String file_path) throws Exception {

		ArrayList<StringPosition> iTable = IndexTable.processIndexTable(100, false, false, folder_path, file_path);

		for (StringPosition sp : iTable) {
			if (sp.getWord().equals(keyword)) {
				return true;
			}
		}

		return false;
	}

//	public static void main(String[] args) throws Exception {
//
//		/*
//		 * Scanner scanner = new Scanner(System.in);
//		 * System.out.print("  >> Please enter a word to search: "); toSearch =
//		 * scanner.next();
//		 */
//		/*
//		 * scanner = new Scanner(System.in);
//		 * System.out.print("  >> Please enter the book to search in (49345 or 56667): "
//		 * ); whichBook = scanner.next();
//		 */
//
//		toSearch = "test";
//		whichBook = "testbeds/9.txt";
//
////		String indexTable = "fileIndexTable" + whichBook;
//		String book = whichBook;
//
////		File fileIndexTable = new File(indexTable);
//		File fileBook = new File(book);
//
//		String readLine = null;
//
//		/*
//		 * BufferedReader brIT = new BufferedReader( new InputStreamReader( new
//		 * FileInputStream(fileIndexTable), "UTF-8") );
//		 */
//
//		BufferedReader brB = new BufferedReader(new InputStreamReader(new FileInputStream(fileBook), "UTF-8"));
//
//		System.out.println("Processing : creating tmp for " + book + "...");
//
//		long startTime1 = System.nanoTime();
//
//		// ArrayList<StringPosition> iTable = IndexTable.processIndexTable(0, false,
//		// false, book);
//		ArrayList<StringPosition> iTable = IndexTable.processIndexTable(100, false, false, book);
//
//		long endTime1 = System.nanoTime();
//		long duration = endTime1 - startTime1;
//		duration = duration / 1000000;
//		System.out.println("Time to process the index table : " + duration + "ms");
//
//		StringPosition wordToSearch = new StringPosition(toSearch);
//		ArrayList<Position> posOfWordToSearch = wordToSearch.getPos();
//
////		System.out.println("Checking " + indexTable + "...");
//
//		long startTime2 = System.nanoTime();
//
//		// on cherche le mot souhaite dans l index table
//		for (StringPosition sp : iTable) {
//
//			if (sp.getWord().equals(toSearch)) {
//
//				System.out.println(sp.displayWordPos());
//
//			}
//
//		}
//
//		long endTime2 = System.nanoTime();
//		long duration2 = endTime2 - startTime2;
//		duration2 = duration2 / 1000000;
//		System.out.println("Time to check and get the result : " + duration2 + "ms");
//
//		// System.out.println("size apres supp "+lignsToDisplay.size());
//		System.out.println("<----->\nResult :\n");
//
//	}

}
