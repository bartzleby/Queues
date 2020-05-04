/******************************************************************************
 *  Name:    Dan Bartz
 *  NetID:   
 *  Precept: 
 *
 *  Partner Name:     
 *  Partner NetID:    
 *  Partner Precept:  
 *
 *  Hours to complete assignment (optional):
 *
 ******************************************************************************/

Programming Assignment 2: Deques and Randomized Queues


/******************************************************************************
 *  Explain briefly how you implemented the randomized queue and deque.
 *  Which data structure did you choose (array, linked list, etc.)
 *  and why?
 *****************************************************************************/

I used a doubly linked list to implement the Deque with pointers to both the head and tail of the list 
so that each of the addFirst() and addLast() operations can be performed in constant time.  
With this structure we also need not worry about resizing.

The randomized queue has been implemented with a resizing array.  We need to use an array 
in order to randomly access from the middle of the structure in constant time.

/******************************************************************************
 *  How much memory (in bytes) do your data types use to store n items
 *  in the worst case? Use the 64-bit memory cost model from Section
 *  1.4 of the textbook and use tilde notation to simplify your answer.
 *  Briefly justify your answers and show your work.
 *
 *  Do not include the memory for the items themselves (as this
 *  memory is allocated by the client and depends on the item type)
 *  or for any iterators, but do include the memory for the references
 *  to the items (in the underlying array or linked list).
 *****************************************************************************/

Randomized Queue:   ~  ____8N bytes
we also must consider that the array will be doubled in size
if capacity is reached, i.e. N is a power of 2.  
We can then say in the worst case
RandomizedQueue will take ~16N bytes.  
If then N/2-1 items are removed
we will have ~32N bytes.

pub. class RandomizedQueue	16 	bytes 	object overhead

  priv Item[] rq			8 	bytes 	reference to Array
  							24 	bytes 	Array
  							8N 	bytes	references to object in Array
  
  priv int n				4 	bytes 	int
  priv int nSpan			4 	bytes 	int
  priv int first			4 	bytes 	int
  priv int last				4 	bytes 	int
  						------------
  						8N + 64
  						

Deque:              ~  ___48N bytes

pub. class Deque			16 	bytes 	object overhead
  priv Node head			8 	bytes 	reference to Node
  priv Node tail			8 	bytes 	reference to Node
  priv int s				4 	bytes 	int primitive
  
  pub inner class Node 16 + 8 	bytes 	OOH + extra inner class OH	-|
    Item item				8 	bytes 	reference					 |
    Node next				8 	bytes 	reference					 | x N
    Node prev				8 	bytes 	reference			---------|

							4 	bytes 	padding
						------------	
						48N + 40 bytes

/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/


/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/

Used an adapted version of Sedgewick's ResizingArrayQueue for my RandomizedQueue


/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/
 
No partner.


/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/

Getting the iterator to function properly.
another problem encountered: how to deal with the gaps



/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/