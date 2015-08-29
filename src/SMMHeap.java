// Student number: C1340032

public class SMMHeap {
  public static void main(String[] args) {
    if (args.length == 1) {
      Heap smm = new Heap(args[0]);
      smm.bottomUp();
      smm.deleteMin();
      smm.deleteMax();
    } else {
      System.out.println("Usage: java SMMHeap <dataFile>");
    }
  }
}
