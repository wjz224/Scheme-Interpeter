;; contains-substring checks if a string contains the given substring.  It does
;; not count how many times: it merely returns true or false.
;;
;; The first argument to contains-substring is the string to search
;; The second argument to contains-substring is the substring to try and fine
;;
;; Here's an example execution: 
;; (contains-substring "hello" "ello") <-- returns true
;; (contains-substring "hello" "yell") <-- returns false
;; (contains-substring "The quick brown fox jumps over lazy dogs" "ox") <-- returns true
;;
;; You should implement this on your own, by comparing one character at a time,
;; and should not use any string comparison functions that are provided by gsi.

;; string-contains function that checks if substring is in str
(define (contains-substring source pattern)
  (let ((n (- (string-length source) 1)) (n2 (string-length pattern))) 
    (and 
        ;;loop that keeps track of index of str and sub, i is for the character index of sub and count is for the character of str
         (let loop ((i 0) (count 0))
            ;;conditional that evaulates to true when i is equal to length of sub (that means we found all characters of sub in a row in str)
            (cond ((= i n2) #t) 
                  ;; if char at index count matches character at index sub than recursively call loop with incremented i and count
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
(display (contains-substring "hello" "lo"))