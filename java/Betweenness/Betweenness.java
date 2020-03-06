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
			HashMap<String, Integer> tmp = ReadFromFile(file_path);
			hm_list.add(tmp);
		}

		float matrice[][] = CalculateJaccardDistanceMatrix(hm_list, seuil);

		result = index(matrice, paths_list);

		return result;
	}

	private static HashMap<String, Integer> ReadFromFile(String filename) throws Exception {
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

	private static int CalculateNumberOccurrences(String word, HashMap<String, Integer> map) {
		if (map.containsKey(word)) {
			return map.get(word);
		} else {
			return 0;
		}
	}

	private static float CalculateJaccardDistance(HashMap<String, Integer> HM1, HashMap<String, Integer> HM2) {
		int numerator = 0;
		int denominator = 0;

		for (String word : HM1.keySet()) {
			int k1 = CalculateNumberOccurrences(word, HM1);
			int k2 = CalculateNumberOccurrences(word, HM2);
			int max = Math.max(k1, k2);
			int min = Math.min(k1, k2);
			numerator += (max - min);
			denominator += max;
		}

		for (String word : HM2.keySet()) {
			if (!HM1.containsKey(word)) {
				int k1 = CalculateNumberOccurrences(word, HM1);
				int k2 = CalculateNumberOccurrences(word, HM2);
				int max = Math.max(k1, k2);
				int min = Math.min(k1, k2);
				numerator += (max - min);
				denominator += max;
			}
		}

		return numerator / (float) denominator;
	}

	private static float[][] CalculateJaccardDistanceMatrix(ArrayList<HashMap<String, Integer>> list, double seuil) {

		float matrice[][] = new float[list.size()][list.size()];

		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				float tmp = CalculateJaccardDistance(list.get(i), list.get(j));
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

		ArrayList<String> Vertices = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			Vertices.add(paths_list.get(i).toString());
//			System.out.println(paths_list.get(i).toString());
		}

		ArrayList<ArrayList<String>> Neighbor = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < size; i++) {
			ArrayList<String> tmp = new ArrayList<String>();
			for (int j = 0; j < size; j++) {
				if (matrice[i][j] == 1) {
					tmp.add(paths_list.get(j).toString());
//					System.out.println(paths_list.get(i).toString());
				}
			}
			Neighbor.add(tmp);
		}
		
		for (int i = 0; i < size; i++) {
			System.out.println(Vertices.get(i));
			for (int j=0; j<Neighbor.get(i).size(); j++) {
				System.out.println("\t"+Neighbor.get(i).get(j));
			}
		}
		System.out.println();

		float[] BetweennessCentrality = new float[Vertices.size()];
		for (int i = 0; i < Vertices.size(); i++) {
			BetweennessCentrality[i] = 0;
		}

		for (String v : Vertices) {
			ArrayList<String> Queue = new ArrayList<String>();
			ArrayList<String> Stack = new ArrayList<String>();
			ArrayList<ArrayList<String>> Pred = new ArrayList<ArrayList<String>>();
			float[] dist = new float[Vertices.size()];
			float[] sigma = new float[Vertices.size()];

			for (int i = 0; i < Vertices.size(); i++) {
				Pred.add(new ArrayList<String>());
				dist[i] = -1;
				sigma[i] = 0;
			}

			dist[Vertices.indexOf(v)] = 0;
			sigma[Vertices.indexOf(v)] = 1;
			Queue.add(v);

			while (!Queue.isEmpty()) {
				String s = Queue.remove(0);
				Stack.add(s);
				for (String w : Neighbor.get(Vertices.indexOf(s))) {
					if (dist[Vertices.indexOf(w)] < 0) {
						dist[Vertices.indexOf(w)] = dist[Vertices.indexOf(s)] + 1;
						Queue.add(w);
					}
					if (dist[Vertices.indexOf(w)] == dist[Vertices.indexOf(s)] + 1) {
						sigma[Vertices.indexOf(w)] += sigma[Vertices.indexOf(s)];
						Pred.get(Vertices.indexOf(w)).add(s);
					}
				}
			}

			float[] delta = new float[Vertices.size()];
			for (int i = 0; i < Vertices.size(); i++) {
				delta[i] = 0;
			}

			while (!Stack.isEmpty()) {
				String w = Stack.remove(Stack.size() - 1);
				for (String s : Pred.get(Vertices.indexOf(w))) {
					delta[Vertices.indexOf(s)] += sigma[Vertices.indexOf(s)] / sigma[Vertices.indexOf(w)]
							* (1 + delta[Vertices.indexOf(w)]);

				}
				if (w != v) {
					BetweennessCentrality[Vertices.indexOf(w)] += delta[Vertices.indexOf(w)];
				}
			}

		}

		for (String v : Vertices) {
			BetweennessCentrality[Vertices.indexOf(v)] /= 2.0;
//			System.out.println(v + ", \t CB=" + BetweennessCentrality[Vertices.indexOf(v)]);
		}
//		System.out.println();

		// normalisation
		float max = 0;
		for (String v : Vertices) {
			if (BetweennessCentrality[Vertices.indexOf(v)] > max)
				max = BetweennessCentrality[Vertices.indexOf(v)];
		}

		for (String v : Vertices) {
			BetweennessCentrality[Vertices.indexOf(v)] /= max;
		}

//		for (String v : Vertices) {
//			System.out.println("after normalisatoin : " + v + ", \t CB=" + BetweennessCentrality[Vertices.indexOf(v)]);
//		}

		// sort by value
		Map<String, Float> map = new HashMap<String, Float>();
		for (String v : Vertices) {
			map.put(v, BetweennessCentrality[Vertices.indexOf(v)]);
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