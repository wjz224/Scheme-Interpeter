package edu.lehigh.cse262.slang.Scanner;

import java.util.ArrayList;

/**
 * Scanner is responsible for taking a string that is the source code of a
 * program, and transforming it into a stream of tokens.
 *
 * [CSE 262] It is tempting to think "if my code doesn't crash when I give it
 * good input, then I have done a good job". However, a good scanner needs to be
 * able to handle incorrect programs. The bare minimum is that the scanner
 * should not crash if the input is invalid. Even better is if the scanner can
 * print a useful diagnostic message about the point in the source code that was
 * incorrect. Best, of course, is if the scanner can somehow "recover" and keep
 * on scanning, so that it can report additional syntax errors.
 *
 * [CSE 262] **In this class, "even better" is good enough for full credit**
 *
 * [CSE 262] With that said, if you make a scanner that can report multiple
 * errors, I'll give you extra credit. But you'll have to let me know that
 * you're doing this... I won't check for it on my own.
 *
 * [CSE 262] This is the only java file that you need to edit in p1. The
 * reference solution has ~240 lines of code (plus 43 blank lines and ~210 lines
 * of comments). Your code may be longer or shorter... the line-of-code count is
 * just a reference.
 *
 * [CSE 262] You are allowed to add private methods and fields to this class.
 * You may also add imports.
 */
public class Scanner {
    enum STATE {
        START, CLEANBREAK, ERROR, 
        INSTR, STR, INSTR_PLUS, 
        VCB, VEC, PRECHAR, CHAR, BOOL,
        AND, BEGIN, COND, DEFINE, IF, LAMBDA, OR, QUOTE, SET,
        PM,INID, ININT, PREDBL, INDBL, INT, DBL, IDENTIFIER;
       // actualString stores the actual string that a string token's value should contain
       private static String actualString = "";
       // textLiteral is the literal text that the token represents.
       private static String textLiteral = "";     
       // row count that keeps track of the line number
       private static int row = 0;
       // col count that keeps track of the char count in the current line number
       private static int col = 0;
        /***
         * Get method for the STATE's row (line number)
         * @return int
         */
        public static int getRow(){
            return row;
        }
        /***
         * Get method for the STATE's col (char number in line)
         * @return int
         */
        public static int getCol(){
            return col;
        }
        /***
         * Get method for the STATE's actualString
         * @return String
         */
        public static String getString(){
            return actualString;
        }
         /***
         * Get method for the STATE's textLiteral
         * @return String
         */
        public static String getLiteral(){
            return textLiteral;
        }
        /***
         * Setter method for STATE's actualString
         */
        public static void setString(String s){
            actualString = s;
        }
        /***
         * Setter method for STATE's textLiteral
         */
        public static void setLiteral(String s){
            textLiteral = s;
        }

        /***
         * start state method that keeps looping itself through cleanbreak until a " is found indicating the start of the a string and transitions to STATE.INSTR by returning STATE.INSTR
         * @param c
         * @returns a state either STATE.INSTR or STATE.START or STATE.VCB
         */
        public static STATE start(char c){
            // current state is START
            col++;
            STATE state = STATE.START;
            // char[] validtoINID = {'!','$','%','&','*','/',':','<','=','>','?','~','_','^'};
            String validToInid = "!$%&*/:<=>?~_^";
            if(validToInid.contains("" + c)|| Character.isLetter(c)){
                state = STATE.INID;
            }
            else if(Character.isDigit(c)){
                state = STATE.ININT;
            }
            else{
                // Switch statement that checks the char for a transition.
                switch(c){
                    case '"':
                        // Encountering a '"' transitions the from START to INSTR
                        state = STATE.INSTR;
                        break;
                    case '#':
                        // Encountering a '#'' transitions the from START to INSTR
                        state = STATE.VCB;
                        break;
                    case '\n':
                        // if we are not searching for a token and new line is hit, than we are going to a new line. Increment rows
                        row++;
                        // if we are not in a new line/row, than reset col.
                        col = 0;
                        break;
                    case '+':
                        state = STATE.PM;
                        break; 
                    case '-':
                        state = STATE.PM;
                        break;
                    default:
                        // Default go back to STATE.START and clear the text Literal. 
                        STATE.setLiteral("");
                        STATE.setString("");
                        state = STATE.START;
                        break;
                }
            }
            // Add character c to textLiteral which is the literal that the token would be off of. 
            // If the state is STATE.start than do not add literal because we did not get char that can transition

            
            // return the state that we transitioned to from the char c.
            return state;
        }
        public static STATE inid(char c){
            String validToInid = "!$%&*/:<=>?~_^";
            STATE state = STATE.INID;
            if(validToInid.contains("" + c)|| Character.isLetter(c) || Character.isDigit(c)){
                return state;
            }
            else{
                state = STATE.IDENTIFIER;
                return state;
            }   
        }
        public static STATE pm(char c){
            STATE state = STATE.PM;
            col++;
            if(Character.isDigit(c)){
                state = STATE.ININT;
            }
            else{
                state = STATE.IDENTIFIER;
            }
            return state;
        }
        public static STATE inint(char c){
            STATE state = STATE.ININT;
            if(Character.isDigit(c)){
                state = STATE.ININT;
            }
            else if(c == '.'){
                state = STATE.PREDBL;
            }
            else{
                state = STATE.INT;
            }
            return state;
        }
        public static STATE predbl(char c){
            STATE state = STATE.PREDBL;
            if(Character.isDigit(c)){
                state = STATE.INDBL;
            }
            return state;
        }
        public static STATE indbl(char c){
            STATE state = STATE.INDBL;
            if(Character.isDigit(c)){
                state = STATE.INDBL;
            }
            else{
                state = STATE.DBL;
            }
            return state;
        }
            
        /***
         * instr state method that transitions to STR if c is '"' or INSTR_PLUS if c is '\\' and add c to the actualString. 
         * If it is any other character just transition back to STATE.INSTR  and add c to actualString.
         * @param c
         * @return a state either STATE.STR, STATE.INSTR_PLUS, or STATE.INSTR.
         */
        public static STATE instr(char c){
            // current state is INSTR
            STATE state = STATE.INSTR;
            // increment col since we are reading ta new char.
            col++;
            switch(c){
                case '"':
                     // Encountering a '"' transitions the from INSTR to STR indicating the end of a string
                    state = STATE.STR;
                    break;
                case '\\':
                    // add c to actualString, which is what the  String tokens value would store.
                    // Encountering a '\\' transitions the from INSTR to INSTR_PLUS
                    state = STATE.INSTR_PLUS;
                    break;
                default:
                    // Any other character just loop back to INSTR and add the char.
                    actualString+=c;
                    // transition back to INSTR.
                    state = STATE.INSTR;
                    break;
            }
            // add c to text literal.

            return state;
        }
        public static STATE instr_plus(char c){
            // increment col since we are reading ta new char.
            col++;
            // current state is INSTR_PLUS
            STATE state = STATE.INSTR_PLUS;
            switch(c){
                case '"':
                    // Encountering a '"' transitions the from INSTR_PLUS to INSTR
                    state = STATE.INSTR;
                    textLiteral += '\"';
                    break;
                case '\\':
                    // Encountering a '"' transitions the from INSTR_PLUS to INSTR
                    state = STATE.INSTR;
                    actualString += '\\';
                    textLiteral += '\\';
                    break;
                case 't':
                    // Encountering a 't' transitions the from INSTR_PLUS to INSTR
                    state = STATE.INSTR;
                    actualString += '\t';
                    textLiteral += '\t';
                    break;
                case 'n':
                     // Encountering a 'n' transitions the from INSTR_PLUS to INSTR
                    state = STATE.INSTR;
                    actualString += '\n';
                    textLiteral += '\n';
                    break;
                default:
                    // any other character u are got an error and go to STATE.error which is a trapping state
                    state = STATE.ERROR;
                    break;
            }
            // add c to textLiteral and actualString
            return state;
        }

        // END OF STRING FDA
        
        // BEGINNING OF VEC_CHAR_BOOL FDA
        public static STATE vcb(char c){
            col++;
            STATE state = STATE.VCB;
            System.out.println("IN VCB");
            switch(c){
                case '(':
                    state = STATE.VEC;
                    break;    
                case '\\':
                    state = STATE.PRECHAR;
                    break;
                case 't':
                    state = STATE.BOOL;
                    break;
                case 'f':
                    state = STATE.BOOL;
                    break;
            }
            return state;      
        }
        
        public static STATE prechar(char c){
            col++;
            STATE state = STATE.PRECHAR;
            if(c == ' '){
                state = STATE.ERROR;
            }
            else{
                state = STATE.CHAR;
            }
            
            return state;
        }
        

    }
    /** Construct a scanner */
    public Scanner() {
    }

    /**
     * scanTokens works through the `source` and transforms it into a list of
     * tokens. It adds an EOF token at the end, unless there is an error.
     *
     * @param source The source code of the program, as one big string, or a
     *               line of code from the REPL.
     *
     * @return A list of tokens
     */
    public TokenStream scanTokens(String source) {
        // [CSE 262] Right now, this function is not implemented, so we are returning an
        // error token.
        
        var tokens = new ArrayList<Tokens.BaseToken>();
        //tokens.add(error);

        STATE state = STATE.START;
        int index = 0; // first index of string from source
        // for loop that goes through the source string and creates tokens from the string.
        for(int i = 0; i < source.length(); i++){
            // c stores the char at current index
            char c = source.charAt(i);
            //System.out.println("in for loop");
            if((state == STATE.INSTR && (c == '\\')) || state == STATE.INSTR_PLUS ){
            }
            else{
                System.out.println("char: " + c);
                STATE.setLiteral(STATE.getLiteral() + c);
                System.out.println(STATE.getLiteral());
            }

            
            switch(state){
                case START:  
                    // state is currently in STATE.START, call STATE.start(c) to get the state to transition to based on c.
                    if(source.indexOf("and") == i){
                        i += 2;
                        var tokAnd = new Tokens.And(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokAnd);
                     }
                     else if(source.indexOf("begin") == i){
                        i += 4;
                        var tokBegin = new Tokens.Begin(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokBegin);
                     }
                     else if(source.indexOf("cond") == i){
                        i += 3;
                        var tokCond = new Tokens.Cond(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokCond);
                     }
                     else if(source.indexOf("define") == i){
                        i += 5;
                        var tokDefine = new Tokens.Define(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokDefine);
                     }
                     else if(source.indexOf("if") == i){
                        i += 1;
                        var tokIf = new Tokens.If(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokIf);
                     }
                     else if(source.indexOf("lambda") == i){
                        i += 5;
                        var tokLambda = new Tokens.Lambda(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokLambda);
                     }
                   
                     else if(source.indexOf("or") == i){
                        i += 1;
                        var tokOr = new Tokens.Or(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokOr);
                     }
            
                     else if(source.indexOf("quote") == i){
                        i += 4;
                        var tokQuote = new Tokens.Quote(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokQuote);
                     }
                     else if(source.indexOf("set!") == i){
                        i += 3;
                        var tokSet = new Tokens.Set(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                        state = STATE.CLEANBREAK;
                        tokens.add(tokSet);
                     }
                     else{
                        state = STATE.start(c);
                     }
        
                        
                    //System.out.println("in start");
                    break;
                case INSTR:
                    // state is currently in STATE.INSTR, call STATE.instr(c) to get the state to transition to based on c.
                    state = STATE.instr(c);
                    break;
                case INSTR_PLUS:
                    // state is currently in STATE.INSTR_PLUS, call STATE.instr_plus(c) to get the state to transition to based on c.
                    state = STATE.instr_plus(c);
                    //System.out.println("in INSTR PLUS");
                    break;
                case VCB:           
                    if(source.indexOf("\\newline") == i || source.indexOf("\\space") == i || source.indexOf("\\tab") == i){
                        state = STATE.CHAR;
                    }
                    else{
                        state = STATE.vcb(c);
                    }
                    break;
                case PRECHAR:
                    if(source.indexOf("\\newline") == i || source.indexOf("\\space") == i || source.indexOf("\\tab") == i){
                        state = STATE.CHAR;
                    }
                    else{
                        state = STATE.prechar(c);
                    }
                    break;
                case INID:
                    state = STATE.inid(c);
                    break;
                case PM:
                    state = STATE.pm(c);
                    break;
                case ININT:
                    state = STATE.inint(c);
                    break;
                case PREDBL:
                    state = STATE.predbl(c);
                    break;
                case INDBL:
                    state = STATE.indbl(c);
                    break;
            }  

            // if state is ever in an accepting state, create and add the token of the respective type and add it to the tokens array.
            if(state == STATE.STR){
                // state is currently in STATE.STR. This is an accepting state. 
                // Create the token as a String by calling Tokens.Str() constructor with the textLiteral, row, col, and actualString stored in state.
                System.out.println("LITERAL: " + STATE.getLiteral() + "TEST");
                var tokStr = new Tokens.Str(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), STATE.getString());
                // add the token to the  tokens array.
                tokens.add(tokStr);
                state = STATE.CLEANBREAK;
            }
            else if(state == STATE.VEC){
                var tokVec = new Tokens.Vec(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                tokens.add(tokVec);
                state = STATE.START;
            }
            else if(state == STATE.CHAR){
                if(i == source.length() - 1){
                    System.out.println("IN CHAR");
                    var tokChar = new Tokens.Char(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), STATE.getLiteral().charAt(i));
                    state = STATE.CLEANBREAK;
                    tokens.add(tokChar);
                }
                else{
                    state = STATE.ERROR;
                }
            }
            else if(state == STATE.BOOL){
                System.out.println("IN BOOL");
                if(STATE.getLiteral().charAt(i) == 't'){
                    var tokBool = new Tokens.Bool(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), true);
                    tokens.add(tokBool);
                }
                else{
                    var tokBool = new Tokens.Bool(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), false);
                    tokens.add(tokBool);
                }
                state = STATE.CLEANBREAK;
            }
            else if(state == STATE.IDENTIFIER || (state == STATE.INID  && i == source.length() - 1)){
                var tokIden = new Tokens.Identifier(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                state = STATE.CLEANBREAK;
                tokens.add(tokIden);
            }
            else if(state == STATE.INT || (state == STATE.ININT && (i == source.length() - 1))){
                var tokInt = new Tokens.Int(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), Integer.parseInt(STATE.getLiteral()));
                state = STATE.CLEANBREAK;
                tokens.add(tokInt);
            }
            else if(state == STATE.DBL || (state == STATE.INDBL && (i == source.length() -1) )){
                var tokDbl = new Tokens.Dbl(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), Double.parseDouble(STATE.getLiteral()));
                state = STATE.CLEANBREAK;
                tokens.add(tokDbl);
            }

            if(state == STATE.CLEANBREAK){
                // each time a function is called, increment col since we are reading a new character
                if(i != source.length() - 1 && source.charAt(i+1) != '\n' && source.charAt(i+1) != ' ' && source.charAt(i+1) != '(' && source.charAt(i+1) != ')'){
                    System.out.println("i value:" + i);
                    state = STATE.ERROR;
                }
                else{
                    // if we are in the CLEANBREAK state than clear the textLiteral and actualString and go to STATE.STARt
                    STATE.setLiteral("");
                    STATE.setString("");
                    state = STATE.START;
                }
    
            }
            if (state == STATE.ERROR){
                if((STATE.getLiteral().equals("\\newline") || (STATE.getLiteral().equals("\\space")) || (STATE.getLiteral().equals("\\tab")) )){
                    var tokChar = new Tokens.Char(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), STATE.getLiteral().charAt(i));
                    state = STATE.CLEANBREAK;
                    tokens.add(tokChar);
                }       
            }
            // if state is  not in an accepting state or at CLEANBREAK by the end, than it was trapped in a state, therefore throw an error.
            else if((state != STATE.STR && state != STATE.START && state != STATE.CHAR && state != STATE.BOOL && state != STATE.IDENTIFIER && state != STATE.INT && state != STATE.DBL) && i == source.length() - 1 ){
                // create error token
                var error = new Tokens.Error("ERROR tokenLiteral: " + STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                // add error token
                tokens.add(error);
                // return TokenStream with the tokens array.
                return new TokenStream(tokens);
            }
            System.out.println(state);
        }
        System.out.println(state +" ending state");
        var EOF = new Tokens.Eof("End of file", STATE.getRow(), STATE.getCol());
        tokens.add(EOF);
        return new TokenStream(tokens);
    }
}