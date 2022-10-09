
;; vector2list takes a vector and returns a list, without using `vector->list`
(define (vector2list vec) 
    (let ((index 0))
     ;; each recursive call increments index by 1.
     (set! index (+ index 1))
     (letrec ((helper (lambda ( lstvec)
     ;; check if lstvec length is greater than or equal to 1. This is the condition for the recursive function to continue
     (let ((listCheck (not(<= (vector-length lstvec) 1))))
     (cond 
         ;; if lstlength is greater than or equal to 1, get the first element of the vector and cons it with the list returned from the recursive call on the sub vector from index to the end.
        (listCheck (cons (vector-ref lstvec 0) (vector2list (subvector lstvec index (vector-length lstvec) ))))
        ;; check if vector is a null object, if it is return a vector from an empty list.
        ((null? lstvec)  (list ))
        ;; if it is not null and our length is 1 or less, than return the only element as the only element in the list.
        (else (list (vector-ref lstvec 0)))
     )
     
     ))))
        ;; call the helper function with the vec passed.
        (helper vec)
  )

)
    
)
;;(define a (vector2list a))

(define a '#(1 2 3 4 5))
(define b (vector2list  a))
(display b)