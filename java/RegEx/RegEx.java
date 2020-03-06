package RegEx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RegEx {
	// MACROS
	static final int CONCAT = 0xC04CA7;
	static final int ETOILE = 0xE7011E;
	static final int ALTERN = 0xA17E54;
	static final int PROTECTION = 0xBADDAD;

	static final int PARENTHESEOUVRANT = 0x16641664;
	static final int PARENTHESEFERMANT = 0x51515151;
	static final int DOT = 46;

	// REGEX
	private static String regEx;

	// CONSTRUCTOR
	public RegEx() {
	}

	// MAIN
	public static void main(String arg[]) {
		System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
//    if (arg.length!=0) {
//      regEx = arg[0];
//    } else {
//      Scanner scanner = new Scanner(System.in);
//      System.out.print("  >> Please enter a regEx: ");
//      regEx = scanner.next();
//    }
		// regEx = "a";
		// regEx = ".|b";
		// regEx = ".|bc*";
		// regEx = "a*b|c";
		// regEx = "S(a|g|r)*on";
//    regEx = "test";
		regEx = "if";
		System.out.println("  >> Parsing regEx \"" + regEx + "\".");
		System.out.println("  >> ...");

		if (regEx.length() < 1) {
			System.err.println("  >> ERROR: empty regEx.");
		} else {
			System.out.print("  >> ASCII codes: [" + (int) regEx.charAt(0));
			for (int i = 1; i < regEx.length(); i++)
				System.out.print("," + (int) regEx.charAt(i));
			System.out.println("].");
			try {
				RegExTree ret = parse();
				System.out.println("  >> Tree result: " + ret.toString() + ".");

				Automate a = Main.epsilonAutomation(ret, 0, Main.nbStates(ret));

				System.out.println("nb etats : " + a.getNbLignes());
				System.out.println("\nStarting States : " + a.getNumberOfStartingState());
				System.out.println("Final States : " + a.getNumberOfFinalState());

				System.out.println();

				System.out.println("\nTransitions");
				a.displayTransitions();

				System.out.println("\nEpsilon transitions");
				a.afficherEpsTransit();

				System.out.println();
				System.out.println(" ===== detminisation ===== ");
				int I_F_State[] = new int[2]; // I_F_State[0] = NumberOfStartingState, I_F_State[1] = NumberOfFinalState
				ArrayList<ArrayList<Integer>> DFA = determinisation(a, I_F_State);

				System.out.println();
				System.out.println(" ===== readFile ===== ");
				// String filename = "testbeds/0.txt";
//		String filename = "testbeds/1.txt";
//		String filename = "testbeds/2.txt";
//		String filename = "testbeds/3.txt";
//		String filename = "testbeds/4.txt";
//		String filename = "testbeds/5.txt";
//		String filename = "testbeds/6.txt";
//		String filename = "testbeds/7.txt";
//		String filename = "testbeds/8.txt";
				String filename = "test/test1.txt";
				// String filename =
				// "/Users/hongxingwang/Java_workspace/DAAR_TME1/src/test.txt";
				ArrayList<String> strList = readFile(filename);

				System.out.println();
				System.out.println(" ===== matching ===== ");
				ArrayList<String> result = new ArrayList<String>();
				for (String str : strList) {
					if (!str.isEmpty()) {
						if (matchingAlgo(str, DFA, I_F_State[0], I_F_State[1])) {
							System.out.println(str);
//					System.out.println();
							result.add(str);
						}
					}
				}
//		System.out.println(result.size() + " results ");

			} catch (Exception e) {
				System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\".");
			}
		}

		System.out.println("  >> ...");
		System.out.println("  >> Parsing completed.");
		System.out.println("Goodbye Mr. Anderson.");
	}
	
	public static ArrayList<String> Recherche(String keyword, String folder_path){
		ArrayList<String> result = new ArrayList<String>();

		// S = ' ' + S + ' ';

		File file = new File(folder_path);
		File[] list_files = file.listFiles();
		for (int i = 0; i < list_files.length; i++) {
			if (list_files[i].isFile()) {
				if (Automate(keyword, list_files[i].getPath())) {
					result.add(list_files[i].getPath());
				}
			}
		}

		return result;
	}

	private static boolean Automate(String keyword, String file_path) {

		regEx = keyword;
		if (regEx.length() < 1) {
			System.err.println("  >> ERROR: empty regEx.");
		} else {
			try {
				RegExTree ret = parse();
				Automate a = Main.epsilonAutomation(ret, 0, Main.nbStates(ret));
				int I_F_State[] = new int[2]; // I_F_State[0] = NumberOfStartingState, I_F_State[1] = NumberOfFinalState
				ArrayList<ArrayList<Integer>> DFA = determinisation(a, I_F_State);

				String filename = file_path;
				ArrayList<String> strList = readFile(filename);
				for (String str : strList) {
					
					if (!str.isEmpty()) {
						if (matchingAlgo(str, DFA, I_F_State[0], I_F_State[1])) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\" in " + file_path);
			}
		}

		return false;
	}

	private static ArrayList<ArrayList<Integer>> determinisation(Automate a, int I_F_State[]) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		ArrayList<Boolean> isInitial = new ArrayList<Boolean>();
		ArrayList<Boolean> isFinal = new ArrayList<Boolean>();
		ArrayList<ArrayList<Integer>> charList = new ArrayList<ArrayList<Integer>>();

		boolean cond = true;
		int index = 0;
		ArrayList<Integer> list = getSuccEpsTransit(0, a);
		result.add(list);

		do {
			list = result.get(index);
			if (list.contains(a.getNumberOfStartingState())) {
				isInitial.add(true);
			} else {
				isInitial.add(false);
			}
			if (list.contains(a.getNumberOfFinalState())) {
				isFinal.add(true);
			} else {
				isFinal.add(false);
			}

			ArrayList<Integer> charL = new ArrayList<Integer>();
			for (int i = 0; i < 265; i++) {
				charL.add(-1);
			}
			for (int n : list) {
				for (int j = 0; j < 256; j++) {
					if (a.getAutomata()[n][j] != -1) {

						ArrayList<Integer> tmp = getSuccEpsTransit(a.getAutomata()[n][j], a);
						if (!result.contains(tmp)) {
							result.add(tmp);
						}
						charL.set(j, result.indexOf(tmp));

					}
				}
			}
			charList.add(charL);

			index++;
			if (index >= result.size()) {
				cond = false;
			}
		} while (cond);

//		System.out.println(" ===== checking start ===== ");
//		System.out.println(" result.size : " + result.size());
//		for (ArrayList<Integer> array : result) {
//			for (int n : array) {
//				System.out.print(n + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
//
//		System.out.println(" isInitial.size : " + isInitial.size());
//		for (Boolean b : isInitial) {
//			System.out.print(b + " ");
//		}
//		System.out.println();
//
//		System.out.println(" isFinal.size : " + isFinal.size());
//		for (Boolean b : isFinal) {
//			System.out.print(b + " ");
//		}
//		System.out.println();
//
//		System.out.println();
//		System.out.println(" charList.size : " + charList.size());
//		for (int i = 0; i < charList.size(); i++) {
//			for (int j = 0; j < 256; j++) {
//				if (charList.get(i).get(j) != -1) {
//					System.out.println(i + " --" + Character.toString((char) j) + "--> " + charList.get(i).get(j));
//
//				}
//			}
//			System.out.println();
//		}
//
//		System.out.println(" ===== checking end ===== ");

		I_F_State[0] = isInitial.indexOf(true);
		I_F_State[1] = isFinal.indexOf(true);

		return charList;
	}

	private static ArrayList<Integer> getSuccEpsTransit(int debut, Automate a) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> queue = new ArrayList<Integer>();
		queue.add(debut);
		while (!queue.isEmpty()) {
			int n = queue.remove(0);
			result.add(n);
			for (int j = 0; j < a.getNbLignes(); j++) {
				if (a.getEpsTransit()[n][j] != -1) {
					queue.add(j);
				}
			}
		}
		return result;
	}

	private static boolean matchingAlgo(String str, ArrayList<ArrayList<Integer>> DFA, int startingState,
			int finalState) {

		int indexStr = 0;
		while (indexStr < str.length()) {

			if (str.charAt(indexStr) != ' ') {
				boolean cond = false;
				int index = indexStr;
				int next_char = startingState;
				ArrayList<Character> CharList = getNextCharList(next_char, DFA);

				do {
					cond = false;
					if (CharList.contains(str.charAt(index)) || CharList.contains('.')) {

						if (CharList.contains(str.charAt(index))) {

//							System.out.println(" before getNextCharIndex : next_char="+next_char);
							next_char = getNextCharIndex(str.charAt(index), DFA);
//							System.out.println(" \t after getNextCharIndex : next_char="+next_char);

							if (next_char == finalState) {
								return true;

							}
							CharList = getNextCharList(next_char, DFA);
							index++;
							cond = true;

						} else {

							for (int i = 0; i < DFA.size(); i++) {
								if (DFA.get(i).get((int) '.') != -1) {
									next_char = DFA.get(i).get((int) '.');
									break;
								}
							}
							if (next_char == finalState) {
								return true;
							}
							CharList = getNextCharList(next_char, DFA);
							index++;
							cond = true;

						}

					}
				} while (cond && (index < str.length()));

			}
			indexStr++;

		}

		return false;
	}

	private static ArrayList<Character> getNextCharList(int indexChar, ArrayList<ArrayList<Integer>> DFA) {
		ArrayList<Character> result = new ArrayList<Character>();

		for (int j = 0; j < 256; j++) {
			if (DFA.get(indexChar).get(j) != -1) {
				result.add((char) j);
			}
		}

		return result;
	}

	private static int getNextCharIndex(char c, ArrayList<ArrayList<Integer>> DFA) {
		int result = -1;

		for (int i = 0; i < DFA.size(); i++) {
			if (DFA.get(i).get((int) c) != -1) {
				return DFA.get(i).get((int) c);
			}
		}

		return result;
	}

	private static ArrayList<String> readFile(String filename) throws Exception {
		ArrayList<String> strList = new ArrayList<String>();
		BufferedReader buffered_inputstreamreader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filename)));
		String line;
		while ((line = buffered_inputstreamreader.readLine()) != null) {
			strList.add(line);
		}
		buffered_inputstreamreader.close();
		return strList;
	}

	// FROM REGEX TO SYNTAX TREE
	private static RegExTree parse() throws Exception {
//		// BEGIN DEBUG: set conditionnal to true for debug example
//		if (false)
//			throw new Exception();
//		RegExTree example = exampleAhoUllman();
//		if (false)
//			return example;
//		// END DEBUG

		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		for (int i = 0; i < regEx.length(); i++)
			result.add(new RegExTree(charToRoot(regEx.charAt(i)), new ArrayList<RegExTree>()));

		return parse(result);
	}

	private static int charToRoot(char c) {
		if (c == '.')
			return DOT;
		if (c == '*')
			return ETOILE;
		if (c == '|')
			return ALTERN;
		if (c == '(')
			return PARENTHESEOUVRANT;
		if (c == ')')
			return PARENTHESEFERMANT;
		return (int) c;
	}

	private static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
		while (containParenthese(result))
			result = processParenthese(result);
		while (containEtoile(result))
			result = processEtoile(result);
		while (containConcat(result))
			result = processConcat(result);
		while (containAltern(result))
			result = processAltern(result);

		if (result.size() > 1)
			throw new Exception();

		return removeProtection(result.get(0));
	}

	private static boolean containParenthese(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == PARENTHESEFERMANT || t.root == PARENTHESEOUVRANT)
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		for (RegExTree t : trees) {
			if (!found && t.root == PARENTHESEFERMANT) {
				boolean done = false;
				ArrayList<RegExTree> content = new ArrayList<RegExTree>();
				while (!done && !result.isEmpty())
					if (result.get(result.size() - 1).root == PARENTHESEOUVRANT) {
						done = true;
						result.remove(result.size() - 1);
					} else
						content.add(0, result.remove(result.size() - 1));
				if (!done)
					throw new Exception();
				found = true;
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(parse(content));
				result.add(new RegExTree(PROTECTION, subTrees));
			} else {
				result.add(t);
			}
		}
		if (!found)
			throw new Exception();
		return result;
	}

	private static boolean containEtoile(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == ETOILE && t.subTrees.isEmpty())
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		for (RegExTree t : trees) {
			if (!found && t.root == ETOILE && t.subTrees.isEmpty()) {
				if (result.isEmpty())
					throw new Exception();
				found = true;
				RegExTree last = result.remove(result.size() - 1);
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(last);
				result.add(new RegExTree(ETOILE, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static boolean containConcat(ArrayList<RegExTree> trees) {
		boolean firstFound = false;
		for (RegExTree t : trees) {
			if (!firstFound && t.root != ALTERN) {
				firstFound = true;
				continue;
			}
			if (firstFound)
				if (t.root != ALTERN)
					return true;
				else
					firstFound = false;
		}
		return false;
	}

	private static ArrayList<RegExTree> processConcat(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		boolean firstFound = false;
		for (RegExTree t : trees) {
			if (!found && !firstFound && t.root != ALTERN) {
				firstFound = true;
				result.add(t);
				continue;
			}
			if (!found && firstFound && t.root == ALTERN) {
				firstFound = false;
				result.add(t);
				continue;
			}
			if (!found && firstFound && t.root != ALTERN) {
				found = true;
				RegExTree last = result.remove(result.size() - 1);
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(last);
				subTrees.add(t);
				result.add(new RegExTree(CONCAT, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static boolean containAltern(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == ALTERN && t.subTrees.isEmpty())
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		RegExTree gauche = null;
		boolean done = false;
		for (RegExTree t : trees) {
			if (!found && t.root == ALTERN && t.subTrees.isEmpty()) {
				if (result.isEmpty())
					throw new Exception();
				found = true;
				gauche = result.remove(result.size() - 1);
				continue;
			}
			if (found && !done) {
				if (gauche == null)
					throw new Exception();
				done = true;
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(gauche);
				subTrees.add(t);
				result.add(new RegExTree(ALTERN, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static RegExTree removeProtection(RegExTree tree) throws Exception {
		if (tree.root == PROTECTION && tree.subTrees.size() != 1)
			throw new Exception();
		if (tree.subTrees.isEmpty())
			return tree;
		if (tree.root == PROTECTION)
			return removeProtection(tree.subTrees.get(0));

		ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
		for (RegExTree t : tree.subTrees)
			subTrees.add(removeProtection(t));
		return new RegExTree(tree.root, subTrees);
	}

	// EXAMPLE
	// --> RegEx from Aho-Ullman book Chap.10 Example 10.25
//	private static RegExTree exampleAhoUllman() {
//		RegExTree a = new RegExTree((int) 'a', new ArrayList<RegExTree>());
//		RegExTree b = new RegExTree((int) 'b', new ArrayList<RegExTree>());
//		RegExTree c = new RegExTree((int) 'c', new ArrayList<RegExTree>());
//		ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
//		subTrees.add(c);
//		RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
//		subTrees = new ArrayList<RegExTree>();
//		subTrees.add(b);
//		subTrees.add(cEtoile);
//		RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
//		subTrees = new ArrayList<RegExTree>();
//		subTrees.add(a);
//		subTrees.add(dotBCEtoile);
//		return new RegExTree(ALTERN, subTrees);
//	}
}

//UTILITARY CLASS
class RegExTree {
	protected int root;
	protected ArrayList<RegExTree> subTrees;

	public RegExTree(int root, ArrayList<RegExTree> subTrees) {
		this.root = root;
		this.subTrees = subTrees;
	}

	// FROM TREE TO PARENTHESIS
	public String toString() {
		if (subTrees.isEmpty())
			return rootToString();
		String result = rootToString() + "(" + subTrees.get(0).toString();
		for (int i = 1; i < subTrees.size(); i++)
			result += "," + subTrees.get(i).toString();
		return result + ")";
	}

	private String rootToString() {
		if (root == RegEx.CONCAT)
			return ".";
		if (root == RegEx.ETOILE)
			return "*";
		if (root == RegEx.ALTERN)
			return "|";
		if (root == RegEx.DOT)
			return ".";
		return Character.toString((char) root);
	}
}
