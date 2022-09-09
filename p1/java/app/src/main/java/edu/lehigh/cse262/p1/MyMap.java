package edu.lehigh.cse262.p1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** MyMap is a wrapper class around the function `map` */
public class MyMap<T> {
  /**
   * Apply `func` to every element in `list`, and return a list containing the
   * results
   * 
   * @param list The list of elements that should be passed to func
   * @param func The function to apply to each element in the list
   * @return A list of the results
   */
  List<T> map(List<T> list, Function<T, T> func) {
    // [CSE 262] Implement Me!
    //if list is null then return null
    if(list == null){
      return null;
    }
    //create a new ArrayList of type List<T> called output
    List<T> output = new ArrayList<>();
    //iterate from 0 to list.size()
    for(int i = 0; i < list.size(); i++){
        //apply func to each element of list and adding it to output
        output.add(func.apply(list.get(i)));
    }
    //return output
    return output;
  }
}
