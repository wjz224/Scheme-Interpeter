# tree: A binary tree, implemented as a class
#
# The tree should support the following methods:
# - ins(x)      - Insert the value x into the tree
# - clear()     - Reset the tree to empty
# - inslist(l)  - Insert all the elements from list `l` into the tree
# - display()   - Use `display` to print the tree
# - inorder(f)  - Traverse the tree using an in-order traversal, applying
#                 function `f` to the value in each non-null position
# - preorder(f) - Traverse the tree using a pre-order traversal, applying
#                 function `f` to the value in each non-null position

# inner node class
class node:
    # node initalize. Starts a node with x
    def __init__(self, x):
        # setting the node's value with x
        self.value = x
        # setting the node's left child as None
        self.left = None
        # setting the node's right child as None
        self.right = None
class tree:
    # tree initalizer
    def __init__(self):
        # start tree root as none  
        self.root = None
    # tree ins(x) method - inserts x into tree
    def ins(self,x):
        # if x is none than return, don't insert
        if(x == None):
            return
        # If there is no root, insert x by creating a root node
        if (self.root == None):
            # creating root as new node with value x
            self.root = node(x)
        # If there is a root, traverse through tree starting from root using recursion to insert x
        else:
            # calling recursive helper function to insert x into the tree starting from root
            self.insHelper(x,self.root)
    # recursive ins helper function that traverses through tree and inserts x as a node in the tree
    def insHelper(self, x, current):
        # if x is less than current value  than go to left child
        if(x < current.value):
            # if left child is none than set left child to new node with value x object.
            # I have to set current.left and can't just traverse and set current = node(x) when current == none because of the scope.
            if(current.left == None):
                # setting current left child to new node object with value x
                current.left = node(x)
            # else call the recursive helper method on the left child
            # I have to set current.left and can't just traverse and set current = node(x) when current == none because of the scope.
            else:   
                # calling recursive helper method on left child
                self.insHelper(x,current.left)
            # if x is greater than or equal to (equal to accoutns for duplicates) current value than go to right child
        elif (x >= current.value):
            # if right child is none than set right child to new node with value x object
            if(current.right == None):
            # setting current right child to new node object with value x
                current.right = node(x)
            else:
            # calling recursive method on right child
                self.insHelper(x,current.right) 
    # tree inslist(l) method - inserts list into tree
    def inslist(self,l):
        # don't insert if list is none, return
        if(l == None):
            return
        # for loop that inserts each element from the list into the tree
        for i in range(len(l)):
            # inserting each element from the list
            self.ins(l[i])
    # tree clear() method - clears method by setting root to None
    def clear(self):
        #setting root to none
        self.root = None
    # tree display() method - display the tree in preorder
    def display(self):
        # if root is None than there is nothing to display
        if(self.root == None):
            # if nothing show an empty list
            # print bracket for style
            print("[", end =" ")
            # #print bracket for style
            print("]", end =" ")
            return
        # print bracket for style 
        print("[", end =" ")
        # calling recursive display helper function starting from root
        self.displayHelper(self.root)
        print("]", end =" ")
    # recursive display helper function that displays the tree in in order
    def displayHelper(self, current): 
        if (current != None):
            # visit the node and print the current's value
            self.displayHelper(current.left)
            # Traverse the left tree by recursive call on the left child
            print(current.value, end = " ")
            # Traverse the right tree by recursive call on the right child
            self.displayHelper(current.right)
    def inorder(self, func):
        # if root is None than there is nothing to traverse
        if(self.root == None):
            return
        # calling recursive inorder helper function that traverses the tree in order and applies the func function
        self.inorderHelper(self.root,func)
    def inorderHelper(self,current,func):
        if(current != None):
            # traverse through left tree by recursively calling the helper function on the left child
            self.inorderHelper(current.left,func) 
            # Once traversed through left tree, visit node and apply func 
            current.value = func(current.value)
            # traverse through right tree by recursively calling the helper function on the right child
            self.inorderHelper(current.right,func)
    def preorder(self, func):
        # if root is None than there is nothing to traverse
        if(self.root == None):
            return
        # calling recursive inorder helper function that traverses the tree in order and applies the func function
        self.preorderHelper(self.root,func)
    def preorderHelper(self,current,func):
        if(current != None):
            # traverse through left tree by recursively calling the helper function on the left child
            self.preorderHelper(current.left,func)
            # Once traversed through left tree, visit node and apply func
            current.value = func(current.value)
            # traverse through right tree by recursively calling the helper function on the right child
            self.preorderHelper(current.right,func)
#testing
#def add2(a):
       # a += 2
        #return a

#o = tree()
#o.ins(2)
#list2 = [1,2,3,4,5,6]
#o.ins(2)
#o.inslist(list2)
#o.display()
#o.preorder(add2)
#o.display()
#o.inorder(add2)
#o.display()
#o.clear()
#o.display()
#print()