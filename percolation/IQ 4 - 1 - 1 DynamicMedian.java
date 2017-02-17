// Source: https://leetcode.com/problems/find-median-from-data-stream/
import java.util.PriorityQueue;
import java.util.Collections;

public class MedianFinder {
    private PriorityQueue<Integer> leftSet;
    // leftSet is a max priority queue
    private PriorityQueue<Integer> rightSet;
    // rightSet is a min priority queue

    public MedianFinder() {
        leftSet = new PriorityQueue<>(Collections.reverseOrder());
        rightSet = new PriorityQueue<>();
    }

    // Adds a number into the data structure.
    public void addNum(int num) {
        if (leftSet.size() == 0) // the initial insertion
            leftSet.add(num);
        else if (num < leftSet.peek()) {
            leftSet.add(num);
            if (leftSet.size() == rightSet.size() + 2)
                rightSet.add(leftSet.poll());
        }
        else {
            rightSet.add(num);
            if (rightSet.size() == leftSet.size() + 2)
                leftSet.add(rightSet.poll());
        }

    }

    // Returns the median of current data stream
    public double findMedian() {
        if (leftSet.size() > rightSet.size())
            return leftSet.peek();
        else if (leftSet.size() < rightSet.size())
            return rightSet.peek();
        else
            return (leftSet.peek() + rightSet.peek()) / 2.0;
    }

    public static void main(String[] args) {
        MedianFinder mf = new MedianFinder();
        mf.addNum(1);
        mf.findMedian();
        mf.addNum(2);
        mf.addNum(3);
        System.out.print(mf.findMedian());
    }
};

// Your MedianFinder object will be instantiated and called as such:
// MedianFinder mf = new MedianFinder();
// mf.addNum(1);
// mf.findMedian();
