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

   private ArrayList<String> incomingData;
   private ArrayList<DistanceTable> incomingTables;
   private DatagramSocket listenSocket;
   private boolean finished;

   public NodeListener (ArrayList<String> incomingData, DatagramSocket listenSocket) {
      this.incomingData = incomingData;
      this.listenSocket = listenSocket;
      finished = false;
   }

   public void run () {
      while (finished == false) {
         try {
            byte[] receiveData = new byte[100000];

            DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
            listenSocket.receive(receivePacket);

            ByteArrayInputStream dataStream = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream deserializer = new ObjectInputStream(dataStream);
            DistanceTable foreignVectorTable = (DistanceTable) deserializer.readObject();

            // String incomingString = new String(receivePacket.getData());
            // incomingData.add(incomingString); // Send this into our input data.
            incomingTables.add(foreignVectorTable);
            System.out.println ("***************** Received A TABLE FROM " + foreignVectorTable.creatorID + " *************************\n ");
         } catch (IOException e) {
            System.out.println ("something went wrong listening!");
         } catch (ClassNotFoundException e) {
            System.out.println ("We were passed something that wasn't a DistanceTable!");
         }
      }
   }
}
