package IndexTable;

import java.io.File;
import java.util.ArrayList;

public class Index {

	public static ArrayList<String> recherche(String keyword, String folder_path) throws Exception {
		ArrayList<String> result = new ArrayList<String>();

		// S = ' ' + S + ' ';

		File file = new File(folder_path);
		File[] list_files = file.listFiles();
		for (int i = 0; i < list_files.length; i++) {
			if (list_files[i].isFile()) {
				if (matchingAlgo(keyword, folder_path, list_files[i].getPath())) {
					result.add(list_files[i].getPath());
				}
			}
		}

		return result;
	}

	public static boolean matchingAlgo(String keyword, String folder_path, String file_path) throws Exception {

		/*ArrayList<StringPosition> iTable = IndexTable.processIndexTable(100, false, false, folder_path, file_path);

		for (StringPosition sp : iTable) {
			if (sp.getWord().equals(keyword)) {
				return true;
			}
		}
*/
		return false;
	}

}
