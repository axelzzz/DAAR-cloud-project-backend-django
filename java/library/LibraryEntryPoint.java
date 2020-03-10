package library;

import py4j.GatewayServer;

public class LibraryEntryPoint {

	private Library library;

    public LibraryEntryPoint() {
      library = Library.getInstance();
    }

    public Library getLibrary() {
        return library;
    }

    public static void main(String[] args) {
    	
    	long startTime1 = System.nanoTime();
    	
        GatewayServer gatewayServer = new GatewayServer(new LibraryEntryPoint());
        
        long endTime1 = System.nanoTime();
	    long duration = endTime1-startTime1;
	    duration = duration / 1000000;
	    System.out.println("duree : "+duration+"ms");
	    
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }
}
