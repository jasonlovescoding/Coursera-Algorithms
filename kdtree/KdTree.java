import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }

    public KdTree() {                               // construct an empty tree of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {                    // is the set empty?
        return root == null;
    }

    public int size() {                        // number of points in the set
        return size;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.NullPointerException();

        Node n = new Node();
        n.p = p;
        n.lb = null;
        n.rt = null;
        root = put(root, n, true, 0, 0, 1, 1);
    }

    private Node put(Node h, Node n, boolean vertical, double xmin, double ymin, double xmax, double ymax) {
        if (h == null) {
            n.rect = new RectHV(xmin, ymin, xmax, ymax);
            size++;
            return n;
        }
        double xcmp = n.p.x() - h.p.x();
        double ycmp = n.p.y() - h.p.y();
        if (xcmp == 0 && ycmp == 0)
            h.p = n.p;
        else {
            if (vertical) {
                if (xcmp < 0) // left
                    h.lb = put(h.lb, n, false, xmin, ymin, h.p.x(), ymax);
                else // right || equal
                    h.rt = put(h.rt, n, false, h.p.x(), ymin, xmax, ymax);
            }
            else {
                if (ycmp < 0) // below
                    h.lb = put(h.lb, n, true, xmin, ymin, xmax, h.p.y());
                else // top || equal
                    h.rt = put(h.rt, n, true, xmin, h.p.y(), xmax, ymax);
            }
        }
        return h;
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null)
            throw new java.lang.NullPointerException();

        Node n = root;
        boolean vertical = true;
        double xcmp, ycmp;
        while (n != null) {
            xcmp = p.x() - n.p.x();
            ycmp = p.y() - n.p.y();
            if (xcmp == 0 && ycmp == 0)
                break;

            if (vertical) { // n is a vertical splitter
                if (xcmp < 0)  // left
                    n = n.lb;
                else  // right || equal
                    n = n.rt;
                vertical = false;
            }
            else { // n is a horizontal splitter
                if (ycmp < 0)  // below
                    n = n.lb;
                else  // top || equal
                    n = n.rt;
                vertical = true;
            }
        }
        if (n == null || n.p.x() != p.x() || n.p.y() != p.y())
            return false;
        return true;
    }

    public void draw() {                        // draw all points to standard draw
        Queue<Node> points = new ArrayDeque<>();
        Node n, lb, rt;
        if (root != null) points.add(root);
        while (!points.isEmpty()) {
            n = points.poll();
            lb = n.lb;
            if (lb != null)
                points.add(lb);
            rt = n.rt;
            if (rt != null)
                points.add(rt);
            n.p.draw();
            n.rect.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle
        if (rect == null)
            throw new java.lang.NullPointerException();
        ArrayList<Point2D> ps = findInRect(root, rect, true);
        return new PointsInRect(ps);
    }

    private ArrayList<Point2D> findInRect(Node h, RectHV rect, boolean vertical) {
        ArrayList<Point2D> ps = new ArrayList<>();
        if (h == null || !rect.intersects(h.rect)) return ps;
        else if (rect.contains(h.p)) {
            ps.add(h.p);
            ps.addAll(findInRect(h.lb, rect, (!vertical)));
            ps.addAll(findInRect(h.rt, rect, (!vertical)));
            return ps;
        }
        else {
            if (vertical) {
                if (rect.xmax() < h.p.x()) // definitely on the left
                    ps.addAll(findInRect(h.lb, rect, false));
                else if (rect.xmin() > h.p.x()) // definitely on the right
                    ps.addAll(findInRect(h.rt, rect, false));
                else {
                    ps.addAll(findInRect(h.lb, rect, false));
                    ps.addAll(findInRect(h.rt, rect, false));
                }
                return ps;
            } else {
                if (rect.ymax() < h.p.y()) // definitely below
                    ps.addAll(findInRect(h.lb, rect, true));
                else if (rect.ymin() > h.p.y()) // definitely at the top
                    ps.addAll(findInRect(h.rt, rect, true));
                else {
                    ps.addAll(findInRect(h.lb, rect, true));
                    ps.addAll(findInRect(h.rt, rect, true));
                }
                return ps;
            }
        }
    }

    private class PointsInRect implements Iterable<Point2D> {
        private ArrayList<Point2D> points;
        private int index;
        private int size;

        public PointsInRect(ArrayList<Point2D> ps) {
            index = 0;
            points = new ArrayList<>();
            size = ps.size();
            for (Point2D each: ps)
                points.add(each);
        }

        public Iterator<Point2D> iterator() {
            return new PointIterator();
        }

        class PointIterator implements Iterator<Point2D> {
            public boolean hasNext() {
                return index < size;
            }

            public Point2D next() {
                return points.get(index++);
            }
        }
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new java.lang.NullPointerException();
        return nearestInTree(root, p, true);
    }

    private Point2D nearestInTree(Node n, Point2D p, boolean vertical) {
        if (n == null)
            return null;

        Point2D lb = null, rt = null;
        double xcmp = p.x() - n.p.x();
        double ycmp = p.y() - n.p.y();
        double dn = n.p.distanceSquaredTo(p);
        double dlb = Double.MAX_VALUE, drt = Double.MAX_VALUE;
        if (xcmp == 0 && ycmp == 0)
            return n.p;

        if (vertical) { // n is a vertical splitter
            if (xcmp < 0) { // target point is on the left
                lb = nearestInTree(n.lb, p, false); // the nearest point on the left
                if (lb != null)
                    dlb = lb.distanceSquaredTo(p);
                if (Math.min(dn, dlb) > Math.pow(xcmp, 2))  // could possibly be on the right
                    rt = nearestInTree(n.rt, p, false); // the nearest point on the right
                if (rt != null)
                    drt = rt.distanceSquaredTo(p);
            }
            else {
                rt = nearestInTree(n.rt, p, false); // the nearest point on the right
                if (rt != null)
                    drt = rt.distanceSquaredTo(p);
                if (Math.min(dn, drt) > Math.pow(xcmp, 2)) // could possibly be on the left
                    lb = nearestInTree(n.lb, p, false); // the nearest point on the left
                if (lb != null)
                    dlb = lb.distanceSquaredTo(p);
            }
        }
        else { // n is a horizontal splitter
            if (ycmp < 0) { // target point is below
                lb = nearestInTree(n.lb, p, true); // the nearest point below
                if (lb != null)
                    dlb = lb.distanceSquaredTo(p);
                if (Math.min(dn, dlb) > Math.pow(ycmp, 2))  // could possibly be at the top
                    rt = nearestInTree(n.rt, p, true); // the nearest point at the top
                if (rt != null)
                    drt = rt.distanceSquaredTo(p);
            }
            else {
                rt = nearestInTree(n.rt, p, true); // the nearest point at the top
                if (rt != null)
                    drt = rt.distanceSquaredTo(p);
                if (Math.min(dn, drt) > Math.pow(ycmp, 2)) // could possibly be below
                    lb = nearestInTree(n.lb, p, true); // the nearest point below
                if (lb != null)
                    dlb = lb.distanceSquaredTo(p);
            }
        }

        if (drt <= dn && drt <= dlb) // '<' is not okay!!
            return rt;
        else if (dlb <= drt && dlb <= dn) // '<' is not okay!!
            return lb;
        return n.p;
    }

    public static void main(String[] args) {                // unit testing of the methods (optional)
    }
}
