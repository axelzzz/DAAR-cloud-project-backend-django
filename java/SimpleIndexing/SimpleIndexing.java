package SimpleIndexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleIndexing {

	public static ArrayList<String> recherche(String keyword, String folder_path) throws Exception {
		ArrayList<String> result = new ArrayList<String>();

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

		File tmp_file = new File("tmp\\" + file_path.substring(folder_path.length() + 1));
		if (tmp_file.exists()) {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(tmp_file));
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals(keyword)) {
					br.close();
					return true;
				}
			}
			br.close();
		} else {
			ArrayList<String> list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_path)));
			String line;
			while ((line = br.readLine()) != null) {
				String reg = "[^a-zA-Z]";
				for (String s : Arrays.asList(line.split(reg))) {
					if (s.isEmpty())
						continue;
					String tmp = s.toLowerCase();
					if (!list.contains(tmp)) {
						list.add(tmp);
					}
				}
			}
			br.close();

			tmp_file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(tmp_file));
			for (String str : list) {
				out.write(str + "\n");
			}
			out.flush();
			out.close();

			if (list.contains(keyword)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) throws IOException {

		String folder_path = "testbeds";

		File file = new File(folder_path);
		File[] list_files = file.listFiles();
		for (int i = 0; i < list_files.length; i++) {

			long startTime = System.currentTimeMillis();
			ArrayList<String> list = new ArrayList<String>();

			if (list_files[i].isFile()) {

				File tmp_file = new File("tmp\\" + list_files[i].getPath().substring(folder_path.length() + 1));
				if (tmp_file.exists()) {
					InputStreamReader reader = new InputStreamReader(new FileInputStream(tmp_file));
					BufferedReader br = new BufferedReader(reader);
					String line;
					while ((line = br.readLine()) != null) {
						list.add(line);
					}
					br.close();
				} else {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(list_files[i].getPath())));
					String line;
					while ((line = br.readLine()) != null) {
						String reg = "[^a-zA-Z]";
						for (String s : Arrays.asList(line.split(reg))) {
							if (s.isEmpty())
								continue;
							String tmp = s.toLowerCase();
							if (!list.contains(tmp)) {
								list.add(tmp);
							}
						}
					}
					br.close();

					tmp_file.createNewFile();
					BufferedWriter out = new BufferedWriter(new FileWriter(tmp_file));
					for (String str : list) {
						out.write(str + "\n");
					}
					out.flush();
					out.close();
				}
			}
//			for (String str : list) {
//				System.out.println(str);
//			}
			long endTime = System.currentTimeMillis();
			System.out.println(list.size() + "\t " + (endTime - startTime));
		}

	}

}
