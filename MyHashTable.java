import java.util.ArrayList;

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
   
   int capacity;
   ArrayList<Node> buckets = new ArrayList();
   
   public MyHashTable (int capacity) {
      this.capacity = capacity;
      buckets.ensureCapacity(capacity);
      
      for(int i =0; i < capacity; i++) {
         buckets.add(null);
      }
   
   }
   
   public void put(K searchKey, V newValue) {
      Node element = new Node(searchKey, newValue);
      int index = hash(searchKey);
      
      if(buckets.get(index) == null) {
         buckets.set(index, element);
      } else {
         int i = index + 1;
         //linear probing
         int probe = 1;
         while(i != index) {
            if(buckets.get(i) == null) {
               element.setOffset(probe);
               buckets.set(i, element);
               break;
            }
            probe++;
            if(i == capacity) {
               i = 0;
            }
            i++;
         }
         
      
      }
      
   }
   public V get(K key) {
      int hash = hash(key) % capacity;
      int originalhash = hash;
      
      while((buckets.get(hash) != null && buckets.get(hash).getKey() != key) && originalhash != hash) {
         hash = (hash + 1) % capacity;
      }
      if(buckets.get(hash) == null) {
         return null;
      } else {
         return buckets.get(hash).getValue();
      }
   
   }
   public boolean containsKey(K searchKey) {
     // boolean result = false;
      int hash = hash(searchKey) % capacity;
      int originalhash = hash;
   //   System.out.println(originalhash);
      
      while((buckets.get(hash) != null && buckets.get(hash).getKey() != searchKey) || buckets.get(hash) == null) {
         hash = (hash + 1) % capacity;
         if(hash == originalhash) {
            return false;
         }
      }
    //  System.out.println(hash);
      //System.out.println(buckets.get(hash));
      return true;
   
   }
   
   private int hash(K key) {
      return Math.abs(key.hashCode()) % capacity;
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
      char key3 = 'f';
      
      test.put(key0, "Hello");
      test.put(key1, "Matt");
      test.put(key2, "hey");
      test.put(key3, "tim");
      
      System.out.println(test);
      
      System.out.println(test.get('a'));
      System.out.println(test.containsKey('f'));
   
   }
}