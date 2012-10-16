import java.io.*;
import java.util.ArrayList;

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public abstract class ConfigParser {

   public static void parse (int dv[][], String configFilePath) {
      BufferedReader inputStream;
      try {
         inputStream = new BufferedReader (new FileReader (configFilePath));
      } catch (FileNotFoundException e) {
         System.out.println ("Could not read Configuration file");
      }

      try {
         String s = inputStream.readLine (); // This line should be the number of neighbours
         // We don't need that information.
         s = inputStream.readLine (); // So read the next one.
         while (s != null) {
            String[] splitLine = s.split (" ");
            if (splitLine.length == 3) { // NODE_ID NODE_DISTANCE NODE_PORT
               neighbours.add(new Neighbour (splitLine[0].charAt(0), Float.parseFloat(splitLine[1]), Integer.parseInt(splitLine[2])));
            } else {
               System.out.println ("Invalid line in the config file");
            }
            s = inputStream.readLine();
         }
      } catch (IOException e) {
         System.out.println ("Something went wrong reading the config file");
      }

   }
}

