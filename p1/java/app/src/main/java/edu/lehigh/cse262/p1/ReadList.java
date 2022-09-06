package edu.lehigh.cse262.p1;

import java.util.List;
import java.util.*;
import java.util.ArrayList;


/**
 * ReadList is a wrapper class around the function `read`
 */
public class ReadList<T> {
  /**
   * Read from stdin until EOF is encountered, and put all of the values into a
   * list. The order in the list should be the reverse of the order in which the
   * elements were added.
   * 
   * @return A list with the values that were read
   */

  List<T> read() {
    // [CSE 262] Implement Me!
    // creating generic List of type T to store the standard inputs that will be returned from the function
    List<T> list = new ArrayList<T>();
    // Scanner object to read standard input
    Scanner input = new Scanner(System.in);
    // while loop that checks and gets inputs from standard input. The while loop stops at EOF
    while(input.hasNextLine()){
      // convert is a variable of generic type T that will store each input from standard input
      T convert = null;
      // try to cast generic type T of input
      try{
       // cast input into generic type T and store it in convert. If can't catch exception.
       convert = (T)input.nextLine();
      }catch(ClassCastException e){
        // print error when exception is caught
        System.out.println("ERROR");
      }
      // add converted input to list 
      list.add(convert);
    }
    // create reverse object 
    MyReverse<T> reverseO = new MyReverse<>();
    // call reverse object's reverse method to get the reverse of the list
    list = reverseO.reverse(list);
    // return list
    return list;
  }
}