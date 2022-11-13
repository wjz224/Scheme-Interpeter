import slang_scanner
import slang_evaluator
# [CSE 262] You will probably find it tedious to create a whole class hierarchy
# for your Python parser.  Instead, consider whether each node type could just
# be a hash table.  In that case, you could have a function for "constructing"
# each "type", by putting some values into a hash table.

ANDNODE, APPLYNODE, BEGINNODE, BOOLNODE, BUILTINNODE, CHARNODE, CONDNODE, CONSNODE, DBLNODE, DEFINENODE, IDENTIFIERNODE, IFNODE, INTNODE, LAMBDADEFNODE, LAMBDAVALNODE, ORNODE, QUOTENODE, SETNODE, STRNODE, SYMBOLNODE, TICKNODE, VECNODE, LETNODE = range(
    0, 23)

# A poor-man's enum: each of our token types is just a number
ABBREV, AND, BEGIN, BOOL, CHAR, COND, DBL, DEFINE, EOFTOKEN, IDENTIFIER, IF, INT, LAMBDA, LEFT_PAREN, OR, QUOTE, RIGHT_PAREN, SET, STR, VECTOR = range(
    0, 20)
# Dictionary layout
"""
AndNode = {'type': "and",'expressions': None}
ApplyNode = {'type': "apply",'expressions': None}
BeginNode = {'type': "begin",'expressions': None}
BoolNode = {'type': "bool",'val': None}
BuiltInFuncNode = {'type': "builtInFunc", 'name': None, 'func': None}
CharNode = {'type': "char", 'val': None}
CondNode = {'type': 'cond', 'conditions': None}
Condition = {'type': 'condition', 'test': None, 'expressions': None}
ConsNode = {'type': 'cons', 'car': None, 'cdr': None }
DblNode = {'type': 'dbl', 'val': None}
DefineNode = {'type': 'define', 'identifier': None, 'expression': None}
IdentifierNode = {'type': 'define', 'name': None}
IfNode = {'type': 'if', 'cond': None, 'ifTrue': None, 'ifFalse': None}
IntNode = {'type': 'int', 'val': None}
LambdaDefNode = {'type': 'lambdaDefNode', 'formals': None, 'body': None}
LambdaValNode = {'type': 'lambdaValNode', 'env': None, 'lambda': None}
OrNode = {'type': "or",'expressions': None}
QuoteNode = {'type': "quote",'datum': None}
SetNode = {'type': "set",'identifier': None, 'expression': None}
StrNode = {'type': "str",'value': None}
SymbolNode = {'type': "symbol",'name': None}
TickNode = {'type': "tick",'datum': None}
VecNode = {'type': "vec",'items': None}
"""

def AndNode(expressions):
    return {'type': ANDNODE, 'expressions': expressions}
def ApplyNode(expressions):
    return {'type': APPLYNODE, 'expressions': expressions}
def BeginNode(expressions):
    return {'type': BEGINNODE, 'expressions': expressions}
def BoolNode(val):
    return {'type': BOOLNODE, 'val': val}
def BuiltInFuncNode(name, func):
    return {'type': BUILTINNODE, 'name': name, 'func': func}
def CharNode(val):
    return {'type': CHARNODE, 'val': val}
def Condition(test, conditions):
    return {'type': CONDNODE, 'tests': test, 'conditions': conditions}
def CondNode(conditions):
    return {'type': CONDNODE, 'conditions': conditions} 
def ConsNode(car, cdr):
    return {'type': CONSNODE, 'car': car, 'cdr':cdr}
def DblNode(val):
    return {'type': DBLNODE, 'val': val}
def DefineNode(identifier, expression):
    return {'type': DEFINENODE, 'identifier': identifier, 'expression': expression}
def IdentifierNode(name):
    return {'type': IDENTIFIERNODE, 'name': name}
def IfNode(cond, iftrue, iffalse):
    return {'type': IFNODE, 'cond': cond, 'iftrue': iftrue, 'iffalse': iffalse}
def IntNode(val):
     return {'type': INTNODE, 'val': val}
def LambdaDefNode(formals, body):
    return{'type': LAMBDADEFNODE, 'formals': formals, 'body': body}
def LambdaValNode(env, lambdaDef):
    return{'type': LAMBDAVALNODE, 'env': env, 'lambdaDef': lambdaDef}
def OrNode(expressions):
    return {'type': ORNODE, 'expressions': expressions}
def QuoteNode(datum):
    return {'type': QUOTENODE, 'datum': datum}
def SetNode(identifier, expression):
    return {'type': SETNODE, 'identifier': identifier, 'expression': expression}
def StrNode(value):
    return {'type': STRNODE, 'value': value}
def SymbolNode(name):
    return {'type': SYMBOLNODE, 'name': name}
def TickNode( datum):
    return {'type': TICKNODE, 'datum': datum}
def VecNode(items):
    return {'type': VECNODE, 'items': items}

    
class Parser:
    """The parser class is responsible for parsing a stream of tokens to produce
    an AST"""
   
    def program(self,tokens):
        # Create a List of Nodes.BaseNode that stores BaseNodes in the fashion of an AST
        AST = []
        # while loop that goes through all the tokens in the TokenStream and creates an AST
        while(tokens.nextToken().type != EOFTOKEN):
            #pass the TokenStream into form to start parsing
            input = self.form(tokens)
            #add the BaseNode to the List of BaseNodes
            AST.append(input)
        
        # return the list of Nodes.BaseNode
        return AST
    
    
     #Transform a stream of tokens into a Node
     #@param tokens a stream of tokens
     #@return A AstNode, because a Scheme program may have multiple
     #top-level expressions.
    def form(self,tokens):
        #Grabbing current token and the one ahead of it
        cur = tokens.nextToken()
        ahead = tokens.nextNextToken()
        #Checking for definition production
        if(cur.type == LEFT_PAREN and ahead.type == DEFINE):
            return self.definition(tokens)
        # else sent to expression production
        else:
            return self.expression(tokens)
            
    def definition(self, tokens):
        #Pop off the first two tokens that indicate a definition
        tokens.popToken()
        tokens.popToken()
        #Grabbing the next token in the stream
        cur = tokens.nextToken()
        # Checking for valid order of tokens
        if(not(cur.type == IDENTIFIER)):
            raise SyntaxError("Invalid definition")
        else:
            #Grab the identifier token and set it to a node
            iden = cur.tokenText
            # pop the identifier token
            tokens.popToken()
            #Create a define node with the identifier node and also a node from calling expressions
            define = DefineNode(iden, self.expression(tokens))
            #Updating current token
            cur = tokens.nextToken()
            #Checking for valid definition order
            if(cur.type ==  RIGHT_PAREN):
                #Popping off the right paren
                tokens.popToken()
                #returning a definition Node
                return define
            # throwing an SyntaxError for invalid definition form
            else:
                raise SyntaxError("Invalid definition")
    def expression(self, tokens):
        #Grabbing the next token and the nextnext token
        cur = tokens.nextToken()
        ahead = tokens.nextNextToken()
        #Checking for valid expression order
        if(cur.type ==  LEFT_PAREN):
            #Checking for valid Quote order
            if(ahead.type ==  QUOTE):
                #Popping off the two tokens that indicate a quote node
                tokens.popToken()
                tokens.popToken()
                #Setting a quote Node with datum(tokens) and casting to IValue
                quote = QuoteNode(self.datum(tokens))
                #Updating cur after a call to datum
                cur = tokens.nextToken()
                #Checking for valid Quote order
                if(cur.type == RIGHT_PAREN):
                    # pop the right paren
                    tokens.popToken()
                    #returning the quote node
                    return quote
                # if its not a right paren then its an invalid Quote Form.
                else:
                    raise SyntaxError("Invalid Expression")
            #Checking for valid Lambda order
            elif(ahead.type == LAMBDA):
                #Popping off the two tokens that indicate a lambda node
                tokens.popToken()
                tokens.popToken()
                #Setting a lambda Node with calls to formals(tokens) and body(tokens)
                lambdaNode = LambdaDefNode(self.formals(tokens), self.body(tokens))
                #updating cur after calls to formals and body
                cur = tokens.nextToken()
                #Checking for valid Lambda order
                if(cur.type == RIGHT_PAREN):
                    # pop the right paren
                    tokens.popToken()
                    #returning the lambda node
                    return lambdaNode
                #Else throw invalid order
                else:
                    raise SyntaxError("Invalid Lambda")
            #Checking for valid If order
            elif(ahead.type == IF):
                #Popping off the two tokens that indicate an if node
                tokens.popToken()
                tokens.popToken()
                #Setting a if node with calls to expression(tokens), expression(tokens), expression(tokens)
                ifNode = IfNode(self.expression(tokens), self.expression(tokens), self.expression(tokens))
                #Updating cur after calling expression(tokens), expression(tokens), expression(tokens)
                cur = tokens.nextToken()
                #Checking for valid if order
                if(cur.type == RIGHT_PAREN):
                    # pop the right paren
                    tokens.popToken()
                    #Returning if node
                    return ifNode
                #Else throw invalid order
                else:
                    raise SyntaxError("Invalid If")
            #Checking for valid Set order
            elif(ahead.type == SET):
                #popping off token that indicate set
                tokens.popToken()
                #Updating cur and ahead
                cur = tokens.nextToken()
                ahead = tokens.nextNextToken()
                #Checking for valid Set order, need 1 identifier
                if(not(ahead.type == IDENTIFIER)):
                    raise SyntaxError("Invalid Set")
                else:
                    #Popping off token that indicate set
                    tokens.popToken()
                    #Setting new identifier token
                    identifier = tokens.nextToken()
                    #Setting new idenntifier node with the identifier token
                    iden = IdentifierNode(identifier.tokenText)
                    #Popping off the indicator token
                    tokens.popToken()
                    #Setting new Set node with Identifier Node and a call to expressions(tokens)
                    setNode = SetNode(iden, self.expression(tokens))
                    #Updating cur
                    cur = tokens.nextToken()
                    #Checking for valid Set order
                    if(cur.type == RIGHT_PAREN):
                        # pop the right paren
                        tokens.popToken()
                        #Returning Set Node
                        return setNode
                    #Else throw invalid Set because invalid Set form
                    else:
                        raise SyntaxError("Invalid Set")

            #Checking for valid And order
            elif(ahead.type == AND):
                #Popping off the tokens that indicate And
                tokens.popToken()
                tokens.popToken()
                #Creating new andList to hold nodes
                andList = []
                #updating cur
                cur = tokens.nextToken()
                #Checking for valid And order
                if(cur.type == RIGHT_PAREN):
                    raise SyntaxError("Invalid And")
                else:
                    #Checking for valid And order
                    while(not(cur.type == RIGHT_PAREN)):
                        #Adding expressions(tokens) to the andList
                        andList.append(self.expression(tokens))
                        #Updating cur
                        cur = tokens.nextToken()
                # pop the right paren
                tokens.popToken()
                #Setting And Node with the andList
                andNode = AndNode(andList)
                #Returning the And Node
                return andNode
            #Checking for valid Or order
            elif(ahead.type == OR):
                #Popping off the tokens that indicate Or 
                tokens.popToken()
                tokens.popToken()
                #Creating orList to hold nodes
                orList = []
                #updating cur
                cur = tokens.nextToken()
                #Checking for valid Or order
                if(cur.type == RIGHT_PAREN):
                    raise SyntaxError("Invalid Or")
                else:
                    #Checking for valid Or order
                    while(not(cur.type == RIGHT_PAREN)):
                        #Adding expression(tokens) to orList
                        orList.append(self.expression(tokens))
                        #Updating cur
                        cur = tokens.nextToken()
                # pop the right paren
                tokens.popToken()
                #Creating Or Node with orList
                orNode = OrNode(orList)
                #returning orNode
                return orNode
            #Checking for valid Begin token
            elif(ahead.type == BEGIN):
                #Popping off the tokens 
                tokens.popToken()
                tokens.popToken()
                #Creating new beginList to hold nodes
                beginList = []
                #Checking for valid Begin order
                if(cur.type == RIGHT_PAREN):
                    raise SyntaxError("Invalid Begin")
                else:
                    #Checking for valid Begin order
                    while(not(cur.type == RIGHT_PAREN)):
                        #Adding expression(tokens) to beginList
                        beginList.append(self.expression(tokens))
                        #updating cur
                        cur = tokens.nextToken()
                # pop the right paren
                tokens.popToken()
                #Creating begin Node to beginList
                beginNode = BeginNode(beginList)
                #return beginNode
                return beginNode

            #Checking for valid Cond order
            elif(ahead.type == COND):
                #Poppin off tokens that indicate Cond order
                tokens.popToken()
                tokens.popToken()
                #Creating condList to hold nodes
                condList = []
                #updating cur
                cur = tokens.nextToken()
                #Checking for valid Cond order
                if(cur.type == RIGHT_PAREN):
                    raise SyntaxError("Invalid Cond")
                else:
                    #Checking for valid Cond order
                    while(not(cur.type == RIGHT_PAREN)):
                        #Adding condition(tokens) to the condList
                        condList.append(self.condition(tokens))
                        #updating cur
                        cur = tokens.nextToken()
                # pop the right paren token
                tokens.popToken()
                #Creating Cond Node with the condList
                condNode = CondNode(condList)
                #returning the condNod
                return condNode
            #Else indicating an application
            else:
                return self.application(tokens)
        #Checking for Abbrev order
        elif(cur.type == ABBREV):
            #pop off Abbrev token
            tokens.popToken()
            #Set a Node to hold datum(tokens)
            temp = self.datum(tokens)
            #Create tick node and cast temp to IValue
            tick = TickNode(temp)
            #Return tick node
            return tick
        #Check for constant order
        elif(cur.type == BOOL or cur.type == INT or cur.type == DBL or cur.type == CHAR or cur.type == STR):
            #Return constant(tokens)
            return self.constant(tokens)
        #Check for Identifier order
        elif(cur.type == IDENTIFIER):
            #return identifier(tokens)
            return self.identifier(tokens)
        else:
            raise SyntaxError("Invalid Expression")
            
    # solve later
    def condition(self,tokens):
        # Create a list of base nodes
        listExp = []
        #Check for valid Condition order
        cur = tokens.nextToken()
        if(cur.type == LEFT_PAREN):
            # pop the left paren
            tokens.popToken()
            #Update the cur
            cur = tokens.nextToken()
            #Create test base node with expression(tokens)
            test = self.expression(tokens)
            #Update the cur
            cur = tokens.nextToken()
            #Checking for valid conditions
            while(not(cur.type == RIGHT_PAREN)):
                #add expression(tokens) to listExp
                listExp.append(self.expression(tokens))
                #update cur
                cur = tokens.nextToken()
            # pop right paren
            tokens.popToken()
            #Create Condition Node with values test and listExp
            conditionList = Condition(test,listExp)
            #return condition
            return conditionList
        #else throw invalid condition
        else:
            raise SyntaxError ("Invalid Condition")
        
    def datum(self, tokens):
        #Create cur node that keeps track of next token
        cur = tokens.nextToken()
        #Checks for valid Bool order
        if(cur.type == BOOL):
            #Create a temp Bool token, boolTemp
            boolTemp = cur
            #Create a Bool Node with value from boolTemp
            boolNode = BoolNode(boolTemp.literal)
            #Pop the bool token
            tokens.popToken()
            #return the bool Node
            return boolNode
        #Checks for valid Int order
        elif(cur.type == INT):
            #Create a temp Int token, intTemp
            intTemp = cur
            #Create a Int Node with value from intTemp
            intNode = IntNode(intTemp.literal)
            #Pop the int token
            tokens.popToken()
            #return the int Node
            return intNode
        #Checks for valid Dbl order
        elif(cur == DBL):
            #Create a temp Dbl token, dblTemp
            dblTemp = cur
            #Create a Dbl Node with value from dblTemp
            dblNode = DblNode(dblTemp.literal)
            #Pop the Dbl token
            tokens.popToken()
            #return the dbl Node
            return dblNode
        #Checks for valid Char order
        elif(cur.type == CHAR):
            #Create a temp Char token, charTemp
            charTemp = cur
            #Create a Char Node with value from charTemp
            charNode = CharNode(charTemp.literal)
            #Pop the char token
            tokens.popToken()
            #return the Char Node
            return charNode
        #Checks for valid Str order
        elif(cur.type == STR):
            #Create a temp Str token, strTemp
            strTemp = cur
            #Create a Str Node with the value from strTemp
            strNode = StrNode(strTemp.literal)
            #Pop the str token
            tokens.popToken()
            #return the Str Node
            return strNode
        #Checks for valid Identifier order
        elif(cur.type == IDENTIFIER):
            #return symbol(tokens)
            return self.symbol(tokens)
        #Checks for valid List order
        elif(cur.type == LEFT_PAREN):
            #return list(tokens)
            return self.list(tokens)
        #Checks for valid Vec order
        elif(cur.type == VECTOR):
            #Pop the vec token
            tokens.popToken()
            #Return vec(tokens)
            return self.vec(tokens)
        #Else return null
        else:
            raise SyntaxError("Invalid Datum")
    def vec(self,tokens):
        #Set cur to next token
        cur = tokens.nextToken()
        #Create a list of IValues
        list = []
        #Checks for valid Vec
        while(not(cur.type == RIGHT_PAREN)):
            #Add datum(tokens) casted to IValue to list
            list.append(self.datum(tokens))
            #update cur
            cur = tokens.nextToken()
        #Pop the right paren
        tokens.popToken()
        #Create Vec Node with values from list
        vec = VecNode(list)
        #return vec
        return vec
    """
    Formals function for the production of a formal
    @param tokens for the TokenStream
    @return List<Nodes.Identifier>
    """
    def formals(self, tokens):
        #Store the current Token
        cur = tokens.nextToken()
        #List of Node.Identifiers that contains the Nodes.Identifier returned from 
        iden = []
        #Check if cur is LeftParen, if it is not then its the incorrect form for formals
        if(cur.type == LEFT_PAREN):
            #Pop the left paren
            tokens.popToken()
            #update cur to next token
            cur = tokens.nextToken()
            #Checks for valid identifiers
            while(cur.type == IDENTIFIER):
                #Create Identifier node with the current identifier
                identifier = self.identifier(tokens)
                #Add the identifier node to iden
                iden.append(identifier)
                #Update cur to next token
                cur = tokens.nextToken()
            # check if there is a right parent token, if it is not then its the incorrect form for formals
            if(cur.type == RIGHT_PAREN):
                # pop the right paren token
                tokens.popToken()
                # return the List of Node Identifiers.
                return iden
            else:
                raise SyntaxError("Invalid Formals 1 ")
        else:
            raise SyntaxError("Invalid Formals 2 ")
    """
    Body function for the production of a body
    @param tokens for the TokenStream
    @return List<Nodes.BaseNode>
    """
    def body(self, tokens):
        #Store the current token
        cur = tokens.nextToken()
        #tore the token ahead
        ahead = tokens.nextNextToken()
        #List of Node.BaseNodes that contains the BaseNodes for the expressions returned from expression() and definitions returned from definition().
        nodes = []
        #While loop that checks if the current token is leftparen and ahead is define. This is repeated because body contains 0 or more definitions
        #If it is than add the Node returned from the definition function
        while(cur.type == LEFT_PAREN and ahead.type == DEFINE):
            nodes.append(self.definition(tokens))
            # Update cur since the definition() method pops tokens
            cur = tokens.nextToken()
            #Update ahead since the definition() method pops tokens
            ahead = tokens.nextNextToken()
        # After 0 or more definitions, the next has to be an expression
        nodes.append(self.expression(tokens))
        # Return the list of base nodes for the lambda.
        return nodes
    """
    Constant function for the production of a constant
    @param tokens for the TokenStream
    @return Nodes.BaseNode (Nodes.Bool, Nodes.Int, Nodes.Dbl, Nodes.Char, Nodes.Str) 
    """
    def constant(self, tokens):
        # check current token
        cur = tokens.nextToken()
        # (If we check the instanceOf the token then we can cast it to that type to access its fields and literals)
        # if the current token is a bool then return a Bool Node.
        if(cur.type == BOOL):
            # Cast the BaseToken to a Tokens.Bool to get the token's boolean literal. 
            boolTemp = cur
            # Create a Nodes.bool from the token's boolean literal
            boolNode = BoolNode(boolTemp.literal)
            # Pop the boolean token after using it.
            tokens.popToken()
            # return the Boolean node
            return boolNode
        # if the current token is an int, then return a Int Node
        elif(cur.type == INT):
            #temp to get the token's int literal
            intTemp = cur
            # Create an Nodes.Int from the token's int literal
            intNode = IntNode(intTemp.literal)
            # Pop the the int token after using it
            tokens.popToken()
            # return the Int Node
            return intNode
        # if the current token is a double, then return a Double node
        elif(cur.type == DBL):
            # Cast the BaseToken to a Tokens.Dbl to get the token's dbl literal
            dblTemp = cur
            # Create a Nodes.dbl from the token's double literal
            dblNode = DblNode(dblTemp.literal)
            # Pop the double token after using it
            tokens.popToken()
            # return the double node.
            return dblNode
        # if the current token is a char, then return a Char node
        elif(cur.type == CHAR):
            # Cast the BaseToken to a Tokens.Char to get the token's char literal
            charTemp = cur
            # Create a Nodes.char from the token's char literal
            charNode = CharNode(charTemp.literal)
            # pop the char token after using it
            tokens.popToken()
            # return the char node.
            return charNode
        # if the current token is a string, then return a String node
        elif(cur.type == STR):
            # Cast the BaseToken to a Tokens.Str to get the token's string literal
            strTemp = cur
            # Create a Nodes.str from the token's string literal
            strNode = StrNode(strTemp.literal)
            # pop the string token after using it
            tokens.popToken()
            # return the string node.
            return strNode
        else:
            # if the token is an instance of none of the above, than it is an invalid token for the constant form
            # return null which we recognize as an error in the method programs()
            raise SyntaxError ("Invalid Constant")
    """
    Application function for the production of a apply
    @param tokens for the TokenStream
    @return Nodes.BaseNode (Nodes.Apply) 
    """
    def application(self, tokens):
        #pop the lparen
        tokens.popToken()
        #List of Node.BaseNodes that contains the BaseNodes for the expressions returned from expression().
        expressionList = []
        # check the current token after popping the token
        cur = tokens.nextToken()
        # check if current is a RightParen token
        if(cur.type == RIGHT_PAREN):
            # if it is, throw an error because application needs 1 or more expressions
            raise SyntaxError("Invalid Apply")
        else:
            # while loop that creates and adds all the expressions for apply to the expressionList
            while(not(cur.type == RIGHT_PAREN)):
                expressionList.append(self.expression(tokens))
                # check new current token after expression, since the expression function pops tokens
                cur = tokens.nextToken()
            # pop the right paren
            tokens.popToken()
            # return the ApplyNode created from the expressionList
            return ApplyNode(expressionList)
    """"
    Symbol function for the production of a symbol
    @param tokens for the TokenStream
    @return Nodes.BaseNode (Nodes.Cons) 
    """
    def list(self, tokens):
        #pop Lparen of a list
        tokens.popToken()
        #Get the new Current token after Lparen
        cur = tokens.nextToken()
        # Create a List<IValue> to store the <IValues> from datum
        list = []
        # If the list is empty, return a empty Cons as the list.
        eval = slang_evaluator.makeDefaultEnv()
        # Since datum is 0 or more (* symbol), we will just keep adding the Nodes returned from the datum until we hit a RightParen token. 
        # If the while loop doesn't execute than the list will be just empty.
        while(not(cur.type == RIGHT_PAREN)):
            # Add the node returned from datum casted as an IValue
            list.append(self.datum(tokens))
            # Check for the current token after datum(tokens) because the datum function pops tokens. 
            cur = tokens.nextToken()
        # try catch block for the list production form
        # Pop the Right Paren
        tokens.popToken()
        if(len(list) == 0):
            return eval.empty
        else:
            # If the list is not empty, return a Cons constructed with the list and an empty cons.
            consNode = ConsNode(list,eval.empty)
            # return the Cons Node 
            return consNode
    """
     * Symbol function for the production of a symbol
     * @param tokens to get the TokenStream
     * @return Nodes.BaseNode (Nodes.Symbol)
    """
    def symbol(self,tokens):
        # Cast the Tokens.BaseToken to Tokens.Identifier 
        iden = tokens.nextToken()
        # Create the SymbolNode from the Token's tokenText
        symbolNode = SymbolNode(iden.tokenText)
        #  Pop the Identifier token.
        tokens.popToken()
        # return the Symbol Node
        return symbolNode
    

    """
     * Identifier Function for the production of an identifier
     * @param tokens
     * @return Nodes.BaseNode (Nodes.Identifier)
     """
    def identifier(self, tokens):
        #Create the identifier node from the Token's TokenText
        identifier = tokens.nextToken()
        identifierNode = IdentifierNode(identifier.tokenText)
        #Pop the identifier token after creating the node.
        tokens.popToken()
        #Return the Identifier Node
        return identifierNode

    def __init__(self, true, false, empty):
        """Construct a parser by caching the environmental constants true,
        false, and empty"""
        self.true = true
        self.false = false
        self.empty = empty

    def parse(self, tokens):
        """parse() is the main routine of the parser.  It parses the token
        stream into an AST."""
        AST = []
        AST = self.program(tokens)
        return AST
