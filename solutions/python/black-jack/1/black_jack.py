"""Functions to help play and score a game of blackjack.

How to play blackjack:    https://bicyclecards.com/how-to-play/blackjack/
"Standard" playing cards: https://en.wikipedia.org/wiki/Standard_52-card_deck
"""

FIGURE_RANGE = range(2,11)

FACE_CARD = ['J', 'Q', 'K']
ACE = 'A'

FACE_CARD_VALUE = 10
ACE_HIGH_VALUE = 11
ACE_LOW_VALUE = 1

BLACKJACK = 21

def value_of_card(card):
    """Determine the scoring value of a card.

    :param card: str - given card.
    :return: int - value of a given card.  See below for values.

    1.  'J', 'Q', or 'K' (otherwise known as "face cards") = 10 
    2.  'A' (ace card) = 1
    3.  '2' - '10' = numerical value.
    """

    if card in FACE_CARD:
        return FACE_CARD_VALUE

    elif card == ACE:
        return ACE_LOW_VALUE

    card_int = int(card)
    if card_int in FIGURE_RANGE:
        return card_int
    else:
        raise Exception("Card out of range")

def higher_card(card1, card2):
    """Determine which card has a higher value in the hand.

    :param card1, card2: str - cards dealt in hand.  See below for values.
    :return: str or tuple - resulting Tuple contains both cards if they are of equal value.

    1.  'J', 'Q', or 'K' (otherwise known as "face cards") = 10
    2.  'A' (ace card) = 1
    3.  '2' - '10' = numerical value.
    """

    value1 = value_of_card(card1)
    value2 = value_of_card(card2)
    if value1 > value2:
        return card1
    elif value2 > value1:
        return card2
    return card1,card2


def value_of_ace(card1, card2):
    """Calculate the most advantageous value for the ace card.

    :param card1, card2: str - card dealt. See below for values.
    :return: int - either 1 or 11 value of the upcoming ace card.

    1.  'J', 'Q', or 'K' (otherwise known as "face cards") = 10
    2.  'A' (ace card) = 11 (if already in hand)
    3.  '2' - '10' = numerical value.
    """

    if (card1 == ACE) or (card2 == ACE):
        return ACE_LOW_VALUE

    value1 = value_of_card(card1)
    value2 = value_of_card(card2)

    if (value1 + value2 + ACE_HIGH_VALUE) <= BLACKJACK:
        return ACE_HIGH_VALUE
    return ACE_LOW_VALUE

def is_blackjack(card1, card2):
    """Determine if the hand is a 'natural' or 'blackjack'.

    :param card1, card2: str - card dealt. See below for values.
    :return: bool - is the hand is a blackjack (two cards worth 21).

    1.  'J', 'Q', or 'K' (otherwise known as "face cards") = 10
    2.  'A' (ace card) = 11 (if already in hand)
    3.  '2' - '10' = numerical value.
    """

    if (value_of_card(card1) == FACE_CARD_VALUE) and (card2 == ACE):
        return True
    elif (value_of_card(card2) == FACE_CARD_VALUE) and (card1 == ACE):
        return True
    return False


def can_split_pairs(card1, card2):
    """Determine if a player can split their hand into two hands.

    :param card1, card2: str - cards dealt.
    :return: bool - can the hand be split into two pairs? (i.e. cards are of the same value).
    """

    if value_of_card(card1) == value_of_card(card2):
        return True
    return False


def can_double_down(card1, card2):
    """Determine if a blackjack player can place a double down bet.

    :param card1, card2: str - first and second cards in hand.
    :return: bool - can the hand can be doubled down? (i.e. totals 9, 10 or 11 points).
    """

    value1 = value_of_card(card1)
    value2 = value_of_card(card2)

    if 9 <= (value1 + value2) <= 11:
        return True
    return False
