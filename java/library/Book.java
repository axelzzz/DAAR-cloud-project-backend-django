package library;

import java.io.File;

import utils.Parser;

public class Book {

	private File bookFile;
	private String nameFile;
		
	private String title;
	private String author;
	private String postingDate;
	private String releaseDate;
	private String language;
	
	public Book(String filepath) {
		
		bookFile = new File(filepath);
		nameFile = bookFile.getName();
		
		title = Parser.parseTitle(bookFile);
		author = Parser.parseAuthor(bookFile);
		postingDate = Parser.parsePostingDate(bookFile);
		releaseDate = Parser.parseReleaseDate(bookFile);
		language = Parser.parseLanguage(bookFile);
		
	}
	
	public String getNameFile() { return nameFile; }	
	public File getFile() { return bookFile; }
	
	public String getTitle() { return title; }	
	public String getAuthor() { return author; }
	public String getPostingDate() { return postingDate; }
	public String getReleaseDate() { return releaseDate; }
	public String getLanguage() { return language; }
	
	public String getMetadata() {
		
		return title+"\n"+author+"\n"+postingDate+"\n"+releaseDate+"\n"+language;
	}
}
