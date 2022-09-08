;; read_list: Use the `read` function to read from the keyboard and put the
;; results into a list.  The code should keep reading until EOF (control-d) is
;; input by the user.  It should use recursion, not iterative constructs.
;;
;; The order of elements in the list returned by (read-list) should the reverse
;; of the order in which they were entered.
;;
;; You should *not* define any other functions in the global namespace.  You may
;; need a helper function, but if you do, you should define it so that it is
;; local to `read-list`.

(define (read-list)
   ;; define local variable p that takes the value of (read)
  (let ((p (read)))
        ;; conditional statement that checks if p is EOF
        (if (eof-object? p)
            ;; if p is EOF this is our end statement and the recursive function returns an empty list to append to the rest of the list
            (list )
            ;; if p is not an EOF it will be created as a list and we will do a recursival call that appends the user's next input to the list
            (append (list p) (read-list) )
        )
      
  )
)

