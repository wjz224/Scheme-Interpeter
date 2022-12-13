import slang_parser
import slang_env

def evaluate(expr, env):
    """Evaluate is responsible for visiting an expression and producing a
    value"""
    print("HELLO")
    if(expr['type'] == slang_parser.INT):
        return visitBool(expr)
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
    
    def ExprEvaluator(self,env):
        self.env = env
    

    def visitIdentifier(self,expr):
        return env.get(expr["name"])
    
    
    def visitDefine(self, expr):
        self.env.put(self, expr["id"], expr["expr"])
        return None
    
    def visitBool(self, expr):
        #return IValue
        return env.get(expr["val"])
    
    def visitInt(self, expr):
        #return IValue
        return expr

    def visitDbl(self, expr):
        #return IValue
        return expr
    
    def visitLambdaVal(self, expr):
        #return IValue
       return expr
    
    def visitLambdaDef(self, expr):
        #return a LambdaVal IValue
        return slang_parser.LambdaDefNode(self.env, expr)

    def visitIf(self, expr):
        #Visiting every expr condition and checking values
        if(env.get(expr["c"] == slang_parser.BoolNode(True))):
            return slang_parser.BoolNode(True)
        return slang_parser.BoolNode(False)
    
    def visitSet(self, expr):
        #Visiting and updating each identifier name
        if(expr["name"] != None):
            env.update(expr["id"], self.evaluate(expr["expr"]))
        else:
            raise Exception("Invalid identifier name")
        return None

    def visitAnd(self, expr):
        #Visiting and checking each expr expression and checking values
        for i in expr['exprs']:
            if(evaluate(i) == slang_parser.BoolNode(False)):
                return slang_parser.BoolNode(False)
        return slang_parser.BoolNode(True)       
    def visitOr(self, expr):
        for i in expr['exprs']:
            if(evaluate(i) == slang_parser.BoolNode(True) or evaluate(i)['type'] != slang_parser.BOOL):
                return slang_parser.BoolNode(True)
        return slang_parser.BoolNode(False)

    def visitBegin(self, expr):
        #Visiting and checking for each expr expression and then returning the last visited expression
        for i in expr["exprs"][:-1]:
            evaluate(i)
        return evaluate(expr["exprs"][-1])
    def visitApply(self, expr):
        #Creating a new List<IValue> and grabbing the first IValue in expr expressions
        '''
        args = []
        firstValue = evaluate(expr["expr"][0])
        #Checking for a Nodes.BuiltInFunc
        if(firstValue["type"] == slang_parser.BuiltInNode):
            #Visiting each expression in expr and saving it into a list of arguments passing the args that will be used to execute a built-in-func. 
            args = []
            for i in expr["exprs"][1:-1]:
                args.append(evaluate(i))
            
            return firstValue['func'](args)
        #Else not a BuiltInFunc
        elif(isinstance(firstValue, slang_parser.LambdaValNode)):
            #If the number of formals matches the number of arguments
            if(((Nodes.LambdaVal) firstValue).lambda.formals.size() + 1 == expr.expressions.size()){
                //Make an inner scope Env
                Nodes.LambdaVal lv = (Nodes.LambdaVal) firstValue;
                Env tempEnv = Env.makeInner(lv.env);
                //Iterate through the arguments and pass it into the inner Env
                for(int i = 1; i < expr.expressions.size(); i++){
                    tempEnv.put(lv.lambda.formals.get(i - 1).name, expr.expressions.get(i).visitValue(this));
                }
                //Visit each value
                var lambda_visitor = new ExprEvaluator(tempEnv);
                for(int i = 0; i < lv.lambda.body.size() - 1; i++){
                    lv.lambda.body.get(i).visitValue(lambda_visitor);
                }
                //Return the last value
                return lv.lambda.body.get(lv.lambda.body.size() - 1).visitValue(lambda_visitor);
            }
            else{
                throw new Exception("Number of Formals does not Equal arguments");
            }
        }
        return null;
        pass
    '''
    def visitCons(self, expr):
        return expr
    def visitVec(self, expr):
        return expr
    def visitSymbol(self, expr):
        return expr
    def visitQuote(self,expr):
        return expr.get('datum')
    def visitTick(self, expr):
        return expr.get('datum')
    def visitChar(self, expr):
        return expr
    def visitStr(self, expr):
        return expr
    def visitBuiltInFunc(self,expr):
        return expr     
    def visitCond(self,expr):
        #for i in expr['conditions']:
            #if(evaluate(i) == slang_parser.BoolNode(True) or evaluate(i)['type'] != slang_parser.BOOL):
        pass