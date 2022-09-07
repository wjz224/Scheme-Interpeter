# read_list: Read from the keyboard and put the results into a list.  The code
# should keep reading until EOF (control-d) is input by the user.
#
# The order of elements in the list returned by read_list should the reverse of
# the order in which they were entered.

def read_list():
    #try recursive function until EOF
    try:
        # empty list
        list = []
        # Tell the user to enter an element to the list
        print("Enter to add element to list:")
        # Append the users input into the list
        list.append(input())
        # Extend the list with additional user inputs by recursive call on read_list function until EOF
        list.extend(read_list())
        # return list for extension and returns entire list when all user inputs are added into list
        return list
    # except catches EOF
    except:
        # Once EOF is reached,the recursive function finally stops and the last element is added
        return list
# test
# print(read_list())