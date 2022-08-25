;; tree: A binary tree, implemented as a "closure"
;;
;; The tree should support the following methods:
;; - 'ins x      - Insert the value x into the tree
;; - 'clear      - Reset the tree to empty
;; - 'inslist l  - Insert all the elements from list `l` into the tree
;; - 'display    - Use `display` to print the tree
;; - 'inorder f  - Traverse the tree using an in-order traversal, applying
;;                 function `f` to the value in each non-null position
;; - 'preorder f - Traverse the tree using a pre-order traversal, applying
;;                 function `f` to the value in each non-null position
;;
;; Note: every method should take two arguments (the method name and a
;; parameter).  If a method is defined as not using any parameters, you
;; should still require a parameter, but your code can ignore it.
;;
;; Note: You should implement the tree as a closure.  One of the simplest
;; examples of a closure that acts like an object is the following:
;;
;; (define (make-my-ds)
;;   (let ((x '())) (lambda (msg arg)
;;       (cond ((eq? msg 'set) (set! x arg) 'ok) ((eq? msg 'get) x) (else 'error)))))
;;
;; In that example, I have intentionally *not* commented anything.  You will
;; need to figure out what is going on there.  If it helps, consider the
;; following sequence:
;;
;; (define ds (make-my-ds)) ; returns nothing
;; (ds 'get 'empty)         ; returns '()
;; (ds 'set 0)              ; returns 'ok
;; (ds 'get 'empty)         ; returns 0
;; (ds 'do 3)               ; returns 'error
;;
;; For full points, your implementation should be *clean*.  That is, the only
;; global symbol exported by this file should be the `make-bst` function.

;; Questions:
;;   - How do you feel about closures versus objects?  Why?
;;   - How do you feel about defining a tree node as a generic triple?
;;   - Contrast your experience solving this problem in Java, Python, and
;;     Scheme.

(define (make-bst)
  #f ;; [CSE 262] Implement Me!
  )

