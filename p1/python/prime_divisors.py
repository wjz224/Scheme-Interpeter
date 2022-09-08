# prime_divisors: compute the prime factorization of a number

def prime_divisors(n):
    # empty list that we will append prime factors to
    p = [] 
    # for loop that starts from 2 (the first prime number) and checks values up to n that is a prime factor of n
    for i in range(2,n,1):
        # when prime factor is satisfies the condition, it repeats until it can no longer evenly divide into n
        while(n%i == 0):
            # append prime factor into list p
            p.append(i)
            # divide n by prime factor
            n = n/i
    # remaining value greater than 2 is appended to list as a prime factor
    if(n > 2):
      # append prime factor into list p
      p.append(n)
      # return list p
    return p
