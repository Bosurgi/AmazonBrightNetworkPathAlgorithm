import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Driver {
    public static class Node {
        public int x;
        public int y;
        public Node previous;

        public Node(int x, int y, Node previous) {
            this.x = x;
            this.y = y;
            this.previous = previous;
        }

        @Override
        public String toString() { return String.format("(%d, %d)", x, y); }

        @Override
        public boolean equals(Object o) {
            Node point = (Node) o;
            return x == point.x && y == point.y;
        }

        public Node offset(int ox, int oy) { return new Node(x + ox, y + oy, this);  }
    }

    public static boolean isFree(int[][] map, Node point) {
        if (point.y < 0 || point.y > map.length - 1) return false;
        if (point.x < 0 || point.x > map[0].length - 1) return false;
        return map[point.y][point.x] == 0;
    }

    public static List<Node> findAdjacent(int[][] map, Node point) {
        List<Node> adjacent = new ArrayList<>();
        Node up = point.offset(0,  1);
        Node down = point.offset(0,  -1);
        Node left = point.offset(-1, 0);
        Node right = point.offset(1, 0);
        if (isFree(map, up)) adjacent.add(up);
        if (isFree(map, down)) adjacent.add(down);
        if (isFree(map, left)) adjacent.add(left);
        if (isFree(map, right)) adjacent.add(right);
        return adjacent;
    }

    public static List<Node> findRoute(int[][] map, Node start, Node end) {
        boolean finished = false;
        List<Node> used = new ArrayList<>();
        used.add(start);
        while (!finished) {
            List<Node> pathNew = new ArrayList<>();
            for(int i = 0; i < used.size(); ++i){
                Node point = used.get(i);
                for (Node adjacent : findAdjacent(map, point)) {
                    if (!used.contains(adjacent) && !pathNew.contains(adjacent)) {
                        pathNew.add(adjacent);
                    }
                }
            }

            for(Node point : pathNew) {
                used.add(point);
                if (end.equals(point)) {
                    finished = true;
                    break;
                }
            }

            if (!finished && pathNew.isEmpty())
                return null;
        }

        List<Node> path = new ArrayList<>();
        Node point = used.get(used.size() - 1);
        while(point.previous != null) {
            path.add(0, point);
            point = point.previous;
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
        Node end = new Node(3, 4, null);
        List<Node> path = findRoute(grid, start, end);
        if (path != null) {
            for (Node point : path) {
                System.out.println(point);
            }
        }
        else
            System.out.println("No path found");
    }
}