# my_reverse: reverse a list without using the python `reverse` function

def my_reverse(l):
    # if list is none than it will return none
    if(l == None):
        return None
    # if list is empty or just one element, return list
    if(len(l) < 2):
        return l
    else:
        # return the list's last element and then add the  recursive call on the the list discluding the last element 
        # the final return would the reversed list
        return [l[-1]] + my_reverse(l[:-1])
# testing
#l = [1,2,3,4,5,6]
#print(my_reverse(l))


