"""rows"""
def rows(max_letter):
    """returns a list of strings that represent a diamond shape with the given letter at its widest point"""
    half_diamond = []
    num_letters = 1 + ord(max_letter) - ord("A")
    for num_letter in range(num_letters):
        letter = chr(num_letter + ord("A"))
        half_row = "".zfill(num_letters).replace("0"," ")
        pivot = num_letters - num_letter - 1
        half_row = half_row[:pivot] + letter + half_row[pivot+1:]
        row = half_row + half_row[:num_letters-1][::-1]
        half_diamond.append(row)

    diamond = half_diamond + half_diamond[::-1][1:]
                
    return diamond