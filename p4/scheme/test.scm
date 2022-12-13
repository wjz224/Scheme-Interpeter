(define (contains-substring source pattern)
  (let ((n (- (string-length source) 1)) (n2 (string-length pattern))) 
    (and 
        ;;loop that keeps track of index of source and pattern, i is for the character index of source and pattern is for the character of source
         (let loop ((i 0) (count 0))
            ;;conditional that evaulates to true when i is equal to length of sub (that means we found all characters of sub consecutively in source)
            (cond ((= i n2) #t) 
                   ;; if char at index i matches wildcard character then recursively call loop with incremented i and count
                  ((char=? (string-ref pattern i) #\?) (loop (+ i 1) (+ count 1)))
                  ;; if char at index count matches character at index i than recursively call loop with incremented i and count
                  ((char=? (string-ref source count) (string-ref pattern i)) (loop (+ i 1) (+ count 1)))
                  ;;when count is not at the end of the string, but the characters at count and i doesn't match, recursively call the loop with a resetted i.
                  ((not (= count n)) 
                    ;;conditional when substring index is greater than 0 we reset the substring index and keep the string index to check if the current index is the start of the substring
                    (cond ((> i 0) (loop 0 count))
                          ;;conditional when substring index is == 0 so we keep the substring index and increment the string index
                          ((= i 0) (loop 0 (+ count 1)))
                    ))
                  ;;return false when count is equal to str length and i is not equal to the length of the substring sub
                  ((= count n) #f)
            )   
         )
    )
  )
)
;;testing
(display (contains-substring "hello" "?ello"))