import slang_parser
import operator
import math
import slang_env


def addMathFuncs(env):
    """Add standard math functions to the given environment"""
    #constants populated in the env.
    env.put("pi", math.pi)
    env.put("e", math.e)
    env.put("tau", math.tau)
    env.put("inf+", math.inf)
    env.put("inf-", -math.inf)
    env.put("nan", math.nan)
    
    def add(args):
        intCount = 0
        dblCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
            if arg['type'] == slang_parser.DBL:
                dblCount+=1 
        if (len(args) > (intCount + dblCount)):
            raise SyntaxError("+ can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise SyntaxError("+ expects at least one argument")
        # Compute, making sure to know the return type
        result = 0
        for arg in args:
            result += arg['val']
        return slang_parser.IntNode(result)
    addFunc = slang_parser.BuiltInNode("+",add)
    env.put(addFunc['name'],addFunc)

    def subtract(args):
            # Type checking: make sure we only have int and dbl arguments. We also will use
            # this to know if we should be returning an Int or a Dbl
        intCount = 0
        dblCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
            if arg['type'] == slang_parser.DBL:
                dblCount+=1
        if (len(args) > (intCount + dblCount)):
            raise SyntaxError("- can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise SyntaxError("- expects at least one argument")
        # Compute, making sure to know the return type
        result = args[0]['val']
        for arg in args[1:]:
            result -= arg['val']
        return slang_parser.IntNode(result)
    subtractFunc = slang_parser.BuiltInNode("-",subtract)
    env.put(subtractFunc["name"], subtractFunc)

    def multiplication(args):
        intCount = 0
        dblCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
            if arg['type'] == slang_parser.DBL:
                dblCount+=1
        if (len(args) > (intCount + dblCount)):
            raise SyntaxError("* can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise SyntaxError("* expects at least one argument")
        # Compute, making sure to know the return type
        result = 1
        for arg in args:
            result *= arg['val']
        return slang_parser.IntNode(result)
    multFunc = slang_parser.BuiltInNode("*",multiplication)
    env.put(multFunc["name"], multFunc)

    def division(args):
        intCount = 0
        dblCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
            if arg['type'] == slang_parser.DBL:
                dblCount+=1
        if (len(args) > (intCount + dblCount)):
            raise SyntaxError("/ can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise SyntaxError("/ expects at least one argument")
        # Compute, making sure to know the return type
        result = args[0]['val']
        for arg in args[1:]:
            result /= arg['val']
        return slang_parser.IntNode(result)
    divFunc = slang_parser.BuiltInNode("/",division)
    env.put(divFunc["name"], divFunc)
    
    def modulo(args):
        # Type checking: make sure we only have int arguments. We also will use
        # this to know if we should be returning an Int or an error
        # modulo procedure should only have 2 arguments and cannot have less.
        if(len(args) != 2):
            raise SyntaxError ("Wrong number of arguments passed into procedure %")
        intCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
        # if len(args) is greater than int counts, throw an error because modulo can only handle int arguments.
        if (len(args) > (intCount)):
            raise SyntaxError("% can only handle Int arguments")
        # Semantic analysis: make sure there are arguments!
        # Compute, making sure to know the return type
        num1 = args[0]['val']
        num2 = args[1]['val']
        result = num1 % num2
        return slang_parser.IntNode(result)
    modFunc = slang_parser.BuiltInNode("%",modulo)
    env.put(modFunc["name"], modFunc)

    def equals(args):
        # Type checking: make sure we only have int, dbl and bool arguments.
        if(len(args) != 2):
            raise SyntaxError ("Wrong number of arguments passed into procedure ==")
        intCount = 0
        dblCount = 0
        boolCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
            if arg['type'] == slang_parser.DBL:
                dblCount+=1
            if arg['type'] == slang_parser.BOOL:
                boolCount+=1
        # Checking for only int, dbl, and bool args
        if (len(args) > (intCount + dblCount + boolCount)):
            raise SyntaxError("== can only handle Int, Dbl, and Bool arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise SyntaxError("== expects two arguments")
        # Compute, making sure to know the return type
        first = args[0]['val']
        second = args[1]['val']
        # check if arg 0 and arg 1 are both numbers that can be compared.
        if(first['type'] == slang_parser.INT or first['type'] == slang_parser.DBL) and (second['type'] == slang_parser.INT or second['type'] == slang_parser.DBL):
            if(first == second):
                return slang_parser.BoolNode(True)
            else:
                return slang_parser.BoolNode(False)
        # check if arg 0 and arg are both booleans that can be compared
        elif(first['type'] == slang_parser.BOOL and second['type'] == slang_parser.BOOL):
            # if bool1 == bool2 return a poundT which is a Boolean Node with true
            if(first == second):
                return slang_parser.BoolNode(True)
            else:
                return slang_parser.BoolNode(False)
        # if arg 0 and arg1 are not both Integers/Dbls or both booleans, than they are not comparable and we will just return false
        else:
            return slang_parser.BoolNode(False)
    equalsFunc = slang_parser.BuiltInNode("==",equals)
    env.put(equalsFunc["name"], equalsFunc)

    def greater(args):
        ab = 0
        #checking for arguments
        if(len(args) == 0):
            raise SyntaxError ("Wrong number of arguments passed into procedure >")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("> can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
                raise SyntaxError ("> can only handle Int and Dbl arguments")
            if(arg['val'] > first):
                return slang_parser.BoolNode(False)
            else:
                first = ab
        return slang_parser.BoolNode(True)
    greaterFunc = slang_parser.BuiltInNode(">",greater)
    env.put(greaterFunc["name"], greaterFunc)
    
    def greaterequal(args):
        ab = 0
        #checking for arguments
        if(len(args) == 0):
            raise SyntaxError ("Wrong number of arguments passed into procedure >=")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError (">= can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
                raise SyntaxError (">= can only handle Int and Dbl arguments")
            if(arg['val'] >= first):
                return slang_parser.BoolNode(False)
            else:
                first = ab
        return slang_parser.BoolNode(True)
    greaterEFunc = slang_parser.BuiltInNode(">=",greaterequal)
    env.put(greaterEFunc["name"], greaterEFunc)

    def less(args):
        ab = 0
        #checking for arguments
        if(len(args) == 0):
            raise SyntaxError ("Wrong number of arguments passed into procedure <")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("< can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
                raise SyntaxError ("< can only handle Int and Dbl arguments")
            if(arg['val'] < first):
                return slang_parser.BoolNode(False)
            else:
                first = ab
        return slang_parser.BoolNode(True)
    lessFunc = slang_parser.BuiltInNode("<",less)
    env.put(lessFunc["name"], lessFunc)

    def lessequal(args):
        ab = 0
        #checking for arguments
        if(len(args) == 0):
            raise SyntaxError ("Wrong number of arguments passed into procedure <=")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("<= can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
                raise SyntaxError ("<= can only handle Int and Dbl arguments")
            if(arg['val'] <= first):
                return slang_parser.BoolNode(False)
            else:
                first = ab
        return slang_parser.BoolNode(True)
    lessEFunc = slang_parser.BuiltInNode("<",lessequal)
    env.put(lessEFunc["name"], lessEFunc)

    def absolute(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure abs")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]

        if(not((first['type'] == slang_parser.INT)) and not(first['type'] == slang_parser.DBL)):
            raise SyntaxError ("abs can only handle Int and Dbl arguments")
        val = args[0]['val']
        #Computing the return dbl 
        return slang_parser.IntNode(abs(val))
    absFunc = slang_parser.BuiltInNode("abs",absolute)
    env.put(absFunc["name"], absFunc)

    def sqrt(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError("Wrong number of arguments passed into procedure sqrt")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]
        if(not(first['type']  == slang_parser.INT) and not(first['type'] == slang_parser.DBL)):
            raise SyntaxError ("sqrt can only handle Int and Dbl arguments")
        val = args[0]['val']
        #Computing the return dbl 
        return slang_parser.IntNode(math.sqrt(val))
    sqrtFunc = slang_parser.BuiltInNode("sqrt",sqrt)
    env.put(sqrtFunc["name"], sqrtFunc)

    def acos(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure acos")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("acos can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.IntNode(acos(first))
    acosFunc = slang_parser.BuiltInNode("acos",acos)
    env.put(acosFunc["name"], acosFunc)

    def asin(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure asin")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("asin can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(asin(first))
    asinFunc = slang_parser.BuiltInNode("asin",asin)
    env.put(asinFunc["name"], asinFunc)

    def atan(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure atan")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("atan can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(atan(first))
    atanFunc = slang_parser.BuiltInNode("atan",atan)
    env.put(atanFunc["name"], atanFunc)
    
    def cos(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure cos")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("cos can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(cos(first))
    cosFunc = slang_parser.BuiltInNode("cos",cos)
    env.put(cosFunc["name"], cosFunc)

    def cosh(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure cosh")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("cosh can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(cosh(first))
    coshFunc = slang_parser.BuiltInNode("cosh",cosh)
    env.put(coshFunc["name"], coshFunc)

    def sin(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure sin")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("sin can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(sin(first))
    sinFunc = slang_parser.BuiltInNode("sin",sin)
    env.put(sinFunc["name"], sinFunc)

    def sinh(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure sinh")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("sinh can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(sinh(first))
    sinhFunc = slang_parser.BuiltInNode("sinh",sinh)
    env.put(sinhFunc["name"], sinhFunc)
    

    def tan(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure tan")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("tan can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(tan(first))
    tanFunc = slang_parser.BuiltInNode("tan",tan)
    env.put(tanFunc["name"], tanFunc)
    
    def tanh(args):
        #Checking for arguments
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure tanh")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not((first['type'] == slang_parser.INT) and not(first['type'] == slang_parser.DBL))):
            raise SyntaxError ("tanh can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(tanh(first))
    tanhFunc = slang_parser.BuiltInNode("tanh",tanh)
    env.put(tanhFunc["name"], tanhFunc)

    def integerf (args):
        # Checking for args
        if(len(args) != 1):
           raise SyntaxError ("Wrong number of arguments passed into procedure integer?")
        # Type checking: make sure we only have int args and computing the return bool
        if(args[0]['type'] == slang_parser.INT):
            return slang_parser.BoolNode(True)
        else:
            return slang_parser.BoolNode(False)
    integerFunc = slang_parser.BuiltInNode("integer?",  integerf)
    env.put(integerFunc["name"], integerFunc)

    def doublef (args):
        # Checking for args
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure double?")
        # Type checking: make sure we only have dbl args and computing the return bool
        if(args[0]['type'] == slang_parser.DBL):
            return slang_parser.BoolNode(True)
        else:
            return slang_parser.BoolNode(False)
        
    doubleFunc = slang_parser.BuiltInNode("double?",  doublef)
    env.put(doubleFunc["name"], doubleFunc)

    def number (args):
        # Checking the args
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure number?")
        # Type checking: making sure we only have int and dbl args and computing the return bool
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            return slang_parser.BoolNode(True)
        else:
            return slang_parser.BoolNode(False)
        
    numberFunc = slang_parser.BuiltInNode("number?",  number)
    env.put(numberFunc["name"], numberFunc)

    def symbol (args):
        # Checking the args
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure symbol?")
        # Type checking: making sure we only have symbol args and computing the return bool
        if(args[0]['type'] == slang_parser.SYMBOL):
            return slang_parser.BoolNode(True)
        else:
            return  slang_parser.BoolNode(False)
    symbolFunc = slang_parser.BuiltInNode("symbol?", symbol)
    env.put(symbolFunc["name"], symbolFunc)

    def procedures (args):
        # Checking the args
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure procedure?")
        # Type checking: making sure we only have BuiltInFunc and LambdaVal args and computing the return bool
        if(args[0]['type'] == slang_parser.BUILTIN and args[0]['type'] ==  slang_parser.LAMBDAVAL):
            return slang_parser.BoolNode(True)
        else:
            return slang_parser.BoolNode(False)
        
    procedureFunc = slang_parser.BuiltInNode("procedure?", procedures)
    env.put(procedureFunc["name"], procedureFunc)

    #log10 func
    def log10 (args):
        log1 = 0
        #Checking the args
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure log10")
        #Type checking: making sure we only have int and dbl args
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            log1 = args[0]['val']
        else:
            raise SyntaxError ('log10 can only handle ints and dbls')
        #Computing the return dbl
        return slang_parser.DblNode(math.log10(log1))
    log10Func = slang_parser.BuiltInNode("log10", log10)
    env.put(log10Func["name"], log10Func)

    # loge func
    def loge (args):
        loge1 = 0
        #Checking the args
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure loge")
        #Type checking: making sure we only have int and dbl args
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            loge1 = args[0]['val']
        else:
            raise SyntaxError ('loge can only handle ints and dbls')
        #Computing the return dbl
        return slang_parser.DblNode(math.log(loge1))
    logeFunc = slang_parser.BuiltInNode("loge", loge)
    env.put(logeFunc["name"], logeFunc)
   
    # pow func
    def pows(args): 
        baseNum = 0
        expNum = 0
        #Checking the args
        if(len(args) != 2):
            raise SyntaxError ("Wrong number of arguments passed into procedure pow")
        
        # Type checking: making sure we only have int and dbl args
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            baseNum = args[0]['val']
            expNum = args[1]['val']
        else:
            raise SyntaxError("pow can only handle int and dbls arguments")
        return slang_parser.DblNode(math.pow(baseNum,expNum))
    powFunc = slang_parser.BuiltInNode("pow", pows)
    env.put(powFunc["name"], powFunc)
 
    # Not Func
    def notf(args):
            #Checking the args
            if(len(args) != 1):
                raise SyntaxError ("Wrong number of arguments passed into procedure not")
            # Type checking: making sure we only have bool args and computing the return bool
            if(not(args[0]['type'] == slang_parser.BOOL)):
                return slang_parser.BoolNode(False)
            if(args[0]['val'] == False):
                return slang_parser.BoolNode(True)
        
            return slang_parser.BoolNode(False)
    notFunc = slang_parser.BuiltInNode("not", notf)
    env.put(notFunc["name"],notFunc)
    # Int To Dbl Func
    def intToDbl(args):
            # Checking the args
            if(len(args) != 1):
                raise SyntaxError ("Wrong number of arguments passed into procedure int->double")
            # Type checking: making sure we only have int args 
            if(not(args[0]['type'] == slang_parser.INT)):
                raise SyntaxError("integer->double can only handle Int arguments")
            # Computing the return dbl
            output = args[0]['val']
            return slang_parser.DblNode(output)
    intToDblFunc = slang_parser.BuiltInNode("integer->double", intToDbl)
    env.put(intToDblFunc["name"],intToDblFunc)
    
    # Dbl to Int function
    def dblToInt(args):
        if(len(args) != 1):
            raise SyntaxError ("Wrong number of arguments passed into procedure double->int")
        # Type checking: Making sure we only have dbl args
        if(not(args[0]['type'] == slang_parser.DBL)):
            raise SyntaxError("double->integer can only handle Dbl arguments")
        return slang_parser.IntNode(int(args[0]['val']))
    dblToIntFunc = slang_parser.BuiltInNode("double->integer", dblToInt)
    env.put(dblToIntFunc["name"],dblToIntFunc)
    # null check function
    def nullCheck(args):
        # checking the args
        if (len(args) != 1):
            raise ("Wrong number of arguments passed into procedure null?")
        # computing the return bool
        if (args.get(0) == slang_parser.ConsNode(None, None)):
            return slang_parser.BoolNode(True)
        return slang_parser.BoolNode(False)
    # Build Null Check Built-In Node and put it into environment
    nullCheckFunc = slang_parser.BuiltInNode("null?", nullCheck)
    env.put(nullCheckFunc["name"],nullCheck)
    
def addListFuncs(env):
    """Add standard list functions to the given environment"""
    def car(args):
        # Type checking: make sure we only have Nodes.Cons
        consCount = 0
        # get count for number of Nodes.Cons arguments
        for arg in args:
            if arg['type'] == slang_parser.CONS:
                consCount+=1
        # if arg.size() > ConsCount there are arguments that are not Cons Nodes. Throw error
        if len(args) > consCount:
            raise SyntaxError("car can only handle Cons arguments")
        # Semantic analysis: make sure there is only one argument
        if len(args) != 1:
            raise SyntaxError("Wrong number of arguments in car. Can only handle 1")
        # Compute the first  lis item and return it.
        return slang_parser.ConsNode(args[0]["car"])
    # Build Null Check Built-In Node and put it into environment
    carFunc = slang_parser.BuiltInNode("car", car)
    env.put(carFunc["name"],carFunc)
        
    def cdr(args):
        # Type checking: make sure we only have Nodes.Cons
        consCount = 0
        # get count for number of Nodes.Cons arguments
        for arg in args:
            if arg['type'] == slang_parser.CONS:
                consCount+=1
        # if arg.size() > ConsCount there are arguments that are not Cons Nodes. Throw error
        if len(args) > consCount:
            raise SyntaxError("cdr can only handle Cons arguments")
        # Semantic analysis: make sure there is only one argument
        if len(args) != 1:
            raise SyntaxError("Wrong number of arguments in cdr. Can only handle 1")
        # Compute the first  lis item and return it.
        return slang_parser.ConsNode(args[0]["cdr"])
    # Build Null Check Built-In Node and put it into environment
    cdrFunc = slang_parser.BuiltInNode("cdr", cdr)
    env.put(cdrFunc["name"],cdrFunc)

    def cons(args):
        # Semantic analysis: make sure there is only two argument
        if len(args) != 2:
            raise SyntaxError("Wrong number of arguments in cons. Can only handle 2")
        # Compute the first  lis item and return it.
        return slang_parser.ConsNode(args[0], args[1])
    # Build Null Check Built-In Node and put it into environment
    consFunc = slang_parser.BuiltInNode("cons", cons)
    env.put(consFunc["name"],consFunc)

    def list(args):
        # Semantic analysis: make sure there is only one argument
        if len(args) < 0:
            raise SyntaxError("Wrong number of arguments in list. Can only handle 0 or more")
        elif len(args) == 0:
            return
        else:
            values = []
            for arg in args:
                values.append(arg)                
        # Compute the first  lis item and return it.
        return slang_parser.ConsNode(values, None)
    # Build Null Check Built-In Node and put it into environment
    listFunc = slang_parser.BuiltInNode("list", list)
    env.put(listFunc["name"],listFunc)

    def listCheck (args):
    # Semantic analysis: make sure there are arguments!
        if len(args) != 1:
            raise SyntaxError("Wrong number of arguments in list?. Can only handle 1 argument");
    # Type checking: make sure we only have a Cons Node 
    # Compute true or false based on whether or not we have a Cons Node
        if(args[0]['type'] == slang_parser.CONS):
            return slang_parser.BoolNode(True)
        return slang_parser.BoolNode(False)
    
    # put list? function into the env
    listCFunc = slang_parser.BuiltInNode("list?", listCheck)
    env.put(listCFunc["name"],listCFunc)

    def setcar (args):
        # Semantic analysis: make sure there are only 2 arguments
        if (len(args) != 2):
            raise SyntaxError("Wrong number of arguments in set-cdr!. Can only handle 2 arguments. A Cons argument followed by a value");
        # Type checking: make sure our first argument is a Cons Node
        if (not(args[0]['type'] == slang_parser.CONS)):
            raise SyntaxError("First argument must be a Cons. Pair Expected")
        # Compute and update the ConsNode's car
        args[0]['car'] = args.get(1)
        # return null because we are just updating
        return None
    # put set-car! into env
    setcarFunc = slang_parser.BuiltInNode("set-car!", setcar)
    env.put(setcarFunc["name"],setcarFunc)
    
    # set-cdr! function
    def setcdr (args):
        # Semantic analysis: make sure there are only 2 arguments
        if (len(args) != 2):
            raise SyntaxError("Wrong number of arguments in set-cdr!. Can only handle 2 arguments. A Cons argument followed by a value")
            # Type checking: make sure our first argument is a Nodes.Cons
        if (not(args[0]['type'] == slang_parser.CONS )):
            raise SyntaxError("First argument must be a Cons. Pair Expected")
        
        # Compute and update the Cons Node's cdr
        args[0]['cdr'] = args[1]
        # return null because we are just updating
        return None
    # put set-cdr! function into hashmap
    setcdrFunc = slang_parser.BuiltInNode("set-cdr!", setcdr)
    env.put(setcdrFunc["name"],setcdrFunc)

        


def addStringFuncs(env):
    def stringappend(args):
        # Type checking: make sure we only have Str arguments
        # get count for number of strings
        strCount = 0
        for arg in args:
            if arg['type'] == slang_parser.STR:
                strCount+=1
        # if len(args) is greater than strCount than there is an argument that is not a Str
        if len(args) > strCount:
            raise SyntaxError("+ can only handle String arguments");
        # Semantic analysis: make sure there are arguments!
        if len(args) != 2:
            raise SyntaxError("Wrong number of arguments in string-append. Can only handle 2")
        # Computing the return str
        result = args[0]["val"] + args[1]["val"]
        return slang_parser.StrNode(result)
    strAppFunc = slang_parser.BuiltInNode("string-append", stringappend)
    env.put(strAppFunc["name"],strAppFunc)
        
    def stringlength(args):
        # Type checking: make sure we only have Str arguments
        # get count for number of strings
        strCount = 0
        for arg in args:
            if arg['type'] == slang_parser.STR:
                strCount+=1
        # if len(args) is greater than strCount than there is an argument that is not a Str
        if len(args) > strCount:
            raise SyntaxError("+ can only handle String arguments");
        # Semantic analysis: make sure there are arguments!
        if len(args) != 1:
            raise SyntaxError("Wrong number of arguments in string-length. Can only handle 1")
        # Computing the return str
        result = len(args[0]["val"])
        return slang_parser.IntNode(result)
    strLengthFunc = slang_parser.BuiltInNode("string-length", stringlength)
    env.put(strLengthFunc["name"],strLengthFunc)

    def substring(args):
        # Semantic analysis: make sure there are arguments!
        if len(args) != 3:
            raise SyntaxError("Wrong number of arguments in substring. Can only handle 3")
        if not(args[0]['type'] == slang_parser.STR and args[1]['type'] == slang_parser.INT and args[2]['type'] == slang_parser.INT):
                raise SyntaxError("first index argument must be a Str, second and third arguments but Ints.")
        # Computing the return str
        strs = args[0]["val"]
        index = args[1]["val"]
        index2 = args[2]["val"]
        #Checking if the indexes are valid
        if(index >=  len(strs) or index < 0):
            raise SyntaxError ("substring Argument 2 out of range")
        if(index2 >=  len(strs) or index2 < 0):
            raise SyntaxError ("substring Argument 3 out of range")
        result = strs[index:index2]
        return slang_parser.StrNode(result)
    substringFunc = slang_parser.BuiltInNode("substring", substring)
    env.put(substringFunc["name"],substringFunc)
    
    def stringq (args):
            # Semantic analysis: make sure there are arguments!
            if (len(args) != 1):
                raise SyntaxError("Wrong number of arguments in string?. Can only handle 1")
            # Type checking: make sure we only have str arguments and computing the return bool
            if(args[0]['type'] == slang_parser.STR):
                return slang_parser.BoolNode(True)
            
            return slang_parser.BoolNode(False)
    # put string? func into env
    stringqFunc = slang_parser.BuiltInNode("string?", stringq)
    env.put(stringqFunc["name"],stringqFunc)
    
    def stringref (args):
            # Semantic analysis: make sure there are arguments!
            if (len(args) != 2):
                raise SyntaxError("Wrong number of arguments in substring. Can only handle 2")
            # Type checking: make sure we only have str and int arguments.
            if(not((args[0]['type'] == slang_parser.STR) and (args[1]['type'] == slang_parser.INT))):
                raise SyntaxError("First argument must be a Str, second argument must be an Int.")
            
            # Computing the arg values
            str = args[0]['val']
            index = args[1]['val']
            # Computing the string-ref char at the given index
            if(index >=  str.length()  or index < 0):
                raise SyntaxError ("string-ref Argument 2 out of range")
            charoutput = str.charAt(index)
            return slang_parser.CharNode(charoutput)
    stringrefFunc = slang_parser.BuiltInNode("string-ref", stringref)
    env.put(stringrefFunc["name"],stringrefFunc)   
       
    def stringmake (args):
        charCount = 0
        # get count for number of char args
        for arg in args :
            if (arg['type'] == slang_parser.CHAR):
                charCount += 1
        
        #Type checking: making sure that we only have char args 
        if (len(args) > (charCount)):
            raise SyntaxError("+ can only handle String arguments");
        # Return empty str node if no args
        if (len(args) == 0 ):
            return slang_parser.StrNode("")
        # Compute the return str
        result = ""
        for arg in args:
            result += arg['val']
        
        return slang_parser(result)  
    # put str function into env
    stringmakeFunc = slang_parser.BuiltInNode("string", stringmake)
    env.put(stringmakeFunc["name"],stringmakeFunc)

def addVectorFuncs(env):
    def vecLength(args):
        # Type checking: make sure we only have vec arguments. 
        vecCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.VEC:
                vecCount+=1
        # if arg size is greater than vecCount that means there must be a argument in the list thats not an argument
        # throw an error because vector-length can only handle vector arguments
        if len(args) > vecCount:
            raise SyntaxError("vector-length can only handle vector arguments")
        # Semantic analysis: only one argument is allwed for vector-length
        if len(args) != 1:
            raise SyntaxError("Wrong number of arguments in vector-length. Can only handle 1")
        # Compute, making sure to know the return type which an integer
        length = len(args[0]["items"])
        # get length of vector which is the length of items array in the node
        # return an  Int Node created with the length of the vector
        return slang_parser.IntNode(length)
    # put the vectorlength function into the env
    vecLenFunc = slang_parser.BuiltInNode("vector-length", vecLength)
    env.put(vecLenFunc["name"],vecLenFunc)
    
    def vecget(args):
        # get count for number of int and dbl args
        vecCount = 0
        print(args)
        for arg in args:
            if arg['type'] == slang_parser.VEC:
                vecCount+=1
        # if arg size is greater than vecCount that means there must be a argument in the list thats not an argument
        # throw an error because vector-length can only handle vector arguments
        if len(args) > vecCount:
            raise SyntaxError("vector-get can only handle vector arguments");
        # Semantic analysis: only one argument is allwed for vector-length
        if len(args) != 2:
            raise SyntaxError("Wrong number of arguments in vector-get. Can only handle 2")
        if not(args[0]['type'] == slang_parser.VEC and args[1]['type'] == slang_parser.INT):
            raise SyntaxError("Wrong argument types. vector-get only handles first argument must be a Vec, second argument must be an Int.")
        # Compute, making sure to know the return type which an integer
        index = args[1]["val"]
        if index >= len(args[0]["items"] or index < 0):
            raise SyntaxError ("vec-get Argument 2 out of range")
        return slang_parser.VecNode(args[0]["items"][index])
    # put the vectorlength function into the env
    vecgetFunc = slang_parser.BuiltInNode("vector-get", vecget)
    env.put(vecgetFunc["name"],vecgetFunc)

    def vecset(args):
        # get count for number of int and dbl args
        vecCount = 0
        for arg in args:
            if arg['type'] == slang_parser.VEC:
                vecCount+=1
        # if arg size is greater than vecCount that means there must be a argument in the list thats not an argument
        # throw an error because vector-length can only handle vector arguments
        if len(args) > vecCount:
            raise SyntaxError("vector-set can only handle vector arguments");
        # Semantic analysis: only one argument is allwed for vector-length
        if len(args) != 3:
            raise SyntaxError("Wrong number of arguments in vector-set. Can only handle 3")
        if not(arg[0]['type'] ==slang_parser.VEC and arg[1]['type'] == slang_parser.INT):
            raise SyntaxError("Wrong argument types. vector-get only handles first argument must be a Vec, second argument must be an Int.")
        # Compute, making sure to know the return type which an integer
        index = args[1]["val"]
        if index >= len(args[0]["items"] or index < 0):
            raise SyntaxError ("vec-get Argument 2 out of range")
        args[0]["items"][index] = args[2]
        return None
    # put the vectorset function into the env
    vecsetFunc = slang_parser.BuiltInNode("vector-set", vecset)
    env.put(vecsetFunc["name"],vecsetFunc)
    
    def vec (args):
            newVector = []
            # computing the vector from the given args
            for arg in args:
                newVector.append(arg)
            return slang_parser.VecNode(newVector)
    # put the vector function into the env
    vecFunc = slang_parser.BuiltInNode("vector", vec)
    env.put(vecFunc["name"],vecFunc)   
  
        
    # vec? function
    def vecq (args):
        #Semantic analysis: make sure there is only one argument
        if (len(args) != 1):
            raise SyntaxError("Wrong number of arguments in vec?. Can only handle 1")
        # Type checking: the argument is a vector and computing boolean to return.
        if(args[0]['type'] == slang_parser.VEC):
            return slang_parser.BoolNode(True)
        
        return slang_parser.BoolNode(False)
    vecqFunc = slang_parser.BuiltInNode("vec?", vecq)
    env.put(vecqFunc["name"],vecqFunc)

    # make-fector  func
    def vecmake (args):
        # Semantic analysis: make sure there is only one argument
        if (len(args) != 1):
            raise SyntaxError("Wrong number of arguments in vec?. Can only handle 1")
        #  Type checking: the argument must be an int node.
        if(not((args[0]['type'] == slang_parser.INT))):
            raise SyntaxError("Wrong argument types. make-vector only handles first argument as an Int.");
        
        # Computing new vector with size given by the arg and contains only poundFs.
        size = args[0]['val']
        newVector = []
        i = 0
        while i < size:
            newVector.append(slang_parser.BoolNode(False))
            i += 1
        
        return slang_parser.VecNode(newVector)

    # put vec-make into env
    vecmakeFunc = slang_parser.BuiltInNode("makle-vector", vecmake)
    env.put(vecmakeFunc["name"],vecmakeFunc)
  
