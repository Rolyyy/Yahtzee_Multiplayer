import java.net.*;
import java.io.*;





public class YahtzeeServer {
  public static void main(String[] args) throws IOException {

	ServerSocket ActionServerSocket = null;
    boolean listening = true;
    String ActionServerName = "YahtzeeServer";
    int ActionServerNumber = 4789;
    
    double SharedVariable = 1;

    //Create the shared object in the global scope...
    
    YahtzeeSharedState ourSharedActionStateObject = new YahtzeeSharedState(SharedVariable);
        
    // Make the server socket

    try {
      ActionServerSocket = new ServerSocket(ActionServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + ActionServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(ActionServerName + " started....");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new YahtzeeServerThread(ActionServerSocket.accept(), "ActionServerThread1", ourSharedActionStateObject).start();
      new YahtzeeServerThread(ActionServerSocket.accept(), "ActionServerThread2", ourSharedActionStateObject).start();
      new YahtzeeServerThread(ActionServerSocket.accept(), "ActionServerThread3", ourSharedActionStateObject).start();
      new YahtzeeServerThread(ActionServerSocket.accept(), "ActionServerThread4", ourSharedActionStateObject).start();
      System.out.println("New " + ActionServerName + " thread started.");
    }
    ActionServerSocket.close();
  }
}