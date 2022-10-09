;; Compute the nand of two values, using only the `or` and `not` functions
;;
;; nand should always return a boolean value

(define (nand a b) 
    ;; the not on the and of a b makes an equivalent nand gate.
    (not (and a b)
)
;; testing
;;(define a (nand #t #t))
;;(display a)