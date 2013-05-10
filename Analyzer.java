package main;

public class Analyzer {

	// TODO :: 
	/*
	 * 
	 * AutoWin17 3m devrait être 4m
	 * Quoi faire? Diminuer la performance pour plus de précision?
	 * Est-ce que sa va arriver souvent qu'il y ait 2 autowins?
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	// Constants
	private final static int VALUE_DEFENCE = 50;
	private final static int VALUE_ATTACK = 200;
	private final static int VALUE_SIZE = 1000;
	private final static int VALUE_AUTOWIN = 1000000;
	private final static int VALUE_LOSE = -99999999;
	
	// Print test function
	public static void printBoardwith2DTable(int[][] pawnsW, int[][] pawnsB)
	{
		//System.out.println("\n--------- printBoardwith2DTable ---------");
		
		for (int y=7;y>=0;y--){
			for (int x=0;x<=7;x++){
				String value = "0";
				Boolean invalid = false;
				for (int i=0;i<pawnsW.length;i++){
					if (pawnsW[i][0] == x && pawnsW[i][1] == y){
						value = "4";
						invalid = true;
					}
				}
				for (int i=0;i<pawnsB.length;i++){
					if (pawnsB[i][0] == x && pawnsB[i][1] == y){
						if (invalid){
							value = "?";
						}else{
							value = "2";
						}						
					}
				}
				//System.out.print(value + " ");
			}
			//System.out.println("");
		}
	}
	
	// Print test function
	public static void printBoardOneSideWith2DTable(int[][] pawns, boolean color)
	{
		if (color){
			//System.out.println("\n--------- printBoardOneSideWith2DTable for White ---------");
		}else{
			//System.out.println("\n--------- printBoardOneSideWith2DTable for Black ---------");
		}
		
		for (int y=7;y>=0;y--){
			for (int x=0;x<=7;x++){
				String value = "0";
				for (int i=0;i<pawns.length;i++){
					if (pawns[i][0] == x && pawns[i][1] == y){
						if (color){
							value = "4";
						}else{
							value = "2";
						}
					}
				}
				//System.out.print(value + " ");
			}
			//System.out.println("");
		}
	}
	
	
	// Main function for analyzing board
	public static int analyze(long boardW, long boardB, boolean color){
		
		int score = 0;
		
		int [][] pawnsW = Bitsmagic.getAllPawns(boardW);
		int [][] pawnsB = Bitsmagic.getAllPawns(boardB);
		
		score += analyzeNumbers(color, pawnsW, pawnsB);
		score -= analyzeNumbers(!color, pawnsW, pawnsB);
		score += analyzeAttacks(color, pawnsW, pawnsB, boardW, boardB);
		score -= analyzeAttacks(!color, pawnsW, pawnsB, boardW, boardB);
		int autoWinUs = analyzeAutowin(color, pawnsW, pawnsB);
		int autoWinThem = analyzeAutowin(!color, pawnsW, pawnsB);
		if (autoWinUs > autoWinThem){
			score += autoWinUs;
		}
		else if (autoWinUs < autoWinThem){
			score -= autoWinThem;
		}
		return score;
	}

		/*************************************************
		 *  			  Analyze Numbers
		 ************************************************/
	public static int analyzeNumbers(boolean color, int[][] pawnsW, int[][] pawnsB){
		if (color){
			if (pawnsW.length == 0){
				return VALUE_LOSE;
			}else{
				return (pawnsW.length * VALUE_SIZE);
			}
		}else{
			if (pawnsB.length == 0){
				return VALUE_LOSE;
			}else{
				return (pawnsB.length * VALUE_SIZE);
			}
		}
		
	}

	/*************************************************
	 *			Analyze Attacks
	 ************************************************/
	public static int analyzeAttacks(boolean color, int[][] pawnsW, int[][] pawnsB, long boardW, long boardB){
		int[][] pawns = color ? pawnsW : pawnsB;
		int offsetY = color ? 1 : -1;
		long board = color ? boardW : boardB;
		long boardEnemy = color ? boardB : boardW;
		int ret = 0;
	       
		for (int i=0;i<pawns.length;i++){
			int[] position = {pawns[i][0], pawns[i][1] + offsetY}; // Analyze Y+1
			// Verifying attack at -1 on x-axis
			position[0] = position[0] - 1;
			if (position[0] >= 0 && position[0] <= 7 && position[1] >= 0 && position[1] <= 7){
				if (Bitsmagic.isPawnAt(position, boardEnemy)){
					ret += VALUE_ATTACK;
				       
					/*************************************************
					 *		  Analyze Defences V2.0
					 ************************************************/
					int[] position2 = {pawns[i][0], pawns[i][1] - offsetY}; // Analyze Y-1
				       
					// Verifying defence at -1 on x-axis
					position2[0] = position2[0] - 1;
					if (position2[0] >= 0 && position2[0] <= 7 && position2[1] >= 0 && position2[1] <= 7){
						if (Bitsmagic.isPawnAt(position2, board)){
							ret += VALUE_DEFENCE;
						}
					}
					// Verifying defence at +1 on x-axis
					position2[0] = position2[0] + 2;
					if (position2[0] >= 0 && position2[0] <= 7 && position2[1] >= 0 && position2[1] <= 7){
						if (Bitsmagic.isPawnAt(position2, board)){
							ret += VALUE_DEFENCE;
						}
					}
				}
			}
			// Verifying attack at +1 on x-axis
			position[0] = position[0] + 2;
			if (position[0] >= 0 && position[0] <= 7 && position[1] >= 0 && position[1] <= 7){
				if (Bitsmagic.isPawnAt(position, boardEnemy)){
					ret += VALUE_ATTACK;
				       
					/*************************************************
					 *		  Analyze Defences V2.0
					 ************************************************/
					int[] position3 = {pawns[i][0], pawns[i][1] - offsetY}; // Analyze Y-1
				       
					// Verifying defence at -1 on x-axis
					position3[0] = position3[0] - 1;
					if (position3[0] >= 0 && position3[0] <= 7 && position3[1] >= 0 && position3[1] <= 7){
						if (Bitsmagic.isPawnAt(position3, board)){
							ret += VALUE_DEFENCE;
						}
					}
					// Verifying defence at +1 on x-axis
					position3[0] = position3[0] + 2;
					if (position3[0] >= 0 && position3[0] <= 7 && position3[1] >= 0 && position3[1] <= 7){
						if (Bitsmagic.isPawnAt(position3, board)){
							ret += VALUE_DEFENCE;
						}
					}
				}
			}
		}
		return ret;
	}
    
		
		/*************************************************
		 *  			  Analyze AutoWin
		 ************************************************/
	public static int analyzeAutowin(boolean color, int[][] pawnsW, int[][] pawnsB){
		// When the function returns score, autowin isn't possible
		
		boolean autoWinLeft = true;
		boolean autoWinRight = true;
		boolean isInEnemySide;
		boolean isInFront;
		boolean isInFront2;
		int ret = 0;
		int offsetY = color ? 1 : -1;
		
		
		int[][] pawns1 = color ? pawnsW : pawnsB; // Current player
		int[][] pawns2 = color ? pawnsB : pawnsW; // Enemy of current player

		for (int i=0;i<pawns1.length;i++){
			boolean cannotGoInFront = false;
			int minPawnValueLeft = pawns1[i][0];
			int minPawnValueRight = 7 - pawns1[i][0];
			isInEnemySide = color ? (pawns1[i][1] >= 4) : (pawns1[i][1] <= 3);
			if (isInEnemySide){ // If the current pawn is in the enemy's side
				autoWinLeft = true; 
				autoWinRight = true;
				//System.out.println("Checking AutoWin for : (" + pawns1[i][0] + ";" + pawns1[i][1] + ")");
				for (int j=0;j<pawns2.length;j++){
					////System.out.println("Checking AutoWin vs : (" + pawns2[j][0] + ";" + pawns2[j][1] + ")");
					if (pawns1[i][0] == pawns2[j][0] && pawns1[i][1] + offsetY == pawns2[j][1]){ // Check right in front
						cannotGoInFront = true;
					}else{
						isInFront = color ? (pawns2[j][0] == pawns1[i][0] && pawns2[j][1] > pawns1[i][1] + offsetY) : (pawns2[j][0] == pawns1[i][0] && pawns2[j][1] < pawns1[i][1] + offsetY);
						if (isInFront){ // Check if there is an enemy pawn in front of the current pawn
							autoWinLeft = false; 
							autoWinRight = false;
							//System.out.print("Checking AutoWin vs : (" + pawns2[j][0] + ";" + pawns2[j][1] + ")");
							//System.out.println("False #1 : Enemy pawn in front");
							break;
						}else if (Math.abs((pawns2[j][0] - pawns1[i][0])) == 1 && (pawns1[i][1] + offsetY) == pawns2[j][1]){ // Check if our pawn is under-attack
							autoWinLeft = false; 
							autoWinRight = false;
							//System.out.print("Checking AutoWin vs : (" + pawns2[j][0] + ";" + pawns2[j][1] + ")");
							//System.out.println("False #2 : Pawn is under-attack");
							break;
						}else if (Math.abs((pawns2[j][0] - pawns1[i][0])) == 1 && (pawns1[i][1] + (offsetY * 3)) == pawns2[j][1]){ // Check if our pawn is under-controlled
							autoWinLeft = false; 
							autoWinRight = false;
							//System.out.print("Checking AutoWin vs : (" + pawns2[j][0] + ";" + pawns2[j][1] + ")");
							//System.out.println("False #3 : Pawn is under-controlled");
							break;
						}else if (pawns2[j][0] < pawns1[i][0]){ // Check left pawns for AutoWinRight
							// Calculate value
							isInFront2 = color ? (pawns2[j][1] > pawns1[i][1] + offsetY) : (pawns2[j][1] < pawns1[i][1] + offsetY);
							if (isInFront2){
								int pawnValueLeft = Math.abs(pawns2[j][0] - pawns1[i][0]) - Math.abs(pawns2[j][1] - pawns1[i][1]);
								if (pawnValueLeft < minPawnValueLeft){
									minPawnValueLeft = pawnValueLeft;
									////System.out.println("minPawnValueLeft = " + minPawnValueLeft);
								}
							}
							
						}else if (pawns2[j][0] > pawns1[i][0]){ // Check right pawns for AutoWinLeft
							// Calculate value
							isInFront2 = color ? (pawns2[j][1] > pawns1[i][1] + offsetY) : (pawns2[j][1] < pawns1[i][1] + offsetY);
							if (isInFront2){
								int pawnValueRight = Math.abs(pawns2[j][0] - pawns1[i][0]) - Math.abs(pawns2[j][1] - pawns1[i][1]);
								if (pawnValueRight < minPawnValueRight){
									minPawnValueRight = pawnValueRight;
									////System.out.println("minPawnValueRight = " + minPawnValueRight);
								}
							}
						}
					}
				} // for
				
				////System.out.println("(minPawnValueLeft + minPawnValueRight) = " + (minPawnValueLeft + minPawnValueRight));
				if ((minPawnValueLeft + minPawnValueRight) >= 0 && autoWinLeft && autoWinRight){
					if ((minPawnValueLeft + minPawnValueRight) == 0 && cannotGoInFront){
						//System.out.println("cannotGoInFront");
					}
					else{
						//System.out.println("AutoWin true");
						int offsetYtoWin;
						if (color){
							offsetYtoWin = 4 - (7 - pawns1[i][1]);
						}else{
							offsetYtoWin = 4 - (pawns1[i][1]);
						}
						if (VALUE_AUTOWIN * offsetYtoWin >= ret){
							ret = VALUE_AUTOWIN * offsetYtoWin;
						}
					}
				}
			} // if
		} // for
					
		return ret;
	}
}
