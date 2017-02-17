// Source: http://www.practice.geeksforgeeks.org/problem-page.php?pid=558
import java.util.Scanner;

class InversionCount {
    private static int mergeAndCount(int[] a, int[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        // assume that a[lo:mid] and a[mid+1:hi] are sorted
        int cnt = 0; // counter of inversions
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) { // i is exhausted
                a[k] = aux[j++];
            }
            else if (j > hi) { // j is exhausted
                a[k] = aux[i++];
            }
            else if (aux[j] < aux[i]) {
                cnt += mid + 1 - i;
                a[k] = aux[j++];
            }
            else {
                a[k] = aux[i++];
            }
        }

        return cnt;
    }

    private static int sortAndCount(int[] a, int[] aux, int lo, int hi) {
        if (hi <= lo) return 0;
        int mid = (lo + hi) / 2;
        int cnt = 0;
        cnt += sortAndCount(a, aux, lo, mid);
        cnt += sortAndCount(a, aux, mid + 1, hi);
        cnt += mergeAndCount(a, aux, lo, mid, hi);
        return cnt;
    }

    public static int inversionCount(int[] a) {
        int[] aux = new int[a.length];
        return sortAndCount(a, aux, 0, a.length - 1);
    }

    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);
        int T = scan.nextInt();
        while (T-- > 0) {
            int N = scan.nextInt();
            int[] a = new int[N];
            for (int i = 0; i < N; i++) {
                a[i] = scan.nextInt();
            }
            System.out.println(inversionCount(a));
        }
    }
}
