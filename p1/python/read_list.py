# read_list: Read from the keyboard and put the results into a list.  The code
# should keep reading until EOF (control-d) is input by the user.
#
# The order of elements in the list returned by read_list should the reverse of
# the order in which they were entered.

def read_list():
    #try recursive function until EOF
    try:
        # empty list
        ls = []
        # Tell the user to enter an element to the list
        print("Enter to add element to list:")
        # Ask user for input and store it in variable x
        x = input()
        # Extend the list with additional user inputs by recursive call on read_list function until EOF
        ls.extend(read_list())
         # Append the users input into the list in reverse order from which they were entered
        ls.append(x)
        # return list for extension and returns the reversed list of all the inputs the user entered
        return ls
    # except catches EOF
    except:
        # Once EOF is reached,the recursive calls finally stop and the functions start returning after the last element is added
        return ls