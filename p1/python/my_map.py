# my_map: apply a function to every element in a list, and return a list
# that holds the results.
#
# Your implementation of this function is not allowed to use the built-in
# `map` function.

def my_map(func, l):
    # if list is none return none
    if(l == None):
        return None
    # For loop applies the function across all elements in the list l
    for i in range(len(l)):
        # Setting the element to value returned from the function
        l[i] = func(l[i])
    # return list after for loop
    return l