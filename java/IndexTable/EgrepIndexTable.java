package IndexTable;

import java.util.ArrayList;

public class EgrepIndexTable {

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

}
