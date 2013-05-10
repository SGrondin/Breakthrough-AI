package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Breakthrough {
	public static Network net;
	public static boolean color;
	public static long currentW;
	public static long currentB;
	public static Round current;
	//public static int nbThreads = Runtime.getRuntime().availableProcessors();
	public static int nbThreads = 4;
	public static boolean DEBUG = false;
	public static boolean MT = !Breakthrough.DEBUG;
	public static boolean SAVED = true;
	
	public static void main(String[] args) {
		
		Breakthrough.net = new Network();
		Breakthrough.net.start();
		
		

		/*String [] matriceBoard = new String[100];
		
		
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".brd");
		    }
		};

		File folder = new File("src/");
		File[] listOfFiles = folder.listFiles(filter);

		for (int i = 0; i < listOfFiles.length; i++) {
			String text = "";
			try {
				text = new Scanner( new File(listOfFiles[i]+"") ).useDelimiter("\\A").next();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("------------------------------------ " + listOfFiles[i] + " ------------------------------------\n" + text);
			
			matriceBoard[i] = text;
			//System.out.println("matriceBoard[i].trim().replace() :: \n" + matriceBoard[i].trim().replace(" ", "").replace("\n", "").replace("\r", ""));
			//System.out.println("Bitsmagic.convertSavedBoard(matriceBoard[i].trim().replace() :: \n" + Bitsmagic.convertSavedBoard(matriceBoard[i].trim().replace(" ", "").replace("\n", "").replace("\r", "")));
			
			//long boards[] = Bitsmagic.convertStringToBitBoard(matriceBoard[i].trim().replace(" ", ""));
			long boards[] = Bitsmagic.convertStringToBitBoard(Bitsmagic.convertSavedBoard(matriceBoard[i].trim().replace(" ", "").replace("\n", "").replace("\r", "")));
			
			
			//System.out.println("--------- printCurrentBitBoardAlex only White ---------");
			//printCurrentBitBoardAlex(boards[1]);
			//System.out.print("");
			
			
			//Bitsmagic.printCurrentBitBoardWithValue(boards[1]);
			
			int valueW = Analyzer.analyze(boards[0], boards[1], true);
			int valueB = Analyzer.analyze(boards[0], boards[1], false);
			
			System.out.println("Value of board #" + (i+1) + " for White player = " + valueW);
			System.out.println("Value of board #" + (i+1) + " for Black player = " + valueB);
		
		}*/
	}
	
	public static String savedGame(String s){
		return Bitsmagic.convertSavedBoard(s.replace("\n", "").replace("\r", ""));
	}
	
	public static void newGameWhite(String s){
		s = s.trim().replace(" ", "");
		if (Breakthrough.SAVED){
			s = Breakthrough.savedGame(s);
		}
		Breakthrough.color = true;
		long boards[] = Bitsmagic.convertStringToBitBoard(s);
		Breakthrough.currentW = boards[0];
		Breakthrough.currentB = boards[1];
		Breakthrough.current = new Round(Breakthrough.currentW, Breakthrough.currentB);
		
		
		/*int[] from = {3,1};
		int[] to = {3,2};
		Breakthrough.net.sendMove(Bitsmagic.convertMoveToString(from, to));*/
	}
	public static void newGameBlack(String s){
		s = s.trim().replace(" ", "");
		if (Breakthrough.SAVED){
			s = Breakthrough.savedGame(s);
		}
		Breakthrough.color = false;
		long boards[] = Bitsmagic.convertStringToBitBoard(s);
		Breakthrough.currentW = boards[0];
		Breakthrough.currentB = boards[1];
		
		System.out.println("Waiting for white move");
	}
	
	public static void moveReceived(String s){
		s = s.trim().replace(" ", "").replace("-", "").toUpperCase();
		System.out.println(">"+s+"<");
		
		int[][] move = {{-1,-1},{-1,-1}};
		move[0][0] = (int) (s.codePointAt(0)-65);
		move[0][1] = Integer.parseInt(s.substring(1,2))-1;
		move[1][0] = (int) (s.codePointAt(2)-65);
		move[1][1] = Integer.parseInt(s.substring(3,4))-1;
		
		char[] table = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		
		long[] boards = Bitsmagic.applyMove(move, currentW, currentB);
		Breakthrough.currentW = boards[0];
		Breakthrough.currentB = boards[1];
		
		//Stop previous round calculation
		//Pick right substree depending on the other team's move
		
		Breakthrough.current = new Round(Breakthrough.currentW, Breakthrough.currentB);
	}
	
	public static void movePicked(int[][] move){
		Breakthrough.net.sendMove(Utils.moveToString(move));
		long[] boards = Bitsmagic.applyMove(move, currentW, currentB);
		Breakthrough.currentW = boards[0];
		Breakthrough.currentB = boards[1];
		Breakthrough.current = null;
		Runtime.getRuntime().gc();
		//Calculate white moves and KEEP THE PROGRESS DONE SO FAR
	}

}
