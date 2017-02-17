import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] pts = null; // used for storing the original points
    private ArrayList<LineSegment> segList = null; // used for storing the confirmed points in a line segment
    private int segCount = -1;
    private int len = -1;

    public FastCollinearPoints(Point[] points) {     // finds all line segments containing 4 or more points
        if (points == null) { // no null pointer
            throw new java.lang.NullPointerException();
        }
        len = points.length;
        pts = new Point[len];

        for (int i = 0; i < len; i++) {
            if (points[i] == null) { // no null pointer
                throw new java.lang.NullPointerException();
            }
            for (int j = 0; j < i; j++)
                if (points[i].compareTo(points[j]) == 0) { // no repeated point
                    throw new java.lang.IllegalArgumentException();
                }

            pts[i] = points[i];
        }
    }

    public int numberOfSegments() {       // the number of line segments
        if (segCount >= 0) {
            return segCount;
        }

        Point[] ptsCopy = new Point[len]; // used for storing the points when evaluating one point
        for (int i = 0; i < len; i++) {
            ptsCopy[i] = pts[i];
        }

        segCount = 0;
        segList = new ArrayList<LineSegment>();
        ArrayList<Point> lineSeg = new ArrayList<Point>();    // used for caching the points to be confirmed
        double slope;
        int acc, fst;
        boolean inline = false;

        for (int i = 0; i < len; i++) {               // evaluate each pts[i] in the array of points
            // sort the auxiliary array according to the slope to pts[i]
            Arrays.sort(ptsCopy, pts[i].slopeOrder());
            fst = 1;
            while (fst < len) { // ptsCopy[0] is equal to pts[i]
                slope = ptsCopy[0].slopeTo(ptsCopy[fst]);
                lineSeg.clear();
                acc = 0;
                inline = false;

                while (fst+acc < len) {
                    if (ptsCopy[0].slopeTo(ptsCopy[fst + acc]) == slope) { // on one line
                        if (ptsCopy[0].compareTo(ptsCopy[fst + acc]) < 0) { // not in a bigger line
                            lineSeg.add(ptsCopy[fst + acc]);
                            acc++;
                        }
                        else { // in a bigger line
                            inline = true;
                            acc++;
                        }
                    }
                    else break;
                }

                if (acc >= 3 && !inline) { // 4 or more points in a line and not in a bigger line
                    Point[] lineSegArray = new Point[acc];
                    lineSeg.toArray(lineSegArray);
                    Arrays.sort(lineSegArray);
                    segList.add(new LineSegment(ptsCopy[0], lineSegArray[acc-1]));
                    segCount++;
                }

                fst += acc;
            }
        }
        return segCount;
    }

    public LineSegment[] segments() {               // the line segments
        if (segList == null) {
            numberOfSegments();
        }

        LineSegment[] rstArray = new LineSegment[segCount];
        segList.toArray(rstArray);
        return rstArray;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}