;; my_reverse: reverse a list without using the scheme `reverse` function
;;
;; Your implementation of this function can use special forms and standard
;; functions, such as `car`, `cdr`, `list`, `append`, and `if`, but it cannot
;; use the built-in `reverse` function.
;;
;; Your implementation should be recursive.

(define (reverse l)
  ;; if statement here checks if list l is null
  (if (null? l)
    ;; if true return l
     l 
    ;; if false append a recursive call of all the elements after the first element in a list using 'cdr'
    ;; then grab the very first element of the list using 'car' (this gets appended to the very end of a new list because of the recursive call used for 'cdr' l)
     (append (reverse (cdr l)) 
             (list (car l)))))
;; testing             
;;(define a (list 1 2 3 4))
;;(display (reverse a))
