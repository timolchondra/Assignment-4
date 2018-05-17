/*
*Timothy Olchondra
*TCSS 342
*
*This program takes in a text file and compresses the file into an output text file. The text file is encoded
*with a String of bytes which is created using Huffman Coding. It uses a map to hold the key, and the value which are
*the characters mapped with a binary string.
*/

import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CodingTree {
  private PriorityQueue<Node> huffTree = new PriorityQueue<Node>();
  private String bits;
  private MyHashTable<String, String> codes = new MyHashTable<String,String>(32768);
  private ArrayList<String> wordArray = new ArrayList<String>();
  private char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',

				  'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',

				  '0','1','2','3','4','5','6','7','8','9','-','\''};
              
  private ArrayList<Character> acceptedChars = new ArrayList<Character>();
  private ArrayList<Character> rejectedChars = new ArrayList<Character>();
  private String[] separateWords;
  private boolean flag;
  Node root;
  StringBuilder makeKey = new StringBuilder();
  
  //this private class implements a Node that holds a char data, the frequency of the data, the branch that the node is in, and its left and right children
  private static class Node implements Comparable<Node>{
    private String data;     //char data to hold value
    private int frequency;    //frequency of char 
    Node left;   
    Node right;
    char branch;     //0 equals left and 1 equals right
    
    public Node(String word) {
      data = word;
      frequency = 1; 
      this.left = null;
      this.right = null;  
    }
    //constructor that sets data and frequency
    public Node(String word, int frequency) {
      data = word;
      this.frequency = frequency; 
    }
    //constructor that sets data, frequency, left children, and right children
    public Node(String word, int frequency, Node left, Node right) {
      data = word;
      this.frequency = frequency;
      this.left = left;
      this.right = right;
    }
    //returns frequency
    public int getFrequency() {
      return frequency;
    }
    //returns data
    public String getData() {
      return data;
    }
    //returns the branch that the node is in
    public char getBranch(){
      return branch;
    }
    //returns true if the node is leaf by checking its left and right children
    public boolean isLeaf(){
      return ((this.left == null) && (this.right == null));
    }
    
    //sets the branch the node is in
    public void setBranch(String branch) {
         if(branch.contains("left")) {
            this.branch ='0';
         } else {
            this.branch = '1';
         }
    }
    //sets the frequency of the node
    public void setFrequency(int frequency) {
      this.frequency = frequency;
    }
    //a compareTo function so that the PriorityQueue sorts the nodes by frequency of chars.
    @Override
    public int compareTo(Node other) {
      return (this.frequency < other.frequency) ? -1 : ((this.frequency == other.frequency) ? 0 : 1);

    }
  }
  
  //constructor that takes in String message, and calls private functions to compress the message.
  public CodingTree(String message) {
  
      addAcceptedChars();
      
      findRejectedChars(message);
//       
//       for(int i = 0; i < 2000; i++ ) {
//          System.out.println("hello");
//       
//       }
      StringBuilder splitter = new StringBuilder();
      for(int i = 0; i < rejectedChars.size();i++) {
         splitter.append(rejectedChars.get(i));
        // separateWords = message.split(splitter.toString());
   //      splitter.setLength(0);
         
       }
//             for(int i = 0; i < 2000; i++ ) {
//          System.out.println("heyyyy");
//       
//       }
        separateWords = message.split(splitter.toString());

 //     System.out.println(splitter.toString());
      
//       
   
      //    System.out.println(separateWords[0]);
    //      System.out.println(separateWords[1]);
       
      
      getEveryWord(message);
//       
//             for(int i = 0; i < 2000; i++ ) {
//          System.out.println("hooooo");
//       
//       }
      
//       for(int i = 0; i < wordArray.size(); i++) {
//          System.out.println(wordArray.get(i));
//       
//       }

      System.out.println(wordArray.size());
      
      makeWordNodes(message);
      
 //                  for(int i = 0; i < 2000; i++ ) {
//          System.out.println("booooo");
//       
//       }
//       
      makeHuffTree();
      
//                   for(int i = 0; i < 2000; i++ ) {
//          System.out.println("tooooo");
//       
//       }
      
      
   //   System.out.println(root.getFrequency());
      
      makeKey();
      
      System.out.println(codes);
     
      encodeMessage(message);

 }
 //stores every unique char from the message into an ArrayList
  private void getEveryWord(String message) {
    //  StringBuilder word = new StringBuilder();
//       for(int i = 0; i < message.length; i++) {
//          if(!(wordArray.contains(message[i]))) {
//              wordArray.add(message[i]);    
//          }
//       }



      Pattern p = Pattern.compile("[a-zA-Z0-9'-]+");
      Matcher m = p.matcher(message);
      
      while(m.find()) {
         String word = m.group();
         if(!wordArray.contains(word)) {
            wordArray.add(word);
         }    
      }
   //   System.out.println(wordArray);
  }
 //counts the frequency of the char in the message and returns the frequency
  private int wordFrequency(String message, String word){
    int frequency = 0;
//     StringBuilder findWord = new StringBuilder();
//     
//     for(int i =0; i < message.length(); i++) {
//          if(!(acceptedChars.contains(message.charAt(i)))) {
//             if(word.equals(findWord.toString())){
//                frequency++;
//             }
//             findWord.setLength(0);
//             continue;     
//          }
//          findWord.append(message.charAt(i));
//     }

    Pattern p = Pattern.compile(word);
    Matcher m = p.matcher(message);
    while( m.find()) {
      frequency++;
    }
   // System.out.println(word + "= " + frequency);
    return frequency;
  }
  //method to make nodes and add it to the priority queue
  private void makeWordNodes(String message) {
    int frequency = 0;
    
    for(int i = 0; i < wordArray.size(); i++) {
       frequency = wordFrequency(message, wordArray.get(i));
       huffTree.offer(new Node(wordArray.get(i),frequency));
    }
      
  }
  //method to create the Huffman Tree using the priorityqueue to help merge the right trees together
  private void makeHuffTree() {
    Node tempNode1;
    Node tempNode2;
    while( huffTree.size() != 1){
     tempNode1 =  huffTree.poll();
     tempNode2 =  huffTree.poll();
     
     tempNode1.setBranch("left");
     tempNode2.setBranch("right");

     huffTree.offer(new Node("\0", (tempNode1.getFrequency() + tempNode2.getFrequency()), tempNode1, tempNode2));
    
    }
    root = huffTree.poll();
  
  }
  //creates the key for the map. Maps char to binary string
  private void makeKey() {
    String key;
    for(int i = 0; i < wordArray.size(); i++) {
     this.flag = false;
     makeKey.setLength(0);
     searchTree(wordArray.get(i), this.root);
     makeKey.reverse();
     
     key = makeKey.toString();

     codes.put(wordArray.get(i), key);
    }
  
  }
  //main method for traversing through the tree and writing the path it takes to reach the desired node
  private void searchTree(String c, Node rt) {

    if(rt.getData().equals(c)) {
        this.flag = true; 
        makeKey.append(rt.getBranch());
	     return;
     }
	  if(rt.isLeaf()&& !rt.getData().equals(c)) {
         return;
     } else {
	      if(!rt.getData().equals(c)) {
		     searchTree(c, rt.left);
		     if(this.flag && rt != this.root){
             makeKey.append(rt.getBranch());
             return;
           }
         }
         if(!rt.getData().equals(c) && !this.flag) {
           searchTree(c, rt.right);
           if(this.flag && rt != this.root) {
              makeKey.append(rt.getBranch());
              return;
           } else { 
              return;
           }
         }

      }
  }
  //compresses the message using the key that was made
  private void encodeMessage(String message) {
      StringBuilder stringbit = new StringBuilder();
      StringBuilder encode = new StringBuilder();
      Pattern p = Pattern.compile("[a-zA-Z0-9'-]+");
      Matcher m = p.matcher(message);
      
      while(m.find()) {
         String word = m.group();
         stringbit.append(codes.get(word));
            
      }

  // stringbit.delete(0,12);
 //  System.out.println(stringbit.toString());
//     BufferedOutp outputStream1 = new BufferedWriter(new FileWriter("test.txt"));
//     outputStream1.write(stringbit.toString());
//     outputStream1.close();
      int value;   
    for(int i = 0; i < stringbit.length(); i+=8) {
      if(i > stringbit.length() - 8) {
				int tempBinInt = Integer.parseInt(stringbit.substring(i, stringbit.length()),2);
				byte byteString = (byte)tempBinInt;
				encode.append((char)byteString);
				
		} else {
         value = Integer.parseInt(stringbit.substring(i,i+7),2);
         byte b = (byte)value;
         encode.append((char)value);
      }
    }
   bits = encode.toString();
  // bits = stringbit.toString();
    System.out.println(bits.length());
    
  }
  public String getBits() {
    return bits;
  }
  public String getCodes(){
    return codes.toString();
  }
  private void addAcceptedChars() {
    for(int i = 0; i < chars.length; i++) {
       acceptedChars.add(chars[i]);
   
    }
  }
  private void findRejectedChars(String message) {
   //  rejectedChars.add('[');
     for(int i = 0; i < message.length(); i++) {   
         if((!(acceptedChars.contains(message.charAt(i)))) && !rejectedChars.contains(message.charAt(i))) {
         //   System.out.println(message.charAt(i));
            rejectedChars.add('\\');
            rejectedChars.add(message.charAt(i));
            rejectedChars.add('|');
         }     
     }
  //   rejectedChars.add(']');
  }
   
}