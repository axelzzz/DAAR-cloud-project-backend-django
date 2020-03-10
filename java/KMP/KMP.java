package KMP;

import java.io.*;
import java.util.*;

public class KMP {

	public static ArrayList<String> recherche(String keyword, String folder_path) throws Exception {
		ArrayList<String> result = new ArrayList<String>();

//		keyword = ' ' + keyword + ' '; // decommenter si c'est une recherche exacte/precise
		int[] retenue = getRetenue(keyword);

		File file = new File(folder_path);
		File[] list_files = file.listFiles();
		for (int i = 0; i < list_files.length; i++) {
			if (list_files[i].isFile()) {
				ArrayList<String> strList = readFile(list_files[i].getPath());
				for (String str : strList) {
//					str = str.toLowerCase();	// decommenter si on ne respecte pas la casse
					if (matchingAlgo(keyword, retenue, str)) {
						result.add(list_files[i].getPath());
						break;
					}
				}
			}
		}

		return result;
	}

	private static ArrayList<String> readFile(String file_path) throws Exception {
		ArrayList<String> strList = new ArrayList<String>();
		BufferedReader buffered_inputstreamreader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file_path)));
		String line;
		while ((line = buffered_inputstreamreader.readLine()) != null) {
			strList.add(line);
		}
		buffered_inputstreamreader.close();
		return strList;
	}

	private static int[] getRetenue(String facteur) {
		int[] retenue = new int[facteur.length() + 1];
		retenue[0] = -1;
		retenue[1] = 0;

		int i = 2;
		int l = 0;

		while (i < facteur.length()) {
			if (facteur.charAt(i) == facteur.charAt(0)) {
				retenue[i] = -1;
				l++;
				i++;
			} else if (facteur.charAt(i - 1) == facteur.charAt(l)) {
				l++;
				retenue[i] = l;
				if (facteur.charAt(i) == facteur.charAt(l)) {
					retenue[i] = 0;
				}
				i++;
			} else if (l != 0) {
				l = retenue[l];
			} else {
				retenue[i] = l;
				i++;
			}
		}

		return retenue;
	}

	private static boolean matchingAlgo(String keyword, int[] retenue, String texte) {
		int i = 0; // indice du texte
		int j = 0; // indice du facteur

		while (i < texte.length()) {
			if (j == keyword.length()) {
				return true;
			}
			if (texte.charAt(i) == keyword.charAt(j)) {
				i++;
				j++;
			} else {
				if (retenue[j] == -1) {
					i++;
					j = 0;
				} else {
					j = retenue[j];
				}
			}
		}

		if (j == keyword.length()) {
			return true;
		} else {
			return false;
		}
	}

}
