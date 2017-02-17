import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] pts = null;
    private ArrayList<LineSegment> segList = null;
    private int segCount = -1;
    private int len = -1;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
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
        Arrays.sort(pts); // pre-sort the points by x-y coordinate for convenient lining
    }

    public int numberOfSegments() {       // the number of line segments
        if (segCount >= 0) {
            return segCount;
        }

        segCount = 0;
        segList = new ArrayList<LineSegment>();
        for (int p = 0; p < len; p++) {
            for (int q = p + 1; q < len; q++) {
                for (int r = q + 1; r < len; r++) {
                    for (int s = r + 1; s < len; s++) {
                        double ptoq = pts[p].slopeTo(pts[q]);
                        double ptor = pts[p].slopeTo(pts[r]);
                        double ptos = pts[p].slopeTo(pts[s]);
                        if (ptoq == ptor && ptor == ptos) {
                            segList.add(new LineSegment(pts[p], pts[s]));
                            segCount++;
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}