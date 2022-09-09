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
    //Create an ArrayList of type List<Integer> called output
    List<Integer> list = new ArrayList<>();
    // iterates up to value and checks for prime divisors
    for(int i = 2; i < value; i++){
      //while: the prime number evenly divides into value
      while(value%i == 0){
        //add the prime number to list
        list.add(i);
        //divide value by i
        value = value/i;
      }
    }
    //if value is > 2 and at this point has no more prime factors less than it, that means that value is a prime factor of the initial value given
    if(value > 2){
      //add value to list
      list.add(value);
    }
    //return list
    return list;
  }
}
