import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class MyHashTable<K, V> {
   
   private class Node {
      K key;
      V value;
      int offset;
      
      
      private Node(K key, V value) {
         this.key = key;
         this.value = value;
         offset = 0;
      }
      private void setOffset(int offset) {
         this.offset = offset;
      }
      public int getOffset() {
    	  return this.offset;
      }
      
      @Override
      public String toString() {
         return (key + "=" + value);
      }
      public K getKey() {
         return key;
      }
      public V getValue() {
         return value;
      }
      
   }
   
  // private MyHashTable<Node, Integer> = new MyHashTable<Node, Integer>(32768);
   int capacity;
   private ArrayList<Node> buckets = new ArrayList<Node>();
   private Set<K> keySet = new HashSet<K>();
   
   public MyHashTable (int capacity) {
      this.capacity = capacity;
      buckets.ensureCapacity(capacity);
      
      for(int i = 0; i < capacity; i++) {
         buckets.add(null);
      }
   }
   
   public void put(K searchKey, V newValue) {
      Node element = new Node(searchKey, newValue);
      keySet.add(searchKey);
      int index = hash(searchKey);
      int probe = 0;
      
     //  if(buckets.get(index) == null || buckets.get(index).getKey() == searchKey) {
//          buckets.set(index, element);
//       } else {
//          int i = (index + 1) % capacity;
//          //linear probing
//          int probe = 1;
//          while(i != index) {
//             if(buckets.get(i) == null) {
//                element.setOffset(probe);
//                buckets.set(i, element);
//                break;
//             }
//             probe++;
//          //   System.out.println(probe);
//             i = (i+1) % capacity;
//          }
//       } 

      while(buckets.get(index)!= null && !buckets.get(index).getKey().equals(searchKey)) {
         index = (index + 1) % capacity;
         if(probe == capacity) {
            break;
         }
         
         probe++;
           
      }
      element.setOffset(probe);
      buckets.set(index, element);
      
   }
   public Node indexGet(int bucketIndex) {
	   Node bucketNode = buckets.get(bucketIndex);
	   return bucketNode;
   }
   public V get(K key) {
      int hash = hash(key);
      int originalhash = hash;
      boolean notFound = false;
      
      if(!containsKey(key)) {
         return null;
      }
      
      while((buckets.get(hash) != null && !buckets.get(hash).getKey().equals(key))) {
         hash = (hash + 1) % capacity;

      }
      if(buckets.get(hash) == null) {
         return null;
      }  else {
         return buckets.get(hash).getValue();
      }
   }
   public boolean containsKey(K searchKey) {
     return keySet.contains(searchKey);
   
   }
   
   private int hash(K key) {
      int hash;
      hash = key.hashCode();
      
      if(hash < 0) {
         hash = (hash % capacity) + capacity;
         return hash;
      } else {
         return hash % capacity;
      }
   }
   
   @Override
   public String toString() {
      StringBuilder string = new StringBuilder();
      
      for(int i = 0; i < buckets.size(); i++) {
         if(buckets.get(i) != null) {
           string.append(buckets.get(i));
           string.append(", ");
         }
      }
      
      return string.toString();
   }
   
   public static void main(String[] args) {
      int capacity = 10;
      MyHashTable<Character, String> test = new MyHashTable<Character, String>(capacity);
      
      char key0 = 'a';
      char key1 = 'b';
      char key2 = 'c';
      char key3 = 'a';
      
      test.put(key0, "Hello");
      test.put(key1, "Matt");
      test.put(key2, "hey");
      test.put(key3, "tim");
      test.put(key0, "Consuela");
      test.put(key0,  "Oreo");
      test.put(key0, "Roger");
      //test.
      
      //System.out.println(test.get(key0).getOffset());
      System.out.println(test);
      
      System.out.println(test.get('p'));
      System.out.println(test.containsKey('k'));
      test.stats();
      System.out.println(test.toString());
     // System.out.println(test.indexGet(0).getValue());
   }
   public int getMaxOffset() {
	   int max = 0;
	   for (int i = 0; i < buckets.size(); i++) {
		  if (buckets.get(i) != null) {
			  if (buckets.get(i).getOffset() > max) {
				  max = buckets.get(i).getOffset();
			  }
		  }
	   }
	   return max;
   }
      ArrayList<Integer> getOffsetFrequencies() {
	   ArrayList<Integer> frequencyList = new ArrayList<Integer>();
	   for (int i = 0; i <= getMaxOffset(); i++) {
		   int sum = 0;
		   for (int j = 0; j < buckets.size(); j++) {
			   if (buckets.get(j) != null) {
				   if (buckets.get(j).getOffset() == i) {
					   sum++;
				   }
			   }
		   }
		   frequencyList.add(sum);
	   }
	   //Collections.reverse(frequencyList);
	   return frequencyList;
   }
  public void printHistogram() {
	   ArrayList<Integer> offsetFrequencies = getOffsetFrequencies();
	   
	   System.out.print("[" + offsetFrequencies.get(0));
	   for (int i = 1; i < offsetFrequencies.size(); i++) {
		   System.out.print(", " + offsetFrequencies.get(i));
	   }
	   System.out.println("]");
	   
   }

  public int getEntries() {
	   //iterate through each bucket
	   //if the bucket is not equal to null
	   //increment a sum.
	   //return the sum.
	   int sum = 0;
	   for (int i = 0; i < buckets.size(); i++) {
		   if (buckets.get(i) != null) {
			   sum++;
		   } 
	   }
	   return sum;
   }
  public void stats() {
	   System.out.println("\n\nHash Table Stats");
	   System.out.println("=================");
	   System.out.println("Number of Entries: " + this.getEntries());
	   System.out.println("Number of Buckets: " + capacity);
	   printHistogram();
	   System.out.println("Fill Percentage: " + (100.0*this.getEntries() 
	   					  / capacity) + "%");
	   System.out.println("Max Linear Probe: " + getMaxOffset());
	   getOffsetFrequencies();
	   
	   
   }
}