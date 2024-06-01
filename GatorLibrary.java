import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Class representing the GatorLibrary application
public class GatorLibrary {
    // Method to parse a line from the input file and perform corresponding operations on RedBlackTree
    private static void parseLine(RedBlackTree redBlackTree, String line) {
        line = line.replaceAll("\"", "");
        if (line.contains("Quit")) {
            redBlackTree.quit();
            System.exit(0);
        }
        int idx1 = line.indexOf("(");
        int idx2 = line.indexOf(")");
        String[] ar = line.substring(idx1 + 1, idx2).split(",");
        //System.out.println(Arrays.toString(ar));
        String operation = line.substring(0, idx1);
        if (operation.equals("InsertBook")) {
            redBlackTree.insertBook(Integer.parseInt(ar[0].trim()), ar[1].trim(), ar[2].trim(), ar[3].trim(), -1, new RedBlackTree.ReservationHeap());
        } else if (operation.equals("PrintBook")) {
            int bookId = Integer.parseInt(ar[0].trim());
            RedBlackTree.Book book = redBlackTree.printBook(bookId);
            if (book != null) {
                System.out.println(book);
            }
            else {
                System.out.println("Book " + bookId + " not found in the library");
            }
        } else if (operation.equals("PrintBooks")) {
            redBlackTree.printBooks(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()));
        } else if (operation.equals("BorrowBook")) {
            redBlackTree.borrowBook(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()), Integer.parseInt(ar[2].trim()));
        } else if (operation.equals("ReturnBook")) {
            redBlackTree.returnBook(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()));
        } else if (operation.equals("DeleteBook")) {
            redBlackTree.deleteBook(Integer.parseInt(ar[0].trim()));
        } else if (operation.equals("FindClosestBook")) {
            redBlackTree.findClosestBook(Integer.parseInt(ar[0].trim()));
        } else if (operation.equals("ColorFlipCount")) {
            redBlackTree.colorFlipCount();
        }
    }

    // Main method of the GatorLibrary application
    public static void main(String[] args) {
        try {
            // Reading input from the specified file
            File file = new File("/Users/gunaathota/Downloads/GatorLibrary/src/input_file2.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;
            RedBlackTree redBlackTree = new RedBlackTree();
            // Processing each line from the input file
            while ((line = br.readLine()) != null) {
                parseLine(redBlackTree, line);
            }
            fr.close();    //closes the stream, release the resources
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}