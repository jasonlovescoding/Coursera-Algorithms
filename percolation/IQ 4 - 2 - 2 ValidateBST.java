// Source: https://leetcode.com/problems/validate-binary-search-tree/
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Solution {
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);
    }

    private boolean validate(TreeNode root, Integer lo, Integer hi) {
        if (root == null)
            return true;
        if ((lo == null || lo.intValue() < root.val) && (hi == null || hi.intValue() > root.val))
            return validate(root.left, lo, root.val) && validate(root.right, root.val, hi);
        else
            return false;
    }
}
