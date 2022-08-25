package edu.lehigh.cse262.p1;

import java.util.List;
import java.util.function.Function;

/**
 * A binary tree, implemented from scratch
 */
public class MyTree<T> {
    /**
     * Insert a value into the tree
     * 
     * @param value The value to insert
     */
    void insert(T value) {
        // [CSE 262] Implement Me!
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