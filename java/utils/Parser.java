package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import library.Book;

public class Parser {
	
	public static void listFilesFolder(final File folder) {

		for(final File fileEntry : folder.listFiles()) {
			/*
	        if (fileEntry.isDirectory()) {
	            listFilesFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	        */
			if (!fileEntry.isDirectory()) 
				System.out.println(fileEntry.getName());
		
		}
	}
	
	
	public static void streamFilesFolder(String path) {
		
		try (Stream<Path> paths = Files.walk(Paths.get(path))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(System.out::println);
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static String parseTitle(File file) {
		
		try (BufferedReader br = new BufferedReader( new FileReader(file) )) {
			
			int i=0;
			String line;
			
			while(i<20) {
				if((line=br.readLine()).contains("Title:") ) 
					return line.replace("Title: ", "");
				i++;
			}
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	
    public static String parseAuthor(File file) {
		
		try (BufferedReader br = new BufferedReader( new FileReader(file) )) {
			
			int i=0;
			String line;
			
			while(i<20) {
				if((line=br.readLine()).contains("Author:") ) 
					return line.replace("Author: ", "");
				i++;
			}
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
    

    public static String parsePostingDate(File file) {
	
		try (BufferedReader br = new BufferedReader( new FileReader(file) )) {
			
			int i=0;
			String line;
			
			while(i<20) {
				if((line=br.readLine()).contains("Posting Date:") ) 
					return line.replace("Posting Date: ", "");
				i++;
			}
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
    
    
    public static String parseReleaseDate(File file) {
    	
		try (BufferedReader br = new BufferedReader( new FileReader(file) )) {
			
			int i=0;
			String line;
			
			while(i<20) {
				if((line=br.readLine()).contains("Release Date:") ) 
					return line.replace("Release Date: ", "");
				i++;
			}
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
    
    
    public static String parseLanguage(File file) {
    	
		try (BufferedReader br = new BufferedReader( new FileReader(file) )) {
			
			int i=0;
			String line;
			
			while(i<20) {
				if((line=br.readLine()).contains("Language:") ) 
					return line.replace("Language: ", "");
				i++;
			}
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
    
	public static void main(String[] args) {
		
		/*
		final File folder = new File("/root/bigFatWorkspace/M2/DAAR/DAAR_Online_Project_Django/database");
		listFilesFolder(folder);
	    */
		//streamFilesFolder("/root/bigFatWorkspace/M2/DAAR/DAAR_Online_Project_Django/database");
	
	    Book b = new Book("/root/bigFatWorkspace/M2/DAAR/DAAR_Online_Project_Django/database/9.txt.utf-8");
	    System.out.println(b.getMetadata());
	}

}
