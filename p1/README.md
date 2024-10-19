# CSE 262 Assignment 1

The purpose of this assignment is to ensure that you are familiar with the three
programming languages that we will use in this class: Java, Python, and Scheme.
Among the goals of this assignment are:

* To make sure you have a proper development environment for using these
  languages
* To introduce you to these languages, if you haven't used them before
* To introduce you to some features of these languages that you may not have
  seen before
* To get you thinking about how to program idiomatically

### Read List

* Did you run into any trouble using `let`?  Why?
  We encountered the error "ill-formed" several times. This happened because we had enclosed the parenthesis that was enclosing the procedure. 
  We also sometimes forgot to provide a pair.
  
* What happens if the user enters several values on one line?
  Each value that is separated by white spaces will be appended to the list as its own value. EX: input: 1 2 3 4 67 -> (67 4 3 2 1)

* What happens if the user enters non-integer values?
  If the user enters non-integer values, each string or char input will be appended into the list as a element. EX: abc a d -> (d a abc)

* Contrast your experience solving this problem in Java, Python, and Scheme.
  In Java,the type was not automatically interpreted and we had to hard cast the input as (T) and see if it matched the type of the ReadList object. If it didnt we had to catch the error as well as EOF. In contrast, in Python and Scheme, the type was automatically interpreted and all we had to do was catch the exception which was EOF. In addition, each language was syntactically different, but especially Scheme since it was a programming language. In Java we used a while loop that ended when EOF was read in the Scanner while in both Python and Scheme we used recursion that ended when EOF was inputted. The difference in the Python and Scheme recursion was how we used the methods. Scheme was procedural while Python was Object oriented.

### Reverse

* What is tail recursion?
  Tail recursion is when a recursive function has the recursive call as the last statement executed in the function.

* Is your code tail recursive?
  My code is not tail recursive because the recursive function is not the last execution that is returned. My last step was appending the list to the recursive call.

* How would you write a test to see if Scheme is applying tail recursion optimizations?
  I would probably add some displays into the code to see the specific order in which my code is ran when receiving certain inputs.

* Contrast your experience solving this problem in Java, Python, and Scheme.
  The logic that we used across all three languages was pretty similar. We recursively removed the first element in a list and passed a list [1-n] recursively into our functions. It was pretty convienuient that Scheme had the car and cdr functions that were able to grab all the elements that we needed and pass that in without having to change the initial list. Using Java and Python however, we used functions like remove which directly changed the list that was passed in.

### Map

* What kinds of values can be in `l`?
  'l' is of type list and the values inside a list can be different kinds of values like ints and strings

* What are the arguments to the function `func`?
  The arguments to the function 'func' are procedures or other functions

* Why is this function built into scheme when it's so simple to write?
  It seems like it was built into scheme because of how useful it is when programming in scheme. Because of schemes unique way of having a procedure and using that procedure on different values when making specific calls like (+ 1 2) it would be pretty useful to have a function that would be able to do that all for you if you decide to do more complicated calculations across a larger set of values.

* Contrast your experience solving this problem in Java, Python, and Scheme.
  We actually found this to be a lot more interesting in how it was done than when coding map in Java and Python. With scheme we were using car and cdr along with a recursive call to make sure that the first element of each time we used cdr was able to have func applied to it. 

### Tree

* How do you feel about closures versus objects?  Why?
  I think closures are convenient in scheme especially because with lambda experssions certain closures can be expressed as objects and that just seems really interesting and the applications of it seem very wide. In Scheme closures feel more powerful because they are able to act as objects.

* How do you feel about defining a tree node as a generic triple?
  Conceptually it seems like having a tree node defined as a generic triple would be great as the left and right could be the pointers to the left and right node while the middle would just be the pointer to the middle node. This makes traversing through the Binary Tree through recursion much more structured and easy.
* Contrast your experience solving this problem in Java, Python, and Scheme.

  For Java and Python, the process in creating the tree was mostly the same. We did find that creating the Python tree was easier though because the types were interpreted for us and we did not need to hard-define variables; this made it so that we did not need to explicitly state that the type of the object needed to extend the comparable type like in Java. 
  Unlike Java or Python, Scheme did not use object-oriented programming which was much harder in our opinion. In Java and Python, the values were changed in memory, AKA side effects, but functional languages like Scheme does not like side effects and acts more on an input and output system. This design of function languages like Scheme made us have to rely more on helper functions and their outputs. This was very different from Python and Java where we called the methods of an instance of a BST.

### Prime Divisors

* Why did you choose the Scheme constructs that you chose in order to solve this
  problem?
  We knew that we had to solve this recursivly and the comments also advised for us to create a helper method to solve this problem. We knew from the previous attempts at prime divisors that it was important to keep track of the specific prime number that you were using as well as the value passed in. We created the helper function findPrime which took in the value and had a default value of 2 which is the smallest prime number. It was hard to figure out how to check if the prime number we passed in was a prime factor of value so we had to do some research into prime decomosition until we figured out how to properly check the range of prime numbers from 2 -> √n. After that it was a lot simplier to visualize our code and then implement similar ideas that we had in java and python

* Contrast your experience solving this problem in Java, Python, and Scheme.
  Conceptually it was harder to figure out how to implement this in Scheme. In Java and Python it was much easier because we were able to use loops to help iterate through the  possible prime factors of the value passed in, however in Scheme you have to do it recursively and just trying to figure out how to append that specific prime factor as well as recursively calling the next ones to check if there are more prime factors was difficult. With the given procedure only taking in a value it took some time to realize that we needed to create another function that also kept track of the specific prime number that we were using to see if it was a factor of value.
