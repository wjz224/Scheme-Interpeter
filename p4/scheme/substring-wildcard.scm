;; substring-wildcard is like contains-substring, but it understands
;; single-character wildcards in the pattern string.  Wildcards are represented
;; by the ? character.  Note that this is a slightly broken way of doing
;; wildcards: the '?' character cannot be matched exactly.
;;
;; Here's an example execution: 
;; (substring-wildcard "hello" "e?lo") <-- returns true
;; (substring-wildcard "hello" "yell") <-- returns false
;; (substring-wildcard "The quick brown fox jumps over lazy dogs" "q?ick") <-- returns true
;;
;; You should implement this on your own, by comparing one character at a time,
;; and should not use any string comparison functions that are provided by gsi.

;; TODO: implement this function

;; substring-wildcard function that was is practically the same from p3 except no use of let.
(define (substring-wildcard source pattern)
    ;;Create lambdas function with parameters n and n2 for the length of source (n) and pattern(n2)
    ((lambda (n n2) 
        (loop source pattern n n2 0 0))
    (- (string-length source) 1) (string-length pattern)))

;; helper function that checks each character
;;loop that keeps track of index of source and pattern, i is for the character index of pattern and count is for the character of source
(define (loop source pattern n n2 i count) 
    (cond ((= i n2) #t) 
      ;; if char at index i matches wildcard character then recursively call loop with incremented i and count
      ((char=? (string-ref pattern i) #\?) (loop source pattern n n2 (+ i 1) (+ count 1)))
      ;; if char at index count matches character at index i than recursively call loop with incremented i and count
      ((char=? (string-ref source count) (string-ref pattern i)) (loop source pattern n n2 (+ i 1) (+ count 1)))
      ;;when count is not at the end of the string, but the characters at count and i doesn't match, recursively call the loop with a resetted i.
      ((not (= count n)) 
      ;;conditional when substring index is greater than 0 we reset the substring index and keep the string index to check if the current index is the start of the substring
      (cond ((> i 0) (loop source pattern n n2 0 count))
        ;;conditional when substring index is == 0 so we keep the substring index and increment the string index
            ((= i 0) (loop source pattern n n2 0 (+ count 1)))
      ))
      ;;return false when count is equal to str length and i is not equal to the length of the substring sub
      ((= count n) #f)
    ) 
)
;;testing
(display (substring-wildcard "hello" "?e2llo"))