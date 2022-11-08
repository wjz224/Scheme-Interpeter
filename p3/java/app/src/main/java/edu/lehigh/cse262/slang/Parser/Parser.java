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
            Tokens.Identifer identifier = cur;
            Nodes.Identifier iden = new Identifier(identifier.tokenText);
            tokens.popToken();
            Nodes.Define define = BaseNode(iden, expression(tokens));
            return define;
        }
    }
    public Nodes.BaseNode expression(TokenStream tokens){
        Tokens.BaseToken cur = tokens.nextToken();
        Tokens.BaseToken ahead = tokens.nextNextToken();
        if(cur instanceof Tokens.LeftParen){
            if(ahead instanceof Tokens.Quote){
                tokens.popToken();
                tokens.popToken();
                Nodes.Quote quote = new Quote(datum(tokens));
                if(cur instanceof Tokens.RightParen){
                    return quote;
                }
            } 
            else if(ahead instanceof Tokens.Lambda){
                tokens.popToken();
                tokens.popToken();
                Nodes.Lambda lambda = new Lambda(formal(tokens), body(tokens));
                
            }  
            else if(ahead instanceof Tokens.If){
                // figure out later
                tokens.popToken();
                tokens.popToken();
            }
            else if(ahead instanceof Tokens.Set){
                tokens.popToken();
                cur = tokens.nextToken();
                ahead = tokens.nextNextToken();
                if(ahead !instanceof Tokens.Identifer){
                    throw new Exception("Error");
                }
                else{
                    tokens.popToken();
                    tokens.popToken();
                    return expression(tokens);
                }
            }
            else if(ahead instanceof Tokens.And){
                tokens.popToken();
                tokens.popToken();
                return expression(tokens);
            }
            else if(ahead instanceof Tokens.Or){
                tokens.popToken();
                tokens.popToken();
                return expression(tokens);
            }
            else if(ahead instanceof Tokens.Begin){
                tokens.popToken();
                tokens.popToken();
                return expression(tokens);
            }
            else if(ahead instanceof Tokens.Cond){
                tokens.popToken();
                tokens.popToken();
                return expression(tokens);
            }
            
            else{
                return application(tokens);
            }
        }
        else if(cur instanceof Tokens.Abbrev){
            return TickNode(datum(tokens));
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
            Nodes.Tick tick = new Nodes.Tick(boolNode);
            return tick;
        }
        else if(cur instanceof Tokens.Int){
            Tokens.Int intTemp = (Tokens.Int) cur;
            Nodes.Int intNode = new Nodes.Int(intTemp.literal);
            Nodes.Tick tick = new Nodes.Tick(intNode);
            return tick;
        }
        else if(cur instanceof Tokens.Dbl){
            Tokens.Dbl dblTemp = (Tokens.Dbl) cur;
            Nodes.Dbl dblNode = new Nodes.Dbl(dblTemp.literal);
            Nodes.Tick tick = new Nodes.Tick(dblNode);
            return tick;
        }
        else if(cur instanceof Tokens.Char){
            Tokens.Char charTemp = (Tokens.Char) cur;
            Nodes.Char charNode = new Nodes.Char(charTemp.literal);
            Nodes.Tick tick = new Nodes.Tick(charNode);
            return tick;
        }
        else if(cur instanceof Tokens.Str){
            Tokens.Str strTemp = (Tokens.Str) cur;
            Nodes.Str strNode = new Nodes.Str(strTemp.literal);
            Nodes.Tick tick = new Nodes.Tick(strNode);
            return tick;
        }
        else if(cur instanceof Tokens.Identifier){
            Nodes.Tick tick = new Nodes.Tick((IValue) symbol(tokens));
            return tick;
        }
        else if(cur instanceof Tokens.LeftParen){
            return list(tokens);
        }
        else if(cur instanceof Tokens.Vec){
            return vec(tokens);
        }
    }
    public Nodes.BaseNode constant(TokenStream tokens){
        
    }
    public Nodes.BaseNode application(TokenStream tokens){
        
    }
    public Nodes.BaseNode symbol(TokenStream tokens){
    
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