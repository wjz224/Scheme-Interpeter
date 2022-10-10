;; charvec2string takes a vector of characters and returns a string
(define (charvec2string cv) 
    ;; turn vector into list
  (let ((vectoList (vector->list cv)))
    ;; turn list to string
    (list->string vectoList)
  )

)
;; testing given a vector characters.
;;(define a '#(#\1 #\2 #\3 #\4 #\5))
;;(define b (charvec2string  a))
;;(display b)