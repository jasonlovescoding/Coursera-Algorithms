// source: http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* the indexing of row and column is one-oriented */
/* the indexing of i and j is zero-oriented */
public class Percolation {
    private WeightedQuickUnionUF model;
    private boolean[] opened;               // denote the state of open
    private int sidelen;                    // side length (n) of the object
    private boolean[] connectTop;           // denote the connection to top
    private boolean[] connectBottom;        // denote the connection to bottom
    private boolean percolated;             // denote percolation

    public Percolation(int n) {   // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();

        model = new WeightedQuickUnionUF(n*n);
        opened = new boolean[n*n];
        sidelen = n;
        connectTop = new boolean[n*n];
        connectBottom = new boolean[n*n];
        percolated = false;

        for (int id = 0; id < n*n; id++) {
            opened[id] = false;
            connectTop[id] = false;
            connectBottom[id] = false;
        }

    }

    private boolean legalsite(int row, int column) {   // is the site legal?
        return (row >= 1) && (row <= sidelen) && (column >= 1) && (column <= sidelen);
    }

    public void open(int row, int column) { // open site if it is not open already
        if (!legalsite(row, column))
            throw new java.lang.IndexOutOfBoundsException();

        int i = row - 1;
        int j = column - 1;
        int siteid = i*sidelen+j;
        boolean toTop = connectTop[model.find(siteid)];
        boolean toBottom = connectBottom[model.find(siteid)];

        if (!opened[siteid]) {
            opened[siteid] = true;

            int up = (i - 1) * sidelen + j;
            if (i >= 1 && opened[up]) {
                if ( connectTop[model.find(siteid-sidelen)] )
                    toTop = true;
                if ( connectBottom[model.find(siteid-sidelen)] )
                    toBottom = true;
                model.union(up, siteid);
            }

            int down = (i + 1) * sidelen + j;
            if (i < sidelen - 1 && opened[down]) {
                if ( connectTop[model.find(siteid+sidelen)] )
                    toTop = true;
                if ( connectBottom[model.find(siteid+sidelen)] )
                    toBottom = true;
                model.union(down, siteid);
            }

            int left = i * sidelen + j - 1;
            if (j >= 1 && opened[left]) {
                if ( connectTop[model.find(siteid-1)] )
                    toTop = true;
                if ( connectBottom[model.find(siteid-1)] )
                    toBottom = true;
                model.union(left, siteid);
            }

            int right = i * sidelen + j + 1;
            if (j < sidelen - 1 && opened[right]) {
                if ( connectTop[model.find(siteid+1)] )
                    toTop = true;
                if ( connectBottom[model.find(siteid+1)] )
                    toBottom = true;
                model.union(right, siteid);
            }

            if (row == 1) toTop = true;
            if (row == sidelen) toBottom = true;

            connectTop[model.find(siteid)] = toTop;
            connectTop[siteid] = toTop;
            connectBottom[model.find(siteid)] = toBottom;
            connectBottom[siteid] = toBottom;

            percolated |= toTop && toBottom;
        }
    }

    public boolean isOpen(int row, int column) {  // is site (row, column) open?
        if (!legalsite(row, column))
            throw new java.lang.IndexOutOfBoundsException();

        int i = row - 1;
        int j = column - 1;
        return opened[i*sidelen + j];
    }

    public boolean isFull(int row, int column) {   // is site (row, column) full?
        if (!legalsite(row, column))
            throw new java.lang.IndexOutOfBoundsException();

        int i = row - 1;
        int j = column - 1;
        return opened[i*sidelen+j] && connectTop[model.find(i*sidelen+j)];
    }

    public boolean percolates() {            // does the system percolate?
        return percolated;
    }

    public static void main(String args[]){
        Percolation model = new Percolation(3);
        model.open(1,3);
        model.open(2,3);
        model.open(3,3);
        model.open(3,1);
        System.out.println(model.isFull(3,1));
        model.open(2,1);
        model.open(1,1);
    }
}
