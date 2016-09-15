package com.yesteapea.utils;

/**
 * A very simple BitSet which wraps around a long. <br>
 * The use case I had in mind for this is as follows.
 * <pre>
 * int[] arr = {1, 2, 3, 4};
 * BitSet64 bs = new BitSet64(0);
 * System.out.printf("Priting all subsets of %s\n", Arrays.toString(arr));
 * while (bs.getValue() != 1 << arr.length) {
 *   System.out.print("{");
 *   for (int i = 0; i < arr.length; i++) {
 *     if (bs.get(i)) {
 *       System.out.print(arr[i]);
 *       System.out.print(" ");
 *     }
 *   }
 *   System.out.println("}");
 *   bs.increment();
 * }
 * </pre>
 */
public class BitSet64 {
  private long value;

  public BitSet64(long value) {
    this.value = value;
  }

  public void increment() {
    value++;
  }

  public void decrement() {
    value--;
  }

  public long getValue() {
    return value;
  }

  public boolean get(int ind) {
    return (1 << ind & value) != 0;
  }


}
