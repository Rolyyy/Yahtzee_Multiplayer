import java.net.*;
import java.io.*;





public class YahtzeeServer {
  public static void main(String[] args) throws IOException {

	ServerSocket YahtzeeServerSocket = null;
    boolean listening = true;
    String YahtzeeServerName = "YahtzeeServer";
    int YahtzeeServerNumber = 4789;
    
    double SharedVariable = 1;

    //Create the shared object in the global scope...
    
    YahtzeeSharedState ourSharedYahtzeeStateObject = new YahtzeeSharedState(SharedVariable);
        
    // Make the server socket

    try {
      YahtzeeServerSocket = new ServerSocket(YahtzeeServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + YahtzeeServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(YahtzeeServerName + " started....");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new YahtzeeServerThread(YahtzeeServerSocket.accept(), "YahtzeeServerThread1", ourSharedYahtzeeStateObject).start();
      new YahtzeeServerThread(YahtzeeServerSocket.accept(), "YahtzeeServerThread2", ourSharedYahtzeeStateObject).start();
      new YahtzeeServerThread(YahtzeeServerSocket.accept(), "YahtzeeServerThread3", ourSharedYahtzeeStateObject).start();
      new YahtzeeServerThread(YahtzeeServerSocket.accept(), "YahtzeeServerThread4", ourSharedYahtzeeStateObject).start();
      System.out.println("New " + YahtzeeServerName + " thread started.");
    }
    YahtzeeServerSocket.close();
  }
}