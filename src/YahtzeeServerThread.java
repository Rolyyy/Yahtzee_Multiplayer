
import java.net.*;
import java.io.*;


public class YahtzeeServerThread extends Thread {

	
  private Socket YahtzeeSocket = null;
  private YahtzeeSharedState mySharedYahtzeeStateObject;
  private String myYahtzeeServerThreadName;
  private double mySharedVariable;
   
  //Setup the thread
  	public YahtzeeServerThread(Socket YahtzeeSocket, String YahtzeeServerThreadName, YahtzeeSharedState SharedObject) {
	
//	  super(YahtzeeServerThreadName);
	  this.YahtzeeSocket = YahtzeeSocket;
	  mySharedYahtzeeStateObject = SharedObject;
	  myYahtzeeServerThreadName = YahtzeeServerThreadName;
	}

  public void run() {
    try {
      System.out.println(myYahtzeeServerThreadName + "initialising.");
      PrintWriter out = new PrintWriter(YahtzeeSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(YahtzeeSocket.getInputStream()));
      String inputLine, outputLine;
      
     
     
      
      while ((inputLine = in.readLine()) != null) {
    	  // Get a lock first
    	  try { 
    		  mySharedYahtzeeStateObject.acquireLock();  
    		  outputLine = mySharedYahtzeeStateObject.processInput(myYahtzeeServerThreadName, inputLine);
    		  out.println(outputLine);
    		  mySharedYahtzeeStateObject.releaseLock();  
    	  } 
    	  catch(InterruptedException e) {
    		  System.err.println("Failed to get lock when reading:"+e);
    	  }
      }

       out.close();
       in.close();
       YahtzeeSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}