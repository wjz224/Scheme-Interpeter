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
    /** Construct a scanner */
    enum STATE {
        START, INSTR, STR, INSTR_PLUS, CLEANBREAK;
        
        public STATE start(char c){
            STATE state = STATE.START;
            if(c == '"'){
                state = STATE.INSTR;
            }
            return state;
        }
        public STATE instr(char c){
            STATE state = STATE.INSTR;
            switch(c){
                case '"':
                    state = STR;
                    break;
                case '\\':
                    state = INSTR_PLUS;
                    break;
                default:
                    state = INSTR;
                    break;
            }
            return state;
        }
        public STATE instr_plus(char c){
            STATE state = STATE.INSTR_PLUS;
            switch(c){
                case '\"':
                    state = INSTR;
                    break;
                case '\\':
                    state = INSTR;
                    break;
                case 't':
                    state = INSTR;
                    break;
                case 'n':
                    state = INSTR;
                    break;
                default:
                    state = CLEANBREAK;
                    System.out.println("INSTR_PLUS error");
                    break;
            }
            return state;
        }
        public STATE str(char c){
            
            STATE state = STATE.STR;
            state = CLEANBREAK;
            return state;
        }
        
    }
    


    
    
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
        var error = new Tokens.Error("scanTokens is not implemented yet", -1, -1);
        var tokens = new ArrayList<Tokens.BaseToken>();
        tokens.add(error);
        return new TokenStream(tokens);
    }
}