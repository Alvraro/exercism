def annotate(garden):
    """ 
    Annotate the garden with flower types and their counts.

    Args:
        garden (list): text flower field
    """

    flowers = []
    for row in range(0, len(garden)):
        if len(garden[row]) != len(garden[0]):
            raise ValueError('The board is invalid with current input.')
        flower_count_row = ''
        for col in range(0, len(garden[row])):
            if garden[row][col] == '*':
                flower_count_row += '*'
            elif garden[row][col] == ' ':
                prev_row = row - 1 if row > 0 else None
                next_row = row + 1 if row < len(garden) - 1 else None
                min_col = max(0, col - 1)
                max_col = min(len(garden[row]) - 1, col + 1)
                count = garden[row][min_col:max_col+1].count('*')
                if prev_row is not None:
                    count += garden[prev_row][min_col:max_col+1].count('*')
                if next_row is not None:
                    count += garden[next_row][min_col:max_col+1].count('*')
                flower_count_row += str(count) if count > 0 else ' '
            else:
                raise ValueError('The board is invalid with current input.')
        flowers.append(flower_count_row)

    return flowers