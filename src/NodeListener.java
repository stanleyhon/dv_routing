import java.io.*;
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

   private ArrayList<DistanceVectorTable> incomingData;
   private DatagramSocket listenSocket;
   private boolean finished = false;

   public NodeListener (ArrayList<DistanceVectorTable> incomingData, DatagramSocket listenSocket) {
      this.incomingData = incomingData;
      this.listenSocket = listenSocket;
   }

   public void stopListening () {
      this.finished = true;
   }

   public void run () {

      while (finished == false) {
         try {
            byte[] receiveData = new byte[5000];
            // check for messages
            DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
            listenSocket.receive(receivePacket);
            ByteArrayInputStream bis = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bis);

            DistanceVectorTable incomingDistanceVector = (DistanceVectorTable) ois.readObject();
            ois.close();
            bis.close();

            incomingData.add(incomingDistanceVector);
            // System.out.println ("***************** Received a vector table from " + incomingDistanceVector.owner + " *************************\n ");

         } catch (IOException e) {
            System.out.println ("something went wrong listening!");
         } catch (ClassNotFoundException e) {
            System.out.println ("something went wrong reconstructing the DistanceVectorTable");
         }
      }
   }
}
