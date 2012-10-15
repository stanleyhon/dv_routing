import java.io.Serializable;
import java.util.HashMap;

/**
 * Written by Stanley Hon
 * Email: stanleyhon348@gmail.com
 * CSE Username: shon348
 * UNSW StudentID: z3373433
 */
public class DistanceTable implements Serializable {
   private HashMap<Character, Float> vectors;
   public Character creatorID;

   public DistanceTable (Character creatorID) {
      vectors = new HashMap<Character, Float>();
   }

   public void addVector (Character nodeID, Float distance) {
      this.vectors.put(nodeID, distance);
   }

   public HashMap<Character, Float> getTable () {
      return this.vectors;
   }
}
