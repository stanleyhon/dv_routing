import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public abstract class ConfigParser {

   public static void parse (Node n, String configFilePath, char owner) {


      BufferedReader inputStream;
      try {
         inputStream = new BufferedReader (new FileReader (configFilePath));
         String s = inputStream.readLine (); // This line should be the number of neighbours

         int graphSize = Integer.parseInt(s);

         n.dvt = new DistanceVectorTable(owner);
         DistanceVectorTable dv = n.dvt;

         s = inputStream.readLine (); // Keep reading



         HashMap<Character, Integer> neighbourPorts = new HashMap<Character, Integer>();
         ArrayList<Character> neighbours = new ArrayList<Character>();
         while (s != null) {
            String[] splitLine = s.split (" ");
            if (splitLine.length == 3) { // NODE_ID NODE_DISTANCE NODE_PORT
               char NODE_ID = splitLine[0].charAt (0);
               Float NODE_DISTANCE = Float.parseFloat (splitLine[1]);
               int NODE_PORT = Integer.parseInt (splitLine[2]);
               // Remember the port
               neighbourPorts.put(NODE_ID, NODE_PORT);
               neighbours.add(NODE_ID);
               // Add the dv entry
               dv.set(NODE_ID, NODE_ID, NODE_DISTANCE);
            } else {
               System.out.println ("Invalid line in the config file");
            }
            s = inputStream.readLine();
         }


         inputStream.close();

         // Pass dv the port information
         n.dvt.neighbourPorts = neighbourPorts;
         n.dvt.neighbours = neighbours;

      } catch (FileNotFoundException e) {
         System.out.println ("Could not read Configuration file");
      } catch (IOException e) {
         System.out.println ("Something went wrong reading the config file");
      }

   }
}

