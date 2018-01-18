// http://poj.org/problem?id=1861
#include <iostream>
#include <utility>
#include <vector>
#include <queue>
using namespace std;
// weight, u, v
typedef pair<int, pair<int, int> > Edge;

class UF {
// union-find class, with weighted rooting and path compression
private:
    vector<int> parent;
    vector<int> rank;

    int root(int i) {
        while (i != parent[i]) {
            parent[i] = parent[parent[i]];
            i = parent[i];
        }
        return i;
    }    

public:
    UF(int n) {
        parent.resize(n);
        rank.resize(n);
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }    
    }

    void unionize(int u, int v) {
        int uroot = root(u);
        int vroot = root(v);
        if (uroot == vroot) return;
        
        if (rank[vroot] < rank[uroot]) {
            parent[vroot] = parent[uroot];
        } else if (rank[uroot] < rank[vroot]) {
            parent[uroot] = parent[vroot];
        } else {
            parent[uroot] = parent[vroot];
            rank[vroot]++;
        }
    }

    bool connected(int u, int v) {
        return root(u) == root(v); 
    }
};

class Solution {
private:

    class EdgeComparator {
    public:
        bool operator() (Edge &e1, Edge &e2) {
            return e1.first > e2.first;
        }    
    };

    void kruskal() {
        // Kruskal's MST algorithm
        priority_queue<Edge, vector< Edge >, EdgeComparator> minpq;
        for (int i = 0; i < graph.size(); i++) {
            minpq.push(graph[i]);
        }

        UF *uf = new UF(n);
        while (!minpq.empty() && mst.size() < n - 1) {
            Edge e = minpq.top(); minpq.pop();
            int u = e.second.first;
            int v = e.second.second;
            if (!uf->connected(u - 1, v - 1)) {
                uf->unionize(u - 1, v - 1);
                mst.push_back(e);
                bottleneck = e.first;
            }
        }
        delete uf;
    }

public:
    int n, m;
    int bottleneck;
    vector< Edge > graph;
    vector< Edge > mst;

    void solve() {
        kruskal();
        cout << bottleneck << endl;
        cout << mst.size() << endl;
        for (int i = 0; i < mst.size(); i++) {
            Edge edge = mst[i];
            cout<< edge.second.first << " " << edge.second.second << endl;
        }
    }
};

int main() {
    Solution solver;
    cin >> solver.n >> solver.m;
    int u, v, w;
    while (solver.m--) {
        cin >> u >> v >> w;
        solver.graph.push_back(make_pair(w, make_pair(u, v)));
    }
    solver.solve();
    return 0;
}