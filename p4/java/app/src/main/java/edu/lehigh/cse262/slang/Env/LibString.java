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
            // Type checking: make sure we only have Str arguments
             // get count for number of strings
            int strCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Str)
                    strCount++;
            }
            // if arg.size() is greater than strCount than there is an argument that is not a Str
            if (args.size() > (strCount))
                throw new Exception("+ can only handle String arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in string-append. Can only handle 2");
            // Computing the return str
            String result = ((Nodes.Str)args.get(0)).val + ((Nodes.Str) args.get(1)).val;
            return new Nodes.Str(result);
        });
        map.put(stringappend.name, stringappend);

        var stringlength = new Nodes.BuiltInFunc("string-length", (List<IValue> args) -> {
            int strCount = 0;
            // get count for number of str args
            for (var arg : args) {
                if (arg instanceof Nodes.Str)
                    strCount++;
            }
            //Making sure we only have str args
            if (args.size() > (strCount))
                throw new Exception("+ can only handle String arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in string-append. Can only handle 2");
            // Computing the return str
            int len = 0;
            len = ((Nodes.Str) args.get(0)).val.length();
            return new Nodes.Int(len);
        });
        map.put(stringlength.name, stringlength);

        var substring = new Nodes.BuiltInFunc("substring", (List<IValue> args) -> {
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 3)
                throw new Exception("Wrong number of arguments in substring. Can only handle 3");
            // Type checking: make sure we only have str and int arguments.
            if(!((args.get(0) instanceof Nodes.Str) && (args.get(1) instanceof Nodes.Int) && (args.get(2) instanceof Nodes.Int))){
                throw new Exception("index argument must be a Str, second and third arguments but Ints.");
            }
            //Computing the arg values
            String str = ((Nodes.Str) args.get(0)).val;
            int index = ((Nodes.Int) args.get(1)).val;
            //Checking if the indexes are valid
            if(index >=  str.length() || index < 0){
                throw new Exception ("substring Argument 2 out of range");
            }
            int index2 = ((Nodes.Int) args.get(2)).val;
            if(index2 >=  str.length() || index2< 0){
                throw new Exception ("substring Argument 3 out of range");
            }
            //Computing the return str
            String output = str.substring(index,index2);
            return new Nodes.Str(output);
        });
        map.put(substring.name, substring);
            
        var stringq = new Nodes.BuiltInFunc("string?", (List<IValue> args) -> {
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 1)
                throw new Exception("Wrong number of arguments in string?. Can only handle 1");
            // Type checking: make sure we only have str arguments and computing the return bool
            if(args.get(0) instanceof Nodes.Str){
                return poundT;
            }
            return poundF;
        });
        map.put(stringq.name, stringq);

        var stringref = new Nodes.BuiltInFunc("string-ref", (List<IValue> args) -> { 
            // Semantic analysis: make sure there are arguments!
            if (args.size() != 2)
                throw new Exception("Wrong number of arguments in substring. Can only handle 2");
            // Type checking: make sure we only have str and int arguments.
            if(!((args.get(0) instanceof Nodes.Str) && (args.get(1) instanceof Nodes.Int))){
                throw new Exception("First argument must be a Str, second argument must be an Int.");
            }
            //Computing the arg values
            String str = ((Nodes.Str) args.get(0)).val;
            int index = ((Nodes.Int) args.get(1)).val;
            //Computing the string-ref char at the given index
            if(index >=  str.length() || index < 0){
                throw new Exception ("string-ref Argument 2 out of range");
            }
            char output = str.charAt(index);
            return new Nodes.Char(output);
        });
        map.put(stringref.name, stringref);

        var stringmake = new Nodes.BuiltInFunc("string", (List<IValue> args) -> { 
            int charCount = 0;
            // get count for number of char args
            for (var arg : args) {
                if (arg instanceof Nodes.Char)
                    charCount++;
            }
            //Type checking: making sure that we only have char args 
            if (args.size() > (charCount))
                throw new Exception("+ can only handle String arguments");
            //Return empty str node if no args
            if (args.size() == 0 ){
                return new Nodes.Str("");
            }
            // Compute the return str
            String result = "";
            for (var arg : args) {
                result += ((Nodes.Char) arg).val;
            }
            return new Nodes.Str(result);
        });
        map.put(stringmake.name, stringmake);        
    }
}
