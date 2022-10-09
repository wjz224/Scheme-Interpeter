package edu.lehigh.cse262.slang.Scanner;

import java.util.ArrayList;

import edu.lehigh.cse262.slang.Scanner.Tokens.Abbrev;

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
        PM,INID, ININT, PREDBL, INDBL, INT, DBL, IDENTIFIER,
        ABBREV,LPAREN,RPAREN,INCOMMENT,EOF;
       // actualString stores the actual string that a string token's value should contain
       private static String actualString = "";
       // textLiteral is the literal text that the token represents.
       private static String textLiteral = "";     
       // row count that keeps track of the line number
       private static int row = 1;
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
         * Setter method for STATE's col (char number in line)
         */
        public static void setCol(int c){
            col = c;
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
            STATE state = STATE.START;
            //String to check for certain epsilon transitions
            String validToInid = "!$%&*/:<=>?~_^";
            //inid transition
            if(validToInid.contains("" + c) || Character.isLetter(c)){
                state = STATE.INID;
            }
            //innit transition
            else if(Character.isDigit(c)){
                state = STATE.ININT;
            }
            //all other state transitions
            else{
                // Switch statement that checks the char for a transition.
                switch(c){
                    case '"':
                        // Encountering a '"' transitions the from START to INSTR
                        state = STATE.INSTR;
                        break;
                    case '#':
                        // Encountering a '#' transitions the from START to INSTR
                        state = STATE.VCB;
                        break;
                    case '\n':
                        // if we are not searching for a token and new line is hit, than we are going to a new line. Increment rows
                        row++;
                        // if we are not in a new line/row, than reset col.
                        col = 0;
                        break;
                    case '+':
                        // Encountering a '+' transitions the from START to PM
                        state = STATE.PM;
                        break; 
                    case '-':
                        // Encountering a '-' transitions the from START to PM
                        state = STATE.PM;
                        break;
                    case '\'':
                        // Encountering a ''' transitions the from START to PM
                        state = STATE.ABBREV;
                        break;
                    default:
                        // Epsilon transitions from START to CLEANBREAK
                        String startc = " \t\n\r\0;()";
                        if(startc.contains("" + c)){
                            STATE.setLiteral("");
                            STATE.setString("");
                            state = STATE.cleanbreak(c);
                        }
                        // Else sends to error state
                        else{
                            state = STATE.ERROR;
                        }
                        break;
                } 
            }
            // return the state that we transitioned to from the char c.
            return state;
        }

        /***
         * cleanbreak state method that finds valid START epsilon transition,'(', ')', '\0', ';' indicating START/LPAREN/RPAREN/EOF/INCOMMENT state 
         * @param c
         * @returns a state either STATE.START or STATE.LPAREN or STATE.RPAREN or STATE.EOF or STATE.INCOMMENT
         */
        public static STATE cleanbreak(char c){
            //set current state to CLEANBREAK
            STATE state = STATE.CLEANBREAK;
            String startCheck = " \t\n\r";
            //String to check if c is a valid epsilon transition to STATE.START
            if(startCheck.contains(""+ c)){
                state = STATE.START;
            }
            else{
                // Switch statement that checks the char for a transition.
                switch(c){
                    case '(':
                        // Encountering a '(' transitions the from CLEANBREAK to LPAREN
                        state = STATE.LPAREN;
                        break;
                    case ')':
                        // Encountering a ')' transitions the from CLEANBREAK to RPAREN
                        state = STATE.RPAREN;
                        break;
                    case '\0':
                        // Encountering a '\0' transitions the from CLEANBREAK to EOF
                        state = STATE.EOF;
                        break;
                    case ';':
                        // Encountering a ';' transitions the from CLEANBREAK to INCOMMENT
                        state = STATE.INCOMMENT;
                        break;
                }
            }
            //return the state that we transitioned to from c
            return state;
        }
        
        //BEGINNING OF THE SPECIAL DFA

        /***
         * incomment state method that finds valid epsilon transition indicating valid START, EOF, INCOMMENT STATE
         * @param c
         * @returns a state either STATE.START or STATE.EOF or STATE.INCOMMENT
         */
        public static STATE incomment(char c){
            STATE state = STATE.INCOMMENT;
            String stateC = "\n";
            String eofC = "\0";
            //String to check for valid '\n' char to send to START state
            if(stateC.contains(""+c)){
                row++;
                col = 0;
                state = STATE.START;
                STATE.setLiteral("");
                STATE.setLiteral("");
            }
            //String to check for valid '\0' char to send to EOF state
            else if(eofC.contains(""+c)){
                state = STATE.EOF;
                STATE.setLiteral("");
                STATE.setLiteral("");
            }
            //Else loop through STATE.INCOMMENT
            else{
                state = STATE.INCOMMENT;   
            } 
            //return the state that we transitioned to from c
            return state;   
        }
                
        //END OF THE SPECIAL DFA
        //BEGINNING OF THE IDS_NUMS DFA

        /***
         * inid state method that keeps looping itself until a valid identifier/error is found indicating either an Identifier token or Error token
         * @param c
         * @returns a state either STATE.INID or STATE.IDENTIFIER or STATE.ERROR
         */
        public static STATE inid(char c){
            String validToInid = "!$%&*/:<=>?~_^.-+";
            STATE state = STATE.INID;
            String inidc = " \t\n\r";
            //String to check for certain epsilon transitions to identifier state
            if(inidc.contains("" + c)){
                state = STATE.IDENTIFIER;
            }
            //String to loop through inid state if valid inid character is passed through
            else if(validToInid.contains("" + c)|| Character.isLetter(c) || Character.isDigit(c)){
                state = STATE.INID;
            }
            //else send to STATE.ERROR
            else{
                state = STATE.ERROR;
            }   
            // return the state that we transitioned to from the char c.
            return state;
        }
        /***
         * pm state method that finds valid identifier/int, indicating either an Identifier token, Error token, or the INNIT state
         * @param c
         * @returns a state either STATE.IDENTIFIER or STATE.ININT or STATE.ERROR
         */
        public static STATE pm(char c){
            STATE state = STATE.PM;
            String pmc = " \t\n\r\0";
            //String to see if c is a valid +/- identifier token
            if(pmc.contains(""+c)){
                state = STATE.IDENTIFIER;
            }
            //Checks to see if there is an int to send to the ININT state
            else if(Character.isDigit(c)){
                state = STATE.ININT;
            }
            //else send to STATE.ERROR
            else{
                state = STATE.ERROR;
            }
            // return the state that we transitioned to from the char c.
            return state;
        }
        /***
         * inint state method that loops through ININT until epsilon transition, '.', or error state, indicating either an Int token, Error token, INNIT state, or PREDBL state
         * @param c
         * @returns a state either STATE.INT or STATE.ININT or STATE.PREDBL or STATE.ERROR
         */
        public static STATE inint(char c){
            STATE state = STATE.ININT;
            String intc = " \t\r\n\0";
            //String to see if there is certain epsilon transitions indicating an Int Token
            if(intc.contains(""+c)){
                state = STATE.INT;
            }
            //Checks if c is another int sending it back to the ININT state
            else if(Character.isDigit(c)){
                state = STATE.ININT;
            }
            //Checks to see if '.' is found indicating PREDBL state
            else if(c == '.'){
                state = STATE.PREDBL;
            }
            //else send to STATE.ERROR
            else{
                state = STATE.ERROR;
            }
            // return the state that we transitioned to from the char
            return state;
        }
        /***
         * predbl state method that finds valid indbl, indicating either a valid INDBL state or Error token
         * @param c
         * @returns a state either STATE.INDBL or STATE.ERROR
         */
        public static STATE predbl(char c){
            STATE state = STATE.PREDBL;
            //Check if c is an int thus indicating a INDBL state
            if(Character.isDigit(c)){
                state = STATE.INDBL;
            }
            //Else send to STATE.ERROR
            else{
                state = STATE.ERROR;
            }
            // return the state that we transitioned to from the char
            return state;
        }
        /***
         * indbl state method that finds valid dbl/indbl/error, indicating either an DBL token, Error token, or the INDBL state
         * @param c
         * @returns a state either STATE.DBL or STATE.INDBL or STATE.ERROR
         */
        public static STATE indbl(char c){
            STATE state = STATE.INDBL;
            String doubleC = " \t\n\r\0";
            //String to check if there is certain epsilon transitions indicatnig a DBL Token
            if(doubleC.contains(""+c)){
                state = STATE.DBL;
            }
            //Check to see if c is an int indicating a INDBL state
            else if(Character.isDigit(c)){
                state = STATE.INDBL;
            }
            //Else send to STATE.ERROR
            else{
                state = STATE.ERROR;
            }
            //return the state that we transitioned to from the char
            return state;
        }
            
        //END OF THE IDS_NUMS DFA
        //BEGINNING OF THE STRINGS DFA

        /***
         * instr state method that transitions to STR if c is '"' or INSTR_PLUS if c is '\\' and add c to the actualString. 
         * If it is any other character just transition back to STATE.INSTR  and add c to actualString.
         * @param c
         * @return a state either STATE.STR, STATE.INSTR_PLUS, or STATE.INSTR.
         */
        public static STATE instr(char c){
            // current state is INSTR
            STATE state = STATE.INSTR;
            // Switch statement that checks the char for a transition.
            switch(c){
                case '"':
                     // Encountering a '"' transitions the from INSTR to STR indicating the end of a string
                    state = STATE.STR;
                    break;
                case '\\':
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
            //return the state that we transitioned to from the char
            return state;
        }
        /***
         * instr_plus state method that finds valid instr/error, indicating either an Error state or INSTR state
         * @param c
         * @returns a state either STATE.INSTR or STATE.ERROR
         */
        public static STATE instr_plus(char c){
            STATE state = STATE.INSTR_PLUS;
            // Switch statement that checks the char for a transition.
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
                    // any other character go to STATE.ERROR
                    actualString += '\\';
                    actualString += c;
                    textLiteral += '\\';
                    textLiteral +=  c;
                    state = STATE.ERROR;
                    break;
            }
            //return the state that we transitioned to from the char
            return state;
        }

        // END OF STRING DFA        
        // BEGINNING OF VEC_CHAR_BOOL FDA

        /***
         * vcb state method that finds valid '(', '\', 't', 'f' indicating valid VEC, PRECHAR, BOOL, or ERROR STATE
         * @param c
         * @returns a state either STATE.INSTR or STATE.ERROR
         */
        public static STATE vcb(char c){
            STATE state = STATE.VCB;
            //Switch statement that checks the char for a transition            
            switch(c){
                case '(':
                    // Encountering a '(' transitions the from VCB to VEC
                    state = STATE.VEC;
                    break;    
                case '\\':
                    // Encountering a '\' transitions the from VCB to PRECHAR
                    state = STATE.PRECHAR;
                    break;
                case 't':
                    // Encountering a 't' transitions the from VCB to BOOL
                    state = STATE.BOOL;
                    break;
                case 'f':
                    // Encountering a 'f' transitions the from VCB to BOOL
                    state = STATE.BOOL;
                    break;
                default:
                    // Default transition to STATE.ERROR
                    state = STATE.ERROR;
                    break;
            }
            //return the state that we transitioned to from c
            return state;      
        }
        /***
         * prechar state method that keeps looping itself until valid char found indicating either an Identifier token or Error token
         * @param c
         * @returns a state either STATE.INID or STATE.IDENTIFIER or STATE.ERROR
         */
        public static STATE prechar(char c){
            String charC = " \t\n\r\0";
            STATE state = STATE.PRECHAR;
            //String to see if c is valid epsilon transition to send back to prechar state
            if(charC.contains("" + c)){
                state = STATE.PRECHAR;
            }
            //else valid char token
            else{
                state = STATE.CHAR;
            }
            //return the state that we transitioned to from c
            return state;
        }

        //END OF VEC_CHAR_BOOL FDA 

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
        int charInt = 0;
        // for loop that goes through the source string and transitions states based on each character read. When we are at an accepting state, create the token.
        for(int i = 0; i < source.length(); i++){
            // c stores the character at current index
            char c = source.charAt(i);
            // Increment col for each new character we read from the strict
            STATE.setCol(STATE.getCol() + 1);
            // If we are in a String DFA state and the character input is '\', than we cannot add the character literal until we know the specific character literal.
            // This is because a String + '\' will result in "\\". Therefore we need to add the specific character literal EX. '\t'.
            // Also if we are get '\n' or ' ', its just empty white space and new line so we just skip over it and dont add it to our text literal..
            if((state == STATE.INSTR && (c == '\\')) || state == STATE.INSTR_PLUS || c == '\n' || c == ' '){
            }
            // All other characters are added to the text Literal
            else{
                STATE.setLiteral(STATE.getLiteral() + c);
            }
            
            // switch statements that transitions given the current state based on the current character.
            switch(state){
                case START:  
                    // If we are at START and the we find the start of a keyword is at the same index we are at, than create the keyword token. 
                    // Increment i to get to the end index (so we don't read over the remaining redundant characters) and go to cleanbreak and add the token to tokens.
                    // The keywords at "and", "begin", "cond", "define", "if" ,"lambda", "or", "quote","set!"
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
               
                    break;
                case INSTR:
                    // state is currently in STATE.INSTR, call STATE.instr(c) to get the state to transition to based on c.
                    state = STATE.instr(c);
                    break;
                case INSTR_PLUS:
                    // state is currently in STATE.INSTR_PLUS, call STATE.instr_plus(c) to get the state to transition to based on c.
                    state = STATE.instr_plus(c);
                    break;
                case VCB:           
                    // We are going to use charInt store the first char in the keyword to pass in as a value for when we create the CHAR token.
                    // Store the current index + 1 in charInt.
                    charInt = i+1;
                     // If we are in state VCB and if our current index is at the start of a specific keyword, than transition to STATE.char
                     // increment i so that it is at the end index of the keyword so we don't read over the same remaining characters.
                     // the keywords in VCB are  "\newline" "\space" "\tab"
                    if(source.indexOf("\\newline") == i){
                        i += 6;
                        state = STATE.CHAR;
                    }
                    else if(source.indexOf("\\space") == i){
                        i += 4;
                        state = STATE.CHAR;
                    }
                    else if(source.indexOf("\\tab") == i){
                        i += 2;
                        state = STATE.CHAR;
                    }
                    // If it not a keyword, call STATE.vcb(c) to get the state to transition to based on c from state vcb.
                    else{
                        state = STATE.vcb(c);
                    }
                    break;
                case PRECHAR:
                    // state is currently in PRECHAR, call STATE.prechar(c) to get the state to transition to based on c.
                    state = STATE.prechar(c);
                    break;
                case INID:
                    // state is currently in INID, call STATE.inid(c) to get the state to transition to based on c.
                    state = STATE.inid(c);
                    break;
                case PM:
                    // state is currently in PM, call STATE.pm(c) to get the state to transition to based on c.
                    state = STATE.pm(c);
                    break;
                case ININT:
                    // state is currently in ININT, call STATE.inint(c) to get the state to transition to based on c.
                    state = STATE.inint(c);
                    break;
                case PREDBL:
                    // state is currently in PM, call STATE.pm(c) to get the state to transition to based on c.
                    state = STATE.predbl(c);
                    break;
                case INDBL:
                    // state is currently in INDBL, call STATE.indbl(c) to get the state to transition to based on c.
                    state = STATE.indbl(c);
                    break;
                case INCOMMENT:
                    // state is currently in INCOMMENT, call STATE.incomment(c) to get the state to transition to based on c.
                    state = STATE.incomment(c);
                    break;
                case CLEANBREAK:
                    // state is currently in CLEANBREAK, call STATE.CLEANBREAK(c) to get the state to transition to based on c.
                    state = STATE.cleanbreak(c);
                    break;
            }  
            // These if and else if statements check if state is ever in an accepting state. 
            // If we are in an accepting state than create and add the token of the respective type and add it to the tokens array.
            if(state == STATE.STR){
                // state is currently in STATE.STR. This is an accepting state. 
                // Create the token as a String by calling Tokens.Str() constructor with the textLiteral, row, col, and actualString stored in state.
                var tokStr = new Tokens.Str(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), STATE.getString());
                // add the new string token to the tokens array.
                tokens.add(tokStr);
                // Transition to CLEANBREAK after creating String token
                state = STATE.CLEANBREAK;
            }
            else if(state == STATE.VEC){
                // state is currently in STATE.VEC. This is an accepting state. 
                // Create the token as a Vec by calling Tokens.Vec() constructor with the textLiteral, row, and col stored in state.
                var tokVec = new Tokens.Vec(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                // add the new vec token to the tokens array
                tokens.add(tokVec);
                // since we don't go to clean break, our code needs to reset Literal and string here.
                STATE.setLiteral("");
                STATE.setString("");
                // Transition to START after creating Vec token
                state = STATE.START;
            }
            else if(state == STATE.CHAR && (STATE.getLiteral().length() > 2)){
                // state is currently in STATE.CHAR. This is an accepting state.
                // the length check is to make sure that length is atleast 3 because our PRECHAR function doesnt takes care of errors of chars with less than 3 in length. 
                // Create the token as a Char by calling Tokens.Char() constructor with the textLiteral, row,col  stored in state and current character of source at charInt.
                var tokChar = new Tokens.Char(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), source.charAt(charInt));
                // if charInt is  0, than that we means we did not encounter a keyword from PRECHAR, therefore use i to get the char.
                if (charInt == 0){
                    tokChar = new Tokens.Char(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), source.charAt(i));
                }
                // reset charInt after creating token
                charInt = 0;
                // Transition to CLEANBREAK after creating CHAR token
                state = STATE.CLEANBREAK;
                // add the new vec token to the tokens array
                tokens.add(tokChar);
            }
            else if(state == STATE.BOOL){
                 // state is currently in STATE.BOOL. This is an accepting state.
                 // if we are in state BOOL and our char is t than pass in true as the value
                if(STATE.getLiteral().charAt(i) == 't'){
                    // Create the token as a Bool by calling Tokens.Bool() constructor with the textLiteral, row, col stored in state and true.
                    var tokBool = new Tokens.Bool(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), true);
                    // add new token to tokens array.
                    tokens.add(tokBool);
                }
                // if we are in state BOOL and our char is not t than pass in false as value.
                else{
                    // Create the token as a Bool by calling Tokens.Bool() constructor with the textLiteral, row, col stored in state and true.
                    var tokBool = new Tokens.Bool(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), false);
                    // add new bool token to tokens array.
                    tokens.add(tokBool);
                }
                // Transition to CLEANBREAK after creating Bool token
                state = STATE.CLEANBREAK;
            }
            // If the state is an identifier, or we are at EOF and in INID, or we are in INID and we are not at EOF and the next character is a white space of some kind, than it is an identifier token.
            else if(state == STATE.IDENTIFIER || (state == STATE.INID && (i == source.length() - 1)) || (state == STATE.INID && i != source.length() -1 && " \t\n\r\0();".contains(""+ source.charAt(i+1)))){
                 // Create the token as a Identifier by calling Tokens.Identifier() constructor with the textLiteral, row, and col stored in state.
                var tokIden = new Tokens.Identifier(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                 // Transition to CLEANBREAK after creating Identifier token
                state = STATE.CLEANBREAK;
                // add new identifier token to tokens array.
                tokens.add(tokIden);
            }
            // If the state is an INT, or we are at EOF and in ININT, or we are in ININT and we are not at EOF and the next character is a white space of some kind, than it is an INT token.
            else if(state == STATE.INT || (state == STATE.ININT && (i == source.length() - 1)) || (state == STATE.ININT && i != source.length() -1 && " \t\n\r\0();".contains(""+ source.charAt(i+1)))){
                // Create the token as an INT by calling Tokens.int() constructor with the textLiteral, row, and col stored in state.
                var tokInt = new Tokens.Int(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), Integer.parseInt(STATE.getLiteral()));
                // Transition to CLEANBREAK after creating INT token
                state = STATE.CLEANBREAK;
                // add new int token to tokens array.
                tokens.add(tokInt);
            } 
            // If the state is an DBL, or we are at EOF and in INDBL, or we are in INDBL and we are not at EOF and the next character is a white space of some kind, than it is an DBL token.
            else if(state == STATE.DBL || (state == STATE.INDBL && (i == source.length() -1) || (state == STATE.INDBL && i != source.length() -1 && " \t\n\r\0();".contains(""+ source.charAt(i+1))) )){
                // Create the token as an DBL by calling Tokens.dbl() constructor with the textLiteral, row, and col stored in state.
                var tokDbl = new Tokens.Dbl(STATE.getLiteral(), STATE.getRow(), STATE.getCol(), Double.parseDouble(STATE.getLiteral()));
                // Transition to CLEANBREAK after creating DBL token
                state = STATE.CLEANBREAK;
                 // add new dbl token to tokens array.
                tokens.add(tokDbl);
            }
            else if(state == STATE.LPAREN){
                // state is currently in STATE.LPAREN. This is an accepting state.
                // Create the token as an LeftParen by calling Tokens.LeftParen() constructor with the textLiteral, row, and col stored in state.
                var tokLparen = new Tokens.LeftParen(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                // Transition to START after creating LeftParen token.
                state = STATE.START;
                // since we don't go to clean break, our code needs to reset Literal and string here.
                STATE.setLiteral("");
                STATE.setString("");
                // add new LeftParen token to tokens array
                tokens.add(tokLparen);
            }
            else if(state == STATE.RPAREN){
                // state is currently in STATE.RPAREN. This is an accepting state.
                // Create the token as an LeftParen by calling Tokens.RightParen() constructor with the textLiteral, row, and col stored in state.
                var tokRparen = new Tokens.RightParen(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                // Transition to START after creating RightParen token
                state = STATE.START;
                // since we don't go to clean break, our code needs to reset Literal and string here.
                STATE.setLiteral("");
                STATE.setString("");
                // add new RightParen token to tokens array
                tokens.add(tokRparen);
            }
            else if(state == STATE.ABBREV){
                // state is currently in STATE.ABBREV. This is an accepting state.
                // Create the token as an Abbrev by calling Tokens.Abbrev() constructor with the textLiteral, row, and col stored in state.
                var tokAbbrev = new Tokens.Abbrev(STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                // Transition to START after creating ABBREV token
                state = STATE.START;
                // since we don't go to clean break, our code needs to reset Literal and string here.
                STATE.setLiteral("");
                STATE.setString("");
                // add new Abbrev token to tokens array
                tokens.add(tokAbbrev);
            }
            else if(state == STATE.EOF){
                // state is currently in STATE.EOF. This is an accepting state.
                // Create the token as an EOF by calling Tokens.EOF() constructor with the textLiteral, row, and col stored in state.
                var EOF = new Tokens.Eof("End of file", STATE.getRow(), STATE.getCol());
                // add new EOF token to tokens array.
                tokens.add(EOF);
                // Since we are add EOF, just return the TokenStream.
                return new TokenStream(tokens);
            }

            if(state == STATE.CLEANBREAK){
                // each time a function is called, increment col since we are reading a new character
                if(i != source.length() - 1 && source.charAt(i+1) != '\n' && source.charAt(i+1) != ' ' && source.charAt(i+1) != '(' && source.charAt(i+1) != ')'){
                    state = STATE.ERROR;
                }
                else{
                    // if we are in the CLEANBREAK state than clear the textLiteral and actualString and go to STATE.STARt
                    STATE.setLiteral("");
                    STATE.setString("");
                    state = STATE.START;
                }
            }
            // if state is  not in an accepting state or at Error by the end, than it was trapped in a state, therefore throw an error.
            if(((state != STATE.STR && state != STATE.START && state != STATE.CHAR && state != STATE.BOOL && state != STATE.IDENTIFIER && state != STATE.INT && state != STATE.DBL && state != STATE.ABBREV && state != STATE.LPAREN && state != STATE.RPAREN && state != STATE.EOF && state != STATE.INCOMMENT) && i == source.length() - 1 ) || state == STATE.ERROR){
                // create error token
                System.out.println("END STATE" + state);
                var error = new Tokens.Error("ERROR tokenLiteral: " + STATE.getLiteral(), STATE.getRow(), STATE.getCol());
                // add error token
                tokens.add(error);
                STATE.setLiteral("");
                STATE.setString("");
                // return TokenStream with the tokens array.
                return new TokenStream(tokens);
            }
        }
        // If we finish the String than we are EOF.
        // Create EOF token using Tokens.Eof() constructor with the textLiteral, row, and col stored in state.
        var EOF = new Tokens.Eof("End of file", STATE.getRow(), STATE.getCol());
        // Add new EOF token to tokens array after for loop
        tokens.add(EOF);
        // return the TokenStream with the tokens array.
        return new TokenStream(tokens);
    }
} 
