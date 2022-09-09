;; my_map: apply a function to every element in a list, and return a list
;; that holds the results.
;;
;; Your implementation of this function is not allowed to use the built-in
;; `map` function.

(define (my-map func l)
  ;; if statement here checks if list l is null
  (if (null? l)
      l
      ;;appending the func applied to the first element of the list ('car 1') and casting it as a list so we can use append
      ;;then recursively call my-map to list from the second element to the end of the list ('cdr 1')
      (if (= 1 (length l))
        (list (func(car l)))
        (append (list (func (car l)))
                (my-map func (cdr l))))))
      
  
;;test
;;(define a (list 1))
;;(define plusone
   ;;(lambda (x)
;;(+ x 1)))
;;(display (my-map plusone a))

