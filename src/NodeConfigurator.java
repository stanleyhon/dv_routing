/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public abstract class NodeConfigurator {

   // This function is passed a Node, and a config file then configures that Node
   // as defined by the configuration file.
   public static void configure (Node n, String configFilePath) {
      // Probably something like
      for (Neighbour neighbour : ConfigParser.parse(configFilePath)) {
         /*
         System.out.println ("We have a neighbour called " + neighbour.getNODE_NAME() + " with cost "
             + neighbour.getNODE_DISTANCE() + " on port " + neighbour.getNODE_PORT());
         */
         n.addNeighbour(neighbour);
      }
   }

}
