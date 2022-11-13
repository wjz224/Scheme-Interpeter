import slang_parser


ANDNODE, APPLYNODE, BEGINNODE, BOOLNODE, BUILTINNODE, CHARNODE, CONDNODE, CONSNODE, DBLNODE, DEFINENODE, IDENTIFIERNODE, IFNODE, INTNODE, LAMBDADEFNODE, LAMBDAVALNODE, ORNODE, QUOTENODE, SETNODE, STRNODE, SYMBOLNODE, TICKNODE, VECNODE, LETNODE = range(
    0, 23)

def AstToXml(expr, indent=""):
    def escape(s):
        escaped = s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("'", "\\'")
        return escaped
    def checkBool(b):
        if b == True:
            return "True"
        else:
            return "False"
    if expr["type"] == IDENTIFIERNODE:
        return indent + "<Identifier val='" + escape(expr["name"]) + "' />\n"
    elif expr["type"] == DEFINENODE:
        str1 = indent + "<Define>\n"
        str1 += AstToXml(expr["identifier"], indent + " ")
        str1 += AstToXml(expr["expression"], indent + " ")
        return str1 + indent + "</Define>\n"
    elif expr["type"] == BOOLNODE:
        return indent + "<Bool val='" + checkBool(expr["val"]) + "' />\n"

    elif expr["type"] == INTNODE:
        return indent + "<Int val='" + str(expr["val"]) + "' />\n"

    elif expr["type"] == DBLNODE:
        return indent + "<Dbl val='" + str(expr["val"]) + "' />\n"

    elif expr["type"] == STRNODE:
        return indent + "<Str val='" + escape("" + expr["value"]) + "' />\n"

    elif expr["type"] == CHARNODE:
        return indent + "<Char val='" + escape("" + expr["val"]) + "' />\n"

    elif expr["type"] == SYMBOLNODE:
        return indent + "<Symbol val='" + escape("" + expr["name"]) + "' />\n"

    elif expr["type"] == IFNODE:
        str1 = indent + "<If>\n"
        str1 += AstToXml(expr["cond"], indent + " ")
        str1 += AstToXml(expr["iftrue"], indent + " ")
        str1 += AstToXml(expr["iffalse"], indent + " ")
        return str1 + indent + "</If>"
    
    elif expr["type"] == QUOTENODE:
        str1 = indent + "<Quote>\n"
        str1 += AstToXml(expr["datum"], indent + " ")
        return str1 + indent + "</Quote>"
    
    elif expr["type"] == TICKNODE:
        str1 = indent + "<Tick>\n"
        str1 += AstToXml(expr["datum"], indent + " ")
        return str1 + indent + "</Tick>\n"

    elif expr["type"] == SETNODE:
        str1 = indent + "<Set>\n"
        str1 += AstToXml(expr["identifier"], indent + " ")
        str1 += AstToXml(expr["expression"], indent + " ")
        return str1 + indent + "</Set>\n"

    elif expr["type"] == VECNODE:
        str1 = indent + "<Vector>\n"
        for i in expr["items"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Vector>\n"
    
    elif expr["type"] == ANDNODE:
        str1 = indent + "<And>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</And>\n"
    
    elif expr["type"] == ORNODE:
        str1 = indent + "<Vector>\n"
        for i in expr["value"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Or>\n"

    elif expr["type"] == BEGINNODE:
        str1 = indent + "<Begin>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Begin>\n"

    elif expr["type"] == APPLYNODE:
        str1 = indent + "<Apply>\n"
        for i in expr["expressions"]:
            str1 += AstToXml(i, indent + " ")
        return str1 + indent + "</Apply>\n"

    elif expr["type"] == LAMBDADEFNODE:
        str1 = indent + "<Lambda>\n"
        str1 += indent + "<Formals>\n"
        for i in expr["formals"]:
            str1 += AstToXml(i, indent + " ")
        str1 += indent + "</Formals>\n"
        str1 += indent + "<Expressions>\n"
        for j in expr["body"]:
            str1 += AstToXml(j, indent + " ")
        str1 += indent + "</Expressions>\n"
        return str1 + indent + "</Lambda>\n"

    elif expr["type"] == CONSNODE:
        str1 = indent + "<Cons>\n"
        if expr["car"] != None:
            for i in expr["car"]:
                indent += " "
                str1 += AstToXml(i, indent)
                str1 += indent + "<Cons>\n"
        else:
            str1 += indent + " <Null />\n"
            str1 += indent + " <Null />\n"
        if expr["cdr"] != None:
            for i in expr["cdr"]:
                str1 += AstToXml(i, indent + " ")
        else:
            indent += " "
            str1 += indent + "<Null />\n"
            str1 += indent + "<Null />\n"
            indent = indent[0:len(indent) - 1]
            str1 += indent + "</Cons>\n"
        if expr["car"] != None:
            for i in expr["car"]:
                indent = indent[0:len(indent) - 1]
                str1 += indent + "</Cons>\n"
        return str1
 
    elif expr["type"] == CONDNODE:
        str1 = indent + "<Cond>\n"
        for i in expr["conditions"]:
            indent += " "
            str1 += indent + "<Condition>\n"
            indent += " "
            str1 += indent + "<Test>\n"
            str1 += AstToXml(i["tests"], indent + " ")
            str1 += indent + "</Test>\n"
            str1 += indent + "<Actions>\n"
            for j in i["conditions"]:
                str1 += AstToXml(j, indent + " ")
            str1 += indent + "</Actions>\n"
            str1 += indent + "</Condition>\n"
        str1 += indent + "</Cond>\n"
        return str1
    
    


    """Convert an AST into its XML-like representation"""
    # [CSE 262] You will need to implement this after you decide on the types
    # for your nodes
    
    return None
