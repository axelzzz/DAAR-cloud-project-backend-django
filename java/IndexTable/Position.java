package IndexTable;

public class Position {

	private int numLigne, offset;
	
	public Position(int numLigne, int offset) {
		this.numLigne = numLigne;
		this.offset = offset;
	}
	
	public int getNumLigne() { return numLigne; }
	
	public int getOffset() { return offset; }
	
	public void setPosition(int l, int o) {
		numLigne = l;
		offset = o;
	}
	
	public String displayPosition() {
		return "(L "+numLigne+", off "+offset+") ";
	}
}
