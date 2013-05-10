package main;

import java.util.Arrays;

/**
 * References: 
 * http://chessprogramming.wikispaces.com/BitScan
 * http://chessprogramming.wikispaces.com/General+Setwise+Operations
 * http://en.wikipedia.org/wiki/Bitboard
 * @author Alex Sanscartier
 *
 */
public class Bitsmagic {

	static long BLACK_1 = 281474976710656L;
	static long BLACK_2 = 562949953421312L;
	static long BLACK_3 = 1125899906842624L;
	static long BLACK_4 = 2251799813685248L;
	static long BLACK_5 = 4503599627370496L;
	static long BLACK_6 = 9007199254740992L;
	static long BLACK_7 = 18014398509481984L;
	static long BLACK_8 = 36028797018963968L;
	static long BLACK_9 = 72057594037927936L;
	static long BLACK_10 = 144115188075855872L;
	static long BLACK_11 = 288230376151711744L;
	static long BLACK_12 = 576460752303423488L;
	static long BLACK_13 = 1152921504606846976L;
	static long BLACK_14 = 2305843009213693952L;
	static long BLACK_15 = 4611686018427387904L;
	static long BLACK_16 = -9223372036854775808L;

	static long WHITE_1 = 1L;
	static long WHITE_2 = 2L;
	static long WHITE_3 = 4L;
	static long WHITE_4 = 8L;
	static long WHITE_5 = 16L;
	static long WHITE_6 = 32L;
	static long WHITE_7 = 64L;
	static long WHITE_8 = 128L;
	static long WHITE_9 = 256L;
	static long WHITE_10 = 512L;
	static long WHITE_11 = 1024L;
	static long WHITE_12 = 2048L;
	static long WHITE_13 = 4096L;
	static long WHITE_14 = 8192L;
	static long WHITE_15 = 16384L;
	static long WHITE_16 = 32768L;	
	
	public final static long ALL_BLACK() {
		return(BLACK_1 | BLACK_2 | BLACK_3 | BLACK_4 | BLACK_5 | BLACK_6 | BLACK_7 | BLACK_8 |
		BLACK_9 | BLACK_10 | BLACK_11 | BLACK_12 | BLACK_13 | BLACK_14 | BLACK_15 | BLACK_16); 
	}
	
	public final static long ALL_WHITE() {
		return(WHITE_1 | WHITE_2 | WHITE_3 | WHITE_4 | WHITE_5 | WHITE_6 | WHITE_7 | WHITE_8 |
		WHITE_9 | WHITE_10 | WHITE_11 | WHITE_12 | WHITE_13 | WHITE_14 | WHITE_15 | WHITE_16); 
	}
	
	public final static long ALL_PIECES() {
		return ALL_BLACK() | ALL_WHITE();
	}

	static final long RANK1 = 255L;
	static final long RANK2 = 65280L;
	static final long RANK3 = 16711680L;
	static final long RANK4 = 4278190080L;
	static final long RANK5 = 1095216660480L;
	static final long RANK6 = 280375465082880L;
	static final long RANK7 = 71776119061217280L;
	static final long RANK8 = -72057594037927936L;

	static final long AFile = 72340172838076673L;
	static final long BFile = 144680345676153346L;
	static final long CFile = 289360691352306692L;
	static final long DFile = 578721382704613384L;	
	static final long EFile = 1157442765409226768L;
	static final long FFile = 2314885530818453536L;
	static final long GFile = 4629771061636907072L;
	static final long HFile = -9187201950435737472L;

	//*==============================================================
	//
	//
	//                   GENERAL PURPOSE FUNCTIONS                     
	//
	//
	//*==============================================================
	public static final boolean checkAnyWinner() {
		return (wCheckWinner() | bCheckWinner());
	}
	public static final long getAllValidMoves(long mPiece) {
		return ((wPieceForwardMovement(mPiece) | wPieceAnyAttack(mPiece))  | (bPieceForwardMovement(mPiece) | bPieceAnyAttack(mPiece)));
	}
	public static final long getAllValidMovesForPiece(boolean white, long mPiece) {
		if(white) 
			return wPieceForwardMovement(mPiece) | wPieceAnyAttack(mPiece);
		else
			return bPieceForwardMovement(mPiece) | bPieceAnyAttack(mPiece);
	}
	public static final boolean validMove(long moveFrom, long moveTo) {
		return (((getAllValidMoves(moveFrom) & moveTo) != 0))? true : false;
	}
	//*==============================================================
	//
	//
	//                   WHITE PLAYER MOVES
	//                        
	//
	//
	//*=============================================================
	public final static long wValidMoves(long mPiece) {
		return (wPieceForwardMovement(mPiece) | wPieceAnyAttack(mPiece));
	}
	public final static long wPieceValidEastAttack(long wPiece) {
		return (wPiece >>> 9) & ~(HFile);
	}
	public final static long wPieceValidWestAttack(long wPiece) {
		return (wPiece >>> 7) & ~(AFile);
	}
	public final static long wPieceForwardMovement(long wPiece) {
		return (wPiece >>> 8);
	}
	public final static long wPieceEastAttack(long wPieces) {
		if((wPieces) != 0)
			return ((wPieces & ~(HFile)) << 9) & ~(ALL_WHITE());
		return 0;
	}
	public final static long wPieceWestAttack(long wPieces) {
		if((wPieces) != 0)
			return ((wPieces & ~(AFile)) << 7) & ~(ALL_WHITE());
		return 0;
	}
	//move to ---- move From
	public final static long wPiecesAbleToCaptureAny(long wPieces, long bPieces) {
		return wPieceAnyAttack(wPieces) & (bPieces & ALL_BLACK());
	}
	public final static long wPieceAnyAttack(long wPieces) {
		return wPieceEastAttack(wPieces) | wPieceWestAttack(wPieces);
	}
	public final static long wPusheeDblAttack(long wPieces) {
		return wPieceEastAttack(wPieces) & wPieceWestAttack(wPieces);
	}
	public final static long wPieceSingleAttack(long wPieces) {
		return wPieceEastAttack(wPieces) ^ wPieceWestAttack(wPieces);
	}
	public final static long wPiecesAbleToCaptureEast(long wPieces, long bPieces) {
		return wPieces & bPieceWestAttack(bPieces);
	}
	public final static long wPiecesAbleToCaptureWest(long wPieces, long bPieces) {
		return wPieces & bPieceEastAttack(bPieces);
	}

	public final static boolean wCheckWinner() {
		return ((ALL_WHITE() & RANK8) != 0)? true : false;
	}
	//Calculate Safe Squares
	public final static long wSafeSquare(long wPieces, long bPieces) {
		long whiteEastAttacks = wPieceEastAttack(wPieces);
		long whiteWestAttacks = wPieceWestAttack(wPieces);
		long blackEastAttacks = bPieceEastAttack(bPieces);
		long blackWestAttacks = bPieceWestAttack(bPieces);
		long wPieceDblAttack = whiteEastAttacks & whiteWestAttacks;
		long wPieceOddAttack = whiteEastAttacks ^ whiteWestAttacks;
		long bPieceDblAttack = blackEastAttacks & blackWestAttacks;
		long bPieceAnyAttack = blackEastAttacks | blackWestAttacks;
		return wPieceDblAttack | ~bPieceAnyAttack | (wPieceOddAttack & ~bPieceDblAttack);
	}
	//*==============================================================
	//
	//
	//                   BLACK PLAYER MOVES
	//                   
	//
	//
	//*==============================================================
	public final static long bSafeSquare(long wPieces, long bPieces) {
		long whiteEastAttacks = wPieceEastAttack(wPieces);
		long whiteWestAttacks = wPieceWestAttack(wPieces);
		long blackEastAttacks = bPieceEastAttack(bPieces);
		long blackWestAttacks = bPieceWestAttack(bPieces);
		long bPieceDblAttack = blackEastAttacks & blackWestAttacks;
		long bPieceOddAttack = blackEastAttacks ^ blackWestAttacks;
		long wPieceDblAttack = whiteEastAttacks & whiteWestAttacks;
		long wPieceAnyAttack = whiteEastAttacks | whiteWestAttacks;
		return bPieceDblAttack | ~wPieceAnyAttack | (bPieceOddAttack & ~wPieceDblAttack);		
	}
	public final static long bValidMoves(long mPiece) {
		return bPieceForwardMovement(mPiece) | bPieceAnyAttack(mPiece);
	}
	public final static long bPieceAnyAttack(long bPieces) {
		return bPieceEastAttack(bPieces) | bPieceWestAttack(bPieces);
	}
	public final static long bPieceDblAttack(long bPieces) {
		return bPieceEastAttack(bPieces) & bPieceWestAttack(bPieces);
	}
	public final static long bPusheeSingleAttack(long bPieces) {
		return bPieceEastAttack(bPieces) ^ bPieceWestAttack(bPieces);
	}
	public final static long bPieceValidEastAttack(long bPieces) {
		return (bPieces << 9) & ~(AFile);
	}
	public final static long bPieceValidWestAttack(long bPieces) {
		return (bPieces << 7) & ~(HFile);
	}
	public final static long bPieceForwardMovement(long bPiece) {
		return	(bPiece << 8);
	}
	public final static long bPieceEastAttack(long bPieces) {
		if((bPieces) != 0)
			return ((bPieces & ~(HFile)) >>> 7) & ~(ALL_BLACK());
		return 0;
	}
	public final static long bPieceWestAttack(long bPieces) {
		if(bPieces != 0)
			return ((bPieces & ~(AFile)) >>> 9) & ~(ALL_BLACK());
		return 0;
	}
	public final static long bPiecesAbleToCaptureEast(long wPieces, long bPieces) {
		return bPieces & wPieceWestAttack(wPieces);
	}
	public final static long bPiecesAbleToCaptureWest(long wPieces, long bPieces) {
		return bPieces & wPieceEastAttack(wPieces);
	}
	//MoveTo ---- MoveFrom
	public final static long bPiecesAbleToCaptureAny(long wPieces, long bPieces) {
		return bPieceAnyAttack(bPieces) & (wPieces & ALL_WHITE());
	}
	public final static boolean bCheckWinner() {
		return ((ALL_BLACK() & RANK1) != 0)? true : false;
	}
	//*==============================================================
	//
	//                   MODIFIED MOVEMENT FUNCTION
	// *if any of these functions return null then there is an invalide move
	//
	//*==============================================================	
	//BLACK MOVEMENTS
	public final static long[] bPieceForwardMovement(long bPiece, long bbw, long bbb) {
		long[] bbs = new long[2];
		long afterMove = (bPiece >>> 8);
		if((afterMove & ~(bbw | bbb)) != 0) {
			bbb = bbb | afterMove;
			bbs[0] = bbw;
			bbs[1] = bbb;
			return bbs;
		}
		return null;
	}
	public final static long[] bPieceEastAttack(long bPiece, long bbw, long bbb) {
		long[] bbs = new long[2];
		long afterMove = ((bPiece & ~(HFile)) >>> 7) & ~(bbb);
		if((bPiece) != 0) {
			if((afterMove & bbw) != 0) {
				//Piece is eaten
				bbw = bbw & ~(afterMove);
			}
			bbb = bbb | afterMove;
			bbs[0] = bbw;
			bbs[1] = bbb;
			return bbs;
		}
		return null;
	}
	public final static long[] bPieceWestAttack(long bPiece, long bbw, long bbb) {
		long[] bbs = new long[2];
		long afterMove = ((bPiece & ~(AFile)) >>> 9) & ~(bbb);
		if((bPiece) != 0) {
			if((afterMove & bbw) != 0) {
				//Piece is eaten
				bbw = bbw & ~(afterMove);
			}
			bbb = bbb | afterMove;
			bbs[0] = bbw;
			bbs[1] = bbb;
			return bbs;
		}
		return null;
	}
	//WHITE MOVEMENTS
	public final static long[] wPieceForwardMovement(long wPiece, long bbw, long bbb) {
		long[] bbs = new long[2];
		long afterMove = (wPiece << 8);
		if((afterMove & ~(bbw | bbb)) != 0) {
			bbw = bbw | afterMove;
			bbs[0] = bbw;
			bbs[1] = bbb;	
			return bbs;
		}
		return null;
	}
	public final static long[] wPieceEastAttack(long wPiece, long bbw, long bbb) {
		long[] bbs = new long[2];
		long afterMove = ((wPiece & ~(HFile)) << 9) & ~(bbw);
		if((wPiece) != 0) {
			if((afterMove & bbb) != 0) {
				//Piece is eaten
				bbb = bbb & ~(afterMove);
			}
			bbw = bbw | afterMove;
			bbs[0] = bbw;
			bbs[1] = bbb;
			return bbs;
		}
		return null;
	}
	public final static long[] wPieceWestAttack(long wPiece, long bbw, long bbb) {
		long[] bbs = new long[2];
		long afterMove = ((wPiece & ~(AFile)) << 7) & ~(bbw);
		if((wPiece) != 0) {
			if((afterMove & bbb) != 0) {
				//Piece is eaten
				bbb = bbb & ~(afterMove);
			}
			bbw = bbw | afterMove;
			bbs[0] = bbw;
			bbs[1] = bbb;
			return bbs;
		}
		return null;
	}
	//*==============================================================
	//
	//                   UTILITY FUNCTION
	//
	//
	//*==============================================================	
	public static String convertSavedBoard(String board) {
		String newBoard = "";
		char[] cArray = board.toCharArray();
		for(int k = 0; k < 8; k++) {
			for(int i = 7; i >= 0; i--) {
				newBoard += cArray[k*8+i];
			}
		}
		return newBoard;
	}
	//*==============================================================
	//
	//                   POSITION FUNCTIONS
	//
	//
	//*==============================================================	
	public static long[] applyMove(int move[][], long bbw, long bbb) {
		long[] bbs = new long[2];
		int xFrom = move[0][0]; int yFrom = move[0][1];
		int xTo   = move[1][0]; int yTo   = move[1][1];
		int bitIndexFrom = (8*yFrom + xFrom);
		
		long pieceToMove = 1L << bitIndexFrom;
		//check if black or white move :
		boolean isWhite = (yTo > yFrom) ? true : false;
		if(isWhite) {
			//if it's white remove the piece being moved to replace it later
			bbw = bbw & ~(pieceToMove);
			//check if left diagonal move
			if(xFrom > xTo) {
				bbs = wPieceWestAttack(pieceToMove, bbw, bbb);
			} else if(xFrom < xTo){
				bbs = wPieceEastAttack(pieceToMove, bbw, bbb);
			} else {
				bbs = wPieceForwardMovement(pieceToMove, bbw, bbb);
			}
		} else {
			//if it's black remove the piece being moved to replace it later
			bbb = bbb & ~(pieceToMove);
			if(xFrom > xTo) {
				bbs = bPieceWestAttack(pieceToMove, bbw, bbb);
			} else if(xFrom < xTo){
				bbs = bPieceEastAttack(pieceToMove, bbw, bbb);
			} else {
				bbs = bPieceForwardMovement(pieceToMove, bbw, bbb);
			}
		}
		return bbs;		
	}
	public static long applyMove(int[][] move, long bb) {
		long movebb;
		int xFrom = move[0][0]; int yFrom = move[0][1];
		int xTo   = move[1][0]; int yTo   = move[1][1];
		int bitIndexFrom = (8*yFrom + xFrom);
		
		bb = bb & ~(1L << bitIndexFrom);
		long pieceToMove = 1 << bitIndexFrom;
		//check if black or white move :
		boolean isWhite = (xTo > xFrom) ? true : false;
		if(isWhite) {
			//check if left diagonal move
			if(xFrom > xTo) {
				movebb = wPieceWestAttack(pieceToMove);
			} else if(xFrom < xTo){
				movebb = wPieceEastAttack(pieceToMove);
			} else {
				movebb = wPieceForwardMovement(pieceToMove);
			}
		} else {
			if(xFrom > xTo) {
				movebb = bPieceWestAttack(pieceToMove);
			} else if(xFrom < xTo){
				movebb = bPieceEastAttack(pieceToMove);
			} else {
				movebb = bPieceForwardMovement(pieceToMove);
			}
		}
		movebb = bb | movebb;
		return movebb;
	}
	public final static boolean isPawnAt(int[] index, long bb, long bb2) {
		return isPawnAt(index, bb | bb2);
	}
	public final static boolean isPawnAt(int[] index, long bb) {
		int bitIndex = (8*index[1] + index[0]); //index[1] = y and index[0] = x
		return (((1L << bitIndex) & bb) != 0)? true : false;
	}
	public static int[][] getAllPawns(long bb) {
		int index = 0;
		int[][] pieceIndexArray = new int[Long.bitCount(bb)][2];
		while (bb!=0) {
			long t = bb & -bb;
			pieceIndexArray[index][0] = getPieceIndex(Long.bitCount(t-1))[0];
			pieceIndexArray[index][1] = getPieceIndex(Long.bitCount(t-1))[1];
			bb ^= t;
			index++;
		}
		return pieceIndexArray;
	}
	private final static int[] getPieceIndex(int squareIndex) {
		int[] indeces = new int[2];
		indeces[0] =  squareIndex & 7;
		indeces[1] =  squareIndex >> 3;
		return indeces;
	}
	private static final byte[] longToByteArray(long l) {
		byte[] result = new byte[8];

		result[7] = (byte) ((l >>> 56));
		result[6] = (byte) ((l >>> 48));
		result[5] = (byte) ((l >>> 40));
		result[4] = (byte) ((l >>> 32));
		result[3] = (byte) ((l >>> 24));
		result[2] = (byte) ((l >>> 16));
		result[1] = (byte) ((l >>> 8));
		result[0] = (byte) ((l >>> 0));
		return result;
	}
	private static final int getBit(byte[] data, int pos) {
		int posByte = pos/8;
		int posBit = pos%8;
		byte valByte = data[posByte];
		int valInt = valByte>>(8-(posBit+1)) & 0x0001;
		return valInt;
	}
	private static final int bitScanForward(long bb) { 
		return Long.bitCount(( (bb & -bb) - 1 ));
	}
	private static final int bitScanReverse(long bb) {
		int result = 0;
		if (bb > 0xFFFFFFFF) {
			bb >>= 32;
			result = 32;
		}
		if (bb > 0xFFFF) {
			bb >>= 16;
			result += 16;
		}
		if (bb > 0xFF) {
			bb >>= 8;
			result += 8;
		}
		return result;
	}
	//*==============================================================
	//
	//                   BITBOARD FUNCTIONS
	//
	//
	//*==============================================================
	// White = 4 Black = 2
	public static final long[] convertStringToBitBoard(String s) {
		long[] bb = new long[2];
		char[] cArray = s.toCharArray();
		for(Integer i = cArray.length-1; i >= 0; i--) {
			long k = (1L << i);
			if(cArray[63-i] == '2') {
				bb[1] = bb[1] | k;
			} else if (cArray[63-i] == '4') {
				bb[0] = bb[0] | k;
			}
		}
		return bb;
	}
	public static final void printNiceCurrentBoard(long bbw, long bbb) {
		String wPieces = Long.toBinaryString(bbw);
		wPieces = String.format("%64s", wPieces).replace(' ', '0').replace('1', 'O');
		String bPieces = Long.toBinaryString(bbb);
		bPieces = String.format("%64s", bPieces).replace(' ', '0').replace('1', 'X');

		char[] wPEchar = wPieces.toCharArray();
		char[] bPRchar = bPieces.toCharArray();	
		String[] strArray = new String[64];

		for(int i = 0; i <= 63; i++) {
			if(wPEchar[i] != '0') {
				strArray[i] = wPEchar[i]+"";
			} else if(bPRchar[i] != '0') {
				strArray[i] = bPRchar[i]+"";
			} else {
				strArray[i] = "-";
			}
		}
		/*
		System.out.println(String.format("%64s", "").replace(' ', '='));
		System.out.println(String.format("%64s", Long.toBinaryString(bbw)).replace('0', '-'));
		System.out.println(wPieces.replace('0', '-'));
		System.out.println(String.format("%64s", "").replace(' ', '='));
		System.out.println(String.format("%64s", Long.toBinaryString(bbb)).replace('0', '-'));
		System.out.println(bPieces.replace('0', '-'));
		System.out.println(String.format("%64s", "").replace(' ', '='));
		*/
		System.out.println(); //Make sure nothing gets in the way of the board

		for(int j = 0; j < wPieces.length()/8; j++) {
			System.out.print(8-j+"| ");
			for(int k = 7; k >= 0; k--) {
				System.out.print(strArray[(j * 8) + k] + " ");
			}
			System.out.println("");
			if(j==7) {
				System.out.println("  ================");
				System.out.println("   A B C D E F G H ");
			}
		}	

	}
	public static final void printCurrentBitBoard() {
		printCurrentBitBoardWithValue(ALL_PIECES());
	}
	public static final String bit64String(long binaryStr) {
		return String.format("%64s", Long.toBinaryString(binaryStr)).replace(' ', '0');
	}
	public static final void printCurrentBitBoardWithValue(long mPieces) {
		String str = Long.toBinaryString(mPieces);
		str = String.format("%64s", str).replace(' ', '0');
		for(int i = 0; i < str.length(); i++) {
			if((i) % 8 == 0)
				System.out.print(" | ");
			System.out.print(str.charAt(i));
		}
		System.out.println(" | ");

		System.out.println("-----------------------------------------------------------------");
		for(int j = 0; j < str.length()/8; j++) {
			System.out.print("| ");
			for(int k = 7; k >= 0; k--) {
				System.out.print(String.format("%02d",((((str.length()-1)/8)-j) * 8  + (7-k))) + ": " + str.charAt((j * 8) + k) + " | ");
			}
			System.out.println();
			System.out.println("-----------------------------------------------------------------");
		}

	}
	private static int count = 0;
	
	public static void main(String[] args) {
		String originalBoard = "2 0 0 0 0 0 2 2 0 0 0 2 2 2 2 0 2 2 2 0 0 0 0 2 0 2 0 2 2 0 0 0 0 4 0 4 0 4 0 0 4 0 4 0 4 0 4 0 0 4 4 0 4 4 0 4 0 4 0 0 0 0 4 0 ";
		String newBoard = convertSavedBoard(originalBoard.trim().replaceAll(" " , ""));
		//System.out.println(newBoard);
		String bbs = newBoard;
		long[] bb = convertStringToBitBoard(bbs);
		
		printNiceCurrentBoard(bb[0], bb[1]);
		
		int[][][] move = {{{4,2},{4,3}},{{4,3},{5,4}},{{5,4},{4,5}},{{4,5},{5,6}},{{5,6},{5,7}}};
		for(int i = 0; i <= move.length-1;i++) {
			bb = applyMove(move[i], bb[0], bb[1]);
			printNiceCurrentBoard(bb[0], bb[1]);
		}
		//int[][] f = {{1,0},{6,0},{1,1},{2,1},{4,1},{5,1},{7,1},{0,2},{2,2},{4,2},{6,2},{1,3},{3,3},{5,3},
		//			{1,4},{3,4},{4,4},{0,5},{1,5},{2,5},{7,5},{3,6},{4,6},{5,6},{6,6},{0,7},{6,7},{7,7}};
		//System.out.println("PAWN_AT" + " {x,y}");
		//for(int i = 0; i < 28; i++) {
		//	System.out.println(isPawnAt(f[i],bb[1] | bb[0]) + "    " + "{" + f[i][0] + "," + f[i][1] + "}");
		//}
		//*/
		//printCurrentBitBoardWithValue(bb[1]);
		//printCurrentBitBoard();
	}
}