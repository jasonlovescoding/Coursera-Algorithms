// Source: http://www.lintcode.com/en/problem/3sum
import java.util.ArrayList;
import java.util.Arrays;
public class Solution {
    /**
     * @param numbers : Give an array numbers of n integer
     * @return : Find all unique triplets in the array which gives the sum of zero.
     */
    public static ArrayList<ArrayList<Integer>> threeSum(int[] numbers) {
        int i = 0, n = numbers.length;
        ArrayList<ArrayList<Integer>> rst = new ArrayList<>();

        Arrays.sort(numbers);
        while (i < n) {
            int hd = i + 1, tl = n - 1;
            while (hd < tl) {
                int threeSum = numbers[i] + numbers[hd] + numbers[tl];
                if (threeSum == 0) {
                    ArrayList<Integer> triple = new ArrayList<>();
                    triple.add(numbers[i]);
                    triple.add(numbers[hd]);
                    triple.add(numbers[tl]);
                    rst.add(triple);
                    while (hd < (n - 1) && numbers[hd] == numbers[hd + 1]) hd++;
                    hd++;
                    while (hd < tl && numbers[tl] == numbers[tl - 1]) tl--;
                    tl--;
                }
                else if (threeSum < 0) hd++;
                else tl--;
            }
            while (i < (n - 1) && numbers[i] == numbers[i + 1]) {
                i++;
            }
            i++;
        }

        return rst;
    }


}
