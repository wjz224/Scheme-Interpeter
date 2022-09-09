package edu.lehigh.cse262.p1;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import javax.xml.validation.Validator;
/**
 * App is the entry point into our program. You are allowed to add fields and
 * methods to App. You may also add `import` statements.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("CSE 262 Project 1");
        // [CSE 262] You should write code here to help you test your
        // implementations
        /* 
        ReadList<Integer> c = new ReadList<Integer>();
        List<Integer> p = new ArrayList<Integer>();
        p = c.read();
        for(int i = 0; i < p.size(); i++){
            System.out.println(p.get(i));
        }
        */
        List<String> test = new ArrayList<String>();
        MyReverse<String> reverseO = new MyReverse<>();
        MyMap<String> mapO = new MyMap<>();
        Function<String, String> testFunc = (t) -> {
            return "zebra" + t;
        };
        test.add("a");
        test.add("b");
        test.add("c");
        test.add("d");
        
        // printing list
        System.out.println(test.toString());
        // testing reverse on list
        test = reverseO.reverse(test);
        System.out.println(test.toString());
        // testing map on list
        test = mapO.map(test,testFunc);
        System.out.println(test.toString());
        // testing prime divisors
        PrimeDivisors pDiv = new PrimeDivisors();
        int val = 28;
        List<Integer> test2 = new ArrayList<>();
        test2 = pDiv.computeDivisors(val);
        System.out.println(test2.toString());
        MyTree<Integer> tree = new MyTree<>();
        List<Integer> treeTest = new ArrayList<>();
        List<Integer> thing = new ArrayList<>();
        thing.add(6);
        thing.add(7);
        thing.add(8);
        thing.add(9);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        tree.insert(1);
        tree.insert(2);
        tree.insert(2);
        tree.insert(2);
        tree.inslist(thing);
        tree.insert(6);
        tree.clear();

    }
}
