import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * 
 * @author A. La Fauci De Leo adapted from Olteanra (2014)
 *
 */
public class Grid {

	private static class Position {
		public int x;
		public int y;
		public Position predecessor;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Position(int x, int y, Position predecessor) {
			this(x, y);
			this.predecessor = predecessor;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Position other = (Position) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}

	private static char[][] matrix;

	private Position[] shortestPath;

	private Stack<Position> path;

	private Position start;
	

	public Grid(char[][] matrix) {
		this.matrix = matrix;
	}
	
	public static char[][] getMatrix(){
	    return matrix;
	}
	
	public int getLenght() {
	    return matrix.length;
	}

	public Position[] getPathDFS() {
		findStart();
		path = new Stack<Position>();
		shortestPath = null;

		if (start != null) {
			next(start);
		}

		return shortestPath;
	}

	public Position[] getPathBFS() {
		findStart();
		path = new Stack<Position>();
		shortestPath = null;

		LinkedList<Position> predecessors = new LinkedList<Position>();
		Queue<Position> queue = new LinkedList<Position>();
		queue.offer(start);
		visit(start);

		if (start == null) {
			return null;
		}

		while (!queue.isEmpty()) {
			Position position = queue.poll();
			predecessors.add(position);

			if (!endFound(position)) {
				Position nextPosition = new Position(position.x + 1, position.y, position);
				if (isVisitable(nextPosition)) {
					queue.offer(nextPosition);
					visit(nextPosition);
				}

				nextPosition = new Position(position.x, position.y + 1, position);
				if (isVisitable(nextPosition)) {
					queue.offer(nextPosition);
					visit(nextPosition);
				}

				nextPosition = new Position(position.x - 1, position.y, position);
				if (isVisitable(nextPosition)) {
					queue.offer(nextPosition);
					visit(nextPosition);
				}

				nextPosition = new Position(position.x, position.y - 1, position);
				if (isVisitable(nextPosition)) {
					queue.offer(nextPosition);
					visit(nextPosition);
				}
			} else {
				break;
			}
		}

		Position position = predecessors.getLast();

		if (position != null) {
			do {
				path.push(position);
				position = position.predecessor;
			} while (position != null);

			shortestPath = new Position[path.size()];
			int i = 0;
			while (!path.isEmpty()) {
				shortestPath[i++] = path.pop();
			}
		}

		cleanUp();
		return shortestPath;
	}

	private void next(Position position) {
		stepForward(position);

		if (shortestPath == null || path.size() < shortestPath.length) {
			if (!endFound(position)) {
				Position nextPosition = new Position(position.x + 1, position.y);
				if (isVisitable(nextPosition)) {
					next(nextPosition);
				}

				nextPosition = new Position(position.x, position.y + 1);
				if (isVisitable(nextPosition)) {
					next(nextPosition);
				}

				nextPosition = new Position(position.x - 1, position.y);
				if (isVisitable(nextPosition)) {
					next(nextPosition);
				}

				nextPosition = new Position(position.x, position.y - 1);
				if (isVisitable(nextPosition)) {
					next(nextPosition);
				}
			} else {
				shortestPath = path.toArray(new Position[0]);
			}
		}

		stepBack();
	}

	private boolean isVisitable(Position position) {
		return position.y >= 0 
				&& position.x >= 0 
				&& position.y < matrix.length
				&& position.x < matrix[position.y].length
				&& (matrix[position.y][position.x] == '1' || endFound(position));
	}

	private boolean endFound(Position position) {
		return matrix[position.y][position.x] == 'E';
	}

	private void stepForward(Position position) {
		path.push(position);
		if (matrix[position.y][position.x] == '1') {
			matrix[position.y][position.x] = 'V';
		}
	}

	private void stepBack() {
		Position position = path.pop();
		if (matrix[position.y][position.x] == 'V') {
			matrix[position.y][position.x] = '1';
		}
	}

	private void visit(Position position) {
		if (matrix[position.y][position.x] == '1') {
			matrix[position.y][position.x] = 'V';
		}
	}

	private void findStart() {
		if (start != null) {
			return;
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 'S') {
					start = new Position(j, i);
				}
			}
		}
	}

	private void cleanUp() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 'V') {
					matrix[i][j] = '1';
				}
			}
		}
	}
	
	/**
	 * Method to add 20 random obstacles to the grid and it checks if they are already taken.
	 * @param grid the starting grid we are using 
	 * @return the new grid to use
	 */
	public static char[][] addingObstacles(Grid grid) {
	    Random rn = new Random();
	    char[][] newMatrix = matrix.clone();
	    
	    for(int count = 20; count >= 0;) {
		int randomX = rn.nextInt(10);
		int randomY = rn.nextInt(10);
		
		if(newMatrix[randomX][randomY] == '1' ) {
		    newMatrix[randomX][randomY] = 'X';
		    count--;
		}
		else if(newMatrix[randomX][randomY] == 'S' || newMatrix[randomX][randomY] == 'E' || newMatrix[randomX][randomY] == 'X' )
		{
		    continue;
		}
	    }
	    
	    return newMatrix;
	}

	public static void main(String[] args) {
		Grid sp = new Grid(new char[][] { 
		    		{ 'S', '1', '1', '1', '1', '1', '1', '1', '1','1' },
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','1' }, 
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','1' },
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','1' }, 
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','1' }, 
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','1' }, 
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','1' }, 
				{ '1', '1', '1', '1', '1', '1', '1', 'X', 'X','X' }, 
				{ '1', '1', '1', '1', '1', '1', '1', 'X', '1','1' }, 
				{ '1', '1', '1', '1', '1', '1', '1', '1', '1','E' }
				});
		

		Position[] path = sp.getPathBFS();
		if (path != null) {
			System.out.println("Path to destination: " + Arrays.toString(path));
			System.out.println("No. Steps: " + path.length);
			sp.cleanUp();
			
		} else {
			System.out.println("Unable to reach delivery point.");
		}
		
		Grid grid2 = new Grid(addingObstacles(sp));
		Position[] path2 = grid2.getPathBFS();
		
		if(path2 != null) {
			System.out.println("Path to destination: " + Arrays.toString(path2));
			System.out.println("No. Steps: " + path2.length);
		}
		
		else {
		    System.out.println("Unable to reach delivery point.");
		}
		
	}
}