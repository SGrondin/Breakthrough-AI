package main;

public class Utils {

	public static boolean moduloTwo(int nb){
		return (nb<<31) == 0;
	}
	
	public static String repeat(String s, int n){
		return new String(new char[n]).replace("\0", s);
	}
	
	public static String moveToString(int[][] move){
		if (move == null){
			return "NOMOVE";
		}
		char[] table = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		return ""+table[move[0][0]]+(move[0][1]+1)+table[move[1][0]]+(move[1][1]+1);
	}
	
	public static void printBoard(long boardW, long boardB){
		int[][] pawnsW = Bitsmagic.getAllPawns(boardW);
		int[][] pawnsB = Bitsmagic.getAllPawns(boardB);
		System.out.println("\n----------------");
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
				System.out.print(value + " ");
			}
			System.out.println("");
		}
	}
}
