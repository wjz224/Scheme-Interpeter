package edu.lehigh.cse262.p1;

import java.util.ArrayList;
import java.util.List;

/** PrimeDivisors is a wrapper class around the function `computeDivisors` */
public class PrimeDivisors {
  /**
   * Compute the prime divisors of `value` and return them as a list
   *
   * @param value The value whose prime divisors are to be computed
   * @return A list of the prime divisors of `value`
   */
  List<Integer> computeDivisors(int value) {
    // [CSE 262] Implement Me!
    List<Integer> list = new ArrayList<>();
    // iterates up to value and checks for prime divisors
    for(int i = 2; i < value; i++){
      // 
      while(value%i == 0){
        list.add(i);
        value = value/i;
      }
    }
    if(value > 2){
      list.add(value);
    }
    return list;
  }
}
