// Source: http://www.lintcode.com/en/problem/median-of-two-sorted-arrays/
import java.util.Arrays;
import java.lang.Math;

class Solution {
    /**
     * @param A: An integer array.
     * @param B: An integer array.
     * @return: a double whose format is *.5 or *.0
     */
    public double findMedianSortedArrays(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        if (m <= n)
            return findMedianAB(A, m, B, n);
        return findMedianAB(B, n, A, m);
    }

    private double findMedianAB(int[] A, int m, int[] B, int n) {
        assert(m <= n);
        if (m == 0) {
            return median(B, n);
        }

        if (m == 1) {
            if (n == 1)
                return MO2(A[0], B[0]);

            if (n % 2 == 1) {
                return (MO3(B[n/2 - 1], B[n/2 +1], A[0]) + B[n/2]) / 2.0;
            }

            return MO3(B[n/2 - 1], B[n/2], A[0]);
        }

        if (m == 2) {
            if (n == 2) {
                return MO4(A[0], A[1], B[0], B[1]);
            }

            if (n % 2 == 1) {
                return MO3( B[n/2],
                        Math.max(A[0], B[n/2 - 1]),
                        Math.min(A[1], B[n/2 + 1]) );
            }

            return MO4( B[n/2], B[n/2 - 1],
                    Math.max( A[0], B[n/2 - 2] ),
                    Math.min( A[1], B[n/2 + 1] ) );
        }

        int midA = (m - 1) / 2;
        int midB = (n - 1) / 2;
        if (A[midA] <= B[midB]) { // comparing the medians is also ok but a bit slower
            return findMedianAB( Arrays.copyOfRange(A, midA, m), m - midA,
                    Arrays.copyOfRange(B, 0, n - midA), n - midA );
        }
        // the most tricky part. reduce the length of BOTH A AND B by midA, rather than by half!
        return findMedianAB( Arrays.copyOfRange(A, 0, m - midA), m - midA,
                Arrays.copyOfRange(B, midA, n), n - midA );
        // try [1,5,6], [2,3,4,7,8] to find that dropping both arrays by half is wrong.
        // drop-by-midA approach guarantees two things:
        // 1, at least we are still dropping elements safely (only that we drop a bit less elements from B)
        // 2, we do not change the being-odd-or-even for the union of sub-A and sub-B from the original one,
        // which is important because we need to compute the median of the subset in the same way
        // as we do for the original union of A and B (odd: middle; even: average of 2 middles)
    }

    private double MO2(int a, int b) {
        return (a + b) / 2.0;
    }

    private double MO3(int a, int b, int c) {
        return a + b + c
                - Math.max(a, Math.max(b, c))
                - Math.min(a, Math.min(b, c));
    }

    private double MO4(int a, int b, int c, int d) {
        int max = Math.max( a, Math.max( b, Math.max( c, d ) ) );
        int min = Math.min( a, Math.min( b, Math.min( c, d ) ) );
        return (a + b + c + d - max - min) / 2.0;
    }

    private double median(int[] arr, int n) {
        if (n == 0)
            return Double.NaN; // error

        if (n % 2 == 0)
            return (arr[n / 2] + arr[n / 2 - 1]) / 2.0;
        return arr[n/2];
    }

}
