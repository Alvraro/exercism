"""all_your_base.py"""
def rebase(input_base: int, digits: list, output_base: int) -> list:
    """Rebase a number represented as a list of digits in one base to another base."""
    if input_base < 2:
        raise ValueError("input base must be >= 2")

    if output_base < 2:
        raise ValueError("output base must be >= 2")

    number = 0
    for exp, digit in enumerate(reversed(digits)):
        if digit < 0 or digit >= input_base:
            raise ValueError("all digits must satisfy 0 <= d < input base")
        number += digit * (input_base ** exp)

    output_digits = []
    while True:
        quotient,remainder = divmod(number, output_base)
        output_digits.append(remainder)
        number = quotient
        if number == 0:
            break

    return list(reversed(output_digits))