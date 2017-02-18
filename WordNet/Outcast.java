import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] d = new int[nouns.length];
        Arrays.fill(d, 0);

        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d.length; j++) {
                d[i] += this.wordNet.distance(nouns[i], nouns[j]);
            }
        }

        int maxId = 0;
        int maxDist = d[0];
        for (int i = 1; i < d.length; i++) {
            if (d[i] > maxDist) {
                maxId = i;
                maxDist = d[i];
            }
        }
        return nouns[maxId];
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}