/* Source: 
 *  http://www.lintcode.com/en/problem/nuts-bolts-problem/
 *
 * public class NBCompare {
 *     public int cmp(String a, String b);
 * }
 * You can use compare.cmp(a, b) to compare nuts "a" and bolts "b",
 * if "a" is bigger than "b", it will return 1, else if they are equal,
 * it will return 0, else if "a" is smaller than "b", it will return -1.
 * When "a" is not a nut or "b" is not a bolt, it will return 2, which is not valid.
 */
public class Solution {
    /**
     * @param nuts: an array of integers
     * @param bolts: an array of integers
     * @param compare: a instance of Comparator
     * @return: nothing
     */
    public void sortNutsAndBolts(String[] nuts, String[] bolts, NBComparator compare) {
        sort(nuts, bolts, 0, nuts.length - 1, compare);
    }

    private int partitionBolts(String nut, String[] bolts, int lo, int hi, NBComparator compare){
        int i = lo;
        int j = hi;

        for (;;){
            while(compare.cmp(nut, bolts[i]) < 0 && i < j) {
                i++;
            }
            while(compare.cmp(nut, bolts[j]) > 0 && i < j){
                j--;
            }
            if (i >= j) break;
            swap(bolts, i, j);
        }

        return i;
    }

    private int partitionNuts(String bolt, String[] nuts,  int lo, int hi, NBComparator compare){
        int i = lo;
        int j = hi;

        for (;;) { // the order o comparison should not change (a nut to a bolt), so this extra partitionNuts is necessary
            while(compare.cmp(nuts[i], bolt) > 0 && i < j){
                i++;
            }
            while(compare.cmp(nuts[j], bolt) < 0 && i < j){
                j--;
            }
            if(i >= j) break;
            swap(nuts, i, j);
        }
        return i;
    }

    public void sort(String[] nuts, String[] bolts, int lo, int hi, NBComparator compare) {
        if (hi <= lo) return;
        int i = partitionBolts(nuts[lo], bolts, lo, hi, compare);
        int j = partitionNuts(bolts[i], nuts, lo, hi, compare);
        assert(i == j);
        sort(nuts, bolts, lo, i-1, compare);
        sort(nuts, bolts, i+1, hi, compare);
    }

    public static void swap(String a[], int p, int q) {
        String tmp = a[p];
        a[p] = a[q];
        a[q] = tmp;
    }
};
