package edu.lehigh.cse262.p1;

import java.util.List;
import java.util.function.Function;

/**
 * A binary tree, implemented from scratch
 */
public class MyTree<T extends Comparable<T>>{
    /**
     * Insert a value into the tree
     * 
     * @param value The value to insert
     */
    // inner class for tree nodes
    private TreeNode root ;
    private int size;
    private class TreeNode{
        // value of node
        T value;
        // left node pointer
        TreeNode left;
        // right node pointer
        TreeNode right;
        // inner class constructor for tree nodes
        TreeNode(T val){
            value = val;
            // left node starts null
            left =  null;
            // right node starts null
            right = null;
        }
        
    }
    // Tree constructor
    MyTree(){
        // start tree with a null root
        root = null;
        size = 0;
    }
    
    void insert(T value) {
        if(root == null){
            root = new TreeNode(value);
        }
        else{
            TreeNode parent, current;
            // initalizing parent
            parent = null;
            // setting current to root
            current = root;
            // While loop iterates through and looks for leaf node until current node is null
            while(current != null){ 
                parent = current;
                // if value is less than current node's value, than go to the left child
                if(value.compareTo(current.value) < 0){
                    // setting current to left child
                    current = current.left;
                }
                else if(value.compareTo(current.value) > 0){
                    // setting current to right child
                    current = current.right;
                }
                else{
                    // if value is found in BST than return and do not do insert 
                    return;
                }
            }
            // when current node is null, its parent left and right childs are the potential spots for inserting the value
            // if value is less than parent value, than it will be the left child.
            if(value.compareTo(parent.value) < 0){
                // Creating a new node with the value and inserting it as the left child
                parent.left = new TreeNode(value);
            }
            // if value is greater than parent value, than it will be right child
            else {
                // Creating a new node with the value and inserting it as the right child
                parent.right = new TreeNode(value);
            }
        }
    }

    /** Clear the tree */
    void clear() {
        // [CSE 262] Implement Me!
    }

    /**
     * Insert all of the elements from some list `l` into the tree
     *
     * @param l The list of elements to insert into the tree
     */

    void inslist(List<T> l) {
        // [CSE 262] Implement Me!
    }

    /**
     * Perform an in-order traversal, applying `func` to every element that is
     * visited
     * 
     * @param func A function to apply to each item
     */
    void inorder(Function<T, T> func) {
        // [CSE 262] Implement Me!
    }

    /**
     * Perform a pre-order traversal, applying `func` to every element that is
     * visited
     * 
     * @param func A function to apply to each item
     */
    void preorder(Function<T, T> func) {
        // [CSE 262] Implement Me!
    }
}