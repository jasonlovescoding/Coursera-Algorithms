// https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1283
#include <iostream>
#include <vector>
#include <utility>
using namespace std;

const int N = 128;
const int INF = 0x3f3f3f3f;

class Solution {
public:
    int n, r, q;
    vector< pair<int, int> > graph[N];
    int dist[N][N];
    int secondDist[N][N];

    void initialize() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dist[i][j] = secondDist[i][j] = INF;
            }
            graph[i].clear();
            dist[i][i] = 0;
        }
    }

    void floyd_warshall() {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
    }

    // this DFS attempts to update the secondSP's distance from src to dst
    // if the current cost is already larger than current secondSP's cost, no possibility, return
    // if the current cost is not larger than that but larger than optimal distance, update
    // then, find all next nodes from dst, and accumulate the cost so far, search that node
    void dfs(int src, int dst, int cost) {
        if (cost > secondDist[src][dst]) return;
        if (cost > dist[src][dst]) secondDist[src][dst] = cost;
        for (int i = 0; i < graph[dst].size(); i++) {
            int nextDst = graph[dst][i].first;
            dfs(src, nextDst, cost + graph[dst][i].second);
        }
    }
};

int main() {
    int s = 0;
    Solution solver;
    while (cin >> solver.n >> solver.r) {
        solver.initialize();
        int u, v, w;
        while (solver.r--) {
            cin >> u >> v >> w;
            solver.dist[v][u] = solver.dist[u][v] = w;
            solver.graph[v].push_back(make_pair(u, w));
            solver.graph[u].push_back(make_pair(v, w));
        }
        solver.floyd_warshall();
        // do dfs for 2nd SP for each vertex
        for (int i = 0; i < solver.n; i++) {
            solver.dfs(i, i, 0);
        }
        cout << "Set #" << ++s << endl;
        cin >> solver.q;
        while (solver.q--) {
            cin >> u >> v;
            if (solver.secondDist[u][v] == INF) {
                cout << "?" << endl;
            } else {
                cout << solver.secondDist[u][v] << endl;
            }
        }
    }
}