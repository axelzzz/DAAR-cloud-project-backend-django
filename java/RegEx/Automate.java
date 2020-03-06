package RegEx;

import java.util.ArrayList;
import java.util.List;

public class Automate {

	private int[][] automata;
	private int[][] epsTransit;
	/*peut remplacer les tableaux par 2 entiers finalement*/
	private boolean[] isFinalState;
	private boolean[] isStartingState;
	private final static int nbCol = 256;
	private int nbLignes;
	
	private static int cpt=0;
	
	/*automata for leafs*/
	public Automate(int nbLignes, int labelTransit) {
		
//		System.out.println( " \t Automate :  ");
		
		if (cpt >= nbLignes) {
			cpt = 0;
		}
		
		this.automata = new int[nbLignes][nbCol];
		this.epsTransit = new int[nbLignes][nbLignes];		
		this.isFinalState = new boolean[nbLignes];
		this.isStartingState = new boolean[nbLignes];
		this.nbLignes = nbLignes;

		for(int i=0 ; i<nbLignes ; i++) {
			
			this.isFinalState[i] = false;
			this.isStartingState[i] = false;
			
			for(int j=0 ; j<nbCol ; j++) 
				this.automata[i][j] = -1;
			for(int j=0 ; j<nbLignes ; j++)
				this.epsTransit[i][j] = -1;
		}
//		System.out.println( " \t init ok  ");
		
		isFinalState[cpt+1] = true;
		isStartingState[cpt] = true;
		
		automata[cpt][labelTransit] = cpt+1;		
		cpt+=2;
		
	}
	
	
	/*automata for nodes*/
	public Automate(int nbLignes, boolean isNode, int startingState, int finalState) {
		
		this.automata = new int[nbLignes][nbCol];
		this.epsTransit = new int[nbLignes][nbLignes];
		this.isFinalState = new boolean[nbLignes];
		this.isStartingState = new boolean[nbLignes];		
		this.nbLignes = nbLignes;
		
		for(int i=0 ; i<nbLignes ; i++) {
			
			this.isFinalState[i] = false;
			this.isStartingState[i] = false;
			
			for(int j=0 ; j<nbCol ; j++) 
				this.automata[i][j] = -1;
			for(int j=0 ; j<nbLignes ; j++)
				this.epsTransit[i][j] = -1;
			
		}
		
		isFinalState[finalState] = true;
		isStartingState[startingState] = true;
	}
	
	public List<Integer> cloneList(List<Integer> toClone) {
		
		List<Integer> res = new ArrayList<>();
		
		if(toClone != null && toClone.size() > 0) {
			for(Integer s:toClone) 
				res.add(Integer.valueOf(s.intValue()));
		}
		
		return res;		
	}
	
	
	public int[][] getAutomata() { return automata; }
	
	public int[][] getEpsTransit() { return epsTransit; }
	
	public int getNbLignes() { return nbLignes; }
	
	public static int getCpt() { return Automate.cpt; }
	
	public static void incCpt() { Automate.cpt++; }
	
	public void setEpsTransit(int fromState, int toState) {
		epsTransit[fromState][toState] = 1;
	}
	

	public int getNumberOfStartingState() {
		
		for(int i=0 ; i<nbLignes ; i++)
			if(isStartingState[i])
				return i;
		return -1;
	}
	
	public int getNumberOfFinalState() {
		
		for(int i=0 ; i<nbLignes ; i++)
			if(isFinalState[i])
				return i;
		return -1;
	}
	
	
	/*Display methods*/	
	public void afficherAutomate() {
		
		for(int i=0 ; i<nbLignes ; i++) {
			for(int j=0 ; j<nbCol ; j++) {
				if(j/100 > 0)
					System.out.print(automata[i][j]+"      ");
				else {
					if(j/10 > 0)
						System.out.print(automata[i][j]+"    ");
					else
						System.out.print(automata[i][j]+"   ");
				}					
			}
			System.out.println();
		}
		
		for(int i=0 ; i<automata[0].length ; i++) 				
			System.out.print(i+"    ");				

		System.out.println("\n");
	}
	
	
	
	
	
	public void displayTransitions() {
		
		for(int i=0 ; i<nbLignes ; i++) {
			for(int j=0 ; j<nbCol ; j++) {
				if(automata[i][j] != -1) 
					System.out.println(i+" --"+Character.toString((char)j)+"--> "+automata[i][j]);
			}
		}
	}
	
	
	
	public void afficherEpsTransit() {
		
		System.out.println();
		for(int i=0 ; i<nbLignes ; i++) {
			for(int j=0 ; j<nbLignes ; j++) {
				if(epsTransit[i][j] != -1)
					System.out.print("eps-transit from "+i+" to "+j+"\n");
			}			
		}
	}
	
	
	/*Treatment methods*/
	public Automate fusionAutomataAltern(Automate toFusion) {
		
		Automate fusion = new Automate(nbLignes, true, this.getNumberOfStartingState()-1, toFusion.getNumberOfFinalState()+1);
		int[][] automataFusion = fusion.getAutomata();
		
		for(int i=0 ; i<this.nbLignes ; i++) {
		
			for(int j=0 ; j<nbCol ; j++) {
				if(automata[i][j] != -1)
					automataFusion[i][j] = automata[i][j];
				else {
					if(toFusion.getAutomata()[i][j] != -1)
						automataFusion[i][j] = toFusion.getAutomata()[i][j];
					else 
						automataFusion[i][j] = -1;
				}
			}
			
			for(int j=0 ; j<nbLignes ; j++) {
				
				if(this.getEpsTransit()[i][j] != -1 && toFusion.getEpsTransit()[i][j] == -1)
					fusion.getEpsTransit()[i][j] = this.getEpsTransit()[i][j];
				else {
					if(this.getEpsTransit()[i][j] == -1 && toFusion.getEpsTransit()[i][j] != -1)
						fusion.getEpsTransit()[i][j] = toFusion.getEpsTransit()[i][j];
					else {
						if(this.getEpsTransit()[i][j] != -1 && toFusion.getEpsTransit()[i][j] != -1)
							fusion.getEpsTransit()[i][j] = this.getEpsTransit()[i][j];
					}
				}
			}
		}
		//pour epstransit faire des fusions comme pr matrice
		fusion.setEpsTransit(fusion.getNumberOfStartingState(), this.getNumberOfStartingState());
		fusion.setEpsTransit(fusion.getNumberOfStartingState(), toFusion.getNumberOfStartingState());
		fusion.setEpsTransit(this.getNumberOfFinalState(), fusion.getNumberOfFinalState());
		fusion.setEpsTransit(toFusion.getNumberOfFinalState(), fusion.getNumberOfFinalState());
		
		return fusion;
	}
	
	public Automate fusionAutomataConcat(Automate toFusion) {
		
		Automate fusion = new Automate(nbLignes, true, this.getNumberOfStartingState(), toFusion.getNumberOfFinalState());
		int[][] automataFusion = fusion.getAutomata();
		
		for(int i=0 ; i<this.nbLignes ; i++) {
			
			for(int j=0 ; j<nbCol ; j++) {
				if(automata[i][j] != -1)
					automataFusion[i][j] = automata[i][j];
				else {
					if(toFusion.getAutomata()[i][j] != -1)
						automataFusion[i][j] = toFusion.getAutomata()[i][j];
					else 
						automataFusion[i][j] = -1;
				}
			}
			
			for(int j=0 ; j<nbLignes ; j++) {
				
				if(this.getEpsTransit()[i][j] != -1 && toFusion.getEpsTransit()[i][j] == -1)
					fusion.getEpsTransit()[i][j] = this.getEpsTransit()[i][j];
				else {
					if(this.getEpsTransit()[i][j] == -1 && toFusion.getEpsTransit()[i][j] != -1)
						fusion.getEpsTransit()[i][j] = toFusion.getEpsTransit()[i][j];
					else {
						if(this.getEpsTransit()[i][j] != -1 && toFusion.getEpsTransit()[i][j] != -1)
							fusion.getEpsTransit()[i][j] = this.getEpsTransit()[i][j];
					}
				}
			}
		}
		
		fusion.setEpsTransit(this.getNumberOfFinalState(), toFusion.getNumberOfStartingState());
		
		return fusion;
		
	}

	
	public Automate fusionAutomataEtoile() {
		
		Automate fusion = new Automate(nbLignes, true, this.getNumberOfStartingState()-1, this.getNumberOfFinalState()+1);
		int[][] automataFusion = fusion.getAutomata();
		
		for(int i=0 ; i<this.nbLignes ; i++) {
			
			for(int j=0 ; j<nbCol ; j++) 			
				automataFusion[i][j] = automata[i][j];	
			
			for(int j=0 ; j<nbLignes ; j++)
				fusion.getEpsTransit()[i][j] = this.getEpsTransit()[i][j];
		}		
				
		
		fusion.setEpsTransit(fusion.getNumberOfStartingState(), this.getNumberOfStartingState());
		fusion.setEpsTransit(fusion.getNumberOfStartingState(), fusion.getNumberOfFinalState());
		fusion.setEpsTransit(this.getNumberOfFinalState(), this.getNumberOfStartingState());
		fusion.setEpsTransit(this.getNumberOfFinalState(), fusion.getNumberOfFinalState());
		
		return fusion;
	}
	
	
	/*
	
	public EnsembleEtats[] tabEpsTransitEnsembleEtats() {
		
		EnsembleEtats[] tab = new EnsembleEtats[nbLignes];
		
		for(int i=0 ; i<nbLignes ; i++) {
			
			EnsembleEtats ensemble = new EnsembleEtats();
			ensemble.addEtat(Integer.valueOf(i));
			
			for(int j=0 ; j<nbLignes ; j++) {
				
				if(epsTransit[i][j] != -1)
					ensemble.addEtat(Integer.valueOf(j));					
			}
			
			tab[i] = ensemble;
		}
		
		return tab;
	}
	
	
	
	
	public void afficherTabEpsTransitEnsembleEtats(EnsembleEtats[] tab) {
		
		for(int i=0 ; i<nbLignes ; i++) {
			System.out.print("L etat "+i+" a une eps transit vers les etats : ");
			
				
			if(tab[i].getEnsemble().size() > 0)
				for(Integer state:tab[i].getEnsemble())
					System.out.print(state+" ");
			
			System.out.println();
		}
		
	}
	
	*/
	
	/*
	public int[][] isThereEpsTransit(int[][] epsTransit) {
		
		int[][] mat = new int[nbLignes][nbLignes];
		
		for(int i=0 ; i<epsTransit.length ; i++) {
			for(int j=0 ; j<epsTransit[0].length ; j++) {
				if(epsTransit[i][j] != -1) {
					System.out.println(i+" --E--> "+epsTransit[i][j]);
					mat[i][epsTransit[i][j]] = 1;
				}
			}
		}
		
		
		return mat;
	}
	*/
	
}
