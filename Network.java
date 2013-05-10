package main;

import java.io.*;
import java.net.*;

class Network {
	
	public BufferedInputStream input;
	public BufferedOutputStream output;
	public Socket MyClient;
	
	public Network () {
    }
	
	public void start(){
		try {
			MyClient = new Socket("localhost", 8888);
		   	input    = new BufferedInputStream(MyClient.getInputStream());
			output   = new BufferedOutputStream(MyClient.getOutputStream());
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));  
			
		   	while(true){
				char cmd = 0;
			   	
	            cmd = (char)input.read();
	            		
	            if(cmd == '3'){// Server sent opponent's move and is waiting for ours
					byte[] aBuffer = new byte[16];
					int size = input.available();
					input.read(aBuffer,0,size);
					
					// Opponent's move [ex : D7D6 or D7-D6]
					String s = new String(aBuffer);
					Breakthrough.moveReceived(s);
				}else if(cmd == '1'){// New game as white
	                byte[] aBuffer = new byte[1024];
					int size = input.available();
					input.read(aBuffer,0,size);
					
	                String s = new String(aBuffer);
	                Breakthrough.newGameWhite(s);
	            }else if(cmd == '2'){ //new game as black
	                byte[] aBuffer = new byte[1024];
					int size = input.available();
					input.read(aBuffer,0,size);
					
	                String s = new String(aBuffer);
	                Breakthrough.newGameBlack(s);
	            }else if(cmd == '4'){ //Invalid
					System.out.println("Invalid move!");				
				}
	        }
		}
		catch (IOException e) {
	   		System.out.println(e);
		}
	}
	
	public void sendMove(String move){
		try {
			System.out.println("Sending >"+move);
			output.write(move.getBytes(),0,move.length());
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}



