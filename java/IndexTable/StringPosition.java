package IndexTable;

import java.util.ArrayList;

public class StringPosition {
	
	private String word;
	private ArrayList<Position> pos;
	
	public StringPosition(String word) {
		
		this.word = word;
		pos = new ArrayList<>();
	}
	
	
	
	public String displayWordPos() {
		
		String res = "\""+word+"\" at pos : ";
		
		for(Position p : pos)
			res+=p.displayPosition();
		
		return res;
	}
	
	
	

	public ArrayList<Position> getPos() { return pos; }
	
	public String getWord() { return word; }
	
	public void setPositions(ArrayList<Position> toSet) { this.pos = toSet; }
	
	
	public void addPosition(Position p) { pos.add(p); }
	
	public void addAllPositions(ArrayList<Position> positions) { 
		
		this.pos.addAll(positions);
	}
	
	
	
	public boolean containsSameElementStructure(Position p) {
		
		for(Position p2:this.pos)
			if(p.equals(p2))
				return true;
		
		return false;
	}
	
	
	
	/*methode qui va regrouper a partir de plusieurs StringPosition, 
	 * toutes les positions du mot qu on veut dans une StringPosition*/
	public StringPosition allPosOfString(ArrayList<StringPosition> sps) {
		
		StringPosition res = new StringPosition(word);
		
		
		for(StringPosition sp:sps) {
	
			if(word.equals(sp.getWord() ) ) {

				res.getPos().addAll(sp.getPos());
				
			}
		}
		//System.out.println("nb pos "+res.getPos().size());	
		return res;
	}
	
	
	
	public boolean isInList(ArrayList<StringPosition> sps) {
		
		for(StringPosition sp:sps)
			if(word == sp.getWord() )
				return true;
						
		return false;	
	}
	
	
	

}
