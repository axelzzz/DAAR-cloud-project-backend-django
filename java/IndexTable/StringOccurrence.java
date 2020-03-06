package IndexTable;

public class StringOccurrence {

	private String word;
	private int nbOcc;
	
	public StringOccurrence(String word, int nbOcc) {
		this.word = word;
		this.nbOcc = nbOcc;
	}
	
	public String getWord() { return word; }
	
	public int getNbOcc() { return nbOcc; }
	
	public void setNbOcc(int n) { nbOcc = n; }
}
