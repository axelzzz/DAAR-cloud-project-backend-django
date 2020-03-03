package KMP;

import java.io.*;
import java.util.*;

public class KMP {

	public static ArrayList<String> Recherche(String keyword, String folder_path) throws Exception {
		long startTime = System.currentTimeMillis();
		ArrayList<String> result = new ArrayList<String>();

		//S = ' ' + S + ' ';
		int[] retenue = GetRetenue(keyword);

		File file = new File(folder_path);
		File[] list_files = file.listFiles();
		for (int i = 0; i < list_files.length; i++) {
			if (list_files[i].isFile()) {
				ArrayList<String> strList = readFile(list_files[i].getPath());
				for (String str : strList) {
					str = str.toLowerCase();
					if (KMP(keyword, retenue, str)) {
//						System.out.println(list_files[i].getPath()); 
						result.add(list_files[i].getPath());
						break;
					}
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("KMP Temps utilise : " + (endTime - startTime) + "ms");
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

	private static int[] GetRetenue(String facteur) {
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

	private static boolean KMP(String keyword, int[] retenue, String texte) {
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
