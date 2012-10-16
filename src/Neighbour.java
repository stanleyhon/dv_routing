/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */

public class Neighbour {
   private char NODE_NAME;
   private int NODE_PORT;

   public Neighbour (char NODE_NAME, float NODE_DISTANCE, int NODE_PORT) {
      this.NODE_NAME = NODE_NAME;
      this.NODE_DISTANCE = NODE_DISTANCE;
      this.NODE_PORT = NODE_PORT;
   }

   public char getNODE_NAME () {
      return NODE_NAME;
   }

   public float getNODE_DISTANCE () {
      return NODE_DISTANCE;
   }

   public int getNODE_PORT () {
      return NODE_PORT;
   }
}
