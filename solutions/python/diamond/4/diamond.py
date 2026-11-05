"""rows"""
def rows(max_letter: str) -> list[str]:
    """returns a list of strings that represent a diamond shape with the given letter at its widest point"""
    letters = [chr(code) for code in range(ord('A'),ord(max_letter)+1)]
    full_letter_line = letters[::-1] + letters[1:]
    half_diamond = [''.join([cell if cell == letter else ' ' for cell in full_letter_line]) for letter in letters]
    diamond = half_diamond + half_diamond[::-1][1:]
    return diamond