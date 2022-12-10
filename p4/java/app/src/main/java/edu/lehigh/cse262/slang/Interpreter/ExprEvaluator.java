package edu.lehigh.cse262.slang.Interpreter;

import java.util.List;
import java.util.ArrayList;

import edu.lehigh.cse262.slang.Env.Env;
import edu.lehigh.cse262.slang.Parser.IAstVisitor;
import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;

/**
 * ExprEvaluator evaluates an AST node. It is the heart of the evaluation
 * portion of our interpreter.
 */
public class ExprEvaluator implements IAstVisitor<IValue> {
    /** The environment in which to do the evaluation */
    private Env env;

    /** Construct an ExprEvaluator by providing an environment */
    public ExprEvaluator(Env env) {
        this.env = env;
    }

    /** Interpret an Identifier */
    @Override
    public IValue visitIdentifier(Nodes.Identifier expr) throws Exception {
        if(env.get(expr.name) != null){
            return env.get(expr.name);
        }
        else{
            throw new Exception ("Null value for identifier error");
        }
    }

    /**
     * Interpret a Define special-form
     *
     * NB: it's OK for this to return null, because definitions aren't
     * expressions
     */
    @Override
    public IValue visitDefine(Nodes.Define expr) throws Exception {
        env.put(expr.identifier.name, expr.expression.visitValue(this));
        return null;
    }

    /** Interpret a Bool value */
    @Override
    public IValue visitBool(Nodes.Bool expr) throws Exception {
        return expr;
    }

    /** Interpret an Int value */
    @Override
    public IValue visitInt(Nodes.Int expr) throws Exception {
        return expr;
    }

    /** Interpret a Dbl value */
    @Override
    public IValue visitDbl(Nodes.Dbl expr) throws Exception {
        return expr;
    }

    /** Interpret a Lambda value */
    @Override
    public IValue visitLambdaVal(Nodes.LambdaVal expr) throws Exception {
       return expr;
    }

    /**
     * Interpret a Lambda definition by creating a Lambda value from it in the
     * current environment
     */
    @Override
    public IValue visitLambdaDef(Nodes.LambdaDef expr) throws Exception {
        return new Nodes.LambdaVal(env, expr);
    }

    /** Interpret an If expression */
    @Override
    public IValue visitIf(Nodes.If expr) throws Exception {
        if(expr.cond.visitValue(this) == env.poundF){
            return expr.ifFalse.visitValue(this);
        }
        return expr.ifTrue.visitValue(this);
    }

    /**
     * Interpret a set! special form. As with Define, this isn't an expression,
     * so it can return null
     */
    @Override
    public IValue visitSet(Nodes.Set expr) throws Exception {
        if(expr.identifier.name != null){
            env.update(expr.identifier.name, expr.expression.visitValue(this));
        }
        else{
            throw new Exception("Invalid identifier name");
        }
        return null;
    }

    /** Interpret an And expression */
    @Override
    public IValue visitAnd(Nodes.And expr) throws Exception {
        for(int i = 0; i < expr.expressions.size(); i++){
            if(expr.expressions.get(i).visitValue(this) == env.poundF){
                return env.poundF;
            }
        }
        return env.poundT;        
    }

    /** Interpret an Or expression */
    @Override
    public IValue visitOr(Nodes.Or expr) throws Exception {
        for(int i = 0; i < expr.expressions.size(); i++){
            if(expr.expressions.get(i).visitValue(this) == env.poundT){
                return env.poundT;
            }
        }
        return env.poundF;
    }

    /** Interpret a Begin expression */
    @Override
    public IValue visitBegin(Nodes.Begin expr) throws Exception {
        for(int i = 0; i < expr.expressions.size()-2; i++){
            expr.expressions.get(0).visitValue(this);
        }
        return expr.expressions.get(expr.expressions.size()-1).visitValue(this);
    }

    /** Interpret a "not special form" expression */
    @Override
    public IValue visitApply(Nodes.Apply expr) throws Exception {
        List<IValue> args = new ArrayList<>();
        IValue firstValue = expr.expressions.get(0).visitValue(this);
        if(firstValue instanceof Nodes.BuiltInFunc){
            for(int i = 1; i < expr.expressions.size()-1; i++){
                args.add(expr.expressions.get(i).visitValue(this));
            }
            return ((Nodes.BuiltInFunc)firstValue).func.execute(args);
        }
        else if(firstValue instanceof Nodes.LambdaVal){
            if(((Nodes.LambdaVal) firstValue).lambda.formals.size() + 1 == expr.expressions.size()){
               Nodes.LambdaVal lv = (Nodes.LambdaVal) firstValue;
               Env tempEnv = Env.makeInner(lv.env);
               var lambda_visitor = new ExprEvaluator(tempEnv);
               for(int i = 1; i < expr.expressions.size(); i++){
                    env.put(lv.lambda.formals.get(i - 1).name, expr.expressions.get(i).visitValue(this));
                }
                for(int i = 0; i < lv.lambda.body.size() - 1; i++){
                    lv.lambda.body.get(i).visitValue(lambda_visitor);
                }
                return lv.lambda.body.get(lv.lambda.body.size() - 1).visitValue(lambda_visitor);
            }
            else{
                throw new Exception("Number of Formals does not Equal arguments");
            }
        }
        

        return null;
    }

    /** Interpret a Cons value */
    @Override
    public IValue visitCons(Nodes.Cons expr) throws Exception {
        return expr;
    }

    /** Interpret a Vec value */
    @Override
    public IValue visitVec(Nodes.Vec expr) throws Exception {
         return expr;
    }

    /** Interpret a Symbol value */
    @Override
    public IValue visitSymbol(Nodes.Symbol expr) throws Exception {
        return expr;
    }

    /** Interpret a Quote expression */
    @Override
    public IValue visitQuote(Nodes.Quote expr) throws Exception {
        return expr.datum;
    }
    
    /** Interpret a quoted datum expression */
    @Override
    public IValue visitTick(Nodes.Tick expr) throws Exception {
        return expr.datum;
    }

    /** Interpret a Char value */
    @Override
    public IValue visitChar(Nodes.Char expr) throws Exception {
        return expr;
    }

    /** Interpret a Str value */
    @Override
    public IValue visitStr(Nodes.Str expr) throws Exception {
        return expr;
    }

    /** Interpret a Built-In Function value */
    @Override
    public IValue visitBuiltInFunc(Nodes.BuiltInFunc expr) throws Exception {
        return expr;
    }

    /** Interpret a Cons expression */
    @Override
    public IValue visitCond(Nodes.Cond expr) throws Exception {
        for(int i = 0; i < expr.conditions.size(); i++){
            if(expr.conditions.get(i).test.visitValue(this) == env.poundT){
                return (new Nodes.Begin(expr.conditions.get(i).expressions).visitValue(this));
            }
        }
        return env.poundF;
    }
}