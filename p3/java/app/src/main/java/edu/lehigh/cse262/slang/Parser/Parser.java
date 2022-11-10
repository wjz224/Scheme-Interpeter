package edu.lehigh.cse262.slang.Parser;

import java.util.*;
import java.util.List;

import edu.lehigh.cse262.slang.Parser.Nodes.Identifier;
import edu.lehigh.cse262.slang.Scanner.TokenStream;
import edu.lehigh.cse262.slang.Scanner.Tokens;
import edu.lehigh.cse262.slang.Scanner.Tokens.LeftParen;


/**
 * Parser is the second step in our interpreter. It is responsible for turning a
 * sequence of tokens into an abstract syntax tree.
 */
public class Parser {
    private final Nodes.Bool _true;
    private final Nodes.Bool _false;
    private final Nodes.Cons _empty;

    public Parser(Nodes.Bool _true, Nodes.Bool _false, Nodes.Cons _empty) {
        this._true = _true;
        this._false = _false;
        this._empty = _empty;
    }

    /**
     * Transform a stream of tokens into an AST
     *
     * @param tokens a stream of tokens
     *
     * @return A list of AstNodes, because a Scheme program may have multiple
     *         top-level expressions.
     *
     */
    public List<Nodes.BaseNode> program(TokenStream tokens){
        // Create a List of Nodes.BaseNode that stores BaseNodes in the fashion of an AST
        List<Nodes.BaseNode> AST = new ArrayList<>();
        // try catch block
        try{
        AST = new ArrayList<>();
            // while loop that goes through all the tokens in the TokenStream and creates an AST
            while(!(tokens.nextToken() instanceof Tokens.Eof)){
                // pass the TokenStream into form to start parsing
                Nodes.BaseNode input = form(tokens);
                // if null is returned, than there was an error in the parsing.
                if(input == null){
                    // Throw an exception saying there was an error in parsing
                    throw new Exception("Invalid tokens caused error in parsing");
                }
                else{
                    // add the BaseNode to the List of BaseNodes
                    AST.add(input);
                }
            }
            // return the list of Nodes.BaseNode
            return AST;
        }
        // catch any errors and print the message.
        catch(Exception e){
            e.getMessage();
        }
        return AST;
    }
    /**
     * Transform a stream of tokens into a Node
     *
     * @param tokens a stream of tokens
     *
     * @return A AstNode, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public Nodes.BaseNode form(TokenStream tokens){
        //Grabbing current token and the one ahead of it
        Tokens.BaseToken cur = tokens.nextToken();
        Tokens.BaseToken ahead = tokens.nextNextToken();
        //Checking for definition production
        if(cur instanceof Tokens.LeftParen && ahead instanceof Tokens.Define){
            return definition(tokens);
        }
        //else sent to expression production
        else{
            return expression(tokens);
        }
    }
    /**
     * Transform a stream of tokens into a Node
     *
     * @param tokens a stream of tokens
     *
     * @return A AstNode, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public Nodes.BaseNode definition(TokenStream tokens){
        //Pop off the first two tokens that indicate a definition
        tokens.popToken();
        tokens.popToken();
        //Grabbing the next token in the stream
        Tokens.BaseToken cur = tokens.nextToken();
        try{
            //Checking for valid order of tokens
            if(!(cur instanceof Tokens.Identifier)){
                throw new Exception("Invalid definition");
            }
            else{
                //Grab the identifier token and set it to a node
                Tokens.Identifier identifier = (Tokens.Identifier) cur;
                Nodes.Identifier iden = new Identifier(identifier.tokenText);
                // pop the identifier token
                tokens.popToken();
                //Create a define node with the identifier node and also a node from calling expressions
                Nodes.Define define = new Nodes.Define(iden, expression(tokens));
                //Updating current token
                cur = tokens.nextToken();
                //Checking for valid definition order
                if(cur instanceof Tokens.RightParen){
                    //Popping off the right paren
                    tokens.popToken();
                    //returning a definition Node
                    return define;
                }
                // throwing an exception for invalid definition form
                else{
                    throw new Exception("Invalid definition");
                }
            }
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    /**
     * Transform a stream of tokens into a Node
     *
     * @param tokens a stream of tokens
     *
     * @return A AstNode, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public Nodes.BaseNode expression(TokenStream tokens){
        //Grabbing the next token and the nextnext token
        Tokens.BaseToken cur = tokens.nextToken();
        Tokens.BaseToken ahead = tokens.nextNextToken();
        //Checking for valid expression order
        if(cur instanceof Tokens.LeftParen){
            //Checking for valid Quote order
            if(ahead instanceof Tokens.Quote){
                //Popping off the two tokens that indicate a quote node
                tokens.popToken();
                tokens.popToken();
                //Setting a quote Node with datum(tokens) and casting to IValue
                Nodes.Quote quote = new Nodes.Quote((IValue) datum(tokens));
                //Updating cur after a call to datum
                cur = tokens.nextToken();
                //Checking for valid Quote order
                if(cur instanceof Tokens.RightParen){
                    // pop the right paren
                    tokens.popToken();
                    //returning the quote node
                    return quote;
                }
            } 
            //Checking for valid Lambda order
            else if(ahead instanceof Tokens.Lambda){
                //Popping off the two tokens that indicate a lambda node
                tokens.popToken();
                tokens.popToken();
                //Setting a lambda Node with calls to formals(tokens) and body(tokens)
                Nodes.LambdaDef lambdaNode = new Nodes.LambdaDef(formals(tokens), body(tokens));
                //updating cur after calls to formals and body
                cur = tokens.nextToken();
                try{
                    //Checking for valid Lambda order
                    if(cur instanceof Tokens.RightParen){
                        // pop the right paren
                        tokens.popToken();
                        //returning the lambda node
                        return lambdaNode;
                    }
                    //Else throw invalid order
                    else{
                        throw new Exception("Invalid Lambda");
                    }
                }catch(Exception e){
                    e.getMessage();
                }
            }  
            //Checking for valid If order
            else if(ahead instanceof Tokens.If){
                //Popping off the two tokens that indicate an if node
                tokens.popToken();
                tokens.popToken();
                //Setting a if node with calls to expression(tokens), expression(tokens), expression(tokens)
                Nodes.If ifNode = new Nodes.If(expression(tokens), expression(tokens), expression(tokens));
                //Updating cur after calling expression(tokens), expression(tokens), expression(tokens)
                cur = tokens.nextToken();
                try{
                    //Checking for valid if order
                    if(cur instanceof Tokens.RightParen){
                        // pop the right paren
                        tokens.popToken();
                        //Returning if node
                        return ifNode;
                    }
                    //Else throw invalid order
                    else{
                        throw new Exception("Invalid If");
                    }
                } catch(Exception e){
                   e.getMessage();
                }
                return null;
                
            }
            //Checking for valid Set order
            else if(ahead instanceof Tokens.Set){
                //popping off token that indicate set
                tokens.popToken();
                //Updating cur and ahead
                cur = tokens.nextToken();
                ahead = tokens.nextNextToken();
                try{
                    //Checking for valid Set order
                    if(!(ahead instanceof Tokens.Identifier)){
                        throw new Exception("Invalid Set");
                    }
                    else{
                        //Popping off token that indicate set
                        tokens.popToken();
                        //Setting new identifier token
                        Tokens.Identifier identifier = (Tokens.Identifier) tokens.nextToken();
                        //Setting new idenntifier node with the identifier token
                        Nodes.Identifier iden = new Identifier(identifier.tokenText);
                        //Popping off the indicator token
                        tokens.popToken();
                        //Setting new Set node with Identifier Node and a call to expressions(tokens)
                        Nodes.Set setNode = new Nodes.Set(iden, expression(tokens));
                        //Updating cur
                        cur = tokens.nextToken();
                        //Checking for valid Set order
                        if(cur instanceof Tokens.RightParen){
                            // pop the right paren
                             tokens.popToken();
                             //Returning Set Node
                            return setNode;
                        }
                        //Else throw invalid Set
                        else{
                            throw new Exception("Invalid Set");
                        }
                    }
                } catch(Exception e){
                    e.getMessage();
                }
                return null;
            }
            //Checking for valid And order
            else if(ahead instanceof Tokens.And){
                //Popping off the tokens that indicate And
                tokens.popToken();
                tokens.popToken();
                //Creating new andList to hold nodes
                List<Nodes.BaseNode> andList = new ArrayList<>();
                //updating cur
                cur = tokens.nextToken();
                try{
                    //Checking for valid And order
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid And");
                    }
                    else{
                        //Checking for valid And order
                        while(!(cur instanceof Tokens.RightParen)){
                            //Adding expressions(tokens) to the andList
                            andList.add(expression(tokens));
                            //Updating cur
                            cur = tokens.nextToken();
                        }
                    }
                }catch(Exception e){
                    e.getMessage();
                }
                // pop the right paren
                tokens.popToken();
                //Setting And Node with the andList
                Nodes.And andNode = new Nodes.And(andList);
                //Returning the And Node
                return andNode;
            }
            //Checking for valid Or order
            else if(ahead instanceof Tokens.Or){
                //Popping off the tokens that indicate Or 
                tokens.popToken();
                tokens.popToken();
                //Creating orList to hold nodes
                List<Nodes.BaseNode> orList = new ArrayList<>();
                try{
                    //updating cur
                    cur = tokens.nextToken();
                    //Checking for valid Or order
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid Or");
                    }
                    else{
                        //Checking for valid Or order
                        while(!(cur instanceof Tokens.RightParen)){
                            //Adding expression(tokens) to orList
                            orList.add(expression(tokens));
                            //Updating cur
                            cur = tokens.nextToken();
                        }
                    }
                } catch(Exception e){
                    e.getMessage();
                }
                // pop the right paren
                tokens.popToken();
                //Creating Or Node with orList
                Nodes.Or orNode = new Nodes.Or(orList);
                //returning orNode
                return orNode;
            }
            //Checking for valid Begin token
            else if(ahead instanceof Tokens.Begin){
                //Popping off the tokens 
                tokens.popToken();
                tokens.popToken();
                //Creating new beginList to hold nodes
                List<Nodes.BaseNode> beginList = new ArrayList<>();
                try{
                    //Checking for valid Begin order
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid");
                    }
                    else{
                        //Checking for valid Begin order
                        while(!(cur instanceof Tokens.RightParen)){
                            //Adding expression(tokens) to beginList
                            beginList.add(expression(tokens));
                            //updating cur
                            cur = tokens.nextToken();
                        }
                    }
                }catch(Exception e){
                    e.getMessage();
                }
                // pop the right paren
                tokens.popToken();
                //Creating begin Node to beginList
                Nodes.Begin beginNode = new Nodes.Begin(beginList);
                //return beginNode
                return beginNode;
            }
            //Checking for valid Cond order
            else if(ahead instanceof Tokens.Cond){
                //Poppin off tokens that indicate Cond order
                tokens.popToken();
                tokens.popToken();
                //Creating condList to hold nodes
                List<Nodes.Cond.Condition> condList = new ArrayList<>();
                //updating cur
                cur = tokens.nextToken();
                try{
                    //Checking for valid Cond order
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid Cond");
                    }
                    else{
                        //Checking for valid Cond order
                        while(!(cur instanceof Tokens.RightParen)){
                            //Adding condition(tokens) to the condList
                            condList.add(condition(tokens));
                            //updating cur
                            cur = tokens.nextToken();
                        }
                    }
                }catch(Exception e){
                    e.getMessage();
                }
                // pop the right paren token
                tokens.popToken();
                //Creating Cond Node with the condList
                Nodes.Cond condNode = new Nodes.Cond(condList);
                //returning the condNode
                return condNode;
            }
            //Else indicating an application
            else{
                return application(tokens);
            }
        }
        //Checking for Abbrev order
        else if(cur instanceof Tokens.Abbrev){
            //pop off Abbrev token
            tokens.popToken();
            //Set a Node to hold datum(tokens)
            Nodes.BaseNode temp = datum(tokens);
            //Create tick node and cast temp to IValue
            Nodes.Tick tick = new Nodes.Tick((IValue)temp);
            //Return tick node
            return tick;
        }
        //Check for constant order
        else if(cur instanceof Tokens.Bool || cur instanceof Tokens.Int || cur instanceof Tokens.Dbl || cur instanceof Tokens.Char || cur instanceof Tokens.Str){
            //Return constant(tokens)
            return constant(tokens);
        }
        //Check for Identifier order
        else if(cur instanceof Tokens.Identifier){
            //return identifier(tokens)
            return identifier(tokens);
        }
        //Else null
        return null;
    }
    /**
     * Transform a stream of tokens into a Condition Node
     *
     * @param tokens a stream of tokens
     *
     * @return A AstNode, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public Nodes.Cond.Condition condition(TokenStream tokens){  
        //Create a list of base nodes
        List<Nodes.BaseNode> listExp = new ArrayList<>();
        try{
            //Check for valid Condition order
            if(tokens.nextToken() instanceof Tokens.LeftParen){
                // pop the left paren
                tokens.popToken();
                //Update the cur
                Tokens.BaseToken cur = tokens.nextToken();
                //Create test base node with expression(tokens)
                Nodes.BaseNode test = expression(tokens);
                //Update the cur
                cur = tokens.nextToken();
                //Checking for valid conditions
                while(!(cur instanceof Tokens.RightParen)){
                    //add expression(tokens) to listExp
                    listExp.add(expression(tokens));
                    //update cur
                    cur = tokens.nextToken();
                }
                // pop right paren
                tokens.popToken();
                //Create Condition Node with values test and listExp
                Nodes.Cond.Condition condition = new Nodes.Cond.Condition(test,listExp);
                //return condition
                return condition;
            }
            //else throw invalid condition
            else{
                throw new Exception ("Invalid Condition");
            }
        }catch(Exception e){
            e.getMessage();
        }
    
        return null;
    }
    /**
     * Transform a stream of tokens into a Node
     *
     * @param tokens a stream of tokens
     *
     * @return A AstNode, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public Nodes.BaseNode datum(TokenStream tokens){
        //Create cur node that keeps track of next token
        Tokens.BaseToken cur = tokens.nextToken();
        //Checks for valid Bool order
        if(cur instanceof Tokens.Bool){
            //Create a temp Bool token, boolTemp
            Tokens.Bool boolTemp = (Tokens.Bool) cur;
            //Create a Bool Node with value from boolTemp
            Nodes.Bool boolNode = new Nodes.Bool(boolTemp.literal);
            //Pop the bool token
            tokens.popToken();
            //return the bool Node
            return boolNode;
        }
        //Checks for valid Int order
        else if(cur instanceof Tokens.Int){
            //Create a temp Int token, intTemp
            Tokens.Int intTemp = (Tokens.Int) cur;
            //Create a Int Node with value from intTemp
            Nodes.Int intNode = new Nodes.Int(intTemp.literal);
            //Pop the int token
            tokens.popToken();
            //return the int Node
            return intNode;
        }
        //Checks for valid Dbl order
        else if(cur instanceof Tokens.Dbl){
            //Create a temp Dbl token, dblTemp
            Tokens.Dbl dblTemp = (Tokens.Dbl) cur;
            //Create a Dbl Node with value from dblTemp
            Nodes.Dbl dblNode = new Nodes.Dbl(dblTemp.literal);
            //Pop the Dbl token
            tokens.popToken();
            //return the dbl Node
            return dblNode;
        }
        //Checks for valid Char order
        else if(cur instanceof Tokens.Char){
            //Create a temp Char token, charTemp
            Tokens.Char charTemp = (Tokens.Char) cur;
            //Create a Char Node with value from charTemp
            Nodes.Char charNode = new Nodes.Char(charTemp.literal);
            //Pop the char token
            tokens.popToken();
            //return the Char Node
            return charNode;
        }
        //Checks for valid Str order
        else if(cur instanceof Tokens.Str){
            //Create a temp Str token, strTemp
            Tokens.Str strTemp = (Tokens.Str) cur;
            //Create a Str Node with the value from strTemp
            Nodes.Str strNode = new Nodes.Str(strTemp.literal);
            //Pop the str token
            tokens.popToken();
            //return the Str Node
            return strNode;
        }
        //Checks for valid Identifier order
        else if(cur instanceof Tokens.Identifier){
            //return symbol(tokens)
            return symbol(tokens);
        }
        //Checks for valid List order
        else if(cur instanceof Tokens.LeftParen){
            //return list(tokens)
            return list(tokens);
        }
        //Checks for valid Vec order
        else if(cur instanceof Tokens.Vec){
            //Pop the vec token
            tokens.popToken();
            //Return vec(tokens)
            return vec(tokens);
        }
        //Else return null
        else{
            return null;
        }
    }
    /**
     * Transform a stream of tokens into a Node
     *
     * @param tokens a stream of tokens
     *
     * @return A AstNode, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public Nodes.BaseNode vec(TokenStream tokens){
        try{
            //Set cur to next token
            Tokens.BaseToken cur = tokens.nextToken();
            //Create a list of IValues
            List<IValue> list = new ArrayList<>();
            //Checks for valid Vec
            while(!(cur instanceof Tokens.RightParen)){
                //Add datum(tokens) casted to IValue to list
                list.add((IValue) datum(tokens));
                //update cur
                cur = tokens.nextToken();
            }
            //Pop the right paren
            tokens.popToken();
            //Create Vec Node with values from list
            Nodes.Vec vec = new Nodes.Vec(list);
            //return vec
            return vec;
        }
        catch(Exception e){
             e.getMessage();
        }
        return null;
    }
    /**
     * Formals function for the production of a formal
     * @param tokens for the TokenStream
     * @return List<Nodes.Identifier>
     */
    public List<Nodes.Identifier> formals(TokenStream tokens){
        // try-catch block for the formal production form
        try{
            // Store the current Token
            Tokens.BaseToken cur = tokens.nextToken();
             // List of Node.Identifiers that contains the Nodes.Identifier returned from 
            List<Nodes.Identifier> iden = new ArrayList<>();
            // Check if cur is LeftParen, if it is not then its the incorrect form for formals
            if(cur instanceof Tokens.LeftParen){
                //Pop the left paren
                tokens.popToken();
                //update cur to next token
                cur = tokens.nextToken();
                //Checks for valid identifiers
                while(cur instanceof Tokens.Identifier){
                    //Create Identifier node with the current identifier
                    Nodes.Identifier identifier = new Identifier(cur.tokenText);
                    //Add the identifier node to iden
                    iden.add(identifier);
                    //Pop the identifier
                    tokens.popToken();
                    //Update cur to next token
                    cur = tokens.nextToken();
                }
                // check if there is a right parent token, if it is not then its the incorrect form for formals
                if(cur instanceof Tokens.RightParen){
                    // pop the right paren token
                    tokens.popToken();
                    // return the List of Node Identifiers.
                    return iden;
                }
                else{
                    throw new Exception("Invalid Formals");
                }
            }
            else{
                throw new Exception("Invalid Formals");
            }
         
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    /**
     * Body function for the production of a body
     * @param tokens for the TokenStream
     * @return List<Nodes.BaseNode>
     */
    public List<Nodes.BaseNode> body(TokenStream tokens){
        // try-catch block for the body production form
        try{
            // Store the current token
            Tokens.BaseToken cur = tokens.nextToken();
            // Store the token ahead
            Tokens.BaseToken ahead = tokens.nextNextToken();
             // List of Node.BaseNodes that contains the BaseNodes for the expressions returned from expression() and definitions returned from definition().
            List<Nodes.BaseNode> nodes = new ArrayList<>();
            // While loop that checks if the current token is leftparen and ahead is define. This is repeated because body contains 0 or more definitions
            // If it is than add the Node returned from the definition function
            while(cur instanceof Tokens.LeftParen && ahead instanceof Tokens.Define){
                nodes.add(definition(tokens));
                // Update cur since the definition() method pops tokens
                cur = tokens.nextToken();
                // Update ahead since the definition() method pops tokens
                ahead = tokens.nextNextToken();
            }
            // After 0 or more definitions, the next has to be an expression
            nodes.add(expression(tokens));
            // Return the list of base nodes for the lambda.
            return nodes;
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    /**
     * Constant function for the production of a constant
     * @param tokens for the TokenStream
     * @return Nodes.BaseNode (Nodes.Bool, Nodes.Int, Nodes.Dbl, Nodes.Char, Nodes.Str) 
     */
    public Nodes.BaseNode constant(TokenStream tokens){
        // Try-catch block for the constant production form
        try{
            // check current token
            Tokens.BaseToken cur = tokens.nextToken();
            // (If we check the instanceOf the token then we can cast it to that type to access its fields and literals)
            // if the current token is a bool then return a Bool Node.
            if(cur instanceof Tokens.Bool){
                // Cast the BaseToken to a Tokens.Bool to get the token's boolean literal. 
                Tokens.Bool boolTemp = (Tokens.Bool) cur;
                // Create a Nodes.bool from the token's boolean literal
                Nodes.Bool boolNode = new Nodes.Bool(boolTemp.literal);
                // Pop the boolean token after using it.
                tokens.popToken();
                // return the Boolean node
                return boolNode;
            }
            // if the current token is an int, then return a Int Node
            else if(cur instanceof Tokens.Int){
                // Cast the Basetoken to a Tokens.Int to get the token's int literal
                Tokens.Int intTemp = (Tokens.Int) cur;
                // Create an Nodes.Int from the token's int literal
                Nodes.Int intNode = new Nodes.Int(intTemp.literal);
                // Pop the the int token after using it
                tokens.popToken();
                // return the Int Node
                return intNode;
            }
            // if the current token is a double, then return a Double node
            else if(cur instanceof Tokens.Dbl){
                // Cast the BaseToken to a Tokens.Dbl to get the token's dbl literal
                Tokens.Dbl dblTemp = (Tokens.Dbl) cur;
                // Create a Nodes.dbl from the token's double literal
                Nodes.Dbl dblNode = new Nodes.Dbl(dblTemp.literal);
                // Pop the double token after using it
                tokens.popToken();
                // return the double node.
                return dblNode;
            }
            // if the current token is a char, then return a Char node
            else if(cur instanceof Tokens.Char){
                // Cast the BaseToken to a Tokens.Char to get the token's char literal
                Tokens.Char charTemp = (Tokens.Char) cur;
                // Create a Nodes.char from the token's char literal
                Nodes.Char charNode = new Nodes.Char(charTemp.literal);
                // pop the char token after using it
                tokens.popToken();
                // return the char node.
                return charNode;
            }
            // if the current token is a string, then return a String node
            else if(cur instanceof Tokens.Str){
                // Cast the BaseToken to a Tokens.Str to get the token's string literal
                Tokens.Str strTemp = (Tokens.Str) cur;
                // Create a Nodes.str from the token's string literal
                Nodes.Str strNode = new Nodes.Str(strTemp.literal);
                // pop the string token after using it
                tokens.popToken();
                // return the string node.
                return strNode;
            }
            else{
                // if the token is an instance of none of the above, than it is an invalid token for the constant form
                // return null which we recognize as an error in the method programs()
                return null;
            }
        }
        catch(Exception e){
            e.getMessage();
        }
        // if the try block fails, than we return null which we recognize as an error in the method programs()
        return null;
    }
    /**
     * Application function for the production of a apply
     * @param tokens for the TokenStream
     * @return Nodes.BaseNode (Nodes.Apply) 
     */
    public Nodes.BaseNode application(TokenStream tokens){
        // try catch block for the apply production form
        try{
            // pop the lparen
            tokens.popToken();
            // List of Node.BaseNodes that contains the BaseNodes for the expressions returned from expression().
            List<Nodes.BaseNode> expressionList = new ArrayList<>();
            // check the current token after popping the token
            Tokens.BaseToken cur = tokens.nextToken();
            // check if current is a RightParen token
            if(cur instanceof Tokens.RightParen){
                // if it is, throw an error because application needs 1 or more expressions
                throw new Exception("Invalid");
            }
            else{
                // while loop that creates and adds all the expressions for apply to the expressionList
                while(!(cur instanceof Tokens.RightParen)){
                    expressionList.add(expression(tokens));
                    // check new current token after expression, since the expression function pops tokens
                    cur = tokens.nextToken();
                }
                // pop the right paren
                tokens.popToken();
                // return the ApplyNode created from the expressionList
                return new Nodes.Apply(expressionList);
            }
        }
        // 
        catch(Exception e){
            e.getMessage();
        }
        // return null when there is an error.
         return null;
    }
    /**
     * Symbol function for the production of a symbol
     * @param tokens for the TokenStream
     * @return Nodes.BaseNode (Nodes.Cons) 
     */
    public Nodes.BaseNode list(TokenStream tokens){
        //pop Lparen of a list
        tokens.popToken();
        // Get the new Current token after Lparen
        Tokens.BaseToken cur = tokens.nextToken();
        // Create a List<IValue> to store the <IValues> from datum
        List<IValue> list = new ArrayList<>();
        // Since datum is 0 or more (* symbol), we will just keep adding the Nodes returned from the datum until we hit a RightParen token. 
        // If the while loop doesn't execute than the list will be just empty.
        while(!(cur instanceof Tokens.RightParen)){
            // Add the node returned from datum casted as an IValue
            list.add((IValue) datum(tokens));
            // Check for the current token after datum(tokens) because the datum function pops tokens. 
            cur = tokens.nextToken();
        }
        // try catch block for the list production form
        try{
            // Pop the Right Paren
            tokens.popToken();
            // If the list is empty, return a empty Cons as the list.
            if(list.size() == 0){
                return this._empty;
            }
            else{
                // If the list is not empty, return a Cons constructed with the list and an empty cons.
                Nodes.Cons consNode = new Nodes.Cons(list,this._empty);
                // return the Cons Node 
                return consNode;
            }
        } 
        catch(Exception e){
            e.getMessage();
        }      
        // return null when there is an error.
        return null;
    }
    /**
     * Symbol function for the production of a symbol
     * @param tokens to get the TokenStream
     * @return Nodes.BaseNode (Nodes.Symbol)
     */
    public Nodes.BaseNode symbol(TokenStream tokens){
        // Cast the Tokens.BaseToken to Tokens.Identifier 
        Tokens.Identifier iden = (Tokens.Identifier) tokens.nextToken();
        // Create the SymbolNode from the Token's tokenText
        Nodes.Symbol symbolNode = new Nodes.Symbol(iden.tokenText);
        // Pop the Identifier token.
        tokens.popToken();
        // return the Symbol Node
        return symbolNode;
    }
    /**
     * Identifier Function for the production of an identifier
     * @param tokens
     * @return Nodes.BaseNode (Nodes.Identifier)
     */
    public Nodes.BaseNode identifier(TokenStream tokens){
        // Create the identifier node from the Token's TokenText
        Nodes.Identifier identifier = new Identifier(tokens.nextToken().tokenText);
        // Pop the identifier token after creating the node.
        tokens.popToken();
        // Return the Identifier Node
        return identifier;
    }
   /**
     * Transform a stream of tokens into an AST
     *
     * @param tokens a stream of tokens
     *
     * @return A list of AstNodes, because a Scheme program may have multiple
     *         top-level expressions.
     *
     */
    public List<Nodes.BaseNode> parse(TokenStream tokens) throws Exception {
        List<Nodes.BaseNode> AST = new ArrayList<>();
        // Try catch block to parse the TokenStream of tokens
        try{
            // Call the program production on the stream of tokens (Function)
            AST = program(tokens); 
        }catch(Exception e){
            e.getMessage();
        }
        return AST;
    }
}