package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;
import java.util.List;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;

/**
 * The purpose of LibMath is to implement all of the standard library functions
 * that we can do on numbers (Integer or Double)
 */
public class LibMath {
    /**
     * Populate the provided `map` with a standard set of mathematical functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        // As a starting point, let's go ahead and put the addition function
        // into the map. This will make it **much** easier to test `apply`, and
        // should provide some useful guidance for making other functions.
        //
        // Note that this code is **very** tedious. Making some helper
        // functions would probably be wise, but it's up to you to figure out
        // how.
        // built in func for addition
        var add = new Nodes.BuiltInFunc("+", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int intCount = 0;
            int dblCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
            }
            if (args.size() > (intCount + dblCount))
                throw new Exception("+ can only handle Int and Dbl arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("+ expects at least one argument");
            // Compute, making sure to know the return type
            if (dblCount > 0) {
                double result = 0;
                for (var arg : args) {
                    if (arg instanceof Nodes.Int)
                        result += ((Nodes.Int) arg).val;
                    else
                        result += ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = 0;
                for (var arg : args) {
                    result += ((Nodes.Int) arg).val;
                }
                return new Nodes.Int(result);
            }
        });
        map.put(add.name, add);
        var subtract = new Nodes.BuiltInFunc("-", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int intCount = 0;
            int dblCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
            }
            if (args.size() > (intCount + dblCount))
                throw new Exception("/ can only handle Int and Dbl arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("/ expects at least one argument");
            // Compute, making sure to know the return type
            if (dblCount > 0) {
                double result = ((Nodes.Dbl)args[0]).val;
                for (int i= 1; i < args.size(); i++) {
                    if (args[i] instanceof Nodes.Int)
                        result -= ((Nodes.Int) args[i]).val;
                    else
                        result -= ((Nodes.Dbl) args[i]).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = ((Nodes.Int)args[0]).val;
                for (int i = 1; i < args.size(); i++) {
                    result -= ((Nodes.Int) args[i]).val;
                }
                return new Nodes.Int(result);
            }
        });

        map.put(subtract.name, subtract);
        
        var multiplication = new Nodes.BuiltInFunc("*", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int intCount = 0;
            int dblCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
            }
            if (args.size() > (intCount + dblCount))
                throw new Exception("* can only handle Int and Dbl arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("* expects at least one argument");
            // Compute, making sure to know the return type
            if (dblCount > 0) {
                double result = 0;
                for (var arg : args) {
                    if (arg instanceof Nodes.Int)
                        result *= ((Nodes.Int) arg).val;
                    else
                        result *= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = 0;
                for (var arg : args) {
                    result *= ((Nodes.Int) arg).val;
                }
                return new Nodes.Int(result);
            }
        });
        map.put(multiplication.name, multiplication);
        // built in func for divison
    var division = new Nodes.BuiltInFunc("/", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int intCount = 0;
            int dblCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
            }
            if (args.size() > (intCount + dblCount))
                throw new Exception("/ can only handle Int and Dbl arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("/ expects at least one argument");
            // Compute, making sure to know the return type
            if (dblCount > 0) {
                double result = ((Nodes.Dbl)args[0]).val;
                for (int i= 1; i < args.size(); i++) {
                    if (args[i] instanceof Nodes.Int)
                        result /= ((Nodes.Int) args[i]).val;
                    else
                        result /= ((Nodes.Dbl) args[i]).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = ((Nodes.Int)args[0]).val;
                for (int i = 1; i < args.size(); i++) {
                    result  /= ((Nodes.Int) args[i]).val;
                }
                return new Nodes.Int(result);
            }
        });
        map.put(division.name, division);
    
    // built in func for divison
    var modulo = new Nodes.BuiltInFunc("%", (List<IValue> args) -> { // (+ 1 2 3 4)
        // Type checking: make sure we only have int and dbl arguments. We also will use
        // this to know if we should be returning an Int or a Dbl
        if(arg.size > 2 || arg.size == 1){
            throw new Exception ("Wrong number of arguments passed into procedure")
        }
        int intCount = 0;
        for (var arg : args) {
            if (arg instanceof Nodes.Int)
                intCount++;
        }
        if (args.size() > (intCount))
            throw new Exception("% can only handle Int arguments");
        // Semantic analysis: make sure there are arguments!
        if (args.size() == 0)
            throw new Exception("% expects two argument");
        // Compute, making sure to know the return type
        if (intCount > 0) {
            int num1 = ((Nodes.Int)args[0]).val;
            int num2 = ((Nodes.Int)args[1]).val;
            int result = ((Nodes.Int) (num % num2)).val;
            return new Nodes.Int(result);
        } 
    });
    map.put(modulo.name, modulo);

    var modulo = new Nodes.BuiltInFunc("==", (List<IValue> args) -> { // (+ 1 2 3 4)
        // Type checking: make sure we only have int and dbl arguments. We also will use
        // this to know if we should be returning an Int or a Dbl
        if(arg.size > 2 || arg.size == 1){
            throw new Exception ("Wrong number of arguments passed into procedure")
        }
        int intCount = 0;
        int dblCount = 0;
        int boolCount = 0;
        for (var arg : args) {
            if (arg instanceof Nodes.Int)
                intCount++;
            if (arg instanceof Nodes.Dbl)
                dblCount++;
            if (arg instanceof Node.Bool)
                boolCount++;
        }
        if (args.size() > (intCount + dblCount + boolCount))
            throw new Exception("== can only handle Int, Dbl, and Bool arguments");
        // Semantic analysis: make sure there are arguments!
        if (args.size() == 0)
            throw new Exception("== expects two arguments");
        // Compute, making sure to know the return type
        var num1 = 0;
        var num2 = 0
        if (args[0] instanceof Nodes.Int){
            num1 =  ((Nodes.Int) args[0]).val;
        }
        else if (args[0] instanceof Nodes.Dbl){
            num1 =  ((Nodes.Dbl) args[0]).val;
        }
        else{
            num1 = ((Nodes.Bool) args[0].val);
        }
        if (args[1] instanceof Nodes.Int){
            num2 =  ((Nodes.Int) args[1]).val;
        }
        else if (args[1] instanceof Nodes.Dbl){
            num2 =  ((Nodes.Dbl) args[1]).val;
        }
        else{
            num2 = ((Nodes.Bool) args[1].val);
        }
        if(num1 == num2){
            return poundT;
        }
        else{
            return poundF
        }
            
            
    });
    map.put(modulo.name, modulo);
    }
}
