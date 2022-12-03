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


        // constants populated in the map.
        var pi = new Nodes.Dbl(Math.PI);
        map.put("pi", pi);
        var e = new Nodes.Dbl(Math.E);
        map.put("e", e);
        var tau = new Nodes.Dbl(Math.PI * 2);
        map.put("tau", tau);
        var infplus = new Nodes.Dbl(Double.POSITIVE_INFINITY);
        map.put("inf+", infplus);
        var infneg = new Nodes.Dbl(Double.NEGATIVE_INFINITY);
        map.put("inf-", infneg);
        var nan = new Nodes.Dbl(Double.POSITIVE_INFINITY - Double.NEGATIVE_INFINITY);
        map.put("nan", nan);
        
        // Addition bulilt in func
        var add = new Nodes.BuiltInFunc("+", (List<IValue> args) -> { // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int intCount = 0;
            int dblCount = 0;
            // get count for number of int and dbl args
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
            // get count for number of int and dbl args
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
            }
            if (args.size() > (intCount + dblCount))
                throw new Exception("- can only handle Int and Dbl arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("- expects at least one argument");
            // Compute, making sure to know the return type
            if (dblCount > 0) {
                double result = 0;
                // check if first argument is a dbl
                if(args.get(0) instanceof Nodes.Dbl){
                    result = ((Nodes.Dbl) args.get(0)).val;    
                }
                else{
                    result = ((Nodes.Int) args.get(0)).val;
                }
                for (int i = 1; i < args.size(); i++) {
                    var arg = args.get(i);
                    if (arg instanceof Nodes.Int)
                        result -= ((Nodes.Int) arg).val;
                    else
                        result -= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = ((Nodes.Int) args.get(0)).val;
                for (int i = 1; i < args.size();i++) {
                    var arg = args.get(i);
                    result -= ((Nodes.Int) arg).val;
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
            // get count for number of int and dbl args
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
                double result = 1;
                for (var arg : args) {
                    if (arg instanceof Nodes.Int)
                        result *= ((Nodes.Int) arg).val;
                    else
                        result *= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = 1;
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
            // get count for number of int and dbl args
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
                double result = 0;
                // check if first argument is a dbl
                if(args.get(0) instanceof Nodes.Dbl){
                    result =  ((Nodes.Dbl) args.get(0)).val;
                }
                else{
                    result = ((Nodes.Int) args.get(0)).val;
                }
                for (int i = 1; i < args.size(); i++) {
                    var arg = args.get(i);
                    if (arg instanceof Nodes.Int)
                        result /= ((Nodes.Int) arg).val;
                    else
                        result /= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = ((Nodes.Int) args.get(0)).val;
                for (int i = 1; i < args.size();i++) {
                    var arg = args.get(i);
                    result /= ((Nodes.Int) arg).val;
                }
                return new Nodes.Int(result);
            }
        });
        map.put(division.name, division);
    
        // built in func for divison
        var modulo = new Nodes.BuiltInFunc("%", (List<IValue> args) -> { // (+ 1 2 3 4)
        // Type checking: make sure we only have int arguments. We also will use
        // this to know if we should be returning an Int or an error
        // modulo procedure should only have 2 arguments and cannot have less.
            if(args.size() != 2){
                throw new Exception ("Wrong number of arguments passed into procedure %");
            }
            int intCount = 0;
            // get count for number of int args
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
            }
            // if arg.size() is greater than int counts, throw an error because modulo can only handle int arguments.
            if (args.size() > (intCount))
                throw new Exception("% can only handle Int arguments");
            // Semantic analysis: make sure there are arguments!
            // Compute, making sure to know the return type
            int num1 = ((Nodes.Int)args.get(0)).val;
            int num2 = ((Nodes.Int)args.get(1)).val;
            int result = num1 % num2;
            return new Nodes.Int(result);
           
        });
        map.put(modulo.name, modulo);
        
        var equals = new Nodes.BuiltInFunc("==", (List<IValue> args) -> { 
            // (+ 1 2 3 4)
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            if(args.size() != 2){
                throw new Exception ("Wrong number of arguments passed into procedure ==");
            }
            int intCount = 0;
            int dblCount = 0;
            int boolCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
                if (arg instanceof Nodes.Bool)
                    boolCount++;
            }
            if (args.size() > (intCount + dblCount + boolCount))
                throw new Exception("== can only handle Int, Dbl, and Bool arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("== expects two arguments");
            // Compute, making sure to know the return type
            double num1 = 0;
            double num2 = 0;
            boolean bool1 = false;
            boolean bool2 = false;
            if (args.get(0) instanceof Nodes.Int){
                num1 =  ((Nodes.Int) args.get(0)).val;
            }
            else if (args.get(0) instanceof Nodes.Dbl){
                num1 =  ((Nodes.Dbl) args.get(0)).val;
            }
            else{
                bool1 = ((Nodes.Bool) args.get(0)).val;
            }
            if (args.get(1) instanceof Nodes.Int){
                num2 =  ((Nodes.Int) args.get(1)).val;
            }
            else if (args.get(1) instanceof Nodes.Dbl){
                num2 =  ((Nodes.Dbl) args.get(1)).val;
            }
            else{
                bool2 = ((Nodes.Bool) args.get(1)).val;
            }
            // check if arg 0 and arg 1 are both numbers that can be compared.
            if(((args.get(0) instanceof Nodes.Int || args.get(0) instanceof Nodes.Dbl) && (args.get(1) instanceof Nodes.Int || args.get(1) instanceof Nodes.Dbl))){
                if(num1 == num2){
                    return poundT;
                }
                else{
                    return poundF;
                }
            }
            // check if arg 0 and arg are both booleans that can be compared
            else if(args.get(0) instanceof Nodes.Bool &&  args.get(1) instanceof Nodes.Bool){
                // if bool1 == bool2 return a poundT which is a Boolean Node with true
                if (bool1 == bool2){
                    return poundT;
                }
                // if they are not equal return a poundF which is a BooleanNode with false
                else{   
                    return poundF;
                }
            }
            // if arg 0 and arg1 are not both Integers/Dbls or both booleans, than they are not comparable and we will just return false
            else{
                return poundF;
            }
        });
        map.put(equals.name, equals);
        
        var greater = new Nodes.BuiltInFunc(">", (List<IValue> args) -> { 
            double ab = 0;
            if(args.size() == 0){
                throw new Exception ("Wrong number of arguments passed into procedure abs");
            }
            double first = 0;
            if(args.get(0) instanceof Nodes.Int){
                first = ((Nodes.Int) args.get(0)).val;
            }
            else if(args.get(0) instanceof Nodes.Dbl){
                first = ((Nodes.Dbl) args.get(0)).val;
            }
            else{
                throw new Exception ("> can only handle Int and Dbl arguments");
            }
            for(int i = 1; i < args.size(); i++){
                if(args.get(i) instanceof Nodes.Int){
                    ab = ((Nodes.Int) args.get(i)).val;
                }
                else if(args.get(0) instanceof Nodes.Dbl){
                    ab = ((Nodes.Dbl) args.get(i)).val;
                }
                else{
                    throw new Exception ("> can only handle Int and Dbl arguments");
                }

                if(ab > first){
                    return poundF;
                }
                else{
                    first = ab;
                }
            }
            return poundT;
        });
        map.put(greater.name, greater);
        
        var greaterequal = new Nodes.BuiltInFunc(">=", (List<IValue> args) -> { 
            double ab = 0;
            if(args.size() == 0){
                throw new Exception ("Wrong number of arguments passed into procedure abs");
            }
            double first = 0;
            if(args.get(0) instanceof Nodes.Int){
                first = ((Nodes.Int) args.get(0)).val;
            }
            else if(args.get(0) instanceof Nodes.Dbl){
                first = ((Nodes.Dbl) args.get(0)).val;
            }
            else{
                throw new Exception (">= can only handle Int and Dbl arguments");
            }
            for(int i = 1; i < args.size(); i++){
                if(args.get(i) instanceof Nodes.Int){
                    ab = ((Nodes.Int) args.get(i)).val;
                }
                else if(args.get(0) instanceof Nodes.Dbl){
                    ab = ((Nodes.Dbl) args.get(i)).val;
                }
                else{
                    throw new Exception (">= can only handle Int and Dbl arguments");
                }

                if(ab >= first){
                    return poundF;
                }
                else{
                    first = ab;
                }
            }
            return poundT;
        });
        map.put(greaterequal.name, greaterequal);


        var less = new Nodes.BuiltInFunc("<", (List<IValue> args) -> { 
            double ab = 0;
            if(args.size() == 0){
                throw new Exception ("Wrong number of arguments passed into procedure abs");
            }
            double first = 0;
            if(args.get(0) instanceof Nodes.Int){
                first = ((Nodes.Int) args.get(0)).val;
            }
            else if(args.get(0) instanceof Nodes.Dbl){
                first = ((Nodes.Dbl) args.get(0)).val;
            }
            else{
                throw new Exception ("< can only handle Int and Dbl arguments");
            }
            for(int i = 1; i < args.size(); i++){
                if(args.get(i) instanceof Nodes.Int){
                    ab = ((Nodes.Int) args.get(i)).val;
                }
                else if(args.get(0) instanceof Nodes.Dbl){
                    ab = ((Nodes.Dbl) args.get(i)).val;
                }
                else{
                    throw new Exception ("< can only handle Int and Dbl arguments");
                }
                if(ab < first){
                    return poundF;
                }
                else{
                    first = ab;
                }
            }
            return poundT;
        });
        map.put(less.name, less);

        var lessequal = new Nodes.BuiltInFunc("<=", (List<IValue> args) -> { 
            double ab = 0;
            if(args.size() == 0){
                throw new Exception ("Wrong number of arguments passed into procedure abs");
            }
            double first = 0;
            if(args.get(0) instanceof Nodes.Int){
                first = ((Nodes.Int) args.get(0)).val;
            }
            else if(args.get(0) instanceof Nodes.Dbl){
                first = ((Nodes.Dbl) args.get(0)).val;
            }
            else{
                throw new Exception ("<= can only handle Int and Dbl arguments");
            }
            for(int i = 1; i < args.size(); i++){
                if(args.get(i) instanceof Nodes.Int){
                    ab = ((Nodes.Int) args.get(i)).val;
                }
                else if(args.get(0) instanceof Nodes.Dbl){
                    ab = ((Nodes.Dbl) args.get(i)).val;
                }
                else{
                    throw new Exception ("<= can only handle Int and Dbl arguments");
                }
                if(ab <= first){
                    return poundF;
                }
                else{
                    first = ab;
                }
            }
            return poundT;
        });
        map.put(lessequal.name, lessequal);
        
        
        var abs = new Nodes.BuiltInFunc("abs", (List<IValue> args) -> { 
            double ab = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure abs");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                ab = ((Nodes.Int) args.get(0)).val;
            }
            else{
                ab = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.abs(ab));
        });
        map.put(abs.name, abs);

        var sqrt = new Nodes.BuiltInFunc("sqrt", (List<IValue> args) -> { 
            double sqrt1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure sqrt");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                sqrt1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                sqrt1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.sqrt(sqrt1));
        });
        map.put(sqrt.name, sqrt);

        var acos = new Nodes.BuiltInFunc("acos", (List<IValue> args) -> { 
            double acos1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure acos");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                acos1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                acos1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.acos(acos1));
        });
        map.put(acos.name, acos);
        

        var asin = new Nodes.BuiltInFunc("asin", (List<IValue> args) -> { 
            double asin1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure asin");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                asin1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                asin1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.asin(asin1));
        });
        map.put(asin.name, asin);

        
        var atan = new Nodes.BuiltInFunc("atan", (List<IValue> args) -> { 
            double atan1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure atan");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                atan1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                atan1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.asin(atan1));
        });
        map.put(atan.name, atan);
    
        
        var cos = new Nodes.BuiltInFunc("cos", (List<IValue> args) -> { 
            double cos1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure cos");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                cos1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                cos1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.asin(cos1));
        });
        map.put(cos.name, cos);

        var cosh = new Nodes.BuiltInFunc("cosh", (List<IValue> args) -> { 
            double cosh1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure cosh");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                cosh1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                cosh1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.cosh(cosh1));
        });
        map.put(cosh.name, cosh);
        
        var sin = new Nodes.BuiltInFunc("sin", (List<IValue> args) -> { 
            double sin1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure sin");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                sin1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                sin1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.sin(sin1));
        });
        map.put(sin.name, sin);

        var sinh = new Nodes.BuiltInFunc("sinh", (List<IValue> args) -> { 
            double sinh1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure sinh");
            }  
            if(args.get(0) instanceof Nodes.Int){
                sinh1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                sinh1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.sinh(sinh1));
        });
        map.put(sinh.name, sinh);

        var tan = new Nodes.BuiltInFunc("tan", (List<IValue> args) -> { 
            double tan1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure tan");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                tan1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                tan1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.tan(tan1));
        });
        map.put(tan.name, tan);

        var tanh = new Nodes.BuiltInFunc("tanh", (List<IValue> args) -> { 
            double tanh1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure tanh");
            }
           
            if(args.get(0) instanceof Nodes.Int){
                tanh1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                tanh1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.tanh(tanh1));
        });
        map.put(tanh.name, tanh);

        var integerFunc = new Nodes.BuiltInFunc("integer?", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure integer?");
            }
            if( args.get(0) instanceof Nodes.Int){
                return poundT;
            }
            else{
                return poundF;
            }
        });
        map.put(integerFunc.name, integerFunc);

        var doubleFunc = new Nodes.BuiltInFunc("double?", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure double?");
            }
            if( args.get(0) instanceof Nodes.Dbl){
                return poundT;
            }
            else{
                return poundF;
            }
        });
        map.put(doubleFunc.name, doubleFunc);
        
        var numberFunc = new Nodes.BuiltInFunc("number?", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure number?");
            }
            if( args.get(0) instanceof Nodes.Int || args.get(0) instanceof Nodes.Dbl){
                return poundT;
            }
            else{
                return poundF;
            }
        });
        map.put(numberFunc.name, numberFunc);
        
        var symbol = new Nodes.BuiltInFunc("symbol?", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure symbol?");
            }
            if(args.get(0) instanceof Nodes.Symbol) {
                return poundT;
            }
            else{
                return poundF;
            }
        });
        map.put(symbol.name, symbol);

        var procedure = new Nodes.BuiltInFunc("procedure?", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure procedure?");
            }
            if(args.get(0) instanceof Nodes.BuiltInFunc ||  args.get(0) instanceof Nodes.LambdaVal) {
                return poundT;
            }
            else{
                return poundF;
            }
        });
        map.put(procedure.name, procedure);

        var log10 = new Nodes.BuiltInFunc("log10", (List<IValue> args) -> { 
            double log1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure log10");
            }
            if(args.get(0) instanceof Nodes.Int){
                log1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                log1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.log10(log1));
        });
        map.put(log10.name, log10);
        
        var loge = new Nodes.BuiltInFunc("loge", (List<IValue> args) -> { 
            double loge1 = 0;
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure loge");
            }
            if(args.get(0) instanceof Nodes.Int){
                loge1 = ((Nodes.Int) args.get(0)).val;
            }
            else{
                loge1 = ((Nodes.Dbl) args.get(0)).val;
            }
            return new Nodes.Dbl(Math.log(loge1));
        });
        map.put(loge.name, loge);

        var pow = new Nodes.BuiltInFunc("pow", (List<IValue> args) -> { 
            double baseNum = 0;
            double expNum = 0;
            if(args.size() != 2){
                throw new Exception ("Wrong number of arguments passed into procedure pow");
            }
            if(args.get(0) instanceof Nodes.Int){
                baseNum = ((Nodes.Int) args.get(0)).val;
            }
            else{
                baseNum = ((Nodes.Dbl) args.get(0)).val;
            }
            if(args.get(1) instanceof Nodes.Int){
                expNum = ((Nodes.Int) args.get(1)).val;
            }
            else{
                expNum = ((Nodes.Dbl) args.get(1)).val;
            }
            return new Nodes.Dbl(Math.pow(baseNum, expNum));
        });
        map.put(pow.name, pow);

        var not = new Nodes.BuiltInFunc("not", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure not");
            }
            if(!(args.get(0) instanceof Nodes.Bool)){
                return poundF;
            }
            if(((Nodes.Bool) args.get(0)).val == false){
                return poundT;
            }
            return poundF;
        });
        map.put(not.name, not);

        var intToDouble = new Nodes.BuiltInFunc("integer->double", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure int->double");
            }
            if(!(args.get(0) instanceof Nodes.Int)){
                throw new Exception("integer->double can only handle Int arguments");
            }
            double output = (double) ((Nodes.Int) args.get(0)).val;
        
            return new Nodes.Dbl(output);
            
        });
        map.put(intToDouble.name, intToDouble);

        var doubleToInt = new Nodes.BuiltInFunc("double->integer", (List<IValue> args) -> { 
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure double->int");
            }
            if(!(args.get(0) instanceof Nodes.Dbl)){
                throw new Exception("integer->double can only handle Dbl arguments");
            }
            int output = (int) ((Nodes.Dbl) args.get(0)).val;
            return new Nodes.Int(output);
            
        });
        map.put(doubleToInt.name, doubleToInt);

        var nullCheck = new Nodes.BuiltInFunc("null?", (List<IValue> args) ->{
            if(args.size() != 1){
                throw new Exception ("Wrong number of arguments passed into procedure ?");
            } 
            if(args.get(0) == null){
                return poundT;
            }
            return poundF;
        });
        map.put(nullCheck.name, nullCheck);

        var andVals = new Nodes.BuiltInFunc("and", (List<IValue> args) ->{
            if(args.size() < 1){
                throw new Exception ("Wrong number of arguments passed into procedure and");
            } 
            if(args.get(0) == null){
                return poundT;
            }
            for (var arg : args) {
                if (arg instanceof Nodes.Bool){
                    if(((Nodes.Bool) arg).val == false){
                        return poundF;
                    }
                }
            }
            return poundT;
        });
        map.put(andVals.name, andVals);

        var orVals = new Nodes.BuiltInFunc("or", (List<IValue> args) ->{
            if(args.size() < 1){
                throw new Exception ("Wrong number of arguments passed into procedure or");
            } 
            if(args.get(0) == null){
                return poundT;
            }
            for (var arg : args) {
                if (arg instanceof Nodes.Bool){
                    if(((Nodes.Bool) arg).val != false){
                        return poundT;
                    }
                }
            }
            return poundF;
        });
        map.put(orVals.name, orVals);       
    }
}
