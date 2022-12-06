package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;
import java.util.List;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;

/**
 * The purpose of LibString is to implement all of the standard library
 * functions that we can do on Strings
 */
public class LibString {
    /**
     * Populate the provided `map` with a standard set of string functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        var stringappend = new Nodes.BuiltInFunc("string-append", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int strCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Str)
                    strCount++;
            }
            if (args.size() > (strCount))
                throw new Exception("+ can only handle String arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in string-append. Can only handle 2");
            // Compute, making sure to know the return type
            String result = ((Nodes.Str)args.get(0)).val + ((Nodes.Str) args.get(1)).val;
            
            return new Nodes.Str(result);
        });
        map.put(stringappend.name, stringappend);

        var stringlength = new Nodes.BuiltInFunc("string-length", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int strCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Str)
                    strCount++;
            }
            if (args.size() > (strCount))
                throw new Exception("+ can only handle String arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in string-append. Can only handle 2");
            // Compute, making sure to know the return type
            int len = 0;
            len = ((Nodes.Str) args.get(0)).val.length();
            return new Nodes.Int(len);
        });
        map.put(stringlength.name, stringlength);

        var substring = new Nodes.BuiltInFunc("substring", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
           
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 3)
                throw new Exception("Wrong number of arguments in substring. Can only handle 3");

            if(!((args.get(0) instanceof Nodes.Str) && (args.get(1) instanceof Nodes.Int) && (args.get(2) instanceof Nodes.Int))){
                throw new Exception("index argument must be a Str, second and third arguments but Ints.");
            }
            String str = ((Nodes.Str) args.get(0)).val;
            
            int index = ((Nodes.Int) args.get(1)).val;
            if(index >=  str.length() || index < 0){
                throw new Exception ("substring Argument 2 out of range");
            }
            int index2 = ((Nodes.Int) args.get(2)).val;
            if(index2 >=  str.length() || index2< 0){
                throw new Exception ("substring Argument 3 out of range");
            }
            String output = str.substring(index,index2);
            return new Nodes.Str(output);
        });
        map.put(substring.name, substring);
            
        var stringq = new Nodes.BuiltInFunc("string?", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in string?. Can only handle 1");

            if(args.get(0) instanceof Nodes.Str){
                return poundT;
            }
            return poundF;
        });
        map.put(stringq.name, stringq);

        var stringref = new Nodes.BuiltInFunc("string-ref", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
           
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in substring. Can only handle 2");

            if(!((args.get(0) instanceof Nodes.Str) && (args.get(1) instanceof Nodes.Int))){
                throw new Exception("First argument must be a Str, second argument must be an Int.");
            }
            String str = ((Nodes.Str) args.get(0)).val;
            int index = ((Nodes.Int) args.get(1)).val;
            if(index >=  str.length() || index < 0){
                throw new Exception ("string-ref Argument 2 out of range");
            }
            char output = str.charAt(index);
            return new Nodes.Char(output);
        });
        map.put(stringref.name, stringref);

        var stringmake = new Nodes.BuiltInFunc("string", (List<IValue> args) -> { // (+ 1 2 3 4)
            int charCount = 0;
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Char)
                    charCount++;
            }
            if (args.size() > (charCount))
                throw new Exception("+ can only handle String arguments");
            if (args.size() == 0 ){
                return new Nodes.Str("");
            }
            // Compute, making sure to know the return type
            String result = "";
            for (var arg : args) {
                result += ((Nodes.Char) arg).val;
            }
            return new Nodes.Str(result);
        });
        map.put(stringmake.name, stringmake);        
    }
}
