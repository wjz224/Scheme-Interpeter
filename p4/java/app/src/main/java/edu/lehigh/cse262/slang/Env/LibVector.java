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
        // vector-length function
        var veclength = new Nodes.BuiltInFunc("vector-length", (List<IValue> args) -> {
            // Type checking: make sure we only have vec arguments. 
            int vecCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Vec)
                    vecCount++;
            }
            // if arg size is greater than vecCount that means there must be a argument in the list thats not an argument
            // throw an error because vector-length can only handle vector arguments
            if (args.size() > (vecCount))
                throw new Exception("vector-length can only handle vector arguments");
            // Semantic analysis: only one argument is allwed for vector-length
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in vector-length. Can only handle 1");
            // Compute, making sure to know the return type which an integer
            int len = 0;
            // get length of vector which is the length of items array in the node
            len = ((Nodes.Vec) args.get(0)).items.length;
            // return an  Int Node created with the length of the vector
            return new Nodes.Int(len);
        });
        // put the vectorlength function into the hashmap 
        map.put(veclength.name, veclength);

        // vector-get function
        var vecget = new Nodes.BuiltInFunc("vector-get", (List<IValue> args) -> { 
             // Semantic analysis: only two arguments allowed for vector-get
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in vector-get. Can only handle 2");
            // Type Checking: check if first argument is a vector node and second argument is an int node.
            if(!((args.get(0) instanceof Nodes.Vec) && (args.get(1) instanceof Nodes.Int))){
                throw new Exception("Wrong argument types. vector-get only handles first argument must be a Vec, second argument must be an Int.");
            }
            // second argument is the index of the vector list we are trying to get
            int index = ((Nodes.Int) args.get(1)).val;
            // if index is greater than or equal to the vector items length or less than 0 than its out of bounds
            if(index >= ((Nodes.Vec) args.get(0)).items.length || index < 0){
                throw new Exception ("vec-get Argument 2 out of range");
            }
            // return the item as a Nodes.Vec of the item at the index.
             return ((Nodes.Vec) args.get(0)).items[index];
        });
        map.put(vecget.name, vecget);

        var vecset = new Nodes.BuiltInFunc("vector-set!", (List<IValue> args) -> {
            // Semantic analysis: make sure there are only and atleast  three arguments
            if (args.size() != 3)
                throw new Exception("Wrong number of arguments in vector-get. Can only handle 2");
            // Type checking: check the first argument is a Vec node and second argument is an Int Node
            if(!((args.get(0) instanceof Nodes.Vec) && (args.get(1) instanceof Nodes.Int))){
                throw new Exception("Wrong argument types. vector-get only handles first argument must be a Vec, second argument must be an Int.");
            }
            // Computing the vecter-set! function
            int index = ((Nodes.Int) args.get(1)).val;
            if(index >= ((Nodes.Vec) args.get(0)).items.length || index < 0){
                throw new Exception ("vec-get Argument 2 out of range");
            }
            ((Nodes.Vec) args.get(0)).items[index] = args.get(2);
            //return null
            return null;
        });
        // put vector-set! into mashmap
        map.put(vecset.name, vecset);
        // vector function
        var vec = new Nodes.BuiltInFunc("vector", (List<IValue> args) -> {
            List<IValue> newVector = new ArrayList<>();
            // computing the vector from the given args
            for(int i = 0; i < args.size(); i++){
                newVector.add(args.get(i));
            }
            return new Nodes.Vec(newVector);
        });
        // putting vector into hushmap
        map.put(vec.name, vec);

        // vec? fucnction
        var vecq = new Nodes.BuiltInFunc("vec?", (List<IValue> args) -> {
            // Semantic analysis: make sure there is only one argument
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in vec?. Can only handle 1");
            // Type checking: the argument is a vector and computing boolean to return.
            if(args.get(0) instanceof Nodes.Vec){
                return poundT;
            }
            return poundF;
        });
        // put vec? into hashmap.
        map.put(vecq.name, vecq);
        // make-vector function
        var vecmake = new Nodes.BuiltInFunc("make-vector", (List<IValue> args) -> { // (+ 1 2 3 4)
    
            // Semantic analysis: make sure there is only one argument
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in vec?. Can only handle 1");
            // Type checking: the argument must be an int node.
            if(!((args.get(0) instanceof Nodes.Int))){
                throw new Exception("Wrong argument types. make-vector only handles first argument as an Int.");
            } 
            // Computing new vector with size given by the arg and contains only poundFs.
            int size = ((Nodes.Int) args.get(0)).val;
            List<IValue> newVector = new ArrayList<>();

            for(int i = 0; i < size; i++){
                newVector.add(poundF);
            }
            return new Nodes.Vec(newVector);
        });
        // put vec-make into hashmap
        map.put(vecmake.name, vecmake);
    }
}
