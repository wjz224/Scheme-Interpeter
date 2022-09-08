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
    def _init_(self, x):
        # setting the node's value with x
        self.value = x
        # setting the node's left child as None
        self.left = None
        # setting the node's right child as None
        self.right = None
class tree:
    # tree initalizer
    def _init_(self):
        # start tree root as none  
        self.root = None
    # tree ins(x) method - inserts x into tree
    def ins(self,x):
        # If there is no root, insert x by creating a root node
        if (self.root == None):
            # creating root as new node with value x
            self.root = node(x)
        # If there is a root, traverse through tree starting from root using recursion to insert x
        else:
            # calling recursive helper function 
            self.insertHelper(x,self.root)
    # recursive helper function that traverses through tree and inserts x as a node in the tree
    def insHelper(self, x, current):
        # if current is None than we are at the spot where the node should be inserted
        if(current == None):
            # setting current to the newly created node with value x
            current = node(x)
        # if current is not None, keep traversing 
        else:
            # if x is less than current value than go to left child
            if(x < current.value):
                # calling recursive helper method on left child
                self.insHelper(current.left)
            # if x is greater than current value than go to right child
            elif (x > current.value):
                # calling recursive method on right child
                self.insHelper(current.right)
            else:
                # return since there the value is a duplicate
                return
