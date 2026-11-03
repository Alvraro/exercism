def annotate(garden):
    """ 
    Annotate the garden with flower types and their counts.

    Args:
        garden (list): text flower field
    """

    flowers = []
    for num_row, garden_row in enumerate(garden):
        if len(garden_row) != len(garden[0]):
            raise ValueError('The board is invalid with current input.')
        flower_count_row = ''
        prev_row = num_row - 1 if num_row > 0 else None
        next_row = num_row + 1 if num_row < len(garden) - 1 else None
        for num_col, cell in enumerate(garden_row):
            if cell == '*':
                flower_count_row += '*'
            elif cell == ' ':
                min_col = max(0, num_col - 1)
                max_col = min(len(garden_row) - 1, num_col + 1)
                count = garden_row[min_col:max_col+1].count('*')
                if prev_row is not None:
                    count += garden[prev_row][min_col:max_col+1].count('*')
                if next_row is not None:
                    count += garden[next_row][min_col:max_col+1].count('*')
                flower_count_row += str(count) if count > 0 else ' '
            else:
                raise ValueError('The board is invalid with current input.')
        flowers.append(flower_count_row)

    return flowers