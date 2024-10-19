(define prime 
    (lambda (n)
      (if (= (round (modulo n count)) 0)
          (cons (list ) (prime (round n) (round count))
          (cons count (prime (round (/ n count)) (round (+ count 1)))) 
      )
    ) 
  )
)