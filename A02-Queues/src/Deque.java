import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int s; // size of deque

    private class Node {
    	private Item item;
    	private Node next;
    	private Node prev;
    }

	/**
	 *  construct an empty deque
	 */
    @SuppressWarnings("unused")
	public Deque() {
    	Node first = null;
    	this.head = first;
    	this.tail = first;
    	this.s = 0;
    }

    /**
     *  is the deque empty?
     * @return
     */
    public boolean isEmpty() {
    	return size() == 0;
    }

    /**
     *  return the number of items on the deque
     * @return
     */
    public int size() {
    	return s;
    }

    /** add the item to the front
     * 
     * @param item
     */
    public void addFirst(Item item) {
    	if (item == null)
    		throw new NullPointerException();
    	
    	Node newFirst = new Node();
    	newFirst.item = item;
    	newFirst.prev = null;
    	newFirst.next = head;
    	
    	try { head.prev = newFirst;
    	} catch (NullPointerException e) {
    		tail = newFirst;
    	}
    	
    	head = newFirst;
    	s++;
    }

    /**
     *  add the item to the back
     * @param item
     */
    public void addLast(Item item) {
    	if (item == null)
    		throw new NullPointerException();
    	
    	Node newLast = new Node();
    	newLast.item = item;
    	newLast.prev = tail;
    	newLast.next = null;
    	
    	try { tail.next = newLast;
    	} catch (NullPointerException e) {
    		head = newLast;
    	}
    	
    	tail = newLast;
    	s++;
    }

    /**
     *  remove and return the item from the front
     * @return
     */
    public Item removeFirst() {
    	if (isEmpty())
    		throw new java.util.NoSuchElementException();
    	
    	Item returnItem = head.item;
    	
    	head = head.next;
    	head.prev = null;
    	s--;
    	
		return returnItem;
	}

    /**
     *  remove and return the item from the back
     * @return
     */
    public Item removeLast() {
    	if (isEmpty())
    		throw new java.util.NoSuchElementException();

    	Item returnItem = tail.item;
    	
    	tail = tail.prev;
    	tail.next = null;
    	s--;
    	
		return returnItem;
	}

    /**
     *  return an iterator over items in order from front to back
     */
    public Iterator<Item> iterator() {
		return new DequeIterator();
	}
    
    private class DequeIterator implements Iterator<Item> {
    	
    	private Node current = head;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			
			if (!hasNext())
				throw new NoSuchElementException();
			
			Item item = current.item;
			current = current.next;
			return item;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
    }

    // unit testing (required)
    public static void main(String[] args) {
    	Deque<String> d = new Deque<>();
    	boolean tempty;
    	
    	tempty = d.isEmpty();
    	System.out.printf("The Deque is empty: %b\n", tempty);
    	
    	d.addLast("T");
    	d.addFirst("D");
    	d.addLast("C");
    	d.addFirst("A");
    	d.addLast("B");
    	d.addLast("Z");
    	
    	tempty = d.isEmpty();
    	System.out.printf("The Deque is empty: %b\n", tempty);
    	
    	System.out.println();
    	
    	System.out.println("head: " + d.head.item);
    	System.out.println("tail: " + d.tail.item);
    	
    	System.out.println("\nIterating:");
    	for (String el: d) {
    		System.out.println(el);
    	}
    	System.out.println("size: " + d.size());
    	
    	System.out.println("removing: " + d.removeFirst());
    	System.out.println("removing: " + d.removeLast());
    	
    	System.out.println();
    	
    	System.out.println("head: " + d.head.item);
    	System.out.println("tail: " + d.tail.item);
    	
    	System.out.println("\nIterating:");
    	for (String el: d) {
    		System.out.println(el);
    	}
    	tempty = d.isEmpty();
    	System.out.printf("The Deque is empty: %b\n", tempty);
    	System.out.println("size: " + d.size());
    }

}