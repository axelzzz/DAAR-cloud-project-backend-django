package Main;

import java.util.ArrayList;

import Betweenness.Betweenness;
import KMP.KMP;
import RegEx.RegEx;
import IndexTable.Index;

public class Main {

	public static void main(String[] args) throws Exception {

		long startTime, endTime;

		String keyword = "Project";
//		String keyword = "anywhere";	// Error : ne peut pas avoir 2 char identiques
		String folder_path = "testbeds";

		startTime = System.currentTimeMillis();
		ArrayList<String> result_KMP = KMP.recherche(keyword, folder_path);
		endTime = System.currentTimeMillis();
		System.out.println("KMP a pris " + (endTime - startTime) + " ms : ");
		for (String S : result_KMP) {
			System.out.println("\t" + S);
		}
		System.out.println();

		
		
//		startTime = System.currentTimeMillis();
//		ArrayList<String> result_I = Index.Recherche(keyword, folder_path);
//		endTime = System.currentTimeMillis();
//		System.out.println("Index a pris " + (endTime - startTime) + " ms : ");
//		for (String S : result_I) {
//			System.out.println("\t" + S);
//		}
//		System.out.println();

		
		
//		startTime = System.currentTimeMillis();
//		ArrayList<String> result_R = RegEx.Recherche(keyword, folder_path);
//		endTime = System.currentTimeMillis();
//		System.out.println("RegEx a pris " + (endTime - startTime) + " ms : ");
//		for (String S : result_R) {
//			System.out.println("\t" + S);
//		}
//		System.out.println();

		
		
//		startTime = System.currentTimeMillis();
//		ArrayList<String> result_classified = Betweenness.classement(result_KMP, 0.75);
//		endTime = System.currentTimeMillis();
//		for (String S : result_classified) {
//			System.out.println(S);
//		}
//		System.out.println("classement a pris " + (endTime - startTime) + " ms : ");

		
		
	}

}