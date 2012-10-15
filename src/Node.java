import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */

public class Node {
   private DatagramSocket listenSocket;

   private DatagramSocket sendSocket;

   private ArrayList<Neighbour> neighbours;

   public char NODE_ID;
   public int NODE_PORT;

   private ArrayList<String> incomingData;

   private DistanceTable distanceTable;

   public Node (char NODE_ID, int NODE_PORT) {
      this.NODE_ID = NODE_ID;
      this.NODE_PORT = NODE_PORT;

      this.distanceTable = new DistanceTable(this.NODE_ID);

      incomingData = new ArrayList<String>();
      neighbours = new ArrayList<Neighbour>();
   }

   public void addNeighbour (Neighbour n) {
      this.neighbours.add(n);
      distanceTable.addVector(n.getNODE_NAME(), n.getNODE_DISTANCE());  // SO BEAST
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

      System.out.println ("setting up thread to listen");
      // Setup a thread to listening
      NodeListener ns = new NodeListener(incomingData, listenSocket);
      ns.start();

      while (1 == 1) {
         // Send our DistanceTable to all our neighbours
         byte[] sendData = {};
         try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
            ObjectOutputStream serializer = new ObjectOutputStream(outputStream);
            serializer.writeObject(this.distanceTable);
            sendData = outputStream.toByteArray();
            System.err.println ("JUST BTW, the serialized thing is " + sendData.length + " long");
         } catch (IOException e) {
            System.out.println ("Something went wrong serialising our DistanceTable");
         }

         System.out.println ("Sending out distanceTables");

         for (Neighbour n : neighbours) {
            try {
               InetAddress IPAddress = InetAddress.getByName ("localhost");
               System.out.println ("trying to send a message to port " + n.getNODE_PORT() + " we're on port " + this.NODE_PORT);
               DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length, IPAddress, n.getNODE_PORT());
               try {
                  sendSocket = new DatagramSocket();
               } catch (SocketException e) {
                  System.out.println ("Had trouble making an outgoing UDP socket");
               }
               sendSocket.send(sendPacket);
               sendSocket.close();
               System.out.println ("finished sending distanceTable to " + n.getNODE_NAME() + "\n\n");
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
