package edu.lehigh.cse262.p1;

import java.util.List;

/** MyReverse is a wrapper class around the function `reverse` */
public class MyReverse<T> {
  /**
   * Return a list that has all of the elements of `in`, but in reverse order
   * 
   * @param in The list to reverse
   * @return A list that is the reverse of `in`
   */
  List<T> reverse(List<T> in) {
    // Base case there is no list return null
    if(in == null){
      return null;
    } 
    // Base case where there is only one element in the list
    if(in.size() < 2){
      return in;
    }
    else{
      // Store removed element
      T remove = in.remove(0);
      // Recursively call reverse function that recursively removes all elements and adds elements back into list in  reverese
      reverse(in);
      // Adding back elements into list
      in.add(remove);
    }
    // return list
    return in;
  }
}
