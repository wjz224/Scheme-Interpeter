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

        var car = new Nodes.BuiltInFunc("car", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int consCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Cons)
                    consCount++;
                
            }
            if (args.size() > (consCount))
                throw new Exception("car can only handle Cons arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in car. Can only handle 1");
            // Compute, making sure to know the return type
            // might have to check types if dont work
            IValue output = ((Nodes.Cons) args.get(0)).car;
            return output;
        });
        map.put(car.name, car);
        var cdr = new Nodes.BuiltInFunc("cdr", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int consCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Cons)
                    consCount++;
            }
            if (args.size() > (consCount))
                throw new Exception("cdr can only handle Cons arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in cdr. Can only handle 1");
            // Compute, making sure to know the return type
            // might have to check types if dont work
            IValue output = ((Nodes.Cons) args.get(0)).cdr;
            return output;
        });
        map.put(cdr.name, cdr);

        var cons = new Nodes.BuiltInFunc("cons", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            // get count for number of int and dbl args
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in cdr. Can only handle 1");
            // Compute, making sure to know the return type
            // might have to check types if dont work
            IValue c1 = args.get(0);
            IValue c2 = args.get(1);
            return new Nodes.Cons(c1, c2);
        });
        map.put(cons.name, cons);

        var list = new Nodes.BuiltInFunc("list", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
           
            // Semantic analysis: make sure there are arguments!
            if (args.size() < 0)
                throw new Exception("Wrong number of arguments in list. Can only handle 0 or more values");
            // Compute, making sure to know the return type
            // might have to check types if dont work
            if (args.size() == 0){
                return empty;
            }
            List<IValue> values = new ArrayList<>();
            for(int i = 0; i < args.size(); i++){
                values.add(args.get(i));
            }
            return new Nodes.Cons(values, empty);
        });
        map.put(list.name, list);
        
        var listCheck = new Nodes.BuiltInFunc("list?", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
           
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in list?. Can only handle 1 argument");
            // Compute, making sure to know the return type
            // might have to check types if dont work
            if(args.get(0) instanceof Nodes.Cons){
                return poundT;
            }
            return poundF;
        });
        map.put(listCheck.name, listCheck);
        
        var setcar = new Nodes.BuiltInFunc("set-car!", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            // get count for number of int and dbl args
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in set-cdr!. Can only handle 2 arguments. A Cons argument followed by a value");
            if (!(args.get(0) instanceof Nodes.Cons)){
                throw new Exception("First argument must be a Cons. Pair Expected");
            }
        
            Nodes.Cons consNode = ((Nodes.Cons) args.get(0));   
            consNode.car = args.get(1);
            // Compute, making sure to know the return type
            // might have to check types if dont work
            return null;
        });
        map.put(setcar.name, setcar);

        var setcdr = new Nodes.BuiltInFunc("set-cdr!", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            // get count for number of int and dbl args
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in set-cdr!. Can only handle 2 arguments. A Cons argument followed by a value");
            if (!(args.get(0) instanceof Nodes.Cons)){
                throw new Exception("First argument must be a Cons. Pair Expected");
            }
        
            Nodes.Cons consNode = ((Nodes.Cons) args.get(0));   
            consNode.cdr = args.get(1);
            // Compute, making sure to know the return type
            // might have to check types if dont work
            return null;
        });
        map.put(setcdr.name, setcdr);
        
    }
}
