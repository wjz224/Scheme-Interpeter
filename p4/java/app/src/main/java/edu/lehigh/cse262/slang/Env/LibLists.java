package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;

/**
 * The purpose of LibLists is to implement all of the standard library functions
 * that we can do on Cons nodes
 */
public class LibLists {
    /**
     * Populate the provided `map` with a standard set of list functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF, Nodes.Cons empty) {
        // car function
        var car = new Nodes.BuiltInFunc("car", (List<IValue> args) -> { 
            // Type checking: make sure we only have Nodes.Cons
            int consCount = 0;
            // get count for number of Nodes.Cons arguments
            for (var arg : args) {
                if (arg instanceof Nodes.Cons)
                    consCount++;
            }
            // if arg.size() > ConsCount there are arguments that are not Cons Nodes. Throw error
            if (args.size() > (consCount))
                throw new Exception("car can only handle Cons arguments");
            // Semantic analysis: make sure there is only one argument
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in car. Can only handle 1");
            // Compute the first  lis item and return it.
            return ((Nodes.Cons) args.get(0)).car;
      
        });
        // put car function into hashmap
        map.put(car.name, car);
        // cdr function
        var cdr = new Nodes.BuiltInFunc("cdr", (List<IValue> args) -> { //
            // Type checking: make sure we only have cons arguments
            int consCount = 0;
            // get count for number cons arguments
            for (var arg : args) {
                if (arg instanceof Nodes.Cons)
                    consCount++;
            }
             // if arg.size() > ConsCount there are arguments that are not Cons Nodes. Throw error
            if (args.size() > (consCount))
                throw new Exception("cdr can only handle Cons arguments");
            // Semantic analysis: make sure there is only one argument
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in cdr. Can only handle 1");
            // Compute, return the cdr Nodes.Cons
            return ((Nodes.Cons) args.get(0)).cdr;
        });
        // put cdr into the hashmap
        map.put(cdr.name, cdr);
        // cons function
        var cons = new Nodes.BuiltInFunc("cons", (List<IValue> args) -> { 
            // Type checking: make sure we only two arguments
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in cdr. Can only handle 1");
            // Compute, Nodes.Cons with the two arguments and return it
            IValue c1 = args.get(0);
            IValue c2 = args.get(1);
            return new Nodes.Cons(c1, c2);
        });
        // put cons function into hashmap.
        map.put(cons.name, cons);

        // list function
        var list = new Nodes.BuiltInFunc("list", (List<IValue> args) -> {
            // Semantic analysis: make sure there are arguments!
            if (args.size() < 0)
                throw new Exception("Wrong number of arguments in list. Can only handle 0 or more values");
        
            if (args.size() == 0){
                return empty;
            }
            // Compute a Cons Node with the list of arguments provided
            List<IValue> values = new ArrayList<>();
            for(int i = 0; i < args.size(); i++){
                values.add(args.get(i));
            }
            return new Nodes.Cons(values, empty);
        });
        // put list function into hashmap
        map.put(list.name, list);
        
        // list? function
        var listCheck = new Nodes.BuiltInFunc("list?", (List<IValue> args) -> { 
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in list?. Can only handle 1 argument");
              // Type checking: make sure we only have a Cons Node 
              // Compute true or false based on whether or not we have a Cons Node
            if(args.get(0) instanceof Nodes.Cons){
                return poundT;
            }
            return poundF;
        });
        // put list? function into the hashmap
        map.put(listCheck.name, listCheck);

        // set-car! function
        var setcar = new Nodes.BuiltInFunc("set-car!", (List<IValue> args) -> {
            // Semantic analysis: make sure there are only 2 arguments
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in set-cdr!. Can only handle 2 arguments. A Cons argument followed by a value");
             // Type checking: make sure our first argument is a Cons Node
            if (!(args.get(0) instanceof Nodes.Cons)){
                throw new Exception("First argument must be a Cons. Pair Expected");
            }
            // Compute and update the ConsNode's car
            Nodes.Cons consNode = ((Nodes.Cons) args.get(0));   
            consNode.car = args.get(1);
            // return null because we are just updating
            return null;
        });
        // put set-car! into hashmap
        map.put(setcar.name, setcar);

        //set-cdr! function
        var setcdr = new Nodes.BuiltInFunc("set-cdr!", (List<IValue> args) -> { 
            // Semantic analysis: make sure there are only 2 arguments
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in set-cdr!. Can only handle 2 arguments. A Cons argument followed by a value");
              // Type checking: make sure our first argument is a Nodes.Cons
            if (!(args.get(0) instanceof Nodes.Cons)){
                throw new Exception("First argument must be a Cons. Pair Expected");
            }
            // Compute and update the Cons Node's cdr
            Nodes.Cons consNode = ((Nodes.Cons) args.get(0));   
            consNode.cdr = args.get(1);
            // return null because we are just updating
            return null;
        });
        // put set-cdr! function into hashmap
        map.put(setcdr.name, setcdr);
        
    }
}
