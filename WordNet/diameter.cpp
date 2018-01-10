/**
 * https://leetcode.com/problems/diameter-of-binary-tree/
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     TreeNode *left;
 *     TreeNode *right;
 *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
 * };
 */
class Solution {
private:
    int diameter(TreeNode* root, int *height) {
        int lh = 0, rh = 0;
        int ld = 0, rd = 0;
        if (root == NULL) {
            *height = 0;
            return 0;
        }
        ld = diameter(root->left, &lh);
        rd = diameter(root->right, &rh);
        *height = max(lh + 1, rh + 1);
        return max(lh + rh, max(ld, rd));
    }

public:
    int diameterOfBinaryTree(TreeNode* root) {
        int height;
        return diameter(root, &height);
    }
};