import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author A. La Fauci De Leo adapted from Stackexchange(2022)
 *
 */
public class Driver {
    
    public static class Node {
	public int x, y;
	public Node previous;
	
	/**
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param previous the previous node
	 */
	public Node(int x, int y, Node previous) {
	    this.x = x;
	    this.y = y;
	    this.previous = previous;
	} // End of constructor

	@Override
	public String toString() {
	    return String.format("(%d, %d)", x, y);
	}
	
	@Override
	public boolean equals(Object o) {
	    Node node = (Node) o;
	    return x == node.x && y == node.y;
	}
	
	
	public Node offset(int offX, int offY) {
	    return new Node (x + offX, y + offY, this);
	}
    } // End on Node class
	
	/**
	 * Method that checks if a point has obstacles
	 * @param grid the 2d map used
	 * @param node the node we want to test
	 * @return true if it is accessible
	 */
	public static boolean isFree(int[][] grid, Node node) {
	    if (node.y < 0 || node.y > grid.length - 1) return false;
	    if (node.x < 0 || node.x > grid[0].length - 1) return false;
	    return grid[node.y][node.x] == 0;
	}
	
	public static List<Node> findAdjacent(int [][] grid, Node node) {
	    List<Node> adjacent = new ArrayList<>();
	    Node up = node.offset(0, 1);
	    Node down = node.offset(0, -1);
	    Node left = node.offset(-1, 0);
	    Node right = node.offset(1, 0);    
	    
	    
	    if(isFree(grid, up)) adjacent.add(up);
	    
	    if(isFree(grid, down)) adjacent.add(down);
	    
	    if(isFree(grid, left)) adjacent.add(left);
	    
	    if(isFree(grid, right)) adjacent.add(right);
	    
	    return adjacent;
	}
	
	public static List<Node> findRoute(int[][] grid, Node start, Node destination) {
	    boolean complete = false;
	    List<Node> explored = new ArrayList<>();
	    explored.add(start);
	    
	    // Loop to add to the list the nodes for the path
	    while(!complete) {
		List<Node> pathNew = new ArrayList<>();
		for(int i = 0; i < explored.size(); ++i) {
		    Node node = explored.get(i);
		    for (Node adjacent : findAdjacent(grid, node)) {
			if(!explored.contains(adjacent) && !pathNew.contains(adjacent)) {
			    pathNew.add(adjacent);
			}
		    }
		}
		
		// End of the loop when it found the destination
		for(Node node : pathNew) {
		    explored.add(node);
		    if(destination.equals(node)) {
			complete = true;
			break;
		    }
		}
		
		if(!complete && pathNew.isEmpty())
		    return null;
	    }
	    
	    List<Node> path = new ArrayList<>();
	    Node node = explored.get(explored.size() - 1);
	    while(node.previous != null) {
		path.add(0, node);
		node = node.previous;
	    }
	    return path;
	}
    
    
    public static void main(String[] args) {
	
	int[][] grid = {
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
		    {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	    };
	
	Node start = new Node(0, 0, null);
	Node end = new Node(9, 9, null);
	List<Node> path = findRoute(grid, start, end);
	if (path != null) {
	    for (Node node : path) {
		System.out.println(node);
	    }
	}
	else
	    System.out.println("Unable to reach delivery point");
    }
}

    

