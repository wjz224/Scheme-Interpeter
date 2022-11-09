package edu.lehigh.cse262.slang.Parser;

import java.util.*;
import java.util.List;

import edu.lehigh.cse262.slang.Parser.Nodes.Identifier;
import edu.lehigh.cse262.slang.Scanner.TokenStream;
import edu.lehigh.cse262.slang.Scanner.Tokens;


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
     */
    public List<Nodes.BaseNode> program(TokenStream tokens){
        List<Nodes.BaseNode> AST = new ArrayList<>();
        while(tokens.hasNext()){
            AST.add(form(tokens));
        }
        return AST;
    }
    public Nodes.BaseNode form(TokenStream tokens){
        Tokens.BaseToken cur = tokens.nextToken();
        Tokens.BaseToken ahead = tokens.nextNextToken();
        if(cur instanceof Tokens.LeftParen && ahead instanceof Tokens.Define){
            return definition(tokens);
        }
        else{
            return expression(tokens);
        }
    }
    public Nodes.BaseNode definition(TokenStream tokens){
        tokens.popToken();
        tokens.popToken();
        Tokens.BaseToken cur = tokens.nextToken();
     
        if(cur !instanceof Tokens.Identifier){
            throw new Exception("this dont work");
        }
        else{
            // pop the identifier token
            Tokens.Identifier identifier = (Tokens.Identifier) cur;
            Nodes.Identifier iden = new Identifier(identifier.tokenText);
            tokens.popToken();
            Nodes.Define define = new Nodes.Define(iden, expression(tokens));
            cur = tokens.nextToken();
            if(cur instanceof Tokens.RightParen){
                return define;
            }
            else{
                throw new Exception("Invalid");
            }
        }
    }
    public Nodes.BaseNode expression(TokenStream tokens){
        Tokens.BaseToken cur = tokens.nextToken();
        Tokens.BaseToken ahead = tokens.nextNextToken();
        if(cur instanceof Tokens.LeftParen){
            if(ahead instanceof Tokens.Quote){
                tokens.popToken();
                tokens.popToken();
                Nodes.Quote quote = new Nodes.Quote((IValue) datum(tokens));
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    // pop the right paren
                    tokens.popToken();
                    return quote;
                }
            } 
            else if(ahead instanceof Tokens.Lambda){
                tokens.popToken();
                tokens.popToken();
                Nodes.LambdaDef lambdaNode = new Nodes.LambdaDef(formal(tokens), body(tokens));
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    // pop the right paren
                    tokens.popToken();
                    return lambdaNode;
                }
                else{
                    throw new Exception("Invalid");
                }
            }  
            else if(ahead instanceof Tokens.If){
                // figure out later
                tokens.popToken();
                tokens.popToken();
                Nodes.If ifNode = new Nodes.If(expression(tokens), expression(tokens), expression(tokens));
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    // pop the right paren
                    tokens.popToken();
                    return ifNode;
                }
                else{
                    throw new Exception("Invalid");
                }
            }
            else if(ahead instanceof Tokens.Set){
                tokens.popToken();
                cur = tokens.nextToken();
                ahead = tokens.nextNextToken();
                if(!(ahead instanceof Tokens.Identifier)){
                    throw new Exception("Error");
                }
                else{
                    tokens.popToken();
                    Tokens.Identifier identifier = (Tokens.Identifier) tokens.nextToken();
                    Nodes.Identifier iden = new Identifier(identifier.tokenText);
                    tokens.popToken();
                    Nodes.Set setNode = new Nodes.Set(iden, expression(tokens));
                    cur = tokens.nextToken();
                    if(cur instanceof Tokens.RightParen){
                        // pop the right paren
                         tokens.popToken();
                        return setNode;
                    }
                    else{
                        throw new Exception("Invalid");
                    }
                }
            }
            else if(ahead instanceof Tokens.And){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> andList = new ArrayList<>();
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    throw new Exception("Invalid");
                }
                else{
                    while(!(cur instanceof Tokens.RightParen)){
                        andList.add(expression(tokens));
                        cur = tokens.nextToken();
                    }
                }
                // pop the right paren
                tokens.popToken();
                Nodes.And andNode = new Nodes.And(andList);
                return andNode;
            }
            else if(ahead instanceof Tokens.Or){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> orList = new ArrayList<>();
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    throw new Exception("Invalid");
                }
                else{
                    while(!(cur instanceof Tokens.RightParen)){
                        orList.add(expression(tokens));
                        cur = tokens.nextToken();
                    }
                }
                // pop the right paren
                tokens.popToken();
                Nodes.Or orNode = new Nodes.Or(orList);
                return orNode;
            }
            else if(ahead instanceof Tokens.Begin){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> beginList = new ArrayList<>();
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    throw new Exception("Invalid");
                }
                else{
                    while(!(cur instanceof Tokens.RightParen)){
                        beginList.add(expression(tokens));
                        cur = tokens.nextToken();
                    }
                }
                // pop the right paren
                tokens.popToken();
                Nodes.Begin beginNode = new Nodes.Begin(beginList);
                return beginNode;
            }
            else if(ahead instanceof Tokens.Cond){
                /** 
                tokens.popToken();
                tokens.popToken();
                // get first expression as the test case
                Nodes.BaseNode test = expression(tokens);
                // get remainder expressions for evaluation.
                List<Nodes.BaseNode> condList = new ArrayList<>();
                cur = tokens.nextToken();
                if(cur instanceof Tokens.RightParen){
                    throw new Exception("Invalid");
                }
                else{
                    while(!(cur instanceof Tokens.RightParen)){
                        condList.add(expression(tokens));
                        cur = tokens.nextToken();
                    }
                }
                // pop the right token
                tokens.popToken();
                
                Nodes.Cond condNode = new Nodes.Cond(condList);
                return condNode;
                */
                return null;
            }
            
            else{
                return application(tokens);
            }
        }
        else if(cur instanceof Tokens.Abbrev){
            tokens.popToken();
            Nodes.Tick tick = new Nodes.Tick((IValue) datum(tokens));
            return tick;
        }
        else if(cur instanceof Tokens.Bool || cur instanceof Tokens.Int || cur instanceof Tokens.Dbl || cur instanceof Tokens.Char || cur instanceof Tokens.Str){
            return constant(tokens);
        }
        else if(cur instanceof Tokens.Identifier){
            return identifier(tokens);
        }
        else{
            return null;
        }
        return null;
    }
    public Nodes.BaseNode datum(TokenStream tokens){
        tokens.popToken();
        Tokens.BaseToken cur = tokens.nextToken();
        if(cur instanceof Tokens.Bool){
            Tokens.Bool boolTemp = (Tokens.Bool) cur;
            Nodes.Bool boolNode = new Nodes.Bool(boolTemp.literal);
            return boolNode;
        }
        else if(cur instanceof Tokens.Int){
            Tokens.Int intTemp = (Tokens.Int) cur;
            Nodes.Int intNode = new Nodes.Int(intTemp.literal);
            return intNode;
        }
        else if(cur instanceof Tokens.Dbl){
            Tokens.Dbl dblTemp = (Tokens.Dbl) cur;
            Nodes.Dbl dblNode = new Nodes.Dbl(dblTemp.literal);
            return dblNode;
        }
        else if(cur instanceof Tokens.Char){
            Tokens.Char charTemp = (Tokens.Char) cur;
            Nodes.Char charNode = new Nodes.Char(charTemp.literal);
            return charNode;
        }
        else if(cur instanceof Tokens.Str){
            Tokens.Str strTemp = (Tokens.Str) cur;
            Nodes.Str strNode = new Nodes.Str(strTemp.literal);
            return strNode;
        }
        else if(cur instanceof Tokens.Identifier){
            return symbol(tokens);
        }
        else if(cur instanceof Tokens.LeftParen){
            return list(tokens);
        }
        else if(cur instanceof Tokens.Vec){
            return vec(tokens);
        }
        tokens.popToken();
    }
    public Nodes.BaseNode constant(TokenStream tokens){
        
    }
    public Nodes.BaseNode application(TokenStream tokens){
        
    }
    public Nodes.BaseNode list(TokenStream tokens){

    }
    public Nodes.BaseNode symbol(TokenStream tokens){
        Tokens.BaseToken cur = tokens.nextToken();
        Tokens.Identifier identifierTemp = identifier(tokens)
        Nodes.Symbol symbolNode = new Nodes.Symbol(identifierTemp.tokenText);
        return symbolNode;
    }
    public Nodes.BaseNode identifier(TokenStream tokens){
        Nodes.Identifier identifier = new Identifier(tokens.nextToken().tokenText);
        tokens.popToken();
        return identifier;
    }
    public List<Nodes.BaseNode> parse(TokenStream tokens) throws Exception {
        List<Nodes.BaseNode> AST = new ArrayList<>();
        /*
        while(tokens.hasNext() == true){
            if(tokens.hasNextNext() == true){
                

            }
            //last token to be put in
            else{

            }
        }
        */
    
        var BoolNode = new Nodes.Bool(true);
        AST.add(BoolNode);
        return AST;
    }
}