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
    #state function that checks for valid epsilon transitions to send to valid state
    #valid inputs to IDENTIFIER, INID, INT, ININT, INSTR, VCB, START, PM, ABBREV states
    #else send to ERROR state
    def start(self, c, ahead):
        global StringLiteral
        global rowNum
        global colNum
        validToInid = "!$%&*/:<=>?~_^"
        startC = " \t\n\r\0"
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

            match c:
                case '"': 
                    if ahead in "\0\n":
                        return "ERROR"
                    else:
                        return "INSTR"
                case '#':
                    return "VCB"
                case '\n':
                    self.rowNum += 1
                    self.colNum = 0
                    return "START"
                case '+':
                    if ahead.isdigit() or ahead in startC:
                        return "PM"
                    else:
                        return "ERROR"
                case '-':
                    if ahead.isdigit() or ahead in startC:
                        return "PM"
                    else:
                        return "ERROR"
                case '\'':
                    return "ABBREV"
                case _:
                    startc = " \t\n\r\0;()"
                    if c in startc:
                        StringLiteral = ""
                        return self.cleanbreak(c)
                    else:
                        return "ERROR"

    def instr(self, c,ahead):
        if c == '"': 
            if ahead not in " \n\r\t\0;()":
                return "ERROR"
            else: 
                return "STR"
        elif ahead == "\0":
            return "ERROR"
        elif c == '\\':
            return "INSTR+"
        else:
            return "INSTR"
        
    def instrp(self, c,ahead):
        global StringLiteral
        match c:
            case '"':
                self.StringLiteral += '\"'
                return "INSTR"
            case '\\':
                self.StringLiteral += '\\'
                return "INSTR"
            case 't':
                self.StringLiteral += '\t'
                return "INSTR"
            case 'n':
                self.StringLiteral += '\n'
                return "INSTR"
            case _:
                self.StringLiteral += '\\'
                self.StringLiteral += 'c'
                return "ERROR"
    def vcb(self, c, ahead):
        match c:
            case '(':
                return "VEC"
            case '\\':
                if ahead in " \n\r\t\0":
                    return "ERROR"
                else:
                    return "PRECHAR"
            case 't':
                if ahead in " \n\r\t\0;()":
                    return "BOOL"
                else:
                    return "ERROR"
            case 'f':
                if ahead in " \n\r\t\0;()":
                    return "BOOL"
                else:
                    return "ERROR"
            case _:
                return "ERROR"
    def prechar(self, c, ahead):
        if ahead in " \n\r\t\0;()":
            return "CHAR"
        else:
            return "ERROR"
    
    def incomment(self, c):
        global StringLiteral
        global rowNum
        global colNum
        if c == "\n":
            self.rowNum+=1
            self.colNum = 0
            self.StringLiteral = ""
            return "START"
        elif c == "\0":
            self.StringLiteral = ""
            return "EOF"  
        else:
            return "INCOMMENT"
      

    def inid(self, c, ahead):
        validToInid = "!$%&*/:<=>?~_^.-+"
        inidc = " \t\n\r\0;()"
        if (c in validToInid or c.isalpha() or c.isdigit()) and ahead in inidc:
            return "IDENTIFIER"
        elif c in validToInid or c.isalpha() or c.isdigit():
            return "INID"
        else:
            return "ERROR"
       
            
    def pm(self, c, ahead):
        pmc = " \t\n\r\0;()"
        if ahead in pmc:
            return "IDENTIFIER"
        elif c.isdigit():
            return "ININT"
        else:
            return"ERROR"
        
    
    def inint(self, c, ahead):
        intc = " \t\n\r\0;()"
        if ahead in intc:
            return "INT"
        elif c.isdigit():
            return "ININT"
        elif c == ".":
            return "PREDBL"
        else:
            return "ERROR"
        
        
    def predbl(self, c, ahead):
        predblc = " \t\n\r\0;()"
        if ahead in predblc:
            return "DBL"
        elif c.isdigit():
            return "INDBL"
        else:
            return "ERROR"
        
    
    def indbl(self, c, ahead):
        dblc = " \t\n\r\0;()"
        print(ahead)
        if ahead in dblc:
            return "DBL"
        elif c.isdigit():
            return "INDBL"
        else:
            return "ERROR"


    def scanTokens(self, source):
        global rowNum
        global colNum
        global StringLiteral
        tokens = []
        # Empty array to stores tokens
        
        state = "START"
        i = 0
        while(i < len(source)):
            self.colNum+=1
            self.StringLiteral += source[i]
            if i >= len(source)-1:
                ahead = "\0"
            else:
                ahead = source[i+1]
            if state == "START":
                if(source.find("and") == i):
                    self.colNum += 2
                    i+=2
                    tokens.append(Token("AndToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("begin") == i):
                    self.colNum += 4
                    i+=4
                    tokens.append(Token("BeginToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("cond") == i):
                    self.colNum += 3
                    i+=3
                    tokens.append(Token("CondToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("define") == i):
                    self.colNum += 5
                    i+=5
                    tokens.append(Token("DefineToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("if") == i):
                    self.colNum +=1
                    i+=1
                    tokens.append(Token("IfToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("lambda") == i):
                    self.colNum += 5
                    i+=5
                    tokens.append(Token("LabmdaToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("or") == i):
                    self.colNum += 1
                    i+=1
                    tokens.append(Token("OrToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("quote") == i):
                    self.colNum += 4
                    i+=4
                    tokens.append(Token("QuoteToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                elif(source.find("set!") == i):
                    self.colNum += 3
                    i+=3
                    tokens.append(Token("SetToken", self.rowNum, self.colNum, self.StringLiteral))
                    state = "CLEANBREAK"
                else:
                    state = self.start(source[i], ahead)
            elif state == "INSTR":
                state = self.instr(source[i],ahead)
            elif state == "INSTR+":
                state = self.instrp(source[i], ahead)
            elif state == "VCB":
                if(source.find("\\newline") == i):
                    self.colNum += 6
                    i+=7
                    state = "CHAR"
                elif(source.find("\\space") == i):
                    self.colNum += 4
                    i+=5
                    state = "CHAR"
                elif(source.find("\\tab") == i):
                    self.colNum += 2
                    i+=3
                    state = "CHAR"
                else:
                    state = self.vcb(source[i],ahead)
            elif state == "PRECHAR":
                state = self.prechar(source[i], ahead)
            elif state == "INID":
                state = self.inid(source[i], ahead)
            elif state == "PM":
                state = self.pm(source[i], ahead)
            elif state == "ININT":
                state = self.inint(source[i], ahead)
            elif state == "PREDBL":
                state = self.predbl(source[i], ahead)
            elif state == "INDBL":
                state = self.indbl(source[i], ahead)
            elif state == "INCOMMENT":
                state = self.incomment(source[i])
            elif state == "CLEANBREAK":
                state = self.cleanbreak(source[i], ahead)
            else:
                state = "ERROR"

            print("found which state" +state)
            if state == "STR":
                tokens.append(Token("StrToken", self.rowNum, self.colNum, self.StringLiteral))
                state = "CLEANBREAK"
            elif state == "VEC":
                tokens.append(Token("VecToken", self.rowNum, self.colNum, self.StringLiteral))
                self.StringLiteral = ""
                state = "START"
            elif state == "CHAR":
                tokens.append(Token("CharToken", self.rowNum, self.colNum, self.StringLiteral))
                state = "CLEANBREAK"       
            elif state == "BOOL":
                tokens.append(Token("BoolToken", self.rowNum, self.colNum, self.StringLiteral))
                state = "CLEANBREAK"
            elif state == "IDENTIFIER":
                tokens.append(Token("IdentifierToken", self.rowNum, self.colNum, self.StringLiteral))
                state = "CLEANBREAK"
            elif state == "INT":
                tokens.append(Token("IntToken", self.rowNum, self.colNum, self.StringLiteral))
                state = "CLEANBREAK"
            elif state == "DBL":
                tokens.append(Token("DblToken", self.rowNum, self.colNum, self.StringLiteral))
                state = "CLEANBREAK"
            elif state == "LPAREN":
                tokens.append(Token("LeftParenToken", self.rowNum, self.colNum, self.StringLiteral))
                self.StringLiteral = ""
                state = "START"
            elif state == "RPAREN":
                tokens.append(Token("RightParenToken", self.rowNum, self.colNum, self.StringLiteral))
                self.StringLiteral = ""
                state = "START"
            elif state == "ABBREV":
                tokens.append(Token("AbbrevToken", self.rowNum, self.colNum, self.StringLiteral))
                self.StringLiteral = ""
                state = "START"                
            elif state == "EOF":
                
                tokens.append(Token("EofToken", self.rowNum, self.colNum, "End of File"))
                return TokenStream(tokens)
            print("token made" +state)
            if state == "CLEANBREAK":
                check = " \t\r\n\0();"
                print("i value:" + str(i) )
                if i >= len(source)-1:
                    newahead = "\0"
                else:
                    newahead = source[i+1]
                
                if newahead in check:
                    self.StringLiteral = ""
                    state = "START"
                else:
                    tokens.append(Token("ErrorToken", self.rowNum, self.colNum, newahead))
                    return TokenStream(tokens)
                
            if state == "ERROR":
                self.colNum += 1
                tokens.append(Token("ErrorToken", self.rowNum, self.colNum, ahead))
                return TokenStream(tokens)
            i+=1
        self.colNum += 1
        tokens.append(Token("EofToken", self.rowNum, self.colNum, "End of File"))
        return TokenStream(tokens)
    
def tokenToXml(token):
    tokenXml = "<" + token.tokentype +" line=" + str(token.rowNum) + " col=" + str(token.colNum) +" val='" + token.StringLiteral +"' />"
    return tokenXml
         
