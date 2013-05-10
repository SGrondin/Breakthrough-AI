package main;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Node implements Runnable {
	public Node parent;
	public volatile Integer value = null;
	public int level;
	public volatile int[][] move;
	public ArrayList<Node> children = new ArrayList<Node>();
	private static long cnt = 0;
	public ExecutorService pool;
	public long boardW;
	public long boardB;
	public boolean color;
	public int depth;
	public boolean completed = false;
	
	public static synchronized void inc(){
		cnt++;
	}
	public static synchronized long getInc(){
		long ret = cnt;
		cnt = 0;
		return ret;
	}
	
	public Node(ExecutorService pool, int depth, Node parent, int level, int[][] move, long boardW, long boardB, boolean color){
		this.pool = pool;
		this.parent = parent;
		this.level = level;
		this.move = move;
		this.boardW = boardW;
		this.boardB = boardB;
		this.color = color;
		this.depth = depth;
	}
	
	public void run(){
		
		if (this.level > 0){
			Node.inc();
		}
		
		boolean childColor;
		
		long[] boards = null;
		if (this.move != null){ //If not top node
			boards = Bitsmagic.applyMove(move, boardW, boardB);
			if (boards != null){
				boardW = boards[0];
				boardB = boards[1];
			}
			childColor = !color;
			depth--;
		}else{
			childColor = color;
		}
		
		if (depth > 0){
			int childLevel = level+1;
			
			int offsetY = childColor ? 1 : -1;
			long myBoard = childColor ? boardW : boardB;
			long enemyBoard = childColor ? boardB : boardW;
			int[][] pawns = Bitsmagic.getAllPawns(myBoard);
			
			if (pawns.length == 0){
				if (this.color == Breakthrough.color){
					this.value = Integer.MIN_VALUE;
				}else{
					this.value = Integer.MAX_VALUE;
				}
				if (this.parent != null){
					this.propagate(this);
				}
				return;
			}
			
			Node node = null;
			for (int i=0;i<pawns.length;i++){
				int[][] newMove1 = {pawns[i], {pawns[i][0]-1, pawns[i][1]+offsetY}}; //Front left move
				if (!this.completed && newMove1[1][0]>=0 && !Bitsmagic.isPawnAt(newMove1[1], myBoard)){
					node = new Node(this.pool, depth, this, childLevel, newMove1, boardW, boardB, childColor);
					if (Breakthrough.MT && this.level == 0){
						this.pool.submit(node);
					}else{
						node.run();
					}
					if (Breakthrough.DEBUG){children.add(node);}
				}
				
				int[][] newMove2 = {pawns[i], {pawns[i][0], pawns[i][1]+offsetY}}; //Front move
				if (!this.completed && !Bitsmagic.isPawnAt(newMove2[1], myBoard, enemyBoard)){
					node = new Node(this.pool, depth, this, childLevel, newMove2, boardW, boardB, childColor);
					if (Breakthrough.MT && this.level == 0){
						this.pool.submit(node);
					}else{
						node.run();
					}
					if (Breakthrough.DEBUG){children.add(node);}
				}
				
				int[][] newMove3 = {pawns[i], {pawns[i][0]+1, pawns[i][1]+offsetY}}; //Front right move
				if (!this.completed && newMove3[1][0]<=7 && !Bitsmagic.isPawnAt(newMove3[1], myBoard)){
					node = new Node(this.pool, depth, this, childLevel, newMove3, boardW, boardB, childColor);
					if (Breakthrough.MT && this.level == 0){
						this.pool.submit(node);
					}else{
						node.run();
					}
					if (Breakthrough.DEBUG){children.add(node);}
				}
				if (this.completed){
					i = Integer.MAX_VALUE - 10;
				}
				else if(i == pawns.length-1){
					this.completed = true;
				}
			}
			if (node != null && this.parent != null){
				this.propagate(this);
			}
			
		}else{
			this.value = Analyzer.analyze(boardW, boardB, Breakthrough.color);
			//Utils.printBoard(boardW, boardB);
			
			if (this.parent != null){
				this.propagate(this);
			}
		}
	}
	
	public void propagate(Node node){
		if (node.parent.value == null || (Utils.moduloTwo(node.parent.level) && node.value > node.parent.value) || (!Utils.moduloTwo(node.parent.level) && node.value < node.parent.value)){
			node.parent.value = node.value;
			
			boolean modulo = Breakthrough.color ? Utils.moduloTwo(node.level) : !Utils.moduloTwo(node.level);
			
			if (node.level > 1 && node.parent.parent.value != null &&
					((modulo && node.parent.value <= node.parent.parent.value) ||
							(!modulo && node.parent.value >= node.parent.parent.value))){
				node.parent.completed = true;
			}
			
			if (node.level == 1){
				node.parent.move = node.move;
			}
		}
		if (Breakthrough.DEBUG){
			String str = "L"+node.level+" "+node.value+" ("+Utils.moveToString(node.move);
			str += ") PARENT L"+node.parent.level+" "+node.parent.value+" ("+Utils.moveToString(node.parent.move)+") "+(node.parent.completed ? "" : "NOT ")+"COMPLETED";
			str += " GPARENT "+((node.parent.parent == null) ? "NULL" : "L"+node.parent.parent.level+" "+node.parent.parent.value+" ("+Utils.moveToString(node.parent.parent.move)+")");
			System.out.println(str);
			//Breakthrough.out.println(str);
		}
		
	}
	
}
