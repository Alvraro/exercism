import string


def annotate(garden):
    """ 
    Annotate the garden with flower types and their counts.

    Args:
        garden (list): text flower field
    """

    flowers = list()
    for i in range(0, len(garden)):
        if len(garden[i]) != len(garden[0]):
            raise ValueError("The board is invalid with current input.")
        row = ''
        for j in range(0, len(garden[i])):
            if(garden[i][j] == '*'):
                row += '*'
            elif(garden[i][j] == ' '):
                prev_row = i - 1 if i > 0 else None
                next_row = i + 1 if i < len(garden) - 1 else None
                min_col = max(0, j - 1)
                max_col = min(len(garden[i]) - 1, j + 1)
                count = garden[i][min_col:max_col+1].count('*')
                if prev_row is not None:
                    count += garden[prev_row][min_col:max_col+1].count('*')
                if next_row is not None:
                    count += garden[next_row][min_col:max_col+1].count('*')
                row += str(count) if count > 0 else ' '
            else:
                raise ValueError("The board is invalid with current input.")
        flowers.append(row)

    return flowers