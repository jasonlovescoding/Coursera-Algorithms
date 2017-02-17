import java.util.List;
import java.util.ArrayList;

public class MajorityNumber2 {
    public List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        Integer n1 = null, n2 = null;
        int c1 = 0, c2 = 0;

        for (int id = 0; id < n; id++) {
            if(n1 != null && nums[id] == n1.intValue()) c1++;
            else if (n2 != null && nums[id] == n2.intValue()) c2++;
            else if (c1 == 0) {
                n1 = nums[id];
                c1 = 1;
            }
            else if (c2 == 0) {
                n2 = nums[id];
                c2 = 1;
            }
            else {
                c1--;
                c2--;
            }
        }
        c1 = c2 = 0;
        for (int id = 0; id < n; id++) {
            if (nums[id] == n1.intValue()) c1++;
            else if (nums[id] == n2.intValue()) c2++;
        }
        List<Integer> rst = new ArrayList<>();
        if (c1 > n/3) rst.add(n1);
        if (c2 > n/3) rst.add(n2);
        return rst;
    }

}
