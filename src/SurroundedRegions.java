import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class SurroundedRegions {
    
    public int totalRow = 0;
    public int totalLine = 0;
    Set<Position> visited = new HashSet<Position>();
    
    class Position {
        int row;
        int line;
        Position(int row, int line) {
            this.row = row;
            this.line = line;
        }
		@Override
		public int hashCode() {
			return (line + " " + row).hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			Position other = (Position) obj;
			if (line == other.line && row == other.row)
				return true;
			return false;
		}
    }

    public void solve(char[][] board) {
        totalRow = board.length;
        totalLine = totalRow != 0 ? board[0].length : 0;
        if (totalRow == 0 || totalLine == 0) {
            return;
        }

        List<Set<Position>> regionSetList = new ArrayList<Set<Position>>();
        Set<Position> regionSet = new HashSet<Position>();
        
        // find all regions where "o" are connected
        for (int row = 0; row < board.length; row++) {
            if (board[0] != null) {
                for (int line = 0; line < board[0].length; line++) {
                    if (board[row][line] == 'O') {
                        Position pos = new Position(row, line);
                        if (!visited.contains(pos)) {
                            findNeighborOWidth(board, pos, regionSet);
                            //regionSetList.add(regionSet);
                            if (!regionSet.isEmpty()) {
                            	regionSet = new HashSet<Position>();
                            }
                        }
                    }
                }
            }
        }
    }

    public Position getPosition(Position pos, int rowOffset, int lineOffset) {
        return new Position(pos.row + rowOffset, pos.line + lineOffset);
    }
    
    public void findNeighborODepth(char[][] board, Position pos, Set<Position> regionSet) {
        if (pos.row < totalRow && pos.row >= 0 && pos.line < totalLine && pos.line >= 0) {
            if (board[pos.row][pos.line] == 'O' && !visited.contains(pos)) {
                regionSet.add(pos);
                visited.add(pos);
                findNeighborODepth(board, getPosition(pos,-1,  0), regionSet); // up
                findNeighborODepth(board, getPosition(pos, 1,  0), regionSet); // down
                findNeighborODepth(board, getPosition(pos, 0, -1), regionSet); // left
                findNeighborODepth(board, getPosition(pos, 0,  1), regionSet); // right
            } else {
                return;
            }
        } else {
            return;
        }
    }
    
    public void findNeighborOWidth(char[][] board, Position rootPos, Set<Position> regionSet) {
        Queue<Position> queue = new LinkedList<Position>();
        queue.offer(rootPos);
        boolean isAnyOAtBorder = false;
        while(!queue.isEmpty()) {
            Position pos = queue.poll();
            if (!visited.contains(pos)) {
            	visited.add(pos);
                if (pos.row < totalRow && pos.row >= 0 && pos.line < totalLine && pos.line >= 0) {
                    if (board[pos.row][pos.line] == 'O') {
                    	if (pos.row+1 >= totalRow || pos.row-1 < 0 || pos.line+1 >= totalLine || pos.line-1 < 0){
                            isAnyOAtBorder = true;
                        }
                        regionSet.add(pos);
                        queue.offer(getPosition(pos,-1,  0)); // up
                        queue.offer(getPosition(pos, 1,  0)); // down
                        queue.offer(getPosition(pos, 0, -1)); // left
                        queue.offer(getPosition(pos, 0,  1)); // right
                    }
                }
            }
        }
        if (!isAnyOAtBorder) {
        	for(Position pos : regionSet) {
                board[pos.row][pos.line] = 'X';
            }
        } else {
            regionSet.clear();
        }
    }
}
