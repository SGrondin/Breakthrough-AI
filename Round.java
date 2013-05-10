package main;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Round {
	public long dStarted = new Date().getTime();
	public long TIME = 4850;
	public ExecutorService pool;
	public Node top;
	public Node best;
	public int depth = 12;
	
	public Round(long boardW, long boardB){
		//Timer timer = new Timer();
		//timer.schedule(new Timekeeper(this, TIME), TIME);
		
		for (int i=1;i<=this.depth;i++){
			Date t1 = new Date();
			pool = Executors.newFixedThreadPool(Breakthrough.nbThreads);
			this.top = new Node(pool, i, null, 0, null, boardW, boardB, Breakthrough.color);
			this.top.run();
			
			try {
				pool.shutdown();
				pool.awaitTermination(TIME, TimeUnit.MILLISECONDS);
				TIME = TIME - (new Date().getTime() - t1.getTime());
			} catch (InterruptedException e) {e.printStackTrace();}
			System.out.println(i+" : "+(pool.isTerminated() ? "Terminated" : "NOT Terminated"));
			if (!pool.isTerminated()){
				pool.shutdownNow();
				try {
					pool.awaitTermination(10, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {e.printStackTrace();}
			}else{
				if (this.top.move != null){
					this.best = this.top;
				}
			}
			
			
			System.out.println("TIME: "+TIME+"  "+Node.getInc()+" nodes. TOP VALUE:"+this.top.value+" ("+Utils.moveToString(this.top.move)+")");
			
			if (Breakthrough.DEBUG){System.out.println(Round.printRound(this.top, 1));}
			
			if (TIME <= 25 || i >= this.depth || this.best.value >= 3500000 || this.top.move == null) {
				i = Integer.MAX_VALUE - 10;
				//if (this.top.move != null && this.top.value != null && this.top.value > this.best.value){
				//	this.best = this.top;
				//}
				Breakthrough.movePicked(this.best.move);
			}
			
			//if (((this.top.value != null && this.top.value >= 1000000) || !terminated) && this.top.move != null){
		}
	}
	
	public static String printRound(Node node, int indent){
		StringBuilder sb = new StringBuilder();
		Iterator<Node> i = node.children.iterator();
		sb.append(node.value+" ("+Utils.moveToString(node.move)+")");
		while(i.hasNext()){
			sb.append("\n"+Utils.repeat("***", indent)+Round.printRound(i.next(), indent+1));
		}
		return sb.toString();
	}
}
