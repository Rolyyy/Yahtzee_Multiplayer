import java.net.*;
import java.io.*;

public class SharedActionState{
	
	private SharedActionState mySharedObj;
	private String myThreadName;
	private double mySharedVariable;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers
	private int p1rounds=1;
	private int p2rounds=1;
	
// Constructor	
	
	SharedActionState(double SharedVariable) {
		mySharedVariable = SharedVariable;
	}

//Attempt to aquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    
		  public synchronized String processOutput(String myThreadName, String theInput) {
			return null;
			  
			  
		  }
		  
		  /* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
    		//System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		
    		// Check what the client said
    		if (theInput.equalsIgnoreCase("send score")) {
    			//Correct request
    			if (myThreadName.equals("ActionServerThread1")) {
    				
    				
 
    				theOutput =  "Current round: " + p1rounds;
    				p1rounds++;
    			}
    			else if (myThreadName.equals("ActionServerThread2")) {
    				theOutput =  "Current round: " + p2rounds;
    				p2rounds++;
    				

    			}
       			else if (myThreadName.equals("ActionServerThread3")) {
       				
       				

       			}
       			else if (myThreadName.equals("ActionServerThread4")) {
    				
    				System.out.println(myThreadName + " made contact " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;
       			}
       			else {System.out.println("Error - thread call not recognised.");}
    		}
    		else { //incorrect request
    			theOutput = "WRONG INPUT";
		
    		}
 
     		//Return the output message to the ActionServer
    		System.out.println(theOutput);
    		return theOutput;
    	}	
}

