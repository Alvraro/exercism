"""Functions to automate Conda airlines ticketing system."""

SEAT_LETTERS = 'ABCD'

def generate_seat_letters(number: int):
    """Generate a series of letters for airline seats.

    :param number: int - total number of seat letters to be generated.
    :return: generator - generator that yields seat letters.

    Seat letters are generated from A to D.
    After D it should start again with A.

    Example: A, B, C, D

    """
    for seat in range(number):
        seat = seat % len(SEAT_LETTERS)
        yield SEAT_LETTERS[seat]


def generate_seats(number: int):
    """Generate a series of identifiers for airline seats.

    :param number: int - total number of seats to be generated.
    :return: generator - generator that yields seat numbers.

    A seat number consists of the row number and the seat letter.

    There is no row 13.
    Each row has 4 seats.

    Seats should be sorted from low to high.

    Example: 3C, 3D, 4A, 4B

    """
    seat_row = 1
    for seat_number,seat_letter in enumerate(generate_seat_letters(number)):
        if seat_number > 0 and seat_number % len(SEAT_LETTERS) == 0:
            seat_row += 1
        if seat_row == 13:
            seat_row += 1
        yield f'{seat_row}{seat_letter}'


def assign_seats(passengers: list[str]):
    """Assign seats to passengers.

    :param passengers: list[str] - a list of strings containing names of passengers.
    :return: dict - with the names of the passengers as keys and seat numbers as values.

    Example output: {"Adele": "1A", "Björk": "1B"}

    """
    seats = generate_seats(len(passengers))
    return { passenger: next(seats) for passenger in passengers }
    

CODE_LENGTH = 12

def generate_codes(seat_numbers: list[str], flight_id: str):
    """Generate codes for a ticket.

    :param seat_numbers: list[str] - list of seat numbers.
    :param flight_id: str - string containing the flight identifier.
    :return: generator - generator that yields 12 character long ticket codes.

    """
    for assigned_seat in seat_numbers:
        code = f'{assigned_seat}{flight_id}'
        padding = "".join('0' for _ in range(CODE_LENGTH - len(code)))
        yield code + padding
