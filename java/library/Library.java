package library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import KMP.KMP;

public class Library {

	private static Library library = null;
	private static final String DATABASE_PATH = "/root/bigFatWorkspace/M2/DAAR/database50";
	private static final String TESTBED_PATH = "/root/bigFatWorkspace/M2/DAAR/DAAR_Offline/testbeds";
	
	private List<Book> books = new ArrayList<>();
	
	private Library(String folderPath) {
	
		try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach( p -> books.add(new Book(p.toString())) );
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Library getInstance() {
		if(library == null)
			library = new Library(DATABASE_PATH);
		
		return library;
	}
	
	
	public List<Book> getBooks() { return books; }
	
	
	public List<Book> getFilteredBooks(String pattern) {		
		
		List<Book> filteredBooks = new ArrayList<>();
		
		try {
			List<String> result_KMP = KMP.recherche(pattern, DATABASE_PATH);			
			
			for(String filepath : result_KMP)
				filteredBooks.add(new Book(filepath));			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return filteredBooks;
		
	}
	
	
	public int size() { return books.size(); }
}
