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
     */
    public List<Nodes.BaseNode> program(TokenStream tokens){
        try{
            List<Nodes.BaseNode> AST = new ArrayList<>();
            while(!(tokens.nextToken() instanceof Tokens.Eof)){
                AST.add(form(tokens));
            }
            return AST;
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
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
        try{
            if(!(cur instanceof Tokens.Identifier)){
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
        catch(Exception e){
            e.getMessage();
        }
        return null;
      
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
                Nodes.LambdaDef lambdaNode = new Nodes.LambdaDef(formals(tokens), body(tokens));
                cur = tokens.nextToken();
                try{
                    if(cur instanceof Tokens.RightParen){
                        // pop the right paren
                        tokens.popToken();
                        return lambdaNode;
                    }
                    else{
                        throw new Exception("Invalid");
                    }
                }catch(Exception e){
                    e.getMessage();
                }
                return null;
            }  
            else if(ahead instanceof Tokens.If){
                // figure out later
                tokens.popToken();
                tokens.popToken();
                Nodes.If ifNode = new Nodes.If(expression(tokens), expression(tokens), expression(tokens));
                cur = tokens.nextToken();
                try{
                    if(cur instanceof Tokens.RightParen){
                        // pop the right paren
                        tokens.popToken();
                        return ifNode;
                    }
                    else{
                        throw new Exception("Invalid");
                    }
                } catch(Exception e){
                   e.getMessage();
                }
                return null;
                
            }
            else if(ahead instanceof Tokens.Set){
                tokens.popToken();
                cur = tokens.nextToken();
                ahead = tokens.nextNextToken();
                try{
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
                } catch(Exception e){
                    e.getMessage();
                }
                return null;
              
            }
            else if(ahead instanceof Tokens.And){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> andList = new ArrayList<>();
                cur = tokens.nextToken();
                try{
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid");
                    }
                    else{
                        while(!(cur instanceof Tokens.RightParen)){
                            andList.add(expression(tokens));
                            cur = tokens.nextToken();
                        }
                    }
                }catch(Exception e){
                    e.getMessage();
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
                try{
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
                } catch(Exception e){
                    e.getMessage();
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
                try{
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid");
                    }
                    else{
                        while(!(cur instanceof Tokens.RightParen)){
                            beginList.add(expression(tokens));
                            cur = tokens.nextToken();
                        }
                    }
                }catch(Exception e){
                    e.getMessage();
                }
                // pop the right paren
                tokens.popToken();
                Nodes.Begin beginNode = new Nodes.Begin(beginList);
                return beginNode;
            }
            else if(ahead instanceof Tokens.Cond){
                tokens.popToken();
                tokens.popToken();
                // get remainder expressions for evaluation.
                List<Nodes.Cond.Condition> condList = new ArrayList<>();
                cur = tokens.nextToken();
                try{
                    if(cur instanceof Tokens.RightParen){
                        throw new Exception("Invalid");
                    }
                    else{
                        while(!(cur instanceof Tokens.RightParen)){
                            condList.add(condition(tokens));
                            cur = tokens.nextToken();
                        }
                    }
                }catch(Exception e){
                    e.getMessage();
                }
                // pop the right token
                tokens.popToken();
                Nodes.Cond condNode = new Nodes.Cond(condList);
                return condNode;
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
    public Nodes.Cond.Condition condition(TokenStream tokens){  
 
        List<Nodes.BaseNode> listExp = new ArrayList<>();
        try{
            if(tokens.nextToken() instanceof Tokens.LeftParen){
                // pop the left paren
                tokens.popToken();
                Tokens.BaseToken cur = tokens.nextToken();
                Nodes.BaseNode test = expression(tokens);
                while(!(cur instanceof Tokens.RightParen)){
                    listExp.add(expression(tokens));
                }
                Nodes.Cond.Condition condition = new Nodes.Cond.Condition(test,listExp);
                return condition;
            }
            else{
                throw new Exception ("Invalid Condition");
            }
        }catch(Exception e){
            e.getMessage();
        }
    
        return null;
    }
    public Nodes.BaseNode datum(TokenStream tokens){
        tokens.popToken();
        Tokens.BaseToken cur = tokens.nextToken();
        if(cur instanceof Tokens.Bool){
            Tokens.Bool boolTemp = (Tokens.Bool) cur;
            Nodes.Bool boolNode = new Nodes.Bool(boolTemp.literal);
            tokens.popToken();
            return boolNode;
        }
        else if(cur instanceof Tokens.Int){
            Tokens.Int intTemp = (Tokens.Int) cur;
            Nodes.Int intNode = new Nodes.Int(intTemp.literal);
            tokens.popToken();
            return intNode;
        }
        else if(cur instanceof Tokens.Dbl){
            Tokens.Dbl dblTemp = (Tokens.Dbl) cur;
            Nodes.Dbl dblNode = new Nodes.Dbl(dblTemp.literal);
            tokens.popToken();
            return dblNode;
        }
        else if(cur instanceof Tokens.Char){
            Tokens.Char charTemp = (Tokens.Char) cur;
            Nodes.Char charNode = new Nodes.Char(charTemp.literal);
            tokens.popToken();
            return charNode;
        }
        else if(cur instanceof Tokens.Str){
            Tokens.Str strTemp = (Tokens.Str) cur;
            Nodes.Str strNode = new Nodes.Str(strTemp.literal);
            tokens.popToken();
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
        else{
            return null;
        }
      
    }
    public Nodes.BaseNode vec(TokenStream tokens){
        try{
            tokens.popToken();
            Tokens.BaseToken cur = tokens.nextToken();
            List<IValue> list = new ArrayList<>();
            while(!(cur instanceof Tokens.RightParen)){
                list.add((IValue) datum(tokens));
            }
            Nodes.Vec vec = new Nodes.Vec(list);
            return vec;
        }
        catch(Exception e){
             e.getMessage();
        }
        return null;
    }
    public List<Nodes.Identifier> formals(TokenStream tokens){
        try{
            Tokens.BaseToken cur = tokens.nextToken();
            List<Nodes.Identifier> iden = new ArrayList<>();
            if(cur instanceof Tokens.LeftParen){
                while(cur instanceof Tokens.Identifier){
                    Nodes.Identifier identifier = new Identifier(cur.tokenText);
                    iden.add(identifier);
                    tokens.popToken();
                    cur = tokens.nextToken();
                }
                if(cur instanceof Tokens.RightParen){
                    tokens.popToken();
                    return iden;
                }
            }
            return iden;
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    public List<Nodes.BaseNode> body(TokenStream tokens){
        try{
            Tokens.BaseToken cur = tokens.nextToken();
            Tokens.BaseToken ahead = tokens.nextNextToken();
            List<Nodes.BaseNode> nodes = new ArrayList<>();
            while(cur instanceof Tokens.LeftParen && ahead instanceof Tokens.Define){
                nodes.add(definition(tokens));
            }
            nodes.add(expression(tokens));
            return nodes;    
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    public Nodes.BaseNode constant(TokenStream tokens){
        try{
            tokens.popToken();
            Tokens.BaseToken cur = tokens.nextToken();
            if(cur instanceof Tokens.Bool){
                Tokens.Bool boolTemp = (Tokens.Bool) cur;
                Nodes.Bool boolNode = new Nodes.Bool(boolTemp.literal);
                tokens.popToken();
                return boolNode;
            }
            else if(cur instanceof Tokens.Int){
                Tokens.Int intTemp = (Tokens.Int) cur;
                Nodes.Int intNode = new Nodes.Int(intTemp.literal);
                tokens.popToken();
                return intNode;
            }
            else if(cur instanceof Tokens.Dbl){
                Tokens.Dbl dblTemp = (Tokens.Dbl) cur;
                Nodes.Dbl dblNode = new Nodes.Dbl(dblTemp.literal);
                tokens.popToken();
                return dblNode;
            }
            else if(cur instanceof Tokens.Char){
                Tokens.Char charTemp = (Tokens.Char) cur;
                Nodes.Char charNode = new Nodes.Char(charTemp.literal);
                tokens.popToken();
                return charNode;
            }
            else if(cur instanceof Tokens.Str){
                Tokens.Str strTemp = (Tokens.Str) cur;
                Nodes.Str strNode = new Nodes.Str(strTemp.literal);
                tokens.popToken();
                return strNode;
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    public Nodes.BaseNode application(TokenStream tokens){
        try{
            tokens.popToken();
            List<Nodes.BaseNode> expressionList = new ArrayList<>();
            Tokens.BaseToken cur = tokens.nextToken();
            if(cur instanceof Tokens.RightParen){
                throw new Exception("Invalid");
            }
            else{
                while(!(cur instanceof Tokens.RightParen)){
                    expressionList.add(expression(tokens));
                    cur = tokens.nextToken();
                }
            }
            // pop the right paren
            tokens.popToken();
        }
        catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    public Nodes.BaseNode list(TokenStream tokens){
        //pop lparen
        tokens.popToken();
        Tokens.BaseToken cur = tokens.nextToken();
        List<IValue> list = new ArrayList<>();
        while(!(cur instanceof Tokens.RightParen)){
            list.add((IValue) datum(tokens));
        }
        Nodes.Cons empty =  new Nodes.Cons((IValue)null,(IValue)null);
        try{
            Nodes.Cons consNode = new Nodes.Cons(list,empty);
            //pop rparen
            tokens.popToken();
            return consNode;
        } 
        catch(Exception exception){
            System.out.println("Cannot construct Cons from empty list");
        }      
        return null;
    }
    public Nodes.BaseNode symbol(TokenStream tokens){
        Nodes.Identifier identifierTemp = new Identifier(tokens.nextToken().tokenText);
        Nodes.Symbol symbolNode = new Nodes.Symbol(identifierTemp);
        tokens.popToken();
        return symbolNode;
    }
    public Nodes.BaseNode identifier(TokenStream tokens){
        Nodes.Identifier identifier = new Identifier(tokens.nextToken().tokenText);
        tokens.popToken();
        return identifier;
    }
    public List<Nodes.BaseNode> parse(TokenStream tokens) throws Exception {
        //List<Nodes.BaseNode> AST = program(tokens);
        List<Nodes.BaseNode> AST = new ArrayList<>();
        AST.add(new Nodes.Bool(false));
        
        return AST;
    }
}