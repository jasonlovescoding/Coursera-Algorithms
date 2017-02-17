import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int count = 0;
        int odd;
        String readin = "";
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            //StdOut.println("count is "+count);
            readin = StdIn.readString();
            //StdOut.println("readin is "+readin);
            if (k == 0) {
                continue;
            }
            else {
                count++;
                if (count > k) {        // reservoir sampling
                    odd = StdRandom.uniform(1, count+1);
                    if (odd <= k) {
                        q.dequeue();
                        q.enqueue(readin);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    q.enqueue(readin);
                }
            }
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }
    }
}