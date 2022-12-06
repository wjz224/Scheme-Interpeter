package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;

/**
 * The purpose of LibVector is to implement all of the standard library
 * functions that we can do on vectors
 */
public class LibVector {
    /**
     * Populate the provided `map` with a standard set of vector functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        var veclength = new Nodes.BuiltInFunc("vector-length", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int vecCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Vec)
                    vecCount++;
            }
            if (args.size() > (vecCount))
                throw new Exception("vector-length can only handle vector arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in vector-length. Can only handle 1");
            // Compute, making sure to know the return type
            int len = 0;
            len = ((Nodes.Vec) args.get(0)).items.length;
            return new Nodes.Int(len);
        });
        map.put(veclength.name, veclength);

        var vecget = new Nodes.BuiltInFunc("vector-get", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
           
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in vector-get. Can only handle 2");

            if(!((args.get(0) instanceof Nodes.Vec) && (args.get(1) instanceof Nodes.Int))){
                throw new Exception("Wrong argument types. vector-get only handles first argument must be a Vec, second argument must be an Int.");
            }
            int index = ((Nodes.Int) args.get(1)).val;
            if(index >= ((Nodes.Vec) args.get(0)).items.length || index < 0){
                throw new Exception ("vec-get Argument 2 out of range");
            }
             return ((Nodes.Vec) args.get(0)).items[index];
        });
        map.put(vecget.name, vecget);

        var vecset = new Nodes.BuiltInFunc("vector-set!", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
           
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 3)
                throw new Exception("Wrong number of arguments in vector-get. Can only handle 2");

            if(!((args.get(0) instanceof Nodes.Vec) && (args.get(1) instanceof Nodes.Int))){
                throw new Exception("Wrong argument types. vector-get only handles first argument must be a Vec, second argument must be an Int.");
            }
            int index = ((Nodes.Int) args.get(1)).val;
            
            if(index >= ((Nodes.Vec) args.get(0)).items.length || index < 0){
                throw new Exception ("vec-get Argument 2 out of range");
            }
            ((Nodes.Vec) args.get(0)).items[index] = args.get(2);
            return null;
        });
        map.put(vecset.name, vecset);
        
        var vec = new Nodes.BuiltInFunc("vector", (List<IValue> args) -> {
            List<IValue> newVector = new ArrayList<>();
            for(int i = 0; i < args.size(); i++){
                newVector.set(i, args.get(i));
            }
            return new Nodes.Vec(newVector);
        });
        map.put(vec.name, vec);
        var vecq = new Nodes.BuiltInFunc("vec?", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in vec?. Can only handle 1");

            if(args.get(0) instanceof Nodes.Vec){
                return poundT;
            }
            return poundF;
        });
        map.put(vecq.name, vecq);

        var vecmake = new Nodes.BuiltInFunc("make-vector", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in vec?. Can only handle 1");
            if(!((args.get(0) instanceof Nodes.Int))){
                throw new Exception("Wrong argument types. make-vector only handles first argument as an Int.");
            } 
            int size = ((Nodes.Int) args.get(0)).val;
            List<IValue> newVector = new ArrayList<>();

            for(int i = 0; i < size; i++){
                newVector.set(i, poundF);
            }
            return new Nodes.Vec(newVector);
        });
        map.put(vecmake.name, vecmake);
    }
}
