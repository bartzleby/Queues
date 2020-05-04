import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/*
 * we implement this randomized queue with a resizing array
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] rq;
    private int n; // size of randomized queue
    
    // the following is very important in this implementation
    // to track gaps in middle of queue after randomized dequeue
    private int nSpan; // size of iteration (= n + gaps)
    // we also must use this generate random indices
    // and for up-sizing array.  
    // It can be thought of as effective size of Q.
    
    private int first; // index of first item in queue
    private int last; // index of final item in queue
    
	/*
	 * A key method of our implementation.
	 */
	private int genRandomNotNull() {
		int i;
		do { i = StdRandom.uniform(nSpan); }
        while (rq[(i + first) % rq.length] == null);
		return i;
	}

	/**
	 *  construct an empty randomized queue
	 */
    @SuppressWarnings("unchecked")
	public RandomizedQueue() {
    	rq = (Item[]) new Object[2];
    	first = 0;
    	last = -1;
    	n = 0; nSpan = 0;
    }

    /**
     * is the queue empty?
     */
    public boolean isEmpty() {
    	return size() == 0;
    }

    /**
     * return the number of items in the queue
     * @return
     */
    public int size() {
    	return n;
    }

    /*
     * resize the underlying array
     * @param capacity
     */
    private void resize(int capacity) {
        assert capacity >= n;
        @SuppressWarnings("unchecked")
		Item[] temp = (Item[]) new Object[capacity];
        int insertionIndex = 0;
        for (int i = 0; i < nSpan; i++) {
        	if (rq[(first + i) % rq.length] == null) continue;
            temp[insertionIndex++] = rq[(first + i) % rq.length];
        }
        rq = temp;
        first = 0;
        last  = n-1;
        nSpan = n;
    }

    /**
     * Adds the item to this queue.
     * @param item the item to add
     */
    public void enqueue(Item item) {
    	
    	if (item == null) throw new NullPointerException();
        
        if (nSpan == rq.length) resize(2*rq.length);   // double size of array if necessary
        if (last+1 == rq.length) last = 0;          // wrap-around
        
        rq[++last] = item;                        // add item
    	
        n++; nSpan++;
        
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     * @return the item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        
        int ri = genRandomNotNull(); // a randomly generated number to offset first index
        
        Item item = rq[(ri + first) % rq.length];
        rq[(ri + first) % rq.length] = null;
        
        // if we are dequeuing first:
        if (ri == 0) {
	        nSpan--;
	        first++;
	        if (first == rq.length) first = 0; // wrap-around
        }
        // if we are dequeuing last:
        if (ri == nSpan) {
        	nSpan--;
        	last--;
        	if (last == -1) last = rq.length-1; // wrap back around
        }
        
        n--;
        trimEnds(); // to ensure neither first nor last point to null.
        if (n > 0 && n == rq.length/4) resize(rq.length/2); // shrink size of array if necessary
        
        return item;
    }

    /*
     * Trim null values from beginning and end of randomized queue array
     */
    private void trimEnds() {
    	
    	while (rq[first] == null) {
	        nSpan--;
	        first++;
	        if (first == rq.length) first = 0; // wrap-around
    	}
    	
    	while (rq[last] == null) {
        	nSpan--;
        	last--;
        	if (last == -1) last = rq.length-1; // wrap back around
    	}
		
	}

	/**
	 * return (but do not delete) a random item
	 * @return sampled_item
	 */
    public Item sample() {
    	return rq[ (genRandomNotNull() + first) % rq.length ];
    }

    /**
     * Returns an iterator that iterates over the items in this queue in random order.
     * @return an iterator that iterates over the items in this queue in random order
     */
    public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}    // return an independent iterator over items in random order
    
    private class RandomizedQueueIterator implements Iterator<Item> {
    	
    	/*
    	 * constructor to generate a random sequence of first index offsets
    	 */
    	private int[] ris = new int[nSpan];
    	RandomizedQueueIterator() {
    		for (int ri = 0; ri < nSpan; ri++) {
    			ris[ri] = ri;
    		}
    		StdRandom.shuffle(ris);
    	}
    	
        private int i = 0;
        private int ni = 0; // counter to track number of non-null items returned by iterator
        					// ( needed to prevent the occasional return of a trailing null )
        public boolean hasNext()  { return ni < n; }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            
            Item item;
            do {
            item = rq[(ris[i++] + first) % rq.length];
            } while (item == null && hasNext());
            
            ni++;
            return item;
        }
    }

    // unit testing
    public static void main(String[] args) {
    	RandomizedQueue<String> rq = new RandomizedQueue<>();
    	
    	hardEnqueueEleven(rq);
    	rq.printQueue();

    	
    	System.out.println("Deqeueing: ");
    	for (int i = 0; i < 7; i++)
    		System.out.println(rq.dequeue());
    	System.out.println();
    	
    	rq.printQueue();
    	
    	System.out.println("size:                    " + rq.n);
    	System.out.println("effective size in array: " + rq.nSpan);
    	System.out.println("first:                   " + rq.first);
    	System.out.println("last:                    " + rq.last);
    	int pri = 0;
    	for (String el: rq) {
    		System.out.println(pri++ + " " + el);
    	} System.out.println();
    	
    	testIteratorIndependence(rq);
    	
    	rq.enqueue("A");
    	rq.enqueue("B");
    	rq.enqueue("C");
    	
    	rq.printQueue();
    	
    	System.out.println("Deqeueing: ");
    	for (int i = 0; i < 3; i++)
    		System.out.println(rq.dequeue());
    	System.out.println();
    	
    	rq.printQueue();
    	
    	rq.enqueue("A");
    	rq.enqueue("B");
    	rq.enqueue("C");
    	
    	rq.printQueue();
    	
    	System.out.println("Deqeueing: ");
    	for (int i = 0; i < 3; i++)
    		System.out.println(rq.dequeue());
    	System.out.println();
    	
    	rq.printQueue();
    	
    	rq.enqueue("A");
    	rq.enqueue("B");
    	rq.enqueue("C");
    	
    	rq.printQueue();
    	
    	System.out.println("Deqeueing: ");
    	for (int i = 0; i < 3; i++)
    		System.out.println(rq.dequeue());
    	System.out.println();
    	
    	rq.printQueue();
    	
    	System.out.println();
    	
    	
    	System.out.println("Sample: "+rq.sample());
    	System.out.println("Sample: "+rq.sample());
    	System.out.println("Sample: "+rq.sample());
    	System.out.println("Sample: "+rq.sample());
    	System.out.println("Sample: "+rq.sample());
    	
    	// isEmpty
    	// size()
    	
    }
    
    // for testing
    private void printQueue() {
    	System.out.print("Current Queue:\n");
    	for (int i = 0; i < rq.length; i++) {;
    		System.out.println(rq[i]);
    	} System.out.println();
    }

	private static void testIteratorIndependence(RandomizedQueue<String> rq) {
		
		System.out.println("Tetsing iterator independence:");
		System.out.printf("n:%3s; nSpan:%3s\n\n", rq.n, rq.nSpan);
		
    	String[] str1s = new String[rq.size()];
    	String[] str2s = new String[rq.size()];
    	String[] str3s = new String[rq.size()];
    	
    	int k;
		int j;
    	int i = 0;
    	for (String el1: rq) {
    		str1s[i++] = el1;
    		j = 0;
    		for (String el2 : rq) {
    			str2s[j++] = el2;
    			k = 0;
    			for (String el3 : rq) {
    				str3s[k++] = el3;
    			}
    		}
    	}
    	
    	for (int p = 0; p < rq.size(); p++) {
    		System.out.printf("%10s %10s %10s\n", str1s[p], str2s[p], str3s[p]);
    	} System.out.println();

	}
	
	private static void hardEnqueueEleven(RandomizedQueue<String> rq) {
		rq.enqueue("zeroth");
    	rq.enqueue("first");
    	rq.enqueue("second");
    	rq.enqueue("third");
    	rq.enqueue("fourth");
    	rq.enqueue("fifth");
    	rq.enqueue("sixth");
    	rq.enqueue("seventh");
    	rq.enqueue("eighth");
    	rq.enqueue("ninth");
    	rq.enqueue("tenth");
	}

}