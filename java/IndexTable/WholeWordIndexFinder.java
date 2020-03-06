package IndexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class WholeWordIndexFinder {
 
    private String searchString;
 
    public WholeWordIndexFinder(String searchString) {
        this.searchString = searchString;
    }
 
    public List<IndexWrapper> findIndexesForKeyword(String keyword) {
        String regex = "\\b"+keyword+"\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(searchString);
 
        List<IndexWrapper> wrappers = new ArrayList<IndexWrapper>();
 
        while(matcher.find() == true){
            int end = matcher.end();
            int start = matcher.start();
            IndexWrapper wrapper = new IndexWrapper(start, end);
            wrappers.add(wrapper);
        }
        return wrappers;
    }
 
    
    public void displayIndexes(List<IndexWrapper> indexes, String keyword) {   
    	    	 
    	System.out.print("Nb Indexes found for keyword \""+keyword+"\" : "+indexes.size()+". Found at index : ");
    	
        for(IndexWrapper iw:indexes)        	
         	System.out.print(iw.getStart()+" ");
        System.out.println( );
    }
    
    
    
    public ArrayList<Integer> indexWrapperToIndexList(List<IndexWrapper> indexes) {
    	
    	ArrayList<Integer> res = new ArrayList<>();
    	
    	for(IndexWrapper iw:indexes) 
    		res.add(Integer.valueOf(iw.getStart()));
    		
    	return res;
    }
    
    
    public static void main(String[] args) {
    	
    	String keyword = "je";
    	WholeWordIndexFinder finder = new WholeWordIndexFinder("je suis je");
        List<IndexWrapper> indexes = finder.findIndexesForKeyword(keyword);        
        finder.displayIndexes(indexes, keyword);
    }
    
}