;; prime_divisors: compute the prime factorization of a number
;;
;; To test this function, open a new `gsi` instance and then type:
;;  (load "prime_divisors.scm")
;; Then you can issue commands such as:
;;  (prime-divisors 60)
;; And you should see results of the form:
;;  (2 2 3 5)

;; This is a skeleton for the prime-divisors function.  For now, it just
;; returns #f (false)
;;
;; Note that you will almost certainly want to write some helper functions,
;; and also that this will probably need to be a recursive function.  You are
;; not required to use good information hiding.  That is, you may `define`
;; other functions in the global namespace and use them from
;; `prime-divisors`.

;;calls the findPrime function and passes in 2 as the first prime number as well as n
(define (prime_divisors n)
  (findPrime 2 n))

;; findPrime function takes parameter n that we will be finding the prime factorization of
(define (findPrime primeNum n)
    ;;if n is less than 2: then you return an empty list 
    (if (< n 2)
        (list )
        ;; else:
        ;; go into an if statement that sees if the square of a prime number is less than n. This means that n is factorizable by primeNum
        (if  (< (* primeNum primeNum) n)
          ;; check if primeNum is a prime factor of n by checking if its divides without a remainder
          (if (= (modulo n primeNum) 0)
            ;; recursive call on findPrime function where instead of n, n/primeNum is passed in because there is no remainder
            ;; primeNum is passed in again to check if the prime factor is used again
            (cons primeNum (findPrime primeNum (/ n primeNum)))
            ;; else:
            ;; we recursively call findPrime and pass in primeNum+1 to check if the next prime number can divide into n 
            (findPrime (+ primeNum 1) n))
          ;; else: 
          ;; return the value of n at the end of recursive calls bc there are no more primeNum * primeNum that are less than n
          (list n))))
      
;;test
;;(display (prime_divisors 60))