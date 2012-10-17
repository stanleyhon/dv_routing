import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
// helloosdflksdfjlsdkfjsldkjsdlfkjlsdkf

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public class Node {
   private DatagramSocket listenSocket;

   private DatagramSocket sendSocket;

   public DistanceVectorTable dvt;

   public char NODE_ID;
   public int NODE_PORT;

   private ArrayList<DistanceVectorTable> incomingData;

   public Node (char NODE_ID, int NODE_PORT) {
      this.NODE_ID = NODE_ID;
      this.NODE_PORT = NODE_PORT;



      incomingData = new ArrayList<DistanceVectorTable>();
   }

   // This method blocks!
   public void start () {
      if (listenSocket != null) {
         System.out.println ("Attempting to start already started node!");
      } else {
         try {
            listenSocket = new DatagramSocket(this.NODE_PORT); // setup our listen socket
         } catch (SocketException e) {
            System.out.println ("Unable to create UDP socket");
         }
      }

      // System.out.println ("setting up thread to listen");
      // Setup a thread to listening
      NodeListener ns = new NodeListener(incomingData, listenSocket);
      ns.start();

      boolean stop = false;
      boolean stablized = false;
      int timesUnchanged = 0;

      HashMap<Character, Boolean> nodeFailureIndicator = new HashMap<Character, Boolean>();
      HashMap<Character, Integer> nodeFailureCount = new HashMap<Character, Integer>();


      for (char c : dvt.neighbours) {
         nodeFailureCount.put(c, 0);
         nodeFailureIndicator.put(c, false);
      }

      while (stop == false) {

         // Assimilate dvt's we got
         boolean changesMade = false;
         for (DistanceVectorTable foreignDVT : this.incomingData) {
            if (this.dvt.assimilate(foreignDVT) == true) {
               changesMade = true;
               timesUnchanged = 0;
            }

            // add one to the count of received by this foreigner
            nodeFailureIndicator.put(foreignDVT.owner, false);
         }

         // See how much things have been failing
         for (char c : dvt.neighbours) {
            if (!dvt.failedNodes.contains(c)) {
               if (nodeFailureIndicator.get(c) == true) {
                  // C MISSED A PACKET!
                  nodeFailureCount.put(c, nodeFailureCount.get(c) + 1);
               } else {
                  nodeFailureCount.put(c, 0);
                  nodeFailureIndicator.put(c, true);
               }
            }
         }

         ArrayList<Character> failThese = new ArrayList<Character>();
         // If anyone hits 3 failures
         for (char c : dvt.neighbours) {
            if (nodeFailureCount.get(c) == 3) {
               if (!this.dvt.failedNodes.contains(c)) {
                  failThese.add(c);
               }
            }
         }

         for (char c : failThese) {
            this.dvt.nodeFailed(c);
         }


         if (changesMade == false) {
            timesUnchanged++;
         } else if (changesMade == true && stablized == true) {
            System.out.println ("DESTABLIZED! recalculating...");
            stablized = false;
            timesUnchanged = 0;
         }

         if (timesUnchanged >= 5 && stablized == false) {
            // display our DVT
            stablized = true;
            this.dvt.displayDVT();
            System.out.println ("STABLIZED!");
         }


         incomingData.clear();

         byte sendData[] = {};
         try {
            // Send our dv to neighbours
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this.dvt);
            sendData = bos.toByteArray();

            oos.close();
            bos.close();

         } catch (IOException e) {
            System.out.println ("Something went wrong serializing the DistanceVectorTable");
         }

         assert (sendData.length != 0) : "Something went wrong serializing the DistanceVectorTable";




         for (char neighbour : this.dvt.neighbourPorts.keySet()) {
            try {
               InetAddress IPAddress = InetAddress.getByName ("localhost");
               // System.out.println ("trying to send a DVT to port " + dvt.neighbourPorts.get(neighbour));
               DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length, IPAddress, dvt.neighbourPorts.get(neighbour));
               try {
                  sendSocket = new DatagramSocket();
               } catch (SocketException e) {
                  System.out.println ("Had trouble making an outgoing UDP socket");
               }
               sendSocket.send(sendPacket);
               sendSocket.close();
               // System.out.println ("finished sending vector table to " + dvt.neighbourPorts.get (neighbour) + "\n\n");
            } catch (UnknownHostException e) {
               System.out.println ("GG Couldn't find localhost...");
            } catch (IOException e) {
               System.out.println ("Something went wrong talking to neighbours");
            }

            try {
               Thread.sleep(5000);
            } catch (InterruptedException e) {
               System.out.println ("Got interrupted while sleeping!");
            }
         }

      }
   }
}
