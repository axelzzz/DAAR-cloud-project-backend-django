package RegEx;

import java.util.ArrayList;

public class EnsembleEtats {
	
	private ArrayList<Integer> ensembleEtats;
	
	public EnsembleEtats() {
		this.ensembleEtats = new ArrayList<>();
	}
	
	public void addEtat(Integer etat) { ensembleEtats.add(etat); }
	
	public ArrayList<Integer> getEnsemble() { return ensembleEtats; }

}
