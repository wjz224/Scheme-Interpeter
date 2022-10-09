;; reduce takes a binary function, a list, and an identity value, and computes
;; the result of repeatedly applying that function
;;
;; Example: (reduce + '(1 2 3) 0) ==> 6
;;
;; Example: (reduce * '() 1) ==> 1
(define (reduce op l identity) 
     ;; check if list is null
    (if (null? l)
        ;; if list is null than just return the identity
        identity
        ;; if list is not null than apply operator on the first element of the list l
        ;; than call the recursive function on the remaining elements of the list
        ;; the remaining elements is the cdr of the list l.
        ;; this continues until the list is cut down into null.
        (op (car l)
            (reduce op (cdr l) identity))))
;;testing

;;(define a (reduce + '(1 2 3) 0))
;;(display a)