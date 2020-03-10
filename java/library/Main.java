package library;

public class Main {
	
	public static void main(String[] args) {
		
		long startTime1 = System.nanoTime();
		
		Library b = Library.getInstance();
		
		long endTime1 = System.nanoTime();
	    long duration = endTime1-startTime1;
	    duration = duration / 1000000;
	    System.out.println("duree : "+duration+"ms");
		System.out.println(b.getBooks().size());
	}

}
