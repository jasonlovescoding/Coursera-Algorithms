// http://www.lintcode.com/zh-cn/problem/course-schedule/
#include <iostream>
#include <stack>
#include <set>
#include <vector>
using namespace std;
#define UNVISITED 0
#define ONSTACK 1
#define VISITED 2

class Solution {
private:    
    vector<int> marked;
    bool cyclic;

    void dfs(vector< set<int> > digraph, int v) {
        if (marked[v] == ONSTACK) {
            cyclic = true;
            return;
        }
        marked[v] = ONSTACK;
        for (int w : digraph[v]) {
            if (marked[w] == UNVISITED) {
                dfs(digraph, w);
            } else if (marked[w] == ONSTACK) {
                cyclic = true;
                return;
            } else if (marked[w] == VISITED) {
                continue;
            }
            if (cyclic) return;
        }
        marked[v] = VISITED;
    }

public:
    bool canFinish(int numCourses, vector<pair<int, int> >& prerequisites) {
        vector< set<int> > digraph;
        digraph.resize(numCourses);
        for (int i = 0; i < prerequisites.size(); i++) {
            int dst = prerequisites[i].first;
            int src = prerequisites[i].second;
            digraph[src].insert(dst);
        }

        marked.resize(digraph.size());
        for (int v = 0; v < marked.size(); v++) {
            marked[v] = 0;
        }
        for (int v = 0; v < marked.size(); v++) {
            if (!marked[v]) {
                dfs(digraph, v);
                if (cyclic) {
                    return false;
                }
            } 
        }
        return true;
    }
}; 

int main() {
    Solution solver;
    int numCourses = 10;
    vector< pair<int, int> > p;
    p.push_back(make_pair(5,8));
    p.push_back(make_pair(3,5));
    p.push_back(make_pair(1,9));
    p.push_back(make_pair(4,5));
    p.push_back(make_pair(0,2));
    p.push_back(make_pair(1,9));
    p.push_back(make_pair(7,8));
    p.push_back(make_pair(4,9));
    solver.canFinish(numCourses, p);
}