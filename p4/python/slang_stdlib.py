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
            raise Exception("+ can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise Exception("+ expects at least one argument")
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
            raise Exception("- can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise Exception("- expects at least one argument")
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
            raise Exception("* can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise Exception("* expects at least one argument")
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
            raise Exception("/ can only handle Int and Dbl arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise Exception("/ expects at least one argument")
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
            raise Exception ("Wrong number of arguments passed into procedure %")
        intCount = 0
        # get count for number of int and dbl args
        for arg in args:
            if arg['type'] == slang_parser.INT:
                intCount+=1
        # if len(args) is greater than int counts, throw an error because modulo can only handle int arguments.
        if (len(args) > (intCount)):
            raise Exception("% can only handle Int arguments")
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
            raise Exception ("Wrong number of arguments passed into procedure ==")
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
            raise Exception("== can only handle Int, Dbl, and Bool arguments")
        # Semantic analysis: make sure there are arguments!
        if (len(args) == 0):
            raise Exception("== expects two arguments")
        # Compute, making sure to know the return type
        first = args[0]['val']
        second = args[1]['val']
        # check if arg 0 and arg 1 are both numbers that can be compared.
        if(((isinstance(first, slang_parser.INT) or isinstance(first, slang_parser.DBL)) and (isinstance(second, slang_parser.INT) or isinstance(second, slang_parser.DBL)))):
            if(first == second):
                return slang_parser.BoolNode(True)
            else:
                return slang_parser.BoolNode(False)
        # check if arg 0 and arg are both booleans that can be compared
        elif(isinstance(first, slang_parser.BOOL) and isinstance(first, slang_parser.BOOL)):
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
            raise Exception ("Wrong number of arguments passed into procedure >")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("> can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not(isinstance(arg, slang_parser.INT) and not(isinstance(arg, slang_parser.DBL)))):
                raise Exception ("> can only handle Int and Dbl arguments")
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
            raise Exception ("Wrong number of arguments passed into procedure >=")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception (">= can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not(isinstance(arg, slang_parser.INT) and not(isinstance(arg, slang_parser.DBL)))):
                raise Exception (">= can only handle Int and Dbl arguments")
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
            raise Exception ("Wrong number of arguments passed into procedure <")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("< can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not(isinstance(arg, slang_parser.INT) and not(isinstance(arg, slang_parser.DBL)))):
                raise Exception ("< can only handle Int and Dbl arguments")
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
            raise Exception ("Wrong number of arguments passed into procedure <=")
        first = args[0]['val']
        #Type checking: make sure we only have int and dbl arguments
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("<= can only handle Int and Dbl arguments")
        #Computing the return bool
        for arg in args[1:]:
            if(not(isinstance(arg, slang_parser.INT) and not(isinstance(arg, slang_parser.DBL)))):
                raise Exception ("<= can only handle Int and Dbl arguments")
            if(arg['val'] <= first):
                return slang_parser.BoolNode(False)
            else:
                first = ab
        return slang_parser.BoolNode(True)
    lessEFunc = slang_parser.BuiltInNode("<",lessequal)
    env.put(lessEFunc["name"], lessEFunc)

    def abs(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure abs")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("abs can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.IntNode(abs(first))
    absFunc = slang_parser.BuiltInNode("abs",abs)
    env.put(absFunc["name"], absFunc)

    def sqrt(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure sqrt")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("sqrt can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.IntNode(sqrt(first))
    sqrtFunc = slang_parser.BuiltInNode("sqrt",sqrt)
    env.put(sqrtFunc["name"], sqrtFunc)

    def acos(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure acos")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("acos can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.IntNode(acos(first))
    acosFunc = slang_parser.BuiltInNode("acos",acos)
    env.put(acosFunc["name"], acosFunc)

    def asin(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure asin")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("asin can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(asin(first))
    asinFunc = slang_parser.BuiltInNode("asin",asin)
    env.put(asinFunc["name"], asinFunc)

    def atan(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure atan")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("atan can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(atan(first))
    atanFunc = slang_parser.BuiltInNode("atan",atan)
    env.put(atanFunc["name"], atanFunc)
    
    def cos(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure cos")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("cos can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(cos(first))
    cosFunc = slang_parser.BuiltInNode("cos",cos)
    env.put(cosFunc["name"], cosFunc)

    def cosh(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure cosh")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("cosh can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(cosh(first))
    coshFunc = slang_parser.BuiltInNode("cosh",cosh)
    env.put(coshFunc["name"], coshFunc)

    def sin(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure sin")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("sin can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(sin(first))
    sinFunc = slang_parser.BuiltInNode("sin",sin)
    env.put(sinFunc["name"], sinFunc)

    def sinh(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure sinh")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("sinh can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(sinh(first))
    sinhFunc = slang_parser.BuiltInNode("sinh",sinh)
    env.put(sinhFunc["name"], sinhFunc)
    

    def tan(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure tan")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("tan can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(tan(first))
    tanFunc = slang_parser.BuiltInNode("tan",tan)
    env.put(tanFunc["name"], tanFunc)
    
    def tanh(args):
        #Checking for arguments
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure tanh")
        #Type checking: make sure we only have int and dbl arguments
        first = args[0]['val']
        if(not(isinstance(first, slang_parser.INT) and not(isinstance(first, slang_parser.DBL)))):
            raise Exception ("tanh can only handle Int and Dbl arguments")
        #Computing the return dbl 
        return slang_parser.DblNode(tanh(first))
    tanhFunc = slang_parser.BuiltInNode("tanh",tanh)
    env.put(tanhFunc["name"], tanhFunc)

    def integerf (args):
        # Checking for args
        if(len(args) != 1):
           raise Exception ("Wrong number of arguments passed into procedure integer?")
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
            raise Exception ("Wrong number of arguments passed into procedure double?")
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
            raise Exception ("Wrong number of arguments passed into procedure number?")
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
            raise Exception ("Wrong number of arguments passed into procedure symbol?")
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
            raise Exception ("Wrong number of arguments passed into procedure procedure?")
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
            raise Exception ("Wrong number of arguments passed into procedure log10")
        #Type checking: making sure we only have int and dbl args
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            log1 = args[0]['val']
        else:
            raise Exception ('log10 can only handle ints and dbls')
        #Computing the return dbl
        return slang_parser.DblNode(math.log10(log1))
    log10Func = slang_parser.BuiltInNode("log10", log10)
    env.put(log10Func["name"], log10Func)

    # loge func
    def loge (args):
        loge1 = 0
        #Checking the args
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure loge")
        #Type checking: making sure we only have int and dbl args
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            loge1 = args[0]['val']
        else:
            raise Exception ('loge can only handle ints and dbls')
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
            raise Exception ("Wrong number of arguments passed into procedure pow")
        
        # Type checking: making sure we only have int and dbl args
        if(args[0]['type'] == slang_parser.INT and args[0]['type'] == slang_parser.DBL):
            baseNum = args[0]['val']
            expNum = args[1]['val']
        else:
            raise Exception("pow can only handle int and dbls arguments")
        return slang_parser.DblNode(math.pow(baseNum,expNum))
    powFunc = slang_parser.BuiltInNode("pow", pows)
    env.put(powFunc["name"], powFunc)
 
    # Not Func
    def notf(args):
            #Checking the args
            if(len(args) != 1):
                raise Exception ("Wrong number of arguments passed into procedure not")
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
                raise Exception ("Wrong number of arguments passed into procedure int->double")
            # Type checking: making sure we only have int args 
            if(not(args[0]['type'] == slang_parser.INT)):
                raise Exception("integer->double can only handle Int arguments")
            # Computing the return dbl
            output = args[0]['val']
            return slang_parser.DblNode(output)
    intToDblFunc = slang_parser.BuiltInNode("integer->double", intToDbl)
    env.put(intToDblFunc["name"],intToDblFunc)
    
    # Dbl to Int function
    def dblToInt(args):
        if(len(args) != 1):
            raise Exception ("Wrong number of arguments passed into procedure double->int")
        # Type checking: Making sure we only have dbl args
        if(not(args[0]['type'] == slang_parser.DBL)):
            raise Exception("double->integer can only handle Dbl arguments")
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
    pass    
    # Build Null Check Built-In Node and put it into environment
    nullCheckFunc = slang_parser.BuiltInNode("null?", nullCheck)
    env.put(nullCheckFunc["name"],nullCheck)
def addListFuncs(env):
    """Add standard list functions to the given environment"""
    pass


def addStringFuncs(env):
    """Add standard string functions to the given environment"""
    pass


def addVectorFuncs(env):
    """Add standard vector functions to the given environment"""
    pass
