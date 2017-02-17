import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int size, tail, capacity;           // we guarantee that head is always 0

    public RandomizedQueue() {                 // construct an empty randomized queue
        size = 0;
        tail = -1;
        capacity = 2;                          // set the initial capacity to 2
        q = (Item[]) new Object[2];
    }

    public boolean isEmpty() {                // is the queue empty?
        return (size == 0);
    }

    public int size() {                       // return the number of items on the queue
        return size;
    }

    private void recapacity(int newCap) {     // resize the array of queue
        Item[] copy = (Item[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            copy[i] = q[i];
        }
        q = copy;
        capacity = newCap;
    }

    public void enqueue(Item item) {          // add the item
        if (item == null)
            throw new java.lang.NullPointerException();

        if (size == capacity) {
            recapacity(2*capacity);
        }
        tail++;
        q[tail] = item;
        size++;
    }

    private void swapItem(int id1, int id2) {   // swap two items in the array by indeces
        Item tmp;
        tmp = q[id1];
        q[id1] = q[id2];
        q[id2] = tmp;
    }

    public Item dequeue() {                  // remove and return a random item
        if (size == 0)
            throw new java.util.NoSuchElementException();

        int randomid = StdRandom.uniform(0, size);
        swapItem(tail, randomid); // swap the random item with the tail to randomize dequeue()

        Item item = q[tail];
        q[tail--] = null;
        size--;
        if ((size > 2) && (size <= capacity/4)) {
            recapacity(capacity/2);
        }
        return item; // return the tail item, which is the random chosen item
    }

    public Item sample() {                    // return (but do not remove) a random item
        if (size == 0)
            throw new java.util.NoSuchElementException();

        return q[StdRandom.uniform(0, size)];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int cur;
        private int[] r;

        public ListIterator() {
            cur = 0;
            r = new int[size];
            for (int i = 0; i < size; i++)
                r[i] = i;
            StdRandom.shuffle(r);
        }

        public boolean hasNext() {
            return (cur < size);
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() { // return the current item and point the current to the next one (random order)
            if (cur == size)
                throw new java.util.NoSuchElementException();

            return q[r[cur++]];
        }

    }
    public static void main(String[] args) {   // unit testing
    }
}
