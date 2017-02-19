// source: http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.HashMap;

public class WordNet {
    private Digraph wordNet;
    private SAP sap;
    private HashMap<Integer, String> id2synset;
    private HashMap<String, HashSet<Integer>> noun2ids;
    // private TreeMap dictionary; // used for storing the gloss

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        verifyNotNull(synsets);
        verifyNotNull(hypernyms);

        // read synsets file and create the symbol table from synset id to synset
        // and the symbol table from a synonym noun to its synset id(s)
        readSynsets(synsets);
        // read hypernym file and build the wordnet
        readHypernyms(hypernyms);
        // verify the wordnet
        verifyWordNet();
        // initialize the SAP
        this.sap = new SAP(this.wordNet);
    }

    private void readSynsets(String synsets) {
        In input = new In(synsets);
        this.id2synset = new HashMap<>();
        this.noun2ids = new HashMap<>();
        HashSet<Integer> set;
        while (input.hasNextLine()) {
            // read one line per time
            String[] tokens = input.readLine().split(",");
            int id = Integer.parseInt(tokens[0]);
            String synset = tokens[1];
            // correspond the id to the synset
            id2synset.put(id, synset);
            // correspond the synonym to its synset id(s)
            for (String synonym : synset.split(" ")) {
                set = noun2ids.get(synonym);
                if (set == null) {
                    set = new HashSet<>();
                    set.add(id);
                    noun2ids.put(synonym, set);
                }
                else {
                    set.add(id);
                }
            }
        }
        input.close();
    }

    private void readHypernyms(String hypernyms) {
        this.wordNet = new Digraph(id2synset.size());
        In input = new In(hypernyms);
        while (input.hasNextLine()) {
            String[] tokens = input.readLine().split(",");
            int hyponymId = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                int hypernymId = Integer.parseInt(tokens[i]);
                wordNet.addEdge(hyponymId, hypernymId);
            }
        }
        input.close();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun2ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        verifyNotNull(word);
        return noun2ids.containsKey(word);
    }

    // shortest ancestral path distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        verifyNotNull(nounA);
        verifyNotNull(nounB);
        verifyNoun(nounA);
        verifyNoun(nounB);

        HashSet<Integer> nounAIds = noun2ids.get(nounA);
        HashSet<Integer> nounBIds = noun2ids.get(nounB);
        return sap.length(nounAIds, nounBIds);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        verifyNotNull(nounA);
        verifyNotNull(nounB);
        verifyNoun(nounA);
        verifyNoun(nounB);

        HashSet<Integer> nounAIds = noun2ids.get(nounA);
        HashSet<Integer> nounBIds = noun2ids.get(nounB);
        int ancestorId = sap.ancestor(nounAIds, nounBIds);
        return id2synset.get(ancestorId);
    }

    private void verifyNotNull(Object var) {
        if (var == null) throw new java.lang.NullPointerException();
    }

    private void verifyNoun(String str) {
        if (!isNoun(str)) throw new java.lang.IllegalArgumentException();
    }

    private class WordNetVerifier {
        // denotes whether the vertex is marked in DFS
        private boolean[] marked;
        // denotes whether the vertex is stacked into postorder in DFS
        private boolean[] posted;
        private boolean acyclic;
        private boolean singlyRooted;

        public WordNetVerifier() {
            this.marked = new boolean[wordNet.V()];
            this.posted = new boolean[wordNet.V()];
            this.acyclic = true;
            // the graph has a cycle iff a vertex is checked when it is marked but not posted
            // which means the DFS call on it has not ended yet, but a path leads back to it
            int rootNumber = 0;
            for (int v = 0; v < wordNet.V(); v++) {
                if (!this.marked[v]) dfs(v);
                if (wordNet.outdegree(v) == 0) rootNumber++; // root has no ancestor
            }
            this.singlyRooted = (rootNumber == 1);
        }

        private void dfs(int v) {
            this.marked[v] = true;
            for (int w : wordNet.adj(v)) {
                // check all the neighbors
                if (this.marked[w] && !this.posted[w]) {
                    this.acyclic = false;
                    return;
                }
                if (!this.marked[w]) dfs(w);
            }
            this.posted[v] = true;
        }

        // returns true if this graph is a legal WordNet (singly rooted DAG)
        public boolean isWordNet() {
            return (acyclic && singlyRooted);
        }
    }

    private void verifyWordNet() {
        WordNetVerifier verifier = new WordNetVerifier();
        if (!verifier.isWordNet()) { // not a singly rooted DAG
            throw new java.lang.IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet model = new WordNet("./src/synsets.txt", "./src/hypernyms.txt");
        StdOut.print(model.distance("1750s", "1790s"));
    }
}
