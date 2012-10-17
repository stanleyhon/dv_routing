/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */

public class dv_routing_v1 {

   public static char NODE_ID;
   public static int NODE_PORT;
   public static String CONFIG_FILE_ADDRESS;

   public static void main (String args[]) {
      if (args.length != 3) {
         System.out.println ("Number of arguments is wrong");
      }
      // args[0] is NODE_ID
      if (args[0].length () == 1 && Character.isLetter (args[0].charAt (0))) {
         NODE_ID = args[0].charAt (0);
      } else {
         System.out.println ("NODE_ID is wrong.");
         return;
      }

      // args[1] is NODE_PORT
      try {
         NODE_PORT = Integer.parseInt (args[1]);
      } catch (NumberFormatException e) {
         System.out.println ("NODE_PORT is not numerical!");
         return;
      }

      // args[2] is CONFIG.TXT
      CONFIG_FILE_ADDRESS = args[2];

      // NodeConfigurator.configure (new Node(), "/Users/stanleyhon/Documents/git/COMP3331-Assignment2/testcases/02routers/A.txt");

      // System.out.println ("My NODE_ID is " + NODE_ID);

      Node n = new Node (NODE_ID, NODE_PORT);
      ConfigParser.parse(n, CONFIG_FILE_ADDRESS, NODE_ID);
      n.start();

      return;
   }
}
