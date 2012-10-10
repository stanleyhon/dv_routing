import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public class NodeListener extends Thread {

   private ArrayList<String> incomingData;
   private DatagramSocket listenSocket;

   public NodeListener (ArrayList<String> incomingData, DatagramSocket listenSocket) {
      this.incomingData = incomingData;
      this.listenSocket = listenSocket;
   }

   public void run () {
      boolean finished = false;
      while (finished == false) {
         try {
            byte[] receiveData = new byte[1024];
            // check for messages
            DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
            System.out.println ("LISTENING\n");
            listenSocket.receive(receivePacket);
            String incomingString = new String(receivePacket.getData());
            incomingData.add(incomingString); // Send this into our input data.
            System.out.println ("Received:\n ");
            System.out.print (incomingString);
         } catch (IOException e) {
            System.out.println ("something went wrong listening!");
         }
      }
   }
}
