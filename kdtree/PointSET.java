import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {                               // construct an empty set of points
        pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {                    // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() {                        // number of points in the set
        return pointSet.size();
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.NullPointerException();
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null)
            throw new java.lang.NullPointerException();
        return pointSet.contains(p);
    }

    public void draw()  {                        // draw all points to standard draw;
        for (Point2D each: pointSet)
            each.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle
        if (rect == null)
            throw new java.lang.NullPointerException();

        ArrayList<Point2D> ps = new ArrayList<>();
        for (Point2D each: pointSet)
            if (rect.contains(each))
                ps.add(each);
        return new PointsInRect(ps);
    }

    private class PointsInRect implements Iterable<Point2D> {
        private ArrayList<Point2D> points;
        private int index;

        public PointsInRect(ArrayList<Point2D> ps) {
            index = 0;
            points = new ArrayList<>();
            for (Point2D each: ps)
                points.add(each);
        }

        public Iterator<Point2D> iterator() {
            return new PointIterator();
        }

        class PointIterator implements Iterator<Point2D> {
            public boolean hasNext() {
                return index < points.size();
            }

            public Point2D next() {
                return points.get(index++);
            }
        }
    }

    public Point2D nearest(Point2D p) {  // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new java.lang.NullPointerException();
        Point2D nearestPoint = null;
        double min = Double.MAX_VALUE;
        double d;
        for (Point2D each: pointSet) {
            d = each.distanceSquaredTo(p);
            if (d < min) {
                min = d;
                nearestPoint = each;
            }
        }
        return nearestPoint;
    }
    public static void main(String[] args) {                 // unit testing of the methods (optional)

    }
}
