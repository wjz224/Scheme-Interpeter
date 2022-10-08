;; Compute the exclusive or of two values, using only and, or, and not
;;
;; xor should always return a boolean value

(define (xor a b) 
    (and (not (and a b)) (or a b))
)
;; testing(
;; define a (xor #t #t))
;;(display a)
