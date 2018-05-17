/*
*Timothy Olchondra
*TCSS 342
*
*This class is the main class. It will drive the file reader and writer, and create an object, CodingTree, which will 
*compress the read in message from the textfile. It will also print out the original size of the file, the compressed size, and 
the compression ratio
*/

import java.io.*;
import java.text.DecimalFormat;
import java.math.RoundingMode;

public class main {
   public static void main(String[] args) throws IOException {
      //create read file, and two output files
     // testCodingTree();
      BufferedReader inputStream = null;
      BufferedWriter outputStream1 = null;
      BufferedWriter outputStream2 = null;
      int c;
      StringBuilder read = new StringBuilder();
      
      DecimalFormat df = new DecimalFormat("#.####");
      df.setRoundingMode(RoundingMode.CEILING);
      
      try {
         inputStream = new BufferedReader(new FileReader("WarAndPeace.txt"));
         outputStream1 = new BufferedWriter(new FileWriter("mycompressed.txt"));
         outputStream2 = new BufferedWriter(new FileWriter("codes.txt"));
         
         
             int count =0;
         //read in the text file char by char
         while((c = inputStream.read()) != -1) {
            read.append((char)c);
            count++;
         }
         String message = read.toString();
         
         System.out.println(count);
         long start = System.currentTimeMillis();
         
         CodingTree codingTree = new CodingTree(message);
         
         long end = System.currentTimeMillis(); //get run time for encoding
         
         outputStream1.write(codingTree.getBits());
         outputStream2.write(codingTree.getCodes());
         
         File file1 = new File("WarAndPeace.txt");
         File file2 = new File("compressedfile.txt");
         
         int fileSizeOriginal = (int)file1.length() / 1024;
         int fileSizeCompression = (int)file2.length() / 1024;

         double compressionRatio = (double)fileSizeOriginal / fileSizeCompression;
         System.out.println("Orginal file size: " + fileSizeOriginal + "kb");
         System.out.println("Compressed file size: " +fileSizeCompression + "kb");
         System.out.println("Compression ratio: " + df.format(compressionRatio));
         System.out.println("Execution Time: " + (end - start) + "ms");
      } finally {
         inputStream.close();
         outputStream1.close();
         outputStream2.close();
 
      
      }
   }
   //test CodingTree by taking in a different file. The text file will be "Life Of Chopin".
   //Since all the methods are private in CodingTree, the way to test my class is to try to run
   //it on a different text file to see if the compression worked
   public static void testCodingTree() throws IOException{
         BufferedReader inputStream = null;
         BufferedWriter outputStream1 = null;
         BufferedWriter outputStream2 = null;
         int c;
         StringBuilder read = new StringBuilder();
        try {
         inputStream = new BufferedReader(new FileReader("LifeOfChopin.txt"));
         outputStream1 = new BufferedWriter(new FileWriter("compressedtest.txt"));
         outputStream2 = new BufferedWriter(new FileWriter("codestest.txt"));
               
         while((c = inputStream.read()) != -1) {
            read.append((char)c);
            
         }
         String message = read.toString();
         CodingTree codingTree = new CodingTree(message);
         
         outputStream1.write(codingTree.getBits());
         outputStream2.write(codingTree.getCodes());
         
         } finally {
            inputStream.close();
            outputStream1.close();
            outputStream2.close();
         
         }
   
   }
}