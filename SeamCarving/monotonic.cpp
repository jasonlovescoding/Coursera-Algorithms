// https://ideone.com/8JQ0tB
//./a.out | dot -Tpng >test.png
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

struct Edge
{
    int from;
    int to;
    int weight;
    int backPtr;
};
using Edges = vector<Edge>;

struct Vertex
{
    vector<int> outEdges;
    int pos = 0;
    int backPtr = -1;
};
using Vertices = vector<Vertex>;

struct QEntry
{
    int pathWeight;
    int inEdge;
};
bool operator < (const QEntry& l, const QEntry& r)
{
    return l.pathWeight > r.pathWeight;
}
using PQ = priority_queue<QEntry>;

void getDecreasingSP(Vertices& vertices, Edges& edges, int src)
{
	// sort all edges w.r.t. increasing weight
    for (auto& v: vertices)
        sort(begin(v.outEdges), end(v.outEdges),
             [&](int from, int to)
             {
                 return edges[from].weight < edges[to].weight;
             });

    PQ pq;
    auto& src_v = vertices[src];
	// for the source vertex, simply position it at the end
	// to indicate all edges have been iterated upon
    for (auto e: src_v.outEdges)
    {
        QEntry entry {edges[e].weight, e};
        pq.push(entry);
        ++src_v.pos;
    }

    while(!pq.empty())
    {
        QEntry top = pq.top();
		// pop the top entry, whose inEdge has the smallest weight
        pq.pop();
        auto& v = vertices[edges[top.inEdge].to];

		// loop until the position of iteration reaches the end, 
		// or the current edge has larger weight than incoming edge (not monotinic)
        while (v.pos < int(v.outEdges.size()) &&
            edges[v.outEdges[v.pos]].weight < edges[top.inEdge].weight)
        {
			// this edge has inEdge as its backPtr
            auto e = v.outEdges[v.pos];
            edges[e].backPtr = top.inEdge;
			// make an entry for its current path weight, and push back
            QEntry entry {top.pathWeight + edges[e].weight, e};
            pq.push(entry);
			// look at the next position
            ++v.pos;

            /*cout << edges[top.inEdge].to << " -> "
                << edges[e].to << " # "
                << entry.pathWeight << '\n';*/
        }
		// if this vertex has no backPtr yet, it is connected for the 1st time
		// mark its backPtr as the current entry's
        if (v.backPtr == -1)
            v.backPtr = top.inEdge;
    }
}

int main()
{
    Edges edges {
        {0, 1, 1, -1},
        {0, 2, 4, -1},
        {0, 3, 9, -1},
        {1, 2, 2, -1},
        {1, 3, 6, -1},
        {2, 3, 3, -1},
        {3, 4, 4, -1},
        {3, 6, 8, -1},
        {4, 5, 5, -1},
        {5, 3, 7, -1},
        {5, 6, 6, -1},
        {6, 2, 5, -1},
        {6, 7, 7, -1},
        {7, 5, 9, -1},
    };

    auto max_from = max_element(begin(edges), end(edges),
                                [](const Edge& l, const Edge& r) {
                                    return l.from < r.from;
                                });

    auto max_to = max_element(begin(edges), end(edges),
                                [](const Edge& l, const Edge& r) {
                                    return l.to < r.to;
                                });

    auto max_v = max(max_from->from, max_to->to);
    Vertices vertices(max_v + 1);
    int edge_nr = 0;

    for (const auto& e: edges)
        vertices[e.from].outEdges.push_back(edge_nr++);

    getDecreasingSP(vertices, edges, 0);

    for (auto e = vertices[max_v].backPtr; e >= 0;)
    {
        auto& mark = edges[e].backPtr;
        e = edges[e].backPtr;
        mark = -2;
    }

    cout << "digraph test {\n";
    cout << "rankdir=LR;\n";
    cout << "node [label=\"\" shape=circle height=0.1 fillcolor=gray style=filled];\n";
    cout << "edge [label=\"\"];\n";

    for (const auto& e: edges)
    {
        cout << 'v' << e.from << " -> v" << e.to << " [label=\"" << e.weight << '"';
        if (e.backPtr == -2)
            cout << " color = red";
        cout << "];\n";
    }

    cout << "}\n";
}
