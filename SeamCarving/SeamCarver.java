import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private int[][] picture; // stored row-major in accordance with the topological ordered relaxation
    private int height, width;
    private boolean transposed;
    private double[][] energy;
    private VerticalSeamFinder verticalSeamFinder;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        validateNotNull(picture);
        this.height = picture.height();
        this.width = picture.width();
        this.picture = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.picture[row][col] = picture.get(col, row).getRGB();
            }
        }
        this.energy = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.energy[row][col] = energy(col, row);
            }
        }
        this.verticalSeamFinder = new VerticalSeamFinder();
    }

    // width of current picture
    public int width() {
        if (transposed) return height;
        return width;
    }

    // height of current picture
    public int height() {
        if (transposed) return width;
        return height;
    }

    // energy of pixel at picture[row][col]
    public double energy(int col, int row) {
        if (transposed) {
            int tmp = col;
            col = row;
            row = tmp;
        }
        validateInBound(col, row);
        if (row == 0 || col == 0 || row == height - 1 || col == width - 1)
            return 1000; // corner case: at the edge
        double gradXSquared = gradSquared(picture[row][col - 1], picture[row][col + 1]);
        double gradYSquared = gradSquared(picture[row - 1][col], picture[row + 1][col]);
        return Math.sqrt(gradXSquared + gradYSquared);
    }

    // get the squared gradient given the start x and end y
    private double gradSquared(int x, int y) {
        int r = ((x >> 16) & 0x0ff) - ((y >> 16) & 0x0ff);
        int g = ((x >> 8) & 0x0ff) - ((y >> 8) & 0x0ff);
        int b = (x & 0x0ff) - (y & 0x0ff);
        return r * r  + g * g  + b * b;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (transposed) transpose();
        return verticalSeamFinder.verticalSeam();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!transposed) transpose();
        return verticalSeamFinder.verticalSeam();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (transposed) transpose();
        validateNotNull(seam);
        validateSeam(seam);
        validatePicture();
        width--;
        for (int row = 0; row < height; row++) {
            System.arraycopy(picture[row], seam[row] + 1, picture[row], seam[row], width - seam[row]);
            System.arraycopy(energy[row], seam[row] + 1, energy[row], seam[row], width - seam[row]);
        }
        for (int row = 1; row < height; row++) { // only the nearby 2 columns of pixels' energy is updated
            int cut = seam[row];
            if (cut > 0) energy[row][cut - 1] = energy(cut - 1, row);
            if (cut < width) energy[row][cut] = energy(cut, row);
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!transposed) transpose();
        validateNotNull(seam);
        validateSeam(seam);
        validatePicture();
        width--;
        for (int row = 0; row < height; row++) {
            System.arraycopy(picture[row], seam[row] + 1, picture[row], seam[row], width - seam[row]);
            System.arraycopy(energy[row], seam[row] + 1, energy[row], seam[row], width - seam[row]);
        }
        for (int row = 1; row < height; row++) { // only the nearby 2 columns of pixels' energy is updated
            int cut = seam[row];
            if (cut > 0) energy[row][cut - 1] = energy(row, cut - 1);
            if (cut < width) energy[row][cut] = energy(row, cut);
        }
    }

    // current picture
    public Picture picture() {
        if (transposed) transpose();
        Picture pictureCurrent = new Picture(width, height);
        // transfer back to picture
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pictureCurrent.set(col, row, new Color(picture[row][col]));
            }
        }
        return pictureCurrent;
    }

    private void transpose() {
        int tmp = height;
        height = width;
        width = tmp;
        int[][] pictureTransposed = new int[height][width];
        double[][] energyTransposed = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pictureTransposed[row][col] = picture[col][row];
                energyTransposed[row][col] = energy[col][row];
            }
        }
        this.picture = pictureTransposed;
        this.energy = energyTransposed;
        transposed = !transposed;
    }

    private void validateInBound(int col, int row) {
        if (col < 0 || col >= width || row < 0 || row >= height) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    private void validateNotNull(Object obj) {
        if (obj == null) {
            throw new java.lang.NullPointerException();
        }
    }

    private void validateSeam(int[] seam) {
        if (seam.length != height) { // incorrect length
            throw new java.lang.IllegalArgumentException();
        }
        for (int row = 0; row < height; row++) { // out-of-bound seam pixel
            if (seam[row] < 0 || seam[row] >= width) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        int colPrev = seam[0];
        for (int row = 1; row < height; row++) { // non-adjacent seam pixel
            int col = seam[row];
            if (Math.abs(col - colPrev) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
            colPrev = col;
        }
    }

    private void validatePicture() {
        if (width <= 1) { // too narrow to cut
            throw new java.lang.IllegalArgumentException();
        }
    }

    private class VerticalSeamFinder {
        private static final double INF = Double.MAX_VALUE;

        public VerticalSeamFinder() {
        }

        public int[] verticalSeam() {
            Stack<Integer> seamCols = new Stack<>();
            double[][] distTo = new double[height][width];
            int[][] colTo = new int[height][width];
            Arrays.fill(distTo[0], 0); // as if there is one superpixel above the first row
            for (int row = 1; row < height; row++) {
                Arrays.fill(distTo[row], INF);
            }

            for (int row = 0; row < height - 1; row++) {
                // not relaxing the last row where there are no more edges
                for (int col = 0; col < width; col++) { // topological ordered relaxation
                    relax(col, row, distTo, colTo);
                }
            }

            int col = 0;
            double minDist = INF;
            for (int j = 0; j < width; j++) { // find the minimum energy path (the seam)
                if (minDist > distTo[height - 1][j]) {
                    minDist = distTo[height - 1][j];
                    col = colTo[height - 1][j];
                }
            }
            // we take the 2 bottom seam pixels at the same column
            seamCols.push(col); // so the column of bottom seam pixel is pushed twice
            for (int i = height - 1; i > 0; i--) {
                col = colTo[i][col];
                seamCols.push(col);
            }

            int[] seam = new int[height];
            int i = 0;
            for (int seamCol : seamCols) {
                seam[i] = seamCol;
                i++;
            }
            return seam;
        }

        // relax all the edges from the pixel at picture[row][col]
        private void relax(int col, int row, double[][] distTo, int[][] colTo) {
            if (col > 0) { // the edge to picture[row + 1][col - 1]
                if (distTo[row + 1][col - 1] == INF
                        || distTo[row + 1][col - 1] > distTo[row][col] + energy[row + 1][col - 1]) {
                    distTo[row + 1][col - 1] = distTo[row][col] + energy[row + 1][col - 1];
                    colTo[row + 1][col - 1] = col;
                }
            }
            if (distTo[row + 1][col] == INF ||
                    distTo[row + 1][col] > distTo[row][col] + energy[row + 1][col]) {
                // the edge to picture[row + 1][col]
                distTo[row + 1][col] = distTo[row][col] + energy[row + 1][col];
                colTo[row + 1][col] = col;
            }
            if (col < width - 1) { // the edge to picture[row + 1][col + 1]
                if (distTo[row + 1][col + 1] == INF ||
                        distTo[row + 1][col + 1] > distTo[row][col] + energy[row + 1][col + 1]) {
                    distTo[row + 1][col + 1] = distTo[row][col] + energy[row + 1][col + 1];
                    colTo[row + 1][col + 1] = col;
                }
            }
        }
    }

}