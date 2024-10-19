<h1>Scheme Interpreter Project</h1>
The Scheme-Interpreter project is an application that will interpret the Scheme language's source code(MIT functional language). <br>

<h2>Scheme Interpreter Structure</h2> 
The repostiory contains 4 directories. The main components of the Scheme Interpreter Project are in the directories p2,p3,and p4. Below is an explanation of each component of the program<br>
<h3>1. /p2 - Scanner: </h3>
The first process in the <b>Interpreter</b> process. Follows a set of specifications (See "/specifications directory") using <b>Lexical Analysis</b> to look for keywords and extract tokens using regular expressions for <b>Tokenization</b>. The tokens will then be used for our parsing process. 
<h3>2. /p3 - Parser:</h3> The second process is the <b>Parser</b>. the Parser uses <b>Syntax analysis</b> to check if the tokenized source code follows the grammar rules of the programming language. During this process, the parser builds an <b>Abstract Syntax Tree (AST)</b>, which represents the hierarchical structure of the code. The AST simplifies the code, making it easier for the interpreter or compiler to analyze and execute.
<h3>3. /p4 - Interpreter:</h3> The third process is the <b>Interpreter</b>.The interpreter is the component that directly executes the code by evaluating the Abstract Syntax Tree (AST) produced by the parser. It traverses the AST node by node, performing the operations specified, such as arithmetic calculations, variable assignments, and function calls, without needing to compile the code into machine language. Unlike a compiler, an interpreter executes the code line-by-line or expression-by-expression, making it ideal for dynamically typed and interpreted languages like Python or JavaScript.

<h2> Project Details</h2>
This project has implementations in both **Java** and **Python**.  <br>
The project ontains two directories, one for each language version: <br>
- `java/`: The Java implementation.
- `python/`: The Python implementation.
