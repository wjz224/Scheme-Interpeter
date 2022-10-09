;; list2vector takes a list and returns a vector, without using `list->vector`
(define (list2vector vec) 
  (letrec ((helper (lambda ( lstvec)
     (let ((listCheck (not(<= (length lstvec) 1))))
     (cond 
        (listCheck (vector-append (vector (car lstvec)) (list2vector (cdr lstvec))))
        ((null? lstvec) (vector (list )))
        (else (vector (car lstvec)))
     )
     
     ))))
        (helper vec)
  )
)


(define a (list2vector (list)))
(display a)