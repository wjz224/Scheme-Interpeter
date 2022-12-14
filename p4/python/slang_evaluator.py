import slang_parser
import slang_env

def evaluate(expr, env):
    """Evaluate is responsible for visiting an expression and producing a
    value"""
    def ExprEvaluator(env):
        return env

    def visitIdentifier(expr):
        if(env.get(expr['name']) != None):
            return env.get(expr["name"])
        else:
            raise Exception ("No value for identifier" + expr['name'])


    def visitDefine(expr):
        env.put(expr["id"]['name'], (evaluate(expr["expr"], env)))
        return env.get(expr["id"]["name"])

    def visitBool(expr):
        #return IValue
        return expr

    def visitInt(expr):
        #return IValue
        return expr

    def visitDbl(expr):
        #return IValue
        return expr

    def visitLambdaVal(expr):
        #return IValue
        return expr

    def visitLambdaDef(expr):
        #return a LambdaVal IValue
        return slang_parser.LambdaValNode(env, expr)

    def visitIf(expr):
        #Visiting every expr condition and checking values
        if((evaluate(expr["cond"],env) == slang_parser.BoolNode(True))):
            return evaluate(expr["true"],env)
        return evaluate(expr["false"],env)

    def visitSet(expr):
        #Visiting and updating each identifier name
        if(expr["name"] != None):
            env.update(expr["id"], evaluate(expr["expr"],env))
        else:
            raise SyntaxError("Invalid identifier name")
        return None

    def visitAnd(expr):
        #Visiting and checking each expr expression and checking values
        for i in expr['exprs']:
            if(evaluate(i,env) == slang_parser.BoolNode(False)):
                return slang_parser.BoolNode(False)
        return slang_parser.BoolNode(True)       
    def visitOr(expr):
        for i in expr['exprs']:
            if(evaluate(i,env) == slang_parser.BoolNode(True) or evaluate(i,env)['type'] != slang_parser.BOOL):
                return slang_parser.BoolNode(True)
        return slang_parser.BoolNode(False)

    def visitBegin(expr):
        #Visiting and checking for each expr expression and then returning the last visited expression
        for i in expr["exprs"][:-1]:
            evaluate(i,env)
        return evaluate(expr["exprs"][-1], env)
    def visitApply(expr):
        #Creating a new List<IValue> and grabbing the first IValue in expr expressions
        args = []
        # get the lambda/built in associated with the identifie
        firstValue = (evaluate(expr["exprs"][0],env))
        #Checking for a Nodes.BuiltInFunc
    
        if(firstValue["type"] == slang_parser.BUILTIN):
            #Visiting each expression in expr and saving it into a list of arguments passing the args that will be used to execute a built-in-func.
            args = []
            for i in expr["exprs"][1:]:
                args.append(evaluate(i,env))
            return firstValue['func'](args)
        #Else not a BuiltInFunc
        elif(firstValue['type'] == slang_parser.LAMBDAVAL):
            #If the number of formals matches the number of arguments
            if((len(firstValue['lambda']['formals']) + 1) == len(expr['exprs'])):
                #Make an inner scope Env
                tempEnv = slang_env.makeInnerEnv(firstValue['env'])
                #Iterate through the arguments and pass it into the inner Env
                i = 1
                while i < len(expr['exprs']):
                    tempEnv.put(firstValue['lambda']['formals'][i-1]['name'], evaluate(expr['exprs'][i],env))
                    i += 1
                #Visit each value
                lambda_visitor = ExprEvaluator(tempEnv)
                j = 0
                while j < len(firstValue['lambda']['exprs']) - 1:
                    evaluate(firstValue['lambda']['exprs'][j],lambda_visitor)
                    j += 1
                
                #Return the last value
                return evaluate(firstValue ['lambda']['exprs'][len(firstValue ['lambda']['exprs'])- 1] , lambda_visitor)
            else:
                raise SyntaxError("Number of Formals does not Equal arguments")
            ""
        return None
        

    def visitCons(expr):
        return expr
    def visitVec(expr):
        return expr
    def visitSymbol(expr):
        return expr
    def visitQuote(expr):
        return expr['datum']
    def visitTick(expr):
        return expr['datum']
    def visitChar(expr):
        return expr
    def visitStr(expr):
        return expr
    def visitBuiltInFunc(expr):
        return expr     
    def visitCond(expr):
        i = 0
        # Visiting and checking each condition and then evaluating the visitied values
        while i < (len(expr['conditions'])):
            if(evaluate(expr['conditions'][i]['test'],env) == slang_parser.BoolNode(True) or not((evaluate(expr['conditions'][i]['test'], env))['type'] == slang_parser.BOOL)):
                return evaluate(slang_parser.BeginNode(expr['conditions'][i]['exprs']),env)
            i += 1
        return slang_parser.BoolNode(False)
    if(expr['type'] == slang_parser.INT):
        return visitInt(expr)
    elif(expr['type'] == slang_parser.BEGIN):
        return visitBegin(expr)
    elif(expr['type'] == slang_parser.BOOL):
        return visitBool(expr)
    elif(expr['type'] == slang_parser.CHAR):
        return visitChar(expr)
    elif(expr['type'] == slang_parser.CONS):
        return visitCons(expr)
    elif(expr['type'] == slang_parser.DBL):
        return visitDbl(expr)
    elif(expr['type'] == slang_parser.OR):
        return visitOr(expr)
    elif(expr['type'] == slang_parser.AND):
        return visitAnd(expr)
    elif(expr['type'] == slang_parser.VEC):
        return visitVec(expr)
    elif(expr['type'] == slang_parser.APPLY):
        return visitApply(expr)
    elif(expr['type'] == slang_parser.BUILTIN):
        return visitBuiltInFunc(expr)
    elif(expr['type'] == slang_parser.COND):
        return visitCond(expr)
    elif(expr['type'] == slang_parser.DEFINE):
        return visitDefine(expr)
    elif(expr['type'] == slang_parser.IDENTIFIER):
        return visitIdentifier(expr)
    elif(expr['type'] == slang_parser.IF):
        return visitIf(expr)
    elif(expr['type'] == slang_parser.LAMBDADEF):
        return visitLambdaDef(expr)
    elif(expr['type'] == slang_parser.LAMBDAVAL):
        return visitLambdaVal(expr)
    elif(expr['type'] == slang_parser.QUOTE):
        return visitQuote(expr)
    elif(expr['type'] == slang_parser.SET):
        return visitSet(expr)
    elif(expr['type'] == slang_parser.STR):
        return visitStr(expr)
    elif(expr['type'] == slang_parser.SYMBOL):
        return visitSymbol(expr)
    elif(expr['type'] == slang_parser.TICK):
        return visitTick(expr)
  
    
    
