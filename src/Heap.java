// Student number: C1340032

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Heap {
  private int count; // Will need it to count the number of items we have
  private int[] items; // This will be the array used by our heap
  private Scanner in, in2; // These will count and work with our input
  private boolean process = false;


  public Heap(String input) {
    // Try opening files
    try {
      in = new Scanner(new File(input));
      in2 = new Scanner(new File(input));
    }

    // If it failes, catch exception, display some output and exit.
    catch (FileNotFoundException ex) {
      println("\nAbbreviations file not found! Try again! \nDebug: " + ex);
      System.exit(0);
    }

    // First scanner counts the data so it can initialize an array with the right size.
    // This is why an ArrayList would have been a much better implementation.
    print("input data: ");
    while (in.hasNextInt()) {
      print(in.nextInt() + " ");
      count++;
    }
    println();
    in.close();
    // Array is initialized with size = number of elements + 1 because it will need room for the dummy node (root)
    items = new int[count + 1];

    // Initialize array with -1
    for (int i = 0; i < items.length; i++) {
      items[i] = -1;
    }
  }

  // Bottom-up construction of the array.
  public void bottomUp() {
    // At which element the level starts.
    int levelStart = count / 2;
    int index = levelStart;


    for (int i = 0; i < levelStart + 1; i++) {
      int next = in2.nextInt();
      items[index] = next;
      index++;
    }

    count = count - (levelStart + 1);


    // PRINT RESULT
    if (process) {
      println("now process next level");
    }

    process = true;
    printArray();
    // Call bubble down method
    siftDown(levelStart);

    // If more elements remain, calls itself.
    if (count / 2 > 0) {
      bottomUp();
    }
  }

  public void siftDown(int initialIndex) {
    for (int i = initialIndex; i < items.length; i++) {
      // If element is a left sibling and not -1 (dummy node) check for P1 and P2
      if (isLeftSibling(i) && items[i + 1] != -1) {
        P1(i);
        P2(i);
      }

      // If element is a right sibling and not -1 (dummy node) check for P1 and P3
      else if (isRightSibling(i) && items[i] != -1) {
        P1(i);
        P3(i);
      }
    }
  }

  public void P1(int i) {
    // If element is a left sibling, bigger than its sibling, then swap them.
    if (isLeftSibling(i) && items[i] > items[i + 1]) {
      swap(i, i + 1);
      println("swap sibling");
      printArray();
    }
    // If element is a right sibling, smaller than its sibling, then swap them.
    else if (isRightSibling(i) && items[i] < items[i - 1]) {
      swap(i, i - 1);
      println("swap sibling");
      printArray();
    }
  }

  public void P2(int i) {
    // If elements don't exist, don't even check for this property
    if (rightChild(i) <= items.length && rightNephew(i) <= items.length) {
      // If element is a left sibling and bigger than one of its nephews or children, then swap with the bigger one.
      if (isLeftSibling(i) && (items[i] > items[leftChild(i)] || items[i] > items[leftNephew(i)])) {
        if (items[leftChild(i)] < items[leftNephew(i)] && items[leftChild(i)] != -1) {
          swap(i, leftChild(i));
          println("swap with children/nephews");
          printArray();
          // After a swap, check for P1 and P2 at the new location.
          P1(leftChild(i));
          P2(leftChild(i));
        } else if (items[leftChild(i)] > items[leftNephew(i)] && items[leftNephew(i)] != -1) {
          swap(i, leftNephew(i));
          println("swap with children/nephews");
          printArray();
          // After a swap, check for P1 and P2 at the new location.
          P1(leftNephew(i));
          P2(leftNephew(i));
        }
      }
    }
  }

  public void P3(int i) {
    // If elements don't exist, don't even check for this property
    if (rightChild(i) <= items.length && rightNephew(i) <= items.length) {
      // If element is a right sibling and smaller than one of its nephews or children, then swap with the bigger one.
      if (isRightSibling(i) && (items[i] < items[rightChild(i)] || items[i] < items[rightNephew(i)])) {
        if (items[rightChild(i)] > items[rightNephew(i)] && items[rightChild(i)] != -1) {
          swap(i, rightChild(i));
          println("swap with children/nephews");
          printArray();
          // After a swap, check for P1 and P3 at the new location.
          P1(rightChild(i));
          P3(rightChild(i));
        } else if (items[rightChild(i)] < items[rightNephew(i)] && items[rightNephew(i)] != -1) {
          swap(i, rightNephew(i));
          println("swap with children/nephews");
          printArray();
          // After a swap, check for P1 and P3 at the new location.
          P1(rightNephew(i));
          P3(rightNephew(i));
        }
      }
    }
  }

  // Deletes the minimum value in the array which sits at index 1.
  public void deleteMin() {
    // Goes through all items starting from the back.
    for (int i = items.length - 1; i > 0; i--) {
      // If it finds a non-empty node, moves the value and replaces the minimum value (index 1) with it.
      // Puts a -1 (an empty node) in its place and finally breaks out of both loops and starts bubbling down.
      if (items[i] >= 0) {
        int temp = items[i];
        items[i] = -1;
        items[1] = temp;
        break;
      }
    }
    println("delete minimum value");
    printArray();
    siftDown(1);
  }

  // Deletes the minimum value in the array which sits at index 1.
  public void deleteMax() {
    // Goes through all items starting from the back.
    for (int i = items.length - 1; i > 0; i--) {
      // If it finds a non-empty node, moves the value and replaces the maximum value (index 2) with it.
      // Puts a -1 (an empty node) in its place and finally breaks out of both loops and starts bubbling down.
      if (items[i] >= 0) {
        int temp = items[i];
        items[i] = -1;
        items[2] = temp;
        break;
      }
    }
    println("delete maximum value");
    printArray();
    siftDown(1);
  }

  // Determines if the node at the provided index is a left sibling.
  public boolean isLeftSibling(int k) {
    // Return true if index is odd (left siblings sit at odd indexes) and not 0 (the root).
    return k % 2 != 0 && k != 0;
  }

  // Determines if the node at the provided index is a right sibling.
  public boolean isRightSibling(int k) {
    // Return true if index is even (right siblings sit at even indexes) and not 0 (the root).
    return k % 2 == 0 && k != 0;
  }

  // Determines the left child of the node at the provided index.
  public int leftChild(int k) {
    return 2 * k + 1;
  }

  // Determines the right child of the node at the provided index.
  public int rightChild(int k) {
    return 2 * k + 2;
  }

  // Determines the left nephew of the node at the provided index.
  public int leftNephew(int k) {
    // If a node is a left sibling, its nephews will be on its right, else on its left. Thus the formulas differ.
    if (k % 2 == 0) {
      return 2 * k - 1;
    } else {
      return 2 * k + 3;
    }
  }

  // Determines the right nephew of the node at the provided index.
  public int rightNephew(int k) {
    // If a node is a left sibling, its nephews will be on its right, else on its left. Thus the formulas differ.
    if (k % 2 == 0) {
      return 2 * k;
    } else {
      return 2 * k + 4;
    }
  }

  // Swaps the positions of the two items whose indexes have been provided as arguments.
  private void swap(int x, int y) {
    int temp;
    temp = items[x];
    items[x] = items[y];
    items[y] = temp;
  }

  // Method for printing the entire array in an appropriate format
  public void printArray() {
    System.out.print("heap array: ");
    for (int i = 0; i < items.length; i++) {
      System.out.print(items[i] + " ");
    }
    System.out.println();
  }

  // The below 3 methods are useful when you have lots of print statements. Makes printing easier for the programmer (me) with less writing.
  public void println(String message) {
    System.out.println(message);
  }

  // Java can differentiate between methods with identical names, as long as they differ in the arguments they take.
  public void println() {
    System.out.println();
  }

  public void print(String message) {
    System.out.print(message);
  }
}
