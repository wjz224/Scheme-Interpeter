(define (empty-bst bst) bst (list )))

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
     (make-bst item empty-bst empty-bst))
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

(define ds empty-bst)
(display ds)

