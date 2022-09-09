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


(define (make-bst msg bst arg)
  ;;takes in parameters
  (lambda (msg bst arg)
        ;; make an empty bst by returning an empty list
        (define make-empty-bst'())

        ;;making bst with 
        (define (make-bst middle left right)  
          (list middle left right))

        ;;get left node
        (define (get-left bst) 
          (car (cdr bst)))
          
        ;;get middle node
        (define (get-middle bst) 
          (car bst))

        ;;get right node
        (define (get-right bst) 
          (car (cdr (cdr bst))))

        ;;check if left/right node are empty
        (define (emptynode? bst) 
          (and (null? (get-left bst)) (null? (get-right bst))))

        ;;check if bst empty
        (define (empty-bst? bst) 
          (null? bst))

        ;;applying f to bst inorder
        (define (inorder f)
          (f (list ))
        )

        ;;applying f to bst in preorder
        (define (preorder f)
          (f (list ))
        )

        ;;insert item into bst
        (define (insert-bst bst item)
        (cond
          ;;check if empty
          ((empty-bst? bst)
            ;;if empty make bst with item as middle and left/right node empty 
          (make-bst item make-empty-bst make-empty-bst))
          ;;if item more than middle go right                                     
          ((> item (get-middle bst))                                         
          (make-bst (get-middle bst)
                (get-left bst)
                (insert-bst (get-right bst) item)))
          ;;if item less than middle go left
          ((< item (get-middle bst)) 
          (make-bst (get-middle bst)
                    (insert-bst (get-left bst) item)
                    (get-right bst)))
          ;;else: just have bst
          (else bst)))
        ;;conditional statements to see argument entered
       (cond  ( (eq? msg 'ins) 
                ;; set x to the returned list from the insert-bst helper function
                (set! x (insert-bst bst arg)) 'inserted) 
                ((eq? msg 'clear) 
                ;; set x to an empty list '()
                (set! x '()) 'cleared)  
                ;; set x to the returned list from the helper function (insert-bstList)
                ((eq? msg 'inslist) 
                (set! x (insert-bstList bst arg)) 'insertedList)
                ;; set x to the returned list from the helper function (bst-display ) 
                ((eq? msg 'display) 
                 (set! x (bst-display bst))'bstdisplayed)
                 ;; set x to the returned list from the helper function (bst-inorder)
                ((eq? msg 'inorder)
                  (set! x (bst-inorder bst arg))'bst-inorder-func-applied)
                   ;; set x to the returned list from the helper function (bst-preorder)
                ((eq? msg 'preorder) 
                 (set! x (bst-preorder bst arg)) 'bst-preorder-func-applied)
                (else 'error)))  
       ;;making empty bst


          )
      



;;test
(display (make-bst 'ins (list ) 20))
;;(display (make-bst 'clear (list ) 0)
;;(display (make-bst 'inslist (list ) (list 1 2 3 4)))
