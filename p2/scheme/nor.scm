;; Compute the nor of two values, using only the `and` and `not` functions
;;
;; nor should always return a boolean value

(define (nor a b)
  ;; the and on the not of a and the not of b is an equivalent nor gate.
 (and (not a) (not b))
)

;; testing
;;(define a (nor #t #t))
;;(display a)