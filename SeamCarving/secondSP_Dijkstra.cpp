// http://poj.org/problem?id=3255
#include <iostream>
#include <vector>
#include <utility>
#include <queue>
using namespace std;
using INT_PAIR = pair<int, int>;

const int N = 8192;
const int INF = 0x3f3f3f3f; 

class Solution {
public:
    int n, r;
    vector<INT_PAIR> graph[N];
    // the optimal distance from vertex 0
    int dist[N];
    // the 2nd optimal distance from vertex 0
    int secondDist[N];

    Solution() {
        memset(dist, INF, N * sizeof(int));
        memset(secondDist, INF, N * sizeof(int));
        dist[0] = 0;
    }

    void solve() {
        // modified Dijkstra: each node, when relaxed, must be pushed back since it could relax other nodes
        priority_queue<INT_PAIR, vector<INT_PAIR>, greater<INT_PAIR>> pq;
        // initially distance is 0, the path starts from vertex 0
        pq.push(make_pair(0, 0)); 
        while (!pq.empty()) {
            INT_PAIR p = pq.top(); pq.pop();
            int cost = p.first;
            int dst = p.second;
            // if the next node's 2nd best distance is smaller than current accunmulative cost,
            // there is no way to update 1st/2nd best cost
            if (secondDist[dst] < p.first) continue;

            for (int i = 0; i < graph[dst].size(); i++) {
                // starting from the next node, relax each outgoing edge
                INT_PAIR edge = graph[dst][i];
                int nextDst = edge.first;
                int nextCost = cost + edge.second;
                if (nextCost < dist[nextDst]) {
                    // update the optimal distance, same as Dijkstra
                    swap(nextCost, dist[nextDst]);
                    // different from Dijkstra, the priority queue allows the same 
                    // destination with different costs
                    pq.push(make_pair(dist[nextDst], nextDst));
                }
                // if the distance is not optimal but is better than sub-optimal,
                // update the 2nd optimal value
                // the case also includes outdated optimal, hence the swap()
                if (dist[nextDst] < nextCost && secondDist[nextDst] > nextCost) {
                    secondDist[nextDst] = nextCost;
                    // another difference is that the 2nd best path is also pushed back to pq,
                    // because it could lead to further 2nd best paths
                    pq.push(make_pair(secondDist[nextDst], nextDst));
                }
            }
        }
    }

};

int main() {
    Solution solver;
    cin >> solver.n >> solver.r;
    int a, b, d;
    while (solver.r--) {
        cin >> a >> b >> d;
        a--, b--;
        solver.graph[a].push_back(make_pair(b, d));
        solver.graph[b].push_back(make_pair(a, d));
    }
    solver.solve();
    cout << solver.secondDist[solver.n - 1] << endl;
    return 0;
}