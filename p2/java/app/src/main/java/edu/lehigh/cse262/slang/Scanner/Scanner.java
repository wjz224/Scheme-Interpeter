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
        START, INSTR, STR, INSTR_PLUS, CLEANBREAK, ERROR;
       private static String actualString = "";
       private static String textLiteral = "";     
       private static int row = 0;
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
         * start state method that keeps looping itself until a " is found indicating the start of the a string and transitions to STATE.INSTR by returning STATE.INSTR
         * @param c
         * @returns a state either STATE.INSTR or STATE.START
         */
        public static STATE start(char c){
            STATE state = STATE.START;
            // each time a function is called, increment col which represents the character
            col++;
            switch(c){
                case '"':
                    textLiteral += c;
                    state = STATE.INSTR;
                    break;
                default:
                    state = STATE.CLEANBREAK;
                    break;
            }
            return state;
        }
        /***
         * 
         * @param c
         * @return
         */
        public static STATE instr(char c){
            STATE state = STATE.INSTR;
            col++;
            switch(c){
                case '"':
                    state = STATE.STR;
                    break;
                case '\\':
                    actualString+=c;
                    state = STATE.INSTR_PLUS;
                    break;
                default:
                    actualString+=c;
                    state = STATE.INSTR;
                    break;
            }
            textLiteral+=c;
            return state;
        }
        public static STATE instr_plus(char c){
            col++;
            STATE state = STATE.INSTR_PLUS;
            switch(c){
                case '"':
                    state = STATE.INSTR;
                    break;
                case '\\':
                    state = STATE.INSTR;
                    break;
                case 't':
                    state = STATE.INSTR;
                    break;
                case 'n':
                    state = STATE.INSTR;
                    break;
                default:
                    state = STATE.ERROR;
                    break;
            }
            textLiteral+=c;
            actualString+=c;
            return state;
        }
        public static STATE str(){
            // each time a string is found, that should be a line statement done thereofore move increment row which represents the line number
            row++;
            return STATE.STR;
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
         
        for(int i = 0; i < source.length(); i++){
            char c = source.charAt(i);
            //System.out.println("in for loop");
            switch(state){
                case START:
                    //System.out.println("in start");
                    state = STATE.start(c);
                    break;
                case INSTR:
                    //System.out.println("in INSTR");
                    state = STATE.instr(c);
                    //System.out.println("LITERAL:" + STATE.getLiteral());
                    //System.out.println("STRING:" + STATE.getString());
                    //System.out.println(state);
                    break;
                case INSTR_PLUS:
                    //System.out.println("in INSTR PLUS");
                    state = STATE.instr_plus(c);
                    break;
                case CLEANBREAK:
                    if(c != ' '){
                        state = STATE.start(c);
                    }
                    break;
            } 
            //System.out.println(state);
            if(state == STATE.STR){
                //System.out.println("in STR");
                state = STATE.str();
                var tokStr = new Tokens.Str(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), STATE.getString());
                //System.out.println("LITERAL:" + STATE.getLiteral());
                //System.out.println("STRING:" + STATE.getString());
                tokens.add(tokStr);
                state = STATE.CLEANBREAK;
            }
            else if((state != STATE.STR && state != STATE.CLEANBREAK) && i == source.length() - 1 ){
                //System.out.println(state);
                var error = new Tokens.Error("ERROR tokenLiteral: " + STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                tokens.add(error);
                //System.out.println("SOURCE LENGTH:" + source.length());
                return new TokenStream(tokens);
            }
            if(state == STATE.CLEANBREAK){
                STATE.setString("");
                STATE.setLiteral("");
            }
        }
        var EOF = new Tokens.Eof("End of file", STATE.getRow(), STATE.getCol());
        tokens.add(EOF);
        return new TokenStream(tokens);
    }
}