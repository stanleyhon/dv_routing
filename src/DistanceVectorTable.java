import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public class DistanceVectorTable implements Serializable {

   public static int MAX_DISTANCE_VECTOR_TABLE_SIZE = 10;
   // The actual table
   private Float table[][] = new Float[MAX_DISTANCE_VECTOR_TABLE_SIZE][MAX_DISTANCE_VECTOR_TABLE_SIZE];

   // Owner ID Character
   public char owner;

   // The sequential list of IDs (e.g. 'A' 'B' 'C' etc)
   private char IDList[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

   // Stores the direct neighbours
   ArrayList<Character> neighbours;

   HashMap<Character, Integer> neighbourPorts;

   ArrayList<Character> failedNodes = new ArrayList<Character>();

   public DistanceVectorTable (char owner) {
      this.owner = owner;

      // Initialise the table to very high values.
      int i = 0;
      while (i < MAX_DISTANCE_VECTOR_TABLE_SIZE) {
         int j = 0;
         while (j < MAX_DISTANCE_VECTOR_TABLE_SIZE) {
            table[i][j] = 9999f;
            j++;
         }
         i++;
      }
   }

   public void set (char via, char to, Float distance) {
      int toIndex = getIndex(to);
      int viaIndex = getIndex(via);

      table[viaIndex][toIndex] = distance;
   }

   public Float lookup (char via, char to) {
      int toIndex = getIndex(to);
      int viaIndex = getIndex(via);
      return table[viaIndex][toIndex];
   }

   public Float lookup (int via, int to) {
      return table[via][to];
   }

   // See if this table has a way of getting to 'to'
   // returns '-' if there is no way.
   public char lookupLink (char to) {
      int row = getIndex(to);
      int i = 0;

      Float bestDistance = 9999f;
      char via = '-';

      while (i < IDList.length) {
         if (table[i][row] != 9999f && table[i][row] != -1f) { // We have a way of getting to 'to'
            if (table[i][row] < bestDistance) {
               bestDistance = table[i][row];
               via = getCharacter(i);
            }
         }
         i++;
      }

      return via;
   }

   public boolean assimilate (DistanceVectorTable dvt) {
      boolean changeMade = false;

      char foreignOwner = dvt.owner;
      char ourselves = this.owner;


      // Find the relevant column
      int foreignColumn = getIndex(foreignOwner);

      // Rule out our row
      int ourRow = getIndex(ourselves);

      // Ask what the best way to get to everything is
      int i = 0;
      while (i < IDList.length) {
         if (IDList[i] != ourselves) { // if we aren't looking for ourselves
            char result = dvt.lookupLink(IDList[i]); // Ask dvt what the best way is.
            if (result != '-' && result != -1f) { // if we found a good result, see if it's better than what we've got
               char ourResult = this.lookupLink(IDList[i]);
               if (ourResult == '-') { // If we don't have anything yet, their result is best - take it!
                  changeMade = true;
                  // make a new entry, via owner to IDList[i], distance:
                  this.set(dvt.owner, IDList[i],
                      // distance to owner + distance from owner to IDList[i];
                      this.lookup(this.lookupLink(dvt.owner), dvt.owner) + dvt.lookup(dvt.lookupLink(IDList[i]) , IDList[i]));

               // May possibly need a case where result == ourResult,
               // Then proceed to see which link to result is cheaper
               // This may be handled by general case... not sure

               } else { // We both have results, see if theirs any better.

                  // Their distance is: Our distance to them            + Their distance to IDList[i]
                  // our best distance to them                          + their best distance to IDList[i]
                  Float theirDistance = this.lookup(this.lookupLink(dvt.owner), dvt.owner) +
                      dvt.lookup(dvt.lookupLink(IDList[i]), IDList[i]);

                  // Our distance is:
                  // our best distance to IDList[i];
                  Float ourDistance = this.lookup(this.lookupLink(IDList[i]), IDList[i]);

                  if (theirDistance < ourDistance) { // Add it to our DV table.
                     changeMade = true;
                     this.set(dvt.owner, IDList[i], theirDistance);
                  }

               }
            } else if (result == -1f) { // INDICATES NODE HAS FAILED
               nodeFailed (IDList[i]);
            }
         }
         i++;
      }
      return changeMade;
   }

   // Returns the index number of the row/col relating to this character
   // e.g. 'A' should return 0
   private int getIndex (char c) {
      int i = 0;
      while (i < IDList.length) {
         if (IDList[i] == c) {
            return i;
         }
         i++;
      }

      assert (false) : "CRITICAL ERROR: Couldn't find specified character in the DV";
      return 0;
   }

   private char getCharacter (int index) {
      int i = 0;
      assert (index < IDList.length) : "CRITICAL ERROR: Couldn't find specified index in ID List";
      return IDList[index];
   }

   public void displayDVT () {
      int i = 0;
      while (i < IDList.length) {
         char best = lookupLink(IDList[i]);

         if (best != '-' && lookup(lookupLink(IDList[i]), IDList[i]) != 9999f && lookup(lookupLink(IDList[i]), IDList[i]) != -1f) {
            char targetNode = IDList[i]; // what we're trying to get to
            char nextHop = lookupLink(IDList[i]); // what the supposed next hop is
            /*
            while (!this.neighbours.contains(nextHop)) {
               nextHop = lookupLink(nextHop);
            }
            */
            Float costOfNextHop = lookup(nextHop, nextHop);
            if (this.failedNodes.contains(IDList[i])) {

            } else {
               System.out.println ("Shortest path to node " + targetNode + ": the next hop is " +
                   lookupLink(IDList[i]) + " and the cost is " + costOfNextHop);
            }

         }

         i++;
      }
      System.out.println();
   }

   public void nodeFailed (char c) {
      // remove it from our neighbours list

      if (this.neighbours.size() != 0 && this.neighbours.contains(c)) {
         this.neighbours.remove((Character) c);
      }

      this.failedNodes.add(c);
      int viaColumn = getIndex(c);
      int i = 0;
      while (i < IDList.length) {
         table[viaColumn][i] = -1f;
         i++;
      }
   }

}
