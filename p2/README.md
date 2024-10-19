# CSE 262: Programming Assignment 2: Scanning "slang"

In the remaining three assignments of the semester, we will be building a full
interpreter for a relatively complete subset of the Scheme programming language.
To avoid confusion, we will call our language "slang" (because it is a
Scheme-like LANGuage).

Slang will be (with very few exceptions) a proper subset of Scheme: any valid
slang program will be a valid scheme program, but not all valid scheme programs
will work in slang.

In the first phase of the assignment, we will only worry about *scanning* slang.
That is, given a string that purports to be slang code, we will try to turn it
into a sequence of tokens.  Just like `gsi`, slang's interpreter can either be
given a file, or take input via a "read, evaluate, print loop" (REPL).

Implementing a scanner for slang will provide an opportunity to learn more about
Scheme.  Implementing it twice, in two different languages, will provide an
opportunity to compare and contrast different programming languages.  We'll also
write some Scheme code as part of this assignment, in order to get a deeper
understanding of functional programming and Scheme syntax.

## Project Details

Scanning is the first step in any compiler or interpreter.  It is the step that
turns source code into "tokens" that are easier to work with throughout the rest
of the compiler/interpreter.  Since the scanner in this assignment is not
connected to a parser, it will simply output the tokens.  Java code for this
output is provided.

As you work on this assignment, you should try to write *idiomatic* code.  Try,
as much as possible, to use language features that make your code more readable,
more familiar to experts, and more succinct and expressive.

## What To Expect

The Java folder has the most starter code, and is probably the most approachable
part of the assignment.  It isn't "easy" to implement a scanner for a regular
language, but it isn't incredibly difficult either.  With just a small amount of
lookahead, you should be able to correctly recognize all of the scheme syntax,
and produce tokens.  A set of test cases is provided.

After you've made progress on your Java code, you should turn to Python.  There
is very little supporting code in the `python` folder.  That is intentional.
You will want to think about whether the Java approach is really right for
Python or not.  In particular, Python's dynamic typing means that it is easy to
use a single `Token` type for all tokens, instead of a class hierarchy like in
Java.
