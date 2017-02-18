import edu.princeton.cs.algs4.*;
import java.util.Arrays;
import java.util.Iterator;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private Digraph digraph;
    private BFSCache vCache, wCache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.digraph = new Digraph(G);
        this.vCache = new BFSCache();
        this.wCache = new BFSCache();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        precalc(v, w);
        return length();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        precalc(v, w);
        return ancestor();
    }

    // length of shortest ancestral path between any vertex in V and any vertex in W; -1 if no such path
    public int length(Iterable<Integer> V, Iterable<Integer> W) {
        precalc(V, W);
        return length();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> V, Iterable<Integer> W) {
        precalc(V, W);
        return ancestor();
    }

    // prepare the caches for finding the SAP from single source
    private void precalc(int v, int w) {
        verifyInput(v);
        verifyInput(w);
        vCache.bfs(v);
        wCache.bfs(w);
    }

    // prepare the caches for finding the SAP from multiple sources
    private void precalc(Iterable<Integer> V, Iterable<Integer> W) {
        verifyInput(V);
        verifyInput(W);
        vCache.bfs(V);
        wCache.bfs(W);
    }

    private int length() {
        int minLen = -1;
        BFSCache[] caches = { vCache, wCache };
        for (BFSCache cache : caches) {
            for (int ancestor : cache) {
                // common ancestor
                if (vCache.hasPathTo(ancestor) && wCache.hasPathTo(ancestor)) {
                    int len = vCache.distTo(ancestor) + wCache.distTo(ancestor);
                    if (len < minLen || minLen == -1) minLen = len;
                }
            }
        }
        return minLen;
    }

    private int ancestor() {
        int minLen = INFINITY;
        int minAncestor = -1;
        BFSCache[] caches = { vCache, wCache };
        for (BFSCache cache : caches) {
            for (int ancestor : cache) {
                // common ancestor
                if (vCache.hasPathTo(ancestor) && wCache.hasPathTo(ancestor)) {
                    int len = vCache.distTo(ancestor) + wCache.distTo(ancestor);
                    if (len < minLen) {
                        minLen = len;
                        minAncestor = ancestor;
                    }
                }
            }
        }
        return minAncestor;
    }

    private void verifyInput(int v) {
        if (v < 0 || v >= digraph.V())
            throw new java.lang.IndexOutOfBoundsException();
    }

    private void verifyInput(Iterable<Integer> V) {
        for (int v : V) {
            verifyInput(v);
        }
    }

    private class BFSCache implements Iterable<Integer> {
        private boolean[] marked;  // marked[v] = is there an s->v path?
        private int[] distTo;      // distTo[v] = length of shortest s->v path
        private Queue<Integer> modified = new Queue<>(); // keep track of the array entries that change

        public BFSCache() {
            this.marked = new boolean[digraph.V()];
            this.distTo = new int[digraph.V()];
            Arrays.fill(this.distTo, INFINITY);
        }

        public Iterator<Integer> iterator() {
            return modified.iterator();
        }

        private void clear() {
            while (!modified.isEmpty()) {
                int v = modified.dequeue();
                marked[v] = false;
                distTo[v] = INFINITY;
            }
        }

        // BFS from single source
        public void bfs(int s) {
            clear(); // restart

            Queue<Integer> q = new Queue<>();
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
            modified.enqueue(s);
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : digraph.adj(v)) {
                    if (!marked[w]) {
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                        modified.enqueue(w);
                    }
                }
            }
        }

        // BFS from multiple sources
        public void bfs(Iterable<Integer> S) {
            clear(); // restart

            Queue<Integer> q = new Queue<Integer>();
            for (int s : S) {
                marked[s] = true;
                distTo[s] = 0;
                q.enqueue(s);
                modified.enqueue(s);
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : digraph.adj(v)) {
                    if (!marked[w]) {
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                        modified.enqueue(w);
                    }
                }
            }
        }

        public boolean hasPathTo(int v) {
            return marked[v];
        }

        public int distTo(int v) {
            return distTo[v];
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("./src/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}