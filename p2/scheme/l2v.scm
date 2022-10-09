;; list2vector takes a list and returns a vector, without using `list->vector`
(define (list2vector vec) 
   ;; recursive helper function that takes in a parameter lstvec
  (letrec ((helper (lambda ( lstvec)
     ;; check if lstlength length is greater than or equal to 1. This is the condition for the recursive function to continue
     (let ((listCheck (not(<= (length lstvec) 1))))
     (cond 
         ;; if lstlength is greater than or equal to 1, make the first element a vector and append it with the vector from the recursive function on the cdr lstvec
        (listCheck (vector-append (vector (car lstvec)) (list2vector (cdr lstvec))))
        ;; check if list is a null object, if it is return a vector from an empty list.
        ((null? lstvec) (vector (list )))
        ;; if it is not null and our length is 1 or less, than return the only element as a vector.
        (else (vector (car lstvec)))
     )
     
     ))))
        ;; call the helper function with the vec passed.
        (helper vec)
  )
)

;; testing
;;(define a (list2vector (list 1 2 3 4)))
;;(display a)