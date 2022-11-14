import slang_parser


ANDNODE, APPLYNODE, BEGINNODE, BOOLNODE, BUILTINNODE, CHARNODE, CONDNODE, CONSNODE, DBLNODE, DEFINENODE, IDENTIFIERNODE, IFNODE, INTNODE, LAMBDADEFNODE, LAMBDAVALNODE, ORNODE, QUOTENODE, SETNODE, STRNODE, SYMBOLNODE, TICKNODE, VECNODE, LETNODE = range(
    0, 23)

def AstToXml(expr, indent=""):   
    """Convert an AST into its XML-like representation"""
    # [CSE 262] You will need to implement this after you decide on the types
    # for your nodes
    # escape function - opposite of the unescaped function.
    def escape(s):
        escaped = s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("'", "\\'")
        return escaped
    # boolean function that returns the string version of the boolean in the dictionary
    def boolVal(b):
        # if true return the string "true"
        if b == True:
            return "true"
        # if false return the string "false"
        else:
            return "false"
    # if expr type is identifier node then just print the identifier val.
    if expr["type"] == IDENTIFIERNODE:
        return indent + "<Identifier val='" + escape(expr["name"]) + "' />\n"
    # elif expr type is a int node then just print the int val.
    elif expr["type"] == INTNODE:
        # 
        return indent + "<Int val='" + str(expr["val"]) + "' />\n"
    # elif expr type is a dbl node then just print the dbl val.
    elif expr["type"] == DBLNODE:
        return indent + "<Dbl val='" + str(expr["val"]) + "' />\n"
    # elif expr type is a char node then just print the char val and replace the print.
    elif expr["type"] == CHARNODE:
        return indent + "<Char val='" + escape(expr["val"]) + "' />\n"
    # elif expr type is a string node then just print the string val and replace the print.
    elif expr["type"] == STRNODE:
        return indent + "<Str val='" + escape(expr["value"]) + "' />\n"
    # elif expr type is a string node then and print the string val and replace the print.
    elif expr["type"] == SYMBOLNODE:
        return indent + "<Symbol val='" + escape(expr["name"]) + "' />\n"
    # elif expr type is a define node then recursively call AstToXml with the identifier and expression and with the incremented indent
    elif expr["type"] == DEFINENODE:
        str1 = indent + "<Define>\n"
        str1 += AstToXml(expr["identifier"], indent + " ")
        str1 += AstToXml(expr["expression"], indent + " ")
        return str1 + indent + "</Define>\n"
    # elif expr type is a boolnode then print the bool val by calling the boolVal function to get the string 
    elif expr["type"] == BOOLNODE:
        return indent + "<Bool val='" + boolVal(expr["val"]) + "' />\n"
    # elif expr type is an if node then recursively call AstToXml with the cond, iftrue, and iffalse expressions and with the incremented indent
    elif expr["type"] == IFNODE:
        str1 = indent + "<If>\n"
        str1 += AstToXml(expr["cond"], indent + " ")
        str1 += AstToXml(expr["iftrue"], indent + " ")
        str1 += AstToXml(expr["iffalse"], indent + " ")
        return str1 + indent + "</If>"
    # elif expr type is quote node then recursively call AstToXml with the datum and with the incremented indent
    elif expr["type"] == QUOTENODE:
        str1 = indent + "<Quote>\n"
        str1 += AstToXml(expr["datum"], indent + " ")
        return str1 + indent + "</Quote>"
    # elif expr type is a tick node then recursively call AstToXml with the datum expression with the incremented indent
    elif expr["type"] == TICKNODE:
        str1 = indent + "<Tick>\n"
        str1 += AstToXml(expr["datum"], indent + " ")
        return str1 + indent + "</Tick>\n"
    # elif expr type is a set node then recursively call AstToXml with the identifier, expression expressions with the incremented indent
    elif expr["type"] == SETNODE:
        str1 = indent + "<Set>\n"
        str1 += AstToXml(expr["identifier"], indent + " ")
        str1 += AstToXml(expr["expression"], indent + " ")
        return str1 + indent + "</Set>\n"
    # elif expr type is an and node go through all the expressions with a for loop that recursively calls AstToXml on each of the expressions
    elif expr["type"] == ANDNODE:
        str1 = indent + "<And>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</And>\n"
    # elif expr type is or node go through all the expressions with a for loop that recursively calls AstToXml on each of the expressions
    elif expr["type"] == ORNODE:
        str1 = indent + "<Or>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Or>\n"
    # elif expr type is begin node go through all the expressions with a for loop that recursively calls AstToXml on each of the expressions
    elif expr["type"] == BEGINNODE:
        str1 = indent + "<Begin>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Begin>\n"
     # elif expr type is vec node go through all the expressions with a for loop that recursively calls AstToXml on each of the items
    elif expr["type"] == VECNODE:
        str1 = indent + "<Vector>\n"
        for i in expr["items"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Vector>\n"
    # elif expr type is apply node go through all the expressions with a for loop that recursively calls AstToXml on each of the expressions
    elif expr["type"] == APPLYNODE:
        str1 = indent + "<Apply>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Apply>\n"
    # elif expr type is lambdadef node go through all the expressions with a for loop that recursively calls AstToXml on each of items in the formals and body
    elif expr["type"] == LAMBDADEFNODE:
        str1 = indent + "<Lambda>\n"
        str1 += indent + "<Formals>\n"
        # for loop that recursively calls AstToXml on each item in formals
        for i in expr["formals"]:
            str1 += AstToXml(i, indent + " ")
        str1 += indent + "</Formals>\n"
        str1 += indent + "<Expressions>\n"
          # for loop that recursively calls AstToXml on each item in body
        for j in expr["body"]:
            str1 += AstToXml(j, indent + " ")
        str1 += indent + "</Expressions>\n"
        return str1 + indent + "</Lambda>\n"
    # elif expr type is CondNode go through all the expressions with a for loop that recursively calls AstToXml on each of the tests and conditions
    elif expr["type"] == CONDNODE:
        str1 = indent + "<Cond>\n"
          # for loop that recursively calls AstToXml on each item in conditions
        for i in expr["conditions"]:
            # increment indent after cond tag
            indent += " "
            str1 += indent + "<Condition>\n"
            # increment indent after condition tag
            indent += " "
            str1 += indent + "<Test>\n"
            # recursive call to AstToXml on the rest
            # for loop that recursively calls AstToXml on each test from each condition item
            str1 += AstToXml(i["tests"], indent + " ")
            str1 += indent + "</Test>\n"
            str1 += indent + "<Actions>\n"
            # for loop that recursive calls AstToXml on each of the conditions
            # for loop that recursively calls AstToXml on each "condition" from the conditions item.
            for j in i["conditions"]:
                str1 += AstToXml(j, indent + " ")
            str1 += indent + "</Actions>\n"
            # decrement indent by one after all the actions tag
            indent = indent[0:len(indent) - 1]
            str1 += indent + "</Condition>\n"
        str1 += indent + "</Cond>\n"
        return str1
     # elif expr type is ConsNode go through all the car and cdr with for loop that recursively calls AstToXml on each of the car and cdr elements
    elif expr["type"] == CONSNODE:
        str1 = indent + "<Cons>\n"
        # if car is not empty, than go through all of the cons elements with a for loop 
        if expr["car"] != None:
            for i in expr["car"]:
                # increment indent every iteration for each cons item
                indent += " "
                # recursively call AstToXml for each cons item
                str1 += AstToXml(i, indent)
                # print cons tag for every cons element
                str1 += indent + "<Cons>\n"
        else:
            # if cons is empty than print null twice
            str1 += indent + " <Null />\n"
            str1 += indent + " <Null />\n"
         # if cdr is not empty, than go through all of the cons elements with a for loop 
        if expr["cdr"] != None:
            for i in expr["cdr"]:
                str1 += AstToXml(i, indent + " ")
        # if cdr is empty, than print null twice
        else:
            # increment indent for null items
            indent += " "
            str1 += indent + "<Null />\n"
            str1 += indent + "<Null />\n"
            # decrement indent after null prints
            indent = indent[0:len(indent) - 1]
            # print cons tag for the first one
            str1 += indent + "</Cons>\n"
        # for loop that prints all the closing cons tags for each item printed from car
        if expr["car"] != None:
            for i in expr["car"]:
                indent = indent[0:len(indent) - 1]
                str1 += indent + "</Cons>\n"
        return str1
    return None
