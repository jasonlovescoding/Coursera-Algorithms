import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        private Item item;
        private Node prev, next;
    }

    public Deque() {                           // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {                // is the deque empty?
        return (size == 0);
    }

    public int size() {                        // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {          // add the item to the front
        if (item == null)
            throw new java.lang.NullPointerException();

        if (size >= 1) {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            first.prev = null;
            oldfirst.prev = first;
            size++;
        }
        else {
            first = new Node();
            first.item = item;
            first.prev = null;
            first.next = null;
            last = first;
            size++;
        }
    }

    public void addLast(Item item) {           // add the item to the end
        if (item == null)
            throw new java.lang.NullPointerException();

        if (size >= 1) {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = oldlast;
            oldlast.next = last;
            size++;
        }
        else {
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
            size++;
        }
    }

    public Item removeFirst() {              // remove and return the item from the front
        if (size == 0)
            throw new java.util.NoSuchElementException();

        Item item = first.item;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }
        size--;
        return item;
    }

    public Item removeLast() {                // remove and return the item from the end
        if (size == 0)
            throw new java.util.NoSuchElementException();

        Item item = last.item;
        if (size == 1) {
            last = null;
            first = null;
        }
        else {
            last = last.prev;
            last.next = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (current == null)
                throw new java.util.NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {   // unit testing

    }
}
