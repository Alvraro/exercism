import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

class Connect {
	private Board board;
	private AStar aStar = new AStar();

	public Connect(String[] board) {
		this.board = new Board(board);
	}

	public Board getBoard() {
		return board;
	}
	
	public Winner computeWinner() {
		Winner winner = Winner.NONE;

		for (Winner player : Winner.values()) {
			if(player == Winner.NONE) continue;
			
			Field start = (player == Winner.PLAYER_O) ? board.topOuterBoardField : board.leftOuterBoardField;
			Field goal = (player == Winner.PLAYER_O) ? board.bottomOuterBoardField : board.rightOuterBoardField;
			Function<Field, Integer> heuristic = manhattanDistanceToGoal(goal);
			Predicate<Field> filter = isFieldOccupiedByPlayer(player);
			List<Connect.Field> path = aStar.findPath(board, start, goal, heuristic, filter);
			if(path != null && !path.isEmpty()) {
				winner = player;
				break;
			}
		}

		return winner;
	}

	class Board {
		private int numRows;
		private int numCols;
		private Field[][] fields;
		private Field leftOuterBoardField;
		private Field rightOuterBoardField;
		private Field topOuterBoardField;
		private Field bottomOuterBoardField;

		public Board(String[] board) {
			numRows = board.length;
			numCols = board[0].replaceAll("\\s+", "").length();
			fields = new Field[numRows][numCols];
			for (int row = 0; row < numRows; ++row) {
				String line = board[row].replaceAll("\\s+", ""); // remove all whitespace from the line
				if(line.length() != numCols) {
					throw new IllegalArgumentException(String.format("All lines of the board must have the same number of columns. Line %d has %d columns, but expected %d.", row, line.length(), numCols));
				}
				for (int col = 0; col < numCols; ++col) {
					fields[row][col] = new Field(this, row, col, Winner.getWinner(line.charAt(col)));
				}
			}
			leftOuterBoardField = new Field(this, Integer.MAX_VALUE, -1, Winner.PLAYER_X);
			rightOuterBoardField = new Field(this, Integer.MAX_VALUE, numCols, Winner.PLAYER_X);
			topOuterBoardField = new Field(this, -1, Integer.MAX_VALUE, Winner.PLAYER_O);
			bottomOuterBoardField = new Field(this, numRows, Integer.MAX_VALUE, Winner.PLAYER_O);
		}
		
		public Set<Field> getAllFields() {
			HashSet<Field> allFields = new HashSet<Field>(numRows * numCols + 4, 1.0f);
			Arrays.asList(fields).forEach(row -> allFields.addAll(Arrays.asList(row)));
			allFields.add(leftOuterBoardField);
			allFields.add(rightOuterBoardField);
			allFields.add(topOuterBoardField);
			allFields.add(bottomOuterBoardField);
			return allFields;
		}
	}

	class Field {
		private Board board;
		private int row;
		private int col;
		private Winner player;

		public Field(Board board, int row, int col, Winner piece) {
			this.board = board;
			this.row = row;
			this.col = col;
			this.player = piece;
		}

		@Override
		public String toString() {
			return String.format("Field(row=%d col=%d %s)", row, col, player);
		}
		
		public boolean equals(Object o) {
			if (o == this) return true;
			if (!(o instanceof Field)) return false;
			Field other = (Field) o;
			return this.row == other.row && this.col == other.col && this.player == other.player;
		}
		
		public boolean isOuterBoardField() {
			return this == board.leftOuterBoardField || this == board.rightOuterBoardField || this == board.topOuterBoardField || this == board.bottomOuterBoardField;
		}
		
		public Set<Field> getAdjacentFields(Predicate<Field> filter) {
			HashSet<Field> adjacentFields = new HashSet<Field>(6, 1.0f);
			
			if(!isOuterBoardField()) {
				for (int r = -1; r <= 1; ++r) {
					for (int c = -1; c <= 1; ++c) {
						// skip the field itself and the undesired diagonals (we only want the 6 adjacent fields in a hex grid)
						if (r == 0 && c == 0)
							continue;
						if (r == -1 && c == -1)
							continue;
						if (r == 1 && c == 1)
							continue;
						
						int adjacentRow = row + r;
						int adjacentCol = col + c;
						if (adjacentRow >= 0 && adjacentRow < board.numRows 
								&& adjacentCol >= 0 && adjacentCol < board.numCols
								&& ((filter == null) || filter.test(board.fields[adjacentRow][adjacentCol]))) {
							adjacentFields.add(board.fields[adjacentRow][adjacentCol]);
						}
					}
				}
				
				// add the outer board fields if the field is on the edge of the board
				if ((row == 0) && ((filter == null) || filter.test(board.topOuterBoardField))) {
					adjacentFields.add(board.topOuterBoardField);
				} 
				if ((row == board.numRows - 1) && ((filter == null) || filter.test(board.bottomOuterBoardField))) {
					adjacentFields.add(board.bottomOuterBoardField);
				}
				if ((col == 0) && ((filter == null) || filter.test(board.leftOuterBoardField))) {
					adjacentFields.add(board.leftOuterBoardField);
				} 
				if ((col == board.numCols - 1) && ((filter == null) || filter.test(board.rightOuterBoardField))) {
					adjacentFields.add(board.rightOuterBoardField);
				}				
			}
			else {
				// add the fields on the edge of the board if the field is an outer board field
				if(this == board.topOuterBoardField) {
					for (int col = 0; col < board.numCols; ++col)
						if((filter == null) || filter.test(board.fields[0][col]))
							adjacentFields.add(board.fields[0][col]);
				} else if(this == board.bottomOuterBoardField) {
					for (int col = 0; col < board.numCols; ++col)
						if((filter == null) || filter.test(board.fields[board.numRows - 1][col]))
							adjacentFields.add(board.fields[board.numRows - 1][col]);
				} else if(this == board.leftOuterBoardField) {
					for (int row = 0; row < board.numRows; ++row)
						if((filter == null) || filter.test(board.fields[row][0]))
							adjacentFields.add(board.fields[row][0]);
				} else if(this == board.rightOuterBoardField) {
					for (int row = 0; row < board.numRows; ++row)
						if((filter == null) || filter.test(board.fields[row][board.numCols - 1]))
							adjacentFields.add(board.fields[row][board.numCols - 1]);
				}			
			}

			return adjacentFields;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
		
		public Winner getPlayer() {
			return player;
		}
	}

	Function<Field, Integer> manhattanDistanceToGoal(Field goal) {
		return field -> {
			int rowDistance = 0;
			if(field.row != Integer.MAX_VALUE && goal.row != Integer.MAX_VALUE) {
				rowDistance = Math.abs(field.row - goal.row);
			}

			int colDistance = 0;
			if(field.col != Integer.MAX_VALUE && goal.col != Integer.MAX_VALUE) {
				colDistance = Math.abs(field.col - goal.col);
			}
			
			return rowDistance + colDistance;
		};
	}

	Predicate<Field> isFieldOccupiedByPlayer(Winner player) {
		return field -> field.player == player;
	}
	
	class AstarPriorityQueue<T> extends PriorityQueue<T> {
		private static final long serialVersionUID = 1L;

		public AstarPriorityQueue(int initialCapacity, HashMap<T, Integer> fscore) {
			super(initialCapacity, (field1, field2) -> Integer.compare(
					fscore.get(field1), fscore.get(field2)));
		}
	}
	
	class AStar {
		public List<Field> findPath(Board board, Field start, Field goal, Function<Field, Integer> heuristic, Predicate<Field> filter) {
			ArrayList<Field> path = new ArrayList<Field>(Math.max(board.numRows, board.numCols));

			// For each field, which field it can most efficiently be reached from
			HashMap<Field, Field> cameFrom = new HashMap<Field, Field>(board.numRows * board.numCols, 1.0f);

			// Cheapest path from start to field
			HashMap<Field, Integer> gscore = new HashMap<Field, Integer>(board.numRows * board.numCols, 1.0f);
			board.getAllFields().forEach(row -> Arrays.asList(row).forEach(field -> gscore.put(field, Integer.MAX_VALUE)));
			gscore.put(start, 0);
	
			// Estimated cheapest path from start to goal through field
			HashMap<Field, Integer> fscore = new HashMap<Field, Integer>(board.numRows * board.numCols, 1.0f);
			board.getAllFields().forEach(row -> Arrays.asList(row).forEach(field -> fscore.put(field, Integer.MAX_VALUE)));
			fscore.put(start, heuristic.apply(start));

			// Fields discovered but not evaluated yet
			PriorityQueue<Field> open = new AstarPriorityQueue<Field>(board.numRows * board.numCols, fscore);
			open.add(start);

			// Fields already evaluated
			HashSet<Field> closed = new HashSet<Field>(board.numRows * board.numCols, 1.0f);

			while (!open.isEmpty()) {
				Field current = open.poll();
				if (current == goal) {
					return reconstructPath(cameFrom, current);
				}
				
				closed.add(current);

				for (Field adjacent : current.getAdjacentFields(filter)) {
					if (closed.contains(adjacent))
						continue;
					
					int tentativeGscore = gscore.get(current) + 1; // all edges have the same cost of 1
					if (tentativeGscore >= gscore.get(adjacent))
						continue;

					cameFrom.put(adjacent, current);
					gscore.put(adjacent, tentativeGscore);
					fscore.put(adjacent, tentativeGscore + heuristic.apply(adjacent));
					if (!open.contains(adjacent)) {
						open.add(adjacent);
					}
				}
			}

			return path;
		}

		private List<Field> reconstructPath(HashMap<Field, Field> cameFrom, Field current) {
				ArrayList<Field> totalPath = new ArrayList<Field>(Math.max(board.numRows, board.numCols));
				totalPath.add(current);
				while (cameFrom.containsKey(current)) {
					current = cameFrom.get(current);
					totalPath.add(current);
				}
				return totalPath;
		}
	}
}
