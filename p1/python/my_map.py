# my_map: apply a function to every element in a list, and return a list
# that holds the results.
#
# Your implementation of this function is not allowed to use the built-in
# `map` function.

def my_map(func, l):
    for i in range(len(l)):
        l[i] = func(l[i])
    return l