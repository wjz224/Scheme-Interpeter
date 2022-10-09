# [CSE 262] This file is a minimal skeleton for a Scheme scanner in Python.  It
# provides a transliteration of the TokenStream class, and the shell of a
# Scanner class.  Please see the README.md file for more discussion.

from lib2to3.pgen2 import token
from sqlite3 import Row


class Token:
    def __init__(self,tokentype, row, col, literal):
        self.__tokentype = tokentype
        self.__row = row
        self.__col = col
        self.__literal = literal
        
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



class Scanner:
    row = 1
    col = 0
    StringLiteral = ""
    
    def __init__(self):
        pass
    def cleanbreak(c):
        global StringLiteral
        isWhiteSpace = " \t\n\r\0"
        if c in isWhiteSpace:
            StringLiteral = ""
            return "START"
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
    def start(self, c):
        global StringLiteral
        global row
        global col
        validToInid = "!$%&*/:<=>?~_^"
        if c in validToInid or c.isalpha():
            return "INID"
        elif c.isdigit():
            return "ININT"
        else:
            match c:
                case '"':
                    return "INSTR"
                case '#':
                    return "VCB"
                case '\n':
                    row += 1
                    col = 0
                    return "START"
                case '+':
                    return "PM"
                case '-':
                    return "PM"
                case '\'':
                    return "ABBREV"
                case _:
                    startc = " \t\n\r\0;()"
                    if c in startc:
                        StringLiteral = ""
                        return self.cleanbreak(c)
                    else:
                        return "ERROR"
                    
    def instr(c):
        if c == '"':
            return "STR"
        elif c == '\\':
            return "INSTR+"
        else:
            return "INSTR"
        
    def instrp(c):
        global StringLiteral
        match c:
            case '"':
                StringLiteral += '\"'
                return "INSTR"
            case '\\':
                StringLiteral += '\\'
                return "INSTR"
            case 't':
                StringLiteral += '\t'
                return "INSTR"
            case 'n':
                StringLiteral += '\n'
                return "INSTR"
            case _:
                StringLiteral += '\\'
                StringLiteral += 'c'
                return "ERROR"
    def vcb(c):
        match c:
            case '(':
                return "VEC"
            case '\\':
                return "PRECHAR"
            case 't':
                return "BOOL"
            case 'f':
                return "BOOL"
            case _:
                return "ERROR"
    def prechar(c, ahead):
        isWhiteSpace = " \t\n\r\0"
        if ahead in isWhiteSpace:
            return "CHAR"
        else:
            return "ERROR"
    
    def incomment(c):
        global StringLiteral
        global row
        global col
        if c == "\n":
            row+=1
            col = 0
            StringLiteral = ""
            return "START"
        elif c == "\0":
            StringLiteral = ""
            return "EOF"  
        else:
            return "INCOMMENT"
      

    def inid(c, ahead):
        validToInid = "!$%&*/:<=>?~_^.-+"
        inidc = " \t\n\r\0"
        if ahead in inidc:
            return "IDENTIFIER"
        elif c in validToInid or c.isalpha() or c.isdigit():
            return "INID"
        else:
            return "ERROR"
       
            
    def pm(c, ahead):
        pmc = " \t\n\r\0"
        if ahead in pmc:
            return "IDENTIFIER"
        elif c.isdigit():
            return "ININT"
        else:
            return"ERROR"
        
    
    def inint(c, ahead):
        intc = " \t\n\r\0"
        if ahead in intc:
            return "INT"
        elif c.isdigit():
            return "ININT"
        elif c == ".":
            return "PREDBL"
        else:
            return "ERROR"
        
        
    def predouble(c):
        if c.isdigit():
            return "DOUBLE"
        else:
            return "ERROR"
        
    
    def indbl(c, ahead):
        dblc = " \t\n\r\0"
        if ahead in dblc:
            return "DBL"
        elif c.isdigit():
            return "INDBL"
        else:
            return "ERROR"


    def scanTokens(self, source):
        global col
        global row
        global StringLiteral
        tokens = []
        # Empty array to stores tokens
        
        state = "START"

        for i in range(len(source)):
            col+=1
            if i == len(source):
                ahead = -1
            else:
                ahead = source[i+1]
            if state == "START":
                if(source.index("and") == i):
                    col += 2
                    i+=2
                    tokens.append(Token("andTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("begin") == i):
                    col += 4
                    i+=4
                    tokens.append(Token("begTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("cond") == i):
                    col += 3
                    i+=3
                    tokens.append(Token("condTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("define") == i):
                    col += 5
                    i+=5
                    tokens.append(Token("defineTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("if") == i):
                    col +=1
                    i+=1
                    tokens.append(Token("ifTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("lambda") == i):
                    col += 5
                    i+=5
                    tokens.append(Token("labmdaTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("or") == i):
                    col += 1
                    i+=1
                    tokens.append(Token("orTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("quote") == i):
                    col += 4
                    i+=4
                    tokens.append(Token("quoteTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                elif(source.index("set!") == i):
                    col += 3
                    i+=3
                    tokens.append(Token("setTok", row, col, StringLiteral))
                    state = "CLEANBREAK"
                else:
                    state = self.start(source[i], ahead)
            elif state == "INSTR":
                state = self.instr(source[i], ahead)
            elif state == "INSTR+":
                state = self.instrp(source[i], ahead)
            elif state == "VCB":
                charInt = i+1
                if(source.index("\\newline") == i):
                    col += 6
                    i+=6
                    state = "CHAR"
                elif(source.index("\\space") == i):
                    col += 4
                    i+=4
                    state = "CHAR"
                elif(source.index("\\tab") == i):
                    col += 2
                    i+=2
                    state = "CHAR"
                else:
                    state = self.vcb(source[i], ahead)
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
            if state == "STR":
                tokens.append(Token("strTok", row, col, StringLiteral))
                state = "CLEANBREAK"
            elif state == "VEC":
                tokens.append(Token("vecTok", row, col, StringLiteral))
                StringLiteral = ""
                state = "START"
            elif state == "CHAR" and len(StringLiteral) > 2:      
                tokens.append(Token("charTok", row, col, StringLiteral))
                state = "CLEANBREAK"       
            elif state == "BOOL":
                tokens.append(Token("boolTok", row, col, StringLiteral))
                state = "CLEANBREAK"
            elif state == "IDENTIFIER":
                tokens.append(Token("identifierToken", row, col, StringLiteral))
                state = "CLEANBREAK"
            elif state == "INT":
                tokens.append(Token("intToken", row, col, StringLiteral))
                state = "CLEANBREAK"
            elif state == "DBL":
                tokens.append(Token("dblToken", row, col, StringLiteral))
                state = "CLEANBREAK"
            elif state == "LPAREN":
                tokens.append(Token("lParenToken", row, col, StringLiteral))
                StringLiteral = ""
                state = "START"
            elif state == "RPAREN":
                tokens.append(Token("rParenToken", row, col, StringLiteral))
                StringLiteral = ""
                state = "START"
            elif state == "ABBREV":
                tokens.append(Token("abbrevToken", row, col, StringLiteral))
                StringLiteral = ""
                state = "START"                
            elif state == "EOF":
                return TokenStream(tokens.append(Token("EOFToken", row, col, "End of File")))
            else:
                state = "ERROR"
            if state == "CLEANBREAK":
                StringLiteral = ""
                state = "START"

            if state == "ERROR":
                return TokenStream(tokens.append(Token("ERROR TOKEN", row, col, StringLiteral)))

        return TokenStream(tokens.append(Token("EOFToken", row, col, "End of File")))

def tokenToXml(token):
    return token.tokentype.literal.format(row, col, StringLiteral)
    
