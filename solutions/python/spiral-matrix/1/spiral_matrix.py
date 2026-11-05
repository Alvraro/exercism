"""spiral_matrix.py"""
def spiral_matrix(size: int) -> list[list[int]]:
    """
    Your task is to return a square matrix of a given size.
    The matrix should be filled with natural numbers, starting from 1 in the top-left corner, increasing in an inward, clockwise spiral order
    """
    matrix = [[0 for _ in range(size)] for _ in range(size)]
    row = 0
    col = 0

    dirs = [(0,1), (1,0), (0,-1), (-1,0)]
    curr_dir = 0

    number = 1
    while number <= size ** 2:
        # Maintain the current direction and move in that direction until we hit a boundary or a filled cell
        while 0 <= row < size and 0 <= col < size and matrix[row][col] == 0:
            matrix[row][col] = number
            number += 1
            row += dirs[curr_dir][0]
            col += dirs[curr_dir][1]

        # Back into the last valid position
        row -= dirs[curr_dir][0]
        col -= dirs[curr_dir][1]
        
        # Change direction clockwise
        curr_dir = (curr_dir + 1) % len(dirs)
        row += dirs[curr_dir][0]
        col += dirs[curr_dir][1]

    return matrix