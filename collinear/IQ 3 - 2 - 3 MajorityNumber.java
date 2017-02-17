/* Source:
*  https://leetcode.com/problems/majority-element-ii/
*/
import java.util.List;
import java.util.ArrayList;
public class Solution {
    public List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        List<Integer> rst = new ArrayList<>();
        if (n <= 1) {
            for (int i = 0; i < n; i++)
                rst.add(nums[i]);
            return rst;
        }
        if (n == 2) {
            rst.add(nums[0]);
            if (nums[0] != nums[1])
                rst.add(nums[1]);
            return rst;
        }
        int n3thc = 0; // the n/3-th counter
        int n2thc = 0; // the 2n/3-th counter
        int n3th = select(nums, n/3);
        int n2th = select(nums, 2*n/3);

        for (int i = 0; i < n; i++) {
            if (nums[i] == n3th) n3thc++;
            else if (nums[i] == n2th) n2thc++;
        }

        if (n3thc > n/3) rst.add(n3th);
        if (n2thc > n/3) rst.add(n2th);
        return rst;
    }

    private static void swap(int[] a, int p, int q) {
        int tmp = a[p];
        a[p] = a[q];
        a[q] = tmp;
    }

    private static int partition(int[] nums, int lo, int hi) { // descending partition
        int i = lo + 1, j = hi;
        for (;;) {
            while (nums[lo] < nums[i] && i < j) i++;
            while (nums[lo] > nums[j] && i < j) j--;
            if (i >= j) break;
            swap(nums, i, j);
        }
        swap(nums, lo, j);
        return j;
    }

    private static int select(int[] nums, int k) { // find the k-th largest in nums
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int j = partition(nums, lo, hi);
            if (j < k) lo = j + 1;
            else if (j > k) hi = j - 1;
            else return nums[k];
        }
        return nums[k];
    }
}
