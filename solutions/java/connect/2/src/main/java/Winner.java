public enum Winner {
	PLAYER_O, PLAYER_X, NONE;

	static Winner getWinner(char winnerChar) {
		switch (winnerChar) {
		case 'O':
			return PLAYER_O;
		case 'X':
			return PLAYER_X;
		case '.':
			return NONE;
		default:
			return null;
		}
	}
}
