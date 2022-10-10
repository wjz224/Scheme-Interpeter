# [CSE 262] This file is a minimal skeleton for a Scheme scanner in Python.  It
# provides a transliteration of the TokenStream class, and the shell of a
# Scanner class.  Please see the README.md file for more discussion.

#Token class that takes in tokentype, rowNum, colNum, and StringLiteral
class Token:
    def __init__(self,tokentype, rowNum, colNum, StringLiteral):
        self.tokentype = tokentype
        self.rowNum = rowNum
        self.colNum = colNum
        self.StringLiteral = StringLiteral
        
#Provided TokenStream class
class TokenStream:
    def __init__(self, tokens):
        self.__tokens = tokens
        self.__next = 0

    def reset(self): self.__next = 0

    def nextToken(self):
        return None if not self.hasNext() else self.__tokens[self.__next]

    def nextNextToken(self):
        return None if not self.hasNextNext() else self.__tokens[self.__next + 1]

    def popToken(self): self.__next += 1

    def hasNext(self): return self.__next < len(self.__tokens)

    def hasNextNext(self): return (self.__next + 1) < len(self.__tokens)


#Beginner of our scanner class
class Scanner:
    #Global variables to keep track of row, column, and StringLiteral
    rowNum = 1
    colNum = 0
    StringLiteral = ""
    
    def __init__(self):
        pass
    #Cleanbreak function that checks for valid epsilon transitions to send to START or
    #valid '(', ')', '\0', ';' to send to LPAREN, RPAREN, EOF, INCOMMENT States
    #else send to CLEANBREAK state
    def cleanbreak(self, c):
        global StringLiteral
        isWhiteSpace = " \t\n\r\0"
        #check is c is whitespace char
        if c in isWhiteSpace:
            self.StringLiteral = ""
            return "START"
        #else check for valid transitions to other states
        else:
            match c:
                case '(':
                    return "LPAREN"
                case ')': 
                    return "RPAREN"
                case '\0':
                    return "EOF"
                case ';':  
                    return "INCOMMENT"  
                case _:
                    return "CLEANBREAK"
    #state start function that checks for valid epsilon transitions to send to valid state
    #valid inputs to IDENTIFIER, INID, INT, ININT, INSTR, VCB, START, PM, ABBREV states
    #else send to ERROR state
    def start(self, c, ahead):
        global StringLiteral
        global rowNum
        global colNum
        validToInid = "!$%&*/:<=>?~_^"
        startC = " \t\n\r\0();"
        #Check if c is a valid IDENTIFIER transition by looking ahead
        if (c in validToInid or c.isalpha()) and ahead in startC:
            return "IDENTIFIER"
        #Check if c is a valid INID transition
        elif c in validToInid or c.isalpha():
            if ahead in validToInid or ahead.isalpha() or ahead.isdigit():
                return "INID"
            else:
                return "ERROR"
        #Check if c is a digit
        elif (c.isdigit()):
            #Check for valid INT state by looking ahead
            if ahead in startC:
                return "INT"
            #Check for valid ININT state
            elif ahead.isdigit() or ahead == '.':
                return "ININT"
            else:
                return "ERROR"
        else:
            #Match case for c to see valid state
            match c:
                #Check if c == '"' 
                case '"': 
                    # Check if ahead is '\0' or '\n', if it is than error because there is an open paren for a string
                    if ahead in "\0\n":
                        return "ERROR"
                    # if its not an '\0' or '\n' than continue to instr
                    else:
                        return "INSTR"
                # Check if c == '#'
                case '#':
                     # If c == '#' than transition to VCB
                    return "VCB"
                # Check if c == '\n'
                case '\n':
                    # If we are in start and we get a new line, than increment rowNum and reset colNum
                    self.rowNum += 1
                    self.colNum = 0
                    return "START"
                # Check if c == '+'
                case '+':
                    # If c == '+' and the the next char is a digit or  a valid character from (startC) than transition to PM
                    if ahead.isdigit() or ahead in startC:
                        return "PM"
                    else:
                    # Otherwise return an error
                        return "ERROR"
                # Check if c == '-'
                case '-':
                    # if  c == '-' and ahead is a digit or a valid character from (startC) than transition to pm
                    if ahead.isdigit() or ahead in startC:
                        return "PM"
                    #Otherwise generate an error
                    else:
                        return "ERROR"
                # Check if c == '\'
                case '\'':
                    # if c == '\' than transition to ABBREV
                    return "ABBREV"
                # default case
                case _:
                    startc = " \t\n\r\0;()"
                    # c is in start c than transition to clean break
                    if c in startc:
                        # clear the literal
                        StringLiteral = ""
                        # return the state from cleanbreak
                        return self.cleanbreak(c)
                    else:
                        return "ERROR"
    # state instr function that checks for valid transitions to STR INSTR+ and INSTR given c and ahead
    def instr(self, c,ahead):
        # Checks if c == '"' 
        if c == '"': 
            # If c is '"' and ahead is not a valid character, than return error because theres an extra character appended to the ending of a string
            if ahead not in " \n\r\t\0;()":
                return "ERROR"
            # If it is a valid character (in essence white spaces and ; () ) than transition to STR
            else: 
                return "STR"
        # if ahead is EOF before the string has a closing '"' than there is an error
        elif ahead == "\0":
            return "ERROR"
        # if c is '\\' than we transition to INSTR+ 
        elif c == '\\':
            return "INSTR+"
        # Anything else we transition INSTR
        else:
            return "INSTR"
    # state instrp function that cheecks for valid transitions to INSTR or error.
    # Transitions to error if it the c is not in the matching case
    def instrp(self, c,ahead):
        global StringLiteral
        # Check if c is a matching case, if it is than append the character literal representation.
        # We need to append the character literal representation because we are reading letter by letter and source has for an extra slash 
        # for example '\\n' in source when in the text it is really '\n'
        # if c is not a matching case than return error
        match c:
            # if c == '"' than append the literal '\"' and transition to INSTR
            case '"':
                self.StringLiteral += '\"'
                return "INSTR"
            # if c == '\\' than append the literal '\\' and transition to INSTR
            case '\\':
                self.StringLiteral += '\\'
                return "INSTR"
            # if c == 't' than append the literal '\t' and transition to INSTR
            case 't':
                self.StringLiteral += '\t'
                return "INSTR"
             # if c == 'n' than append the literal '\n' and transition to INSTR
            case 'n':
                self.StringLiteral += '\n'
                return "INSTR"
             # default
            case _:
                self.StringLiteral += '\\'
                self.StringLiteral += 'c'
                return "ERROR"
    # state vcb function that checks for valid transitions to VEC,PRECHAR, BOOL, OR ERROR
    def vcb(self, c, ahead):
        # Match c with a case, if it is none of the cases than return error
        match c:
            # if c == '(' than transition to VEC
            case '(':
                return "VEC"
             # if c == '\\' transition to PRECHAR if the ahead is not a invalid character.
            case '\\':
                # if ahead is a whitespace, newline, or EOF than there isn't a character given therefore error out.
                if ahead in " \n\r\t\0":
                    return "ERROR"
                # if ahead is not those than it is a valid character therefore transition to prechar
                else:
                    return "PRECHAR"
            # if c == 't'and ahead is a valid character than transition to BOOL, otherwise return error
            case 't':
                 # if ahead is a whitespace, newline, or EOF or () ;, than there is nothing after the t, therefore it is a bool. Transition to bool
                if ahead in " \n\r\t\0;()":
                    return "BOOL"
                # If ahead is not one of those than there is something after the t which extends it which is invalid. Therefore error out
                else:
                    return "ERROR"
             # if c == 'f'
            case 'f':
                # if ahead is a whitespace, newline, or EOF or () ;, than there is nothing after the f, therefore it is a bool. Transition to bool
                if ahead in " \n\r\t\0;()":
                    return "BOOL"
                # If ahead is not one of those than there is something after the f which extends it which is invalid. Therefore error out
                else:
                    return "ERROR"
            # default error trapped in state
            case _:
                return "ERROR"
    # state prechar function that checks for valid transitions to CHAR or ERROR
    def prechar(self, c, ahead):
        # if ahead is a whitespace, newline, EOF, or ; (), than the charcter length is only 3 which is a valid character.
        # Therefore transition to CHAR
        if ahead in " \n\r\t\0;()":
            return "CHAR"
        # If is not one of those than the char is more than a length of 3 therefore Error out.
        else:
            return "ERROR"
    # state incomment  function that checks for valid transitions to START, EOF, and INCOMMENT
    def incomment(self, c):
        global StringLiteral
        global rowNum
        global colNum
        # if c == '\n' while we are in a comment than we are in a newLine 
        # Therefore increment rowNum, colNum and reset the StringLiteral and Transition to START
        if c == "\n":
            self.rowNum+=1
            self.colNum = 0
            self.StringLiteral = ""
            return "START"
        #Check to see if c is EOF char
        elif c == "\0":
            #Reset StringLiteral
            self.StringLiteral = ""
            #Return EOF state
            return "EOF"  
        #Else send to INCOMMENT state
        else:
            return "INCOMMENT"
      
    #inid function that checks for valid transitions to IDENTIFIER, INID, ERROR states
    def inid(self, c, ahead):
        validToInid = "!$%&*/:<=>?~_^.-+"
        inidc = " \t\n\r\0;()"
        #Check if c is a valid IDENTIFIER and ahead is a valid epsilon transition to send to IDENTIFIER state
        if (c in validToInid or c.isalpha() or c.isdigit()) and ahead in inidc:
            return "IDENTIFIER"
        #Check if c is a valid identifier to send to INID state
        elif c in validToInid or c.isalpha() or c.isdigit():
            return "INID"
        #Else send to ERROR state
        else:
            return "ERROR"
       
    #pm function that checks for valid transitions to IDENTIFIER, ININT, ERROR states
    def pm(self, c, ahead):
        pmc = " \t\n\r\0;()"
        #Check if ahead is valid epsilon transition to IDENTIFIER state
        if ahead in pmc:
            return "IDENTIFIER"
        #Check if c is a digit to send to the ININT state
        elif c.isdigit():
            return "ININT"
        #Else send to ERROR state
        else:
            return"ERROR"
        
    #int function that checks for valid transitions to INT, ININT, PREDBL, ERROR states
    def inint(self, c, ahead):
        intc = " \t\n\r\0;()"
        #Check if ahead is a valid epsilon transition to INT state
        if ahead in intc:
            return "INT"
        #Check if c is a digit to send to the ININT state
        elif c.isdigit():
            return "ININT"
        #Check if c is a '.' to send to PREDBL state
        elif c == ".":
            return "PREDBL"
        #Else send to ERROR state
        else:
            return "ERROR"
        
    #predbl function that checks for valid transitions to DBL, INDBL, ERROR states
    def predbl(self, c, ahead):
        predblc = " \t\n\r\0;()"
        #Check if ahead is a valid epsilon transition to DBL state
        if ahead in predblc:
            return "DBL"
        #Check if c is a digit to send to INDBL state
        elif c.isdigit():
            return "INDBL"
        #Else send to ERROR state
        else:
            return "ERROR"
        
    #indbl function that checks for valid transitions to DBL, INDBL, ERROR states
    def indbl(self, c, ahead):
        dblc = " \t\n\r\0;()"
        #Check if ahead is valid epsilon transition to DBL state
        if ahead in dblc:
            return "DBL"
        #Check if c is a digit to loop through INDBL again
        elif c.isdigit():
            return "INDBL"
        #Else send to ERROR state
        else:
            return "ERROR"

    #scanTokens functions that iterates through source and finds all valid tokens in source
    def scanTokens(self, source):
        #globals rowNum, colNum, and StringLiteral
        global rowNum
        global colNum
        global StringLiteral
        #instantiating tokens list
        tokens = []
        #setting state to START
        state = "START"
        #start at i = 0 for while loop
        i = 0
        #while loop to iterate through source
        while(i < len(source)):
            #increment colNum by one
            self.colNum+=1
            #add to StringLiteral
            self.StringLiteral += source[i]
            #check if i is at last index of source and assign ahead if not out of bounds
            if i >= len(source)-1:
                ahead = "\0"
            else:
                ahead = source[i+1]
            #if state == "START"
            if state == "START": 
                #check for keyword 'and' and set state to CLEANBREAK
                if(source.find("and") == i):
                    self.colNum += 2
                    i+=2
                    tokens.append(Token("AndToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'begin' and set state to CLEANBREAK
                elif(source.find("begin") == i):
                    self.colNum += 4
                    i+=4
                    tokens.append(Token("BeginToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'cond' and set state to CLEANBREAK
                elif(source.find("cond") == i):
                    self.colNum += 3
                    i+=3
                    tokens.append(Token("CondToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'define' and set state to CLEANBREAK
                elif(source.find("define") == i):
                    self.colNum += 5
                    i+=5
                    tokens.append(Token("DefineToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'if' and set state to CLEANBREAK
                elif(source.find("if") == i):
                    self.colNum +=1
                    i+=1
                    tokens.append(Token("IfToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'lambda' and set state to CLEANBREAK
                elif(source.find("lambda") == i):
                    self.colNum += 5
                    i+=5
                    tokens.append(Token("LabmdaToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'or' and set state to CLEANBREAK
                elif(source.find("or") == i):
                    self.colNum += 1
                    i+=1
                    tokens.append(Token("OrToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'quote' and set state to CLEANBREAK
                elif(source.find("quote") == i):
                    self.colNum += 4
                    i+=4
                    tokens.append(Token("QuoteToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #check for keyword 'set!' and set state to CLEANBREAK
                elif(source.find("set!") == i):
                    self.colNum += 3
                    i+=3
                    tokens.append(Token("SetToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                #else set state to start(source[i], ahead)
                else:
                    state = self.start(source[i], ahead)
            #elif state == "INSTR" and set state to instr(source[i], ahead)
            elif state == "INSTR":
                state = self.instr(source[i],ahead)
            #elif state == "INSTR+" and set state to instrp(source[i], ahead)
            elif state == "INSTR+":
                state = self.instrp(source[i], ahead)
            #elif state == "VCB" 
            elif state == "VCB":
                #check for \\newline and set state to CHAR
                if(source.find("\\newline") == i):
                    self.colNum += 6
                    i+=7
                    state = "CHAR"
                #check for \\space and set state to CHAR
                elif(source.find("\\space") == i):
                    self.colNum += 4
                    i+=5
                    state = "CHAR"
                #check for \\tab and set state to CHAR
                elif(source.find("\\tab") == i):
                    self.colNum += 2
                    i+=3
                    state = "CHAR"
                #else set state to vcb(source[i], ahead)
                else:
                    state = self.vcb(source[i],ahead)
            #elif state == "PRECHAR" set state to prechar(source[i], ahead)
            elif state == "PRECHAR":
                state = self.prechar(source[i], ahead)
            #elif state == "INID" set state to inid(source[i], ahead)
            elif state == "INID":
                state = self.inid(source[i], ahead)
            #elif state == "PM" set state to pm(source[i], ahead)
            elif state == "PM":
                state = self.pm(source[i], ahead)
            #elif state == "ININT" set state to inint(source[i], ahead)
            elif state == "ININT":
                state = self.inint(source[i], ahead)
            #elif state == "PREDBL" set state to predbl(source[i], ahead)
            elif state == "PREDBL":
                state = self.predbl(source[i], ahead)
            #elif state == "INDBL" set state to indbl(source[i], ahead)
            elif state == "INDBL":
                state = self.indbl(source[i], ahead)
            #elif state == "INCOMMENT" set state to incomment(source[i])
            elif state == "INCOMMENT":
                state = self.incomment(source[i])
            #elif state == "CLEANBREAK" set state to cleanbreak(source[i], ahead)
            elif state == "CLEANBREAK":
                state = self.cleanbreak(source[i], ahead)
            #else state = "ERROR"
            else:
                state = "ERROR"

            #if state == "STR"
            if state == "STR":
                #Appending StrToken
                tokens.append(Token("StrToken", self.rowNum, self.colNum, self.StringLiteral))
                #Set state to CLEANBREAK state
                state = "CLEANBREAK"
            #elif state == "VEC"
            elif state == "VEC":
                #Appending VecToken
                tokens.append(Token("VecToken", self.rowNum, self.colNum, self.StringLiteral))
                #Resetting StringLiteral
                self.StringLiteral = ""
                #Set state to START state
                state = "START"
            #elif state == "CHAR"
            elif state == "CHAR":
                #Appending CharToken
                tokens.append(Token("CharToken", self.rowNum, self.colNum, self.StringLiteral))
                #Set state to CLEANBREAK
                state = "CLEANBREAK"  
            #elif state == "BOOL"     
            elif state == "BOOL":
                #Appending BoolToken
                tokens.append(Token("BoolToken", self.rowNum, self.colNum, self.StringLiteral))
                #Set state to CLEANBREAK state
                state = "CLEANBREAK"
            #elif state == "IDENTIFIER"
            elif state == "IDENTIFIER":
                #Appending IdentifierToken
                tokens.append(Token("IdentifierToken", self.rowNum, self.colNum, self.StringLiteral))
                #Set state to CLEANBREAK state
                state = "CLEANBREAK"
            #elif state == "INT"
            elif state == "INT":
                #Appending IntToken
                tokens.append(Token("IntToken", self.rowNum, self.colNum, self.StringLiteral))
                #Set state to CLEANBREAK state
                state = "CLEANBREAK"
            #elif state == "DBL"
            elif state == "DBL":
                #Appending DblToken
                tokens.append(Token("DblToken", self.rowNum, self.colNum, self.StringLiteral))
                #Set state to CLEANBREAK state
                state = "CLEANBREAK"
            #elif state == "LPAREN"
            elif state == "LPAREN":
                #Appending LeftParenToken
                tokens.append(Token("LeftParenToken", self.rowNum, self.colNum, self.StringLiteral))
                #Resetting StringLiteral
                self.StringLiteral = ""
                #Set state to START state
                state = "START"
            #elif state == "RPAREN"
            elif state == "RPAREN":
                #Apppending RightParenToken
                tokens.append(Token("RightParenToken", self.rowNum, self.colNum, self.StringLiteral))
                #Resetting StringLiteral
                self.StringLiteral = ""
                #Set state to START state
                state = "START"
            #elif state == "ABBREV"
            elif state == "ABBREV":
                #Appending AbbrevToken
                tokens.append(Token("AbbrevToken", self.rowNum, self.colNum, self.StringLiteral))
                #Resetting StringLiteral
                self.StringLiteral = ""
                #Set state to START state
                state = "START"      
            #elif state == "EOF"          
            elif state == "EOF":
                #Appending EofToken
                tokens.append(Token("EofToken", self.rowNum, self.colNum, "End of File"))
                #Returning TokenStream(tokens)
                return TokenStream(tokens)
            #If state == "CLEANBREAK"
            if state == "CLEANBREAK":
                check = " \t\r\n\0();"
                #Check if i is >= len(source)-1 indicating the end of source
                #Assigning newAhead if out of bounds
                if i >= len(source)-1:
                    newAhead = "\0"
                else:
                    newAhead = source[i+1]
                #Checking if newAhead is epsilon transition to START state
                if newAhead in check:
                    self.StringLiteral = ""
                    state = "START"
                #Else appending ErrorToken and returning TokenStream(tokens)
                else:
                    tokens.append(Token("ErrorToken", self.rowNum, self.colNum, newAhead))
                    return TokenStream(tokens)
            #If the state == "ERROR"
            if state == "ERROR":
                #Increment colNum by 1
                self.colNum += 1
                #Append ErrorToken to tokens
                tokens.append(Token("ErrorToken", self.rowNum, self.colNum, ahead))
                #Return TokenStream
                return TokenStream(tokens)
            #increment i in the while loop
            i+=1
        #add one to colNum once we exit the loop
        self.colNum += 1
        #appending an EOF token
        tokens.append(Token("EofToken", self.rowNum, self.colNum, "End of File"))
        #returning TokenStream(tokens)
        return TokenStream(tokens)
        
#tokenToXML function that retuns a string to display our tokentype, rowNum, colNum, and StringLiteral
def tokenToXml(token):
    tokenXml = "<" + token.tokentype +" line=" + str(token.rowNum) + " col=" + str(token.colNum) +" val='" + token.StringLiteral +"' />"
    return tokenXml
         
