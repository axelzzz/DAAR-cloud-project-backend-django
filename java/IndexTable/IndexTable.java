package IndexTable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class IndexTable {

	public static void displayMap(ArrayList<Map.Entry<String, Integer>> map) {

		for (int i = 0; i < map.size(); i++)
			System.out.println(map.get(i).getKey() + ": " + map.get(i).getValue());
	}

	public static boolean contientDejaMot(ArrayList<StringPosition> list, String mot) {

		for (StringPosition sp : list)
			if (sp.getWord().equals(mot))
				return true;
		return false;
	}

	public static boolean egalPosition(Position p1, Position p2) {

		return p1.getNumLigne() == p2.getNumLigne() && p1.getOffset() == p2.getOffset();
	}

	public static boolean egalStringPosition(StringPosition sp1, StringPosition sp2) {

		if (!sp1.getWord().equals(sp2.getWord()))
			return false;

		if (sp1.getPos().size() != sp2.getPos().size())
			return false;
		else {

			ArrayList<Position> pos1 = sp1.getPos();
			ArrayList<Position> pos2 = sp2.getPos();

			for (int i = 0; i < sp1.getPos().size(); i++) {

				if (!egalPosition(pos1.get(i), pos2.get(i)))
					return false;
			}

			return true;
		}
	}

	public static boolean contientDejaStringPosition(ArrayList<StringPosition> list, StringPosition toTest) {

		for (StringPosition sp : list)
			if (egalStringPosition(sp, toTest))
				return true;
		return false;
	}

	public static ArrayList<StringPosition> supprimerDoublons(ArrayList<StringPosition> list) {

		ArrayList<StringPosition> res = new ArrayList<>();

		for (StringPosition sp : list)
			if (contientDejaStringPosition(res, sp))
				continue;
			else
				res.add(sp);

		return res;
	}

	public static ArrayList<Position> getPositionsOfWordInListOfStringPosition(ArrayList<StringPosition> lsp,
			String word) {

		for (StringPosition sp : lsp)
			if (word.equals(sp.getWord()))
				return sp.getPos();

		return null;
	}

	public static ArrayList<String> getBlackList() {

		ArrayList<String> blackList = new ArrayList<>();

//		blackList.add("the");
//		blackList.add("and");
//		blackList.add("has");
//		blackList.add("with");
//		blackList.add("her");
//		blackList.add("from");
//		blackList.add("but");
//		blackList.add("are");
//		blackList.add("was");
//		blackList.add("this");
//		blackList.add("its");
//		blackList.add("you");
//		blackList.add("that");
//		blackList.add("after");
//		blackList.add("for");
//		blackList.add("his");
//		blackList.add("which");
//		blackList.add("have");
//		blackList.add("The");
//		blackList.add("been");
//		blackList.add("their");
//		blackList.add("not");
//		blackList.add("were");
//		blackList.add("had");
//		blackList.add("they");

		return blackList;
	}

	/*
	 * nbLigneBreak : ligne a laquelle on arrete le traitement doBreak : si on veut
	 * arreter le traitement a une certaine ligne display : true pour afficher les
	 * tables sur la console book : name of book
	 */
	public static ArrayList<StringPosition> processIndexTable(int nbLigneBreak, boolean doBreak, 
										boolean display, String folder_path, 
										String file_path,
										String nameFile) throws Exception {

		ArrayList<StringPosition> finalListPositions = new ArrayList<>();

		//ArrayList<String> blackList = getBlackList();

		File file = new File(file_path);		

		File tmp_file = new File("tmp\\" + file_path.substring(folder_path.length()+1));


		if (tmp_file.exists()) {
			BufferedReader buffered_inputstreamreader = new BufferedReader(
					new InputStreamReader(new FileInputStream(tmp_file)));
			String line;
			while ((line = buffered_inputstreamreader.readLine()) != null) {
				String[] sp = line.split("\"");
				finalListPositions.add(new StringPosition(sp[1]));
			}
			buffered_inputstreamreader.close();
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			ArrayList<String> strList = new ArrayList<String>();
			String readLine = null;
			int numLigne = 1;

			/* table d'index */
			Map<StringOccurrence, ArrayList<Position>> indexTable = new LinkedHashMap<>();

			ArrayList<StringPosition> sPositions = new ArrayList<>();

			while ((readLine = br.readLine()) != null) {

				String[] wordsSplit = readLine.split("[^a-zA-Z]");

				WholeWordIndexFinder finder = new WholeWordIndexFinder(readLine);

				for (String word : wordsSplit) {

					if (word.isEmpty())
						continue;

					/* choisir de mettre en lower case ou pas, attention aux noms */
//					word = word.toLowerCase();


					/* on calcule les index dans la ligne ou se trouve le mot courant */
					List<IndexWrapper> indexes = finder.findIndexesForKeyword(word);

					// finder.displayIndexes(indexes, word);
					ArrayList<Integer> indx = finder.indexWrapperToIndexList(indexes);


					/* on definit les positions (num L, offset) du mot */
					StringPosition sp = new StringPosition(word);

					for (Integer i : indx)
						sp.addPosition(new Position(numLigne, i.intValue()));


					/* enregistrer dans une liste la pos du mot -> (ligne + offset) */
					sPositions.add(sp);

					strList.add(word);


				}

				if (doBreak)
					if (numLigne == nbLigneBreak)
						break;
				numLigne++;

			}

			sPositions = supprimerDoublons(sPositions);

			for (StringPosition sp : sPositions) {

				/*
				 * pas la peine de calculer pour une autre occurrence de mot si on l'a deja fait
				 * une fois
				 */
				if (contientDejaMot(finalListPositions, sp.getWord()))
					continue;

				else
					finalListPositions.add(sp.allPosOfString(sPositions));

			}

			br.close();


			Map<String, Integer> wordsCount = new TreeMap<String, Integer>(); // key = word, value = freqence
			for (String li : strList) {
				if (wordsCount.get(li) != null) {
					wordsCount.put(li, wordsCount.get(li) + 1);
				} else {
					wordsCount.put(li, 1);
				}
			}

			/* map qui contient les mots tries par ordre d occurrence */
			ArrayList<Map.Entry<String, Integer>> mapSortedWords = SortMap(wordsCount);

			/* on cree un fichier pour la table d index sans occ */

			File fileIndexTable = new File("/root/bigFatWorkspace/M2/DAAR/db-indexes50/index"+nameFile);//tmp_file;
			fileIndexTable.createNewFile();
			BufferedWriter out2 = new BufferedWriter(new FileWriter(fileIndexTable));

			for (int i = 0; i < mapSortedWords.size(); i++) {

				Entry<String, Integer> e = mapSortedWords.get(i);

				/* on remplit la table d index */
				StringOccurrence so = new StringOccurrence(e.getKey(), e.getValue().intValue());
				ArrayList<Position> lp = getPositionsOfWordInListOfStringPosition(finalListPositions, e.getKey());
				indexTable.put(so, lp);

				/* on ecrit dans un fichier */
				//out2.write(" \"" + so.getWord() + "\"");
				out2.write(so.getWord()+" ");
				
				if (display)
					System.out.print(" \"" + so.getWord() + "\" " + so.getNbOcc() + " ");

				for (Position p : lp) {

					out2.write(p.displayPosition());

					if (display)
						System.out.print(p.displayPosition());
				}

				out2.write("\n");

				if (display)
					System.out.println();
			}

			out2.flush();
			out2.close();
		}

		return finalListPositions;

	}

	public static ArrayList<Map.Entry<String, Integer>> SortMap(Map<String, Integer> oldmap) {

		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldmap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue() - o2.getValue(); // ordre croissante
			}
		});

		return list;
	}

	public static void main(String[] args) {

		try {
			File folder = new File("/root/bigFatWorkspace/M2/DAAR/database500");
			for(final File fileEntry : folder.listFiles()) {
				
				if (!fileEntry.isDirectory()) 
				{
					ArrayList<StringPosition> finalLsp = processIndexTable(0, false, false, folder.getAbsolutePath(), 
					fileEntry.getAbsolutePath(), 
					fileEntry.getName());
				}	
			
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
