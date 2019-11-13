import java.net.*;
import java.io.*;

public class YahtzeeSharedState{
	
	private YahtzeeSharedState mySharedObj;
	private String myThreadName;
	private double mySharedVariable;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers
	
	private int p1rounds=1;
	private int p2rounds=1;
	private int p3rounds=1;
	
	private int p1score=0;
	private int p2score=0;
	private int p3score=0;
	
	private boolean p1finish= false;
	private boolean p2finish= false;
	private boolean p3finish= false;
	
// Constructor	
	YahtzeeSharedState(double SharedVariable) {
		mySharedVariable = SharedVariable; //In this case, this is the Global round check. This is to keep players on the same count.
	}

//Attempt to aquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        //System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		   // System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      //System.out.println(me.getName()+" released a lock!");
		  }
	
	
    
		  public synchronized String processOutput(String myThreadName, String theInput) {
			return null;
			  
			  
		  }
		  
		  /* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
    		//System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		
    		if(theInput.equals("OVER") ) {
    			System.out.println("'OVER' if loop run!");
    			if (myThreadName.equals("ActionServerThread1")) {
    				p1finish = true;
    			}
    			if (myThreadName.equals("ActionServerThread2")) {
    				p2finish = true;
    			}
    			if (myThreadName.equals("ActionServerThread3")) {
    				p3finish = true;
    			}
    			
    			
    			
    			if(p1finish = true && p2finish == true && p3finish == true) {
    				String winner = winnerCheck();
    				
    				
    				switch(winner) {
    				case "p1":
    					theOutput = "Player 1 has WON with " + p1score + " points ! P2 score: " + p2score + ". P3 score: " + p3score + ".";
    					System.out.println(theOutput);
    		    		return theOutput;
    				case "p2":
    					theOutput = "Player 2 has WON with " + p2score + " points ! P1 score: " + p1score + ". P3 score: " + p3score + ".";
    					System.out.println(theOutput);
    		    		return theOutput;
    				case "p3":
    					theOutput = "Player 3 has WON with " + p3score + " points ! P1 score: " + p1score + ". P2 score: " + p2score + ".";
    					System.out.println(theOutput);
    		    		return theOutput;
    				}
    				
    				/**
    				if(winner == "p1") {
    					theOutput = "Player 1 has WON with " + p1score + " points ! P2 finished with " + p2score + ".";
    					System.out.println(theOutput);
    		    		return theOutput;
    				}
    				else {
    					theOutput = "Player 2 has WON with " + p2score + " points ! P1 finished with " + p1score + ".";
    					System.out.println(theOutput);
    		    		return theOutput;
    				}
    				*/
    			}
    			else {
    				theOutput = "not all players have finished...";
    				System.out.println(theOutput);
    	    		return theOutput;
    			}
    			
    			
    		}
    		
    		int int_input = Integer.parseInt(theInput);
    		if(mySharedVariable<14) {
    		if (int_input > -1) {
    			//Correct request
    			if (myThreadName.equals("ActionServerThread1")) {
    				
    				if(mySharedVariable == p1rounds) {
    				
    				p1score = int_input;
    				System.out.println("Shared round: " + mySharedVariable);
    				theOutput =  "P1 Round: " + p1rounds +  "| score: " + p1score + "| P2 score:" + p2score + "| P3 score:" + p3score;
    				p1rounds++;
    				IncrementGlobalRoundCheck();
    				}
    					else {
    					theOutput = "mismatch"; //Letting client know player is ahead of other players
    					}
    				
    			}
    			
    			
    			else if (myThreadName.equals("ActionServerThread2")) {
    				
    				if(mySharedVariable == p2rounds) {
        				
        				p2score = int_input;
        				System.out.println("Shared round: " + mySharedVariable);
        				theOutput =  "P2 Round: " + p2rounds +  "| score: " + p2score + "| P1  score:" + p1score + "| P3  score:" + p3score;
        				p2rounds++;
        				IncrementGlobalRoundCheck();
        				}
        				else {
        					theOutput = "mismatch"; //Letting client know player is ahead of other players
        				}
    				}
    			
    			
       			else if (myThreadName.equals("ActionServerThread3")) {
       				
       					if(mySharedVariable == p3rounds) {
        				
        				p3score = int_input;
        				System.out.println("Shared round: " + mySharedVariable);
        				theOutput =  "P3 Round: " + p3rounds +  "| score: " + p3score + "| P1  score:" + p1score + "| P2  score:" + p2score;
        				p3rounds++;
        				IncrementGlobalRoundCheck();
        				}
        				else {
        					theOutput = "mismatch"; //Letting client know player is ahead of other players
        				}
       				
       			}
    			
       			
    			
    			
       			else {System.out.println("Error - thread call not recognised.");}
    		}
    		else { //incorrect request
    			theOutput = "WRONG INPUT";
    		}
    		}
    		else {
    			theOutput = "GAME OVER!";
    		}
     		//Return the output message to the ActionServer
    		System.out.println(theOutput);
    		return theOutput;
    	}

		public void IncrementGlobalRoundCheck() {
			if(p1rounds > mySharedVariable && p2rounds > mySharedVariable && p3rounds > mySharedVariable) {
				mySharedVariable++;
			}
			
		}
		
		public String winnerCheck() {
			
			String winner;
			
			if(p1score>p2score && p1score>p3score) {
				winner = "p1";
				return winner;
			}
			else if(p2score>p1score && p2score>p3score) {
				winner = "p2";
				return winner;
			}
			else {
				winner = "p3";
				return winner;
			}
			
		}
}

