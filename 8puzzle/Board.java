import java.util.ArrayList;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] board;                  // the play board
    private int n;                          // dimension of the board
    private ArrayList<Board> neighbors;     // the neighboring boards within one move

    public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                board[i][j] = blocks[i][j];
            }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {                // board dimension n
        return n;
    }

    public int hamming() {                 // number of blocks out of place
        int hamCount = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != i * n + j + 1)
                    hamCount++;
            }
        return hamCount;
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int manhDist = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != 0) {
                    int x = (board[i][j] - 1) % n;
                    int y = (board[i][j] - 1) / n;
                    manhDist += Math.abs(x - j) + Math.abs(y - i);
                }
        return manhDist;
    }

    public boolean isGoal() {               // is this board the goal board?
        return (hamming() == 0);
    }

    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int x1, y1;
        int x2, y2;
        do { // the blank square is not a block
            x1 = StdRandom.uniform(0, n);
            y1 = StdRandom.uniform(0, n);
            x2 = StdRandom.uniform(0, n);
            y2 = StdRandom.uniform(0, n);
        } while (board[x1][y1] == 0 || board[x2][y2] == 0 || board[x1][y1] == board[x2][y2]);
        // swap forward
        int tmp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = tmp;
        Board junior = new Board(board);
        // swap back
        board[x2][y2] = board[x1][y1];
        board[x1][y1] = tmp;

        return junior;
    }

    public boolean equals(Object y) {       // does this board equal y?
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension() != n)
            return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != that.board[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {    // all neighboring boards
        int x0 = n, y0 = n;              // coordinates of the blank square
        neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    x0 = i;
                    y0 = j;
                }
            }
        if (x0 + 1 < n) {
            // blank square moves rightward
            board[x0][y0] = board[x0 + 1][y0];
            board[x0 + 1][y0] = 0;
            neighbors.add(new Board(board));
            // move back
            board[x0 + 1][y0] = board[x0][y0];
            board[x0][y0] = 0;
        }
        if (x0 - 1 >= 0) {
            // blank square moves leftward
            board[x0][y0] = board[x0 - 1][y0];
            board[x0 - 1][y0] = 0;
            neighbors.add(new Board(board));
            // move back
            board[x0 - 1][y0] = board[x0][y0];
            board[x0][y0] = 0;
        }
        if (y0 + 1 < n) {
            // blank square moves downward
            board[x0][y0] = board[x0][y0 + 1];
            board[x0][y0 + 1] = 0;
            neighbors.add(new Board(board));
            // move back
            board[x0][y0 + 1] = board[x0][y0];
            board[x0][y0] = 0;
        }
        if (y0 - 1 >= 0) {
            // blank square moves upward
            board[x0][y0] = board[x0][y0 - 1];
            board[x0][y0 - 1] = 0;
            neighbors.add(new Board(board));
            // move back
            board[x0][y0 - 1] = board[x0][y0];
            board[x0][y0] = 0;
        }
        return new BoardNeighbors();
    }

    private class BoardNeighbors implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new NeighborIterator();
        }

        class NeighborIterator implements Iterator<Board> {
            private int index = 0;
            private int neighborCount = neighbors.size();

            public boolean hasNext() {
                return index < neighborCount;
            }

            public Board next() {
                return neighbors.get(index++);
            }
        }
    }

    public String toString() {  // string representation of this board (in the output format specified below)
        String boardStr = String.format("%d\n", n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardStr = boardStr.concat(String.format("%3d ", board[i][j]));
            }
            boardStr = boardStr.concat("\n");
        }
        return boardStr;
    }

    public static void main(String[] args) { // unit tests (not graded)

    }
}