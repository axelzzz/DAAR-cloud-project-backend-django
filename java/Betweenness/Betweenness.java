package Betweenness;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Betweenness {

	public static ArrayList<String> classement(ArrayList<String> paths_list, double seuil) throws Exception {
		ArrayList<String> result = new ArrayList<String>();

		ArrayList<HashMap<String, Integer>> hm_list = new ArrayList<HashMap<String, Integer>>();

		for (String file_path : paths_list) {
			HashMap<String, Integer> tmp = readFromFile(file_path);
			hm_list.add(tmp);
		}

		float matrice[][] = calculateJaccardDistanceMatrix(hm_list, seuil);

		result = index(matrice, paths_list);

		return result;
	}

	private static HashMap<String, Integer> readFromFile(String filename) throws Exception {
		HashMap<String, Integer> strList = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		String line;
		while ((line = br.readLine()) != null) {
			String reg = "[^a-zA-Z]";
			for (String s : Arrays.asList(line.split(reg))) {
				if (s.isEmpty())
					continue;
				String tmp = s.toLowerCase();
				if (strList.containsKey(tmp)) {
					strList.put(tmp, strList.get(tmp) + 1);
				} else {
					strList.put(tmp, 1);
				}
			}
		}
		br.close();
//		System.out.println(strList);
		return strList;
	}

	private static int calculateNumberOccurrences(String word, HashMap<String, Integer> map) {
		if (map.containsKey(word)) {
			return map.get(word);
		} else {
			return 0;
		}
	}

	private static float calculateJaccardDistance(HashMap<String, Integer> hM1, HashMap<String, Integer> hM2) {
		int numerator = 0;
		int denominator = 0;

		for (String word : hM1.keySet()) {
			int k1 = calculateNumberOccurrences(word, hM1);
			int k2 = calculateNumberOccurrences(word, hM2);
			int max = Math.max(k1, k2);
			int min = Math.min(k1, k2);
			numerator += (max - min);
			denominator += max;
		}

		for (String word : hM2.keySet()) {
			if (!hM1.containsKey(word)) {
				int k1 = calculateNumberOccurrences(word, hM1);
				int k2 = calculateNumberOccurrences(word, hM2);
				int max = Math.max(k1, k2);
				int min = Math.min(k1, k2);
				numerator += (max - min);
				denominator += max;
			}
		}

		return numerator / (float) denominator;
	}

	private static float[][] calculateJaccardDistanceMatrix(ArrayList<HashMap<String, Integer>> list, double seuil) {

		float matrice[][] = new float[list.size()][list.size()];

		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				float tmp = calculateJaccardDistance(list.get(i), list.get(j));
				matrice[i][j] = tmp;
				matrice[j][i] = tmp;
			}
		}

//		for (int i = 0; i < list.size(); i++) {
//			for (int j = 0; j < list.size(); j++) {
//				System.out.print(matrice[i][j] + "\t");
//			}
//			System.out.println();
//		}
//		System.out.println();

		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (matrice[i][j] >= seuil) {
					matrice[i][j] = 1;
				} else {
					matrice[i][j] = 0;
				}
			}
		}

//		for (int i = 0; i < list.size(); i++) {
//			for (int j = 0; j < list.size(); j++) {
//				System.out.print(matrice[i][j] + "\t");
//			}
//			System.out.println();
//		}
//		System.out.println();

		return matrice;
	}

	private static ArrayList<String> index(float matrice[][], ArrayList<String> paths_list) {
		int size = paths_list.size();
		ArrayList<String> result = new ArrayList<String>();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(matrice[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();

		ArrayList<String> vertices = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			vertices.add(paths_list.get(i).toString());
//			System.out.println(paths_list.get(i).toString());
		}

		ArrayList<ArrayList<String>> neighbor = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < size; i++) {
			ArrayList<String> tmp = new ArrayList<String>();
			for (int j = 0; j < size; j++) {
				if (matrice[i][j] == 1) {
					tmp.add(paths_list.get(j).toString());
//					System.out.println(paths_list.get(i).toString());
				}
			}
			neighbor.add(tmp);
		}
		
		for (int i = 0; i < size; i++) {
			System.out.println(vertices.get(i));
			for (int j=0; j<neighbor.get(i).size(); j++) {
				System.out.println("\t"+neighbor.get(i).get(j));
			}
		}
		System.out.println();

		float[] betweennessCentrality = new float[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			betweennessCentrality[i] = 0;
		}

		for (String v : vertices) {
			ArrayList<String> queue = new ArrayList<String>();
			ArrayList<String> stack = new ArrayList<String>();
			ArrayList<ArrayList<String>> pred = new ArrayList<ArrayList<String>>();
			float[] dist = new float[vertices.size()];
			float[] sigma = new float[vertices.size()];

			for (int i = 0; i < vertices.size(); i++) {
				pred.add(new ArrayList<String>());
				dist[i] = -1;
				sigma[i] = 0;
			}

			dist[vertices.indexOf(v)] = 0;
			sigma[vertices.indexOf(v)] = 1;
			queue.add(v);

			while (!queue.isEmpty()) {
				String s = queue.remove(0);
				stack.add(s);
				for (String w : neighbor.get(vertices.indexOf(s))) {
					if (dist[vertices.indexOf(w)] < 0) {
						dist[vertices.indexOf(w)] = dist[vertices.indexOf(s)] + 1;
						queue.add(w);
					}
					if (dist[vertices.indexOf(w)] == dist[vertices.indexOf(s)] + 1) {
						sigma[vertices.indexOf(w)] += sigma[vertices.indexOf(s)];
						pred.get(vertices.indexOf(w)).add(s);
					}
				}
			}

			float[] delta = new float[vertices.size()];
			for (int i = 0; i < vertices.size(); i++) {
				delta[i] = 0;
			}

			while (!stack.isEmpty()) {
				String w = stack.remove(stack.size() - 1);
				for (String s : pred.get(vertices.indexOf(w))) {
					delta[vertices.indexOf(s)] += sigma[vertices.indexOf(s)] / sigma[vertices.indexOf(w)]
							* (1 + delta[vertices.indexOf(w)]);

				}
				if (w != v) {
					betweennessCentrality[vertices.indexOf(w)] += delta[vertices.indexOf(w)];
				}
			}

		}

		for (String v : vertices) {
			betweennessCentrality[vertices.indexOf(v)] /= 2.0;
//			System.out.println(v + ", \t CB=" + BetweennessCentrality[Vertices.indexOf(v)]);
		}
//		System.out.println();

		// normalisation
		float max = 0;
		for (String v : vertices) {
			if (betweennessCentrality[vertices.indexOf(v)] > max)
				max = betweennessCentrality[vertices.indexOf(v)];
		}

		for (String v : vertices) {
			betweennessCentrality[vertices.indexOf(v)] /= max;
		}

//		for (String v : Vertices) {
//			System.out.println("after normalisatoin : " + v + ", \t CB=" + BetweennessCentrality[Vertices.indexOf(v)]);
//		}

		// sort by value
		Map<String, Float> map = new HashMap<String, Float>();
		for (String v : vertices) {
			map.put(v, betweennessCentrality[vertices.indexOf(v)]);
		}
		List<Map.Entry<String, Float>> infoIds = new ArrayList<Map.Entry<String, Float>>(map.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<String, Float>>() {
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				return (o2.getValue()).toString().compareTo(o1.getValue().toString());
			}
		});

		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
//			System.out.println(id);
			result.add(id);
		}

		return result;
	}

}