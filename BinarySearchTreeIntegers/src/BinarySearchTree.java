import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;


/**
 * A class that represents a binary search tree.
 * @author William Collins, with some minor modifications by Richard Beeby
 *
 * @param <E> the element type for the tree
 */
public class BinarySearchTree<E extends Comparable<? super E>> extends AbstractSet<E> 
{
    // The root of this tree. This is null when the tree is empty.
	protected Entry<E> root;

	// The number of entries in this tree. This is zero when the tree is empty.
    protected int size;        
    
    
    // Counts the number of modifications made to this tree
    // other than by the iterator.
    protected int modCount = 0;
    
    List<Integer> dataSet = new LinkedList<>();
    
    /**
     *  Initializes this BinarySearchTree object to be empty, to contain only 
     *  elements of type E, to be ordered by the Comparable interface, and to 
     *  contain no duplicate elements.
     *
     */ 
    public BinarySearchTree() 
    {
        root = null;
        size = 0;  
    } // default constructor


    /**
     * Initializes this BinarySearchTree object to contain a shallow copy of
     * a specified BinarySearchTree object.
     * The worstTime(n) is O(n), where n is the number of elements in the
     * specified BinarySearchTree object.
     *
     * @param otherTree - the specified BinarySearchTree object that this
     *                BinarySearchTree object will be assigned a shallow copy of.
     * 
     */
    public BinarySearchTree (BinarySearchTree<? extends E> otherTree)
    {
         root = copy (otherTree.root, null);
         size = otherTree.size;  
    } // copy constructor


    protected Entry<E> copy (Entry<? extends E> p, Entry<E> parent)
    {
        if (p != null)
        {
            Entry<E> q = new Entry<E> (p.element, parent);
            q.left = copy (p.left, q);
            q.right = copy (p.right, q);
            return q;
        } // if
        return null;
    } // method copy
    
  
    public boolean equals (Object obj)
    {
        if (!(obj instanceof BinarySearchTree))
            return false;
        return equals (root, ((BinarySearchTree<? extends E>)obj).root);
    } // method 1-parameter equals
    
    public boolean equals (Entry<E> p, Entry<? extends E> q)
    {
       if (p == null || q == null)
           return p == q;      
       if (!p.element.equals (q.element))
           return false;
       if (equals (p.left, q.left) && equals (p.right, q.right) )
           return true;            
       return false;     
    } // method 2-parameter equals
    
    /**
     *  Returns the size of this BinarySearchTree object.
     *
     * @return the size of this BinarySearchTree object.
     *
     */   
    public int size( )
    {
        return size;
    } // method size()
  

    /**
     *  Returns an iterator positioned at the smallest element in this 
     *  BinarySearchTree object.
     *
     *  @return an iterator positioned at the smallest element in this
     *                BinarySearchTree object.
     *
     */
    public Iterator<E> iterator()
    {
         return new TreeIterator();
    } // method iterator

    /**
     *  Determines if there is an element in this BinarySearchTree object that
     *  equals a specified element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).  
     *
     *  @param obj - the element sought in this BinarySearchTree object.
     *
     *  @return true - if there is an element in this BinarySearchTree object that
     *                equals obj; otherwise, return false.
     *
     *  @throws ClassCastException - if obj cannot be compared to the 
     *           elements in this BinarySearchTree object. 
     *  @throws NullPointerException - if obj is null.
     *  
     */ 
    public boolean contains (Object obj) 
    {
        return getEntry (obj) != null;
    } // method contains //

 

    /**
     *  Ensures that this BinarySearchTree object contains a specified element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     *  @param element - the element whose presence is ensured in this 
     *                 BinarySearchTree object.
     *
     *  @return true - if this BinarySearchTree object changed as a result of this
     *                method call (that is, if element was actually inserted); 
     *                otherwise, return false.
     *
     *  @throws ClassCastException - if element cannot be compared to the 
     *                  elements already in this BinarySearchTree object.
     *  @throws NullPointerException - if element is null.
     *
     */
    public boolean add (E element)  
    {
        if (root == null) 
        {
            if (element == null)
                throw new NullPointerException();
            root = new Entry<E> (element, null);
            size++;
            modCount++;
            return true;
        } // empty tree
        else 
        {
            Entry<E> temp = root;

            int comp;

            while (true) 
            {
                comp =  element.compareTo (temp.element);
                if (comp == 0)
                    return false;
                if (comp < 0)
                    if (temp.left != null)
                        temp = temp.left;
                    else 
                    {
                        temp.left = new Entry<E> (element, temp);
                        size++;
                        modCount++;
                        return true;
                    } // temp.left == null
                    else if (temp.right != null)
                        temp = temp.right;
                    else 
                    {
                        temp.right = new Entry<E> (element, temp);
                        size++;
                        modCount++;
                        return true;
                    } // temp.right == null
            } // while
        } // root not null
    } // method add


    /**
     *  Ensures that this BinarySearchTree object does not contain a specified 
     *  element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     *  @param obj - the object whose absence is ensured in this 
     *                 BinarySearchTree object.
     *
     *  @return true - if this BinarySearchTree object changed as a result of this
     *                method call (that is, if obj was actually removed); otherwise,
     *                return false.
     *
     *  @throws ClassCastException - if obj cannot be compared to the 
     *                  elements already in this BinarySearchTree object. 
     *  @throws NullPointerException - if obj is null.
     *
     */
    public boolean remove (Object obj)
    {
        Entry<E> e = getEntry (obj);
        if (e == null)
            return false;
        deleteEntry (e);
        modCount++;
        return true;
    } // method remove /


    /**
     *  Finds the Entry object that houses a specified element, if there is such
     *  an Entry. The worstTime(n) is O(n), and averageTime(n) is O(log n).
     *
     *  @param obj - the element whose Entry is sought.
     *
     *  @return the Entry object that houses obj - if there is such an Entry;
     *                otherwise, return null.  
     *
     *  @throws ClassCastException - if obj is not comparable to the elements
     *                  already in this BinarySearchTree object.
     *  @throws NullPointerException - if obj is null.
     *
     */
    protected Entry<E> getEntry (Object obj) 
    {
        int comp;

        if (obj == null)
           throw new NullPointerException();
        Entry<E> e = root;
        while (e != null) 
        {
            comp = ((Comparable<? super E>)obj).compareTo (e.element);
            if (comp == 0)
                return e;
            else if (comp < 0)
                e = e.left;
            else
                e = e.right;
        } // while
        return null;
    } // method getEntry/
    
  

     /**
      *  Deletes the element in a specified Entry object from this BinarySearchTree.
      *  
      *  @param p – the Entry object whose element is to be deleted from this
      *                 BinarySearchTree object.
      *
      *  @return the Entry object that was actually deleted from this 
      *                BinarySearchTree object. 
      *
      */
    protected Entry<E> deleteEntry (Entry<E> p) 
    {
        size--;

        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if (p.left != null && p.right != null) 
        {
            Entry<E> s = successor (p);
            p.element = s.element;
            p = s;
        } // p had two children


        // At this point, p has either no children or one child.

        Entry<E> replacement;
         
        if (p.left != null)
            replacement = p.left;
        else
            replacement = p.right;

        // If p has at least one child, link replacement to p.parent.
        if (replacement != null) 
        {
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;
        } // p has at least one child  
        else if (p.parent == null)
            root = null;
        else 
        {
            if (p == p.parent.left)
                p.parent.left = null;
            else
                p.parent.right = null;        
        } // p has a parent but no children
        return p;
    } // method deleteEntry


    /**
     *  Finds the successor of a specified Entry object in this BinarySearchTree.
     *  The worstTime(n) is O(n) and averageTime(n) is constant.
     *
     *  @param e - the Entry object whose successor is to be found.
     *
     *  @return the successor of e, if e has a successor; otherwise, return null.
     *
     */
    protected Entry<E> successor (Entry<E> e) 
    {
        if (e == null)
            return null;
        else if (e.right != null) 
        {
            // successor is leftmost Entry in right subtree of e
            Entry<E> p = e.right;
            while (p.left != null)
                p = p.left;
            return p;

        } // e has a right child
        else 
        {

            // go up the tree to the left as far as possible, then go up
            // to the right.
            Entry<E> p = e.parent;
            Entry<E> ch = e;
            while (p != null && ch == p.right) 
            {
                ch = p;
                p = p.parent;
            } // while
            return p;
        } // e has no right child
    } // method successor
    
    
    
    // Derek Banas helped in the creation of the following three tree Traversals 
    // inOrder, preOrder and postOrder the url is:
    // https://www.youtube.com/watch?v=M6lYob8STMI
    public void inOrderTraversal(Entry<E> roo)
    {
    	if(roo != null){
    		inOrderTraversal(roo.left);
    		System.out.print(roo.element + " ");
    		inOrderTraversal(roo.right); 	
    	}
    }
    public void preOrderTraversal(Entry<E> roo)
    {
    	if(roo != null){
    		System.out.print(roo.element + " ");
    		preOrderTraversal(roo.left);
    		preOrderTraversal(roo.right); 	
    	}
    }
    public void postOrderTraversal(Entry<E> roo)
    {
    	if(roo != null){
    		postOrderTraversal(roo.left);
    		postOrderTraversal(roo.right);
    		System.out.print(roo.element + " "); 	
    	}
    }
    
    
    // Stackoverflow helped in the creation of this breadthfirst search
    // The question was asked by by an unknown user and answered by Dhaval
    // The the answer was edited by Ivan
    // Stackoverflow helped in the creation of this breadthfirst search
    // The question was asked by by an unknown user and answered by Dhaval
    // The the answer was edited by Ivan
    public void breadth(Entry<E> root) {
    	String ep = "";
    	Queue<Entry> queue = new LinkedList<Entry>() ;
        if (root == null)
            System.out.println("Tree is empty");
        queue.clear();
        queue.add(root);
        while(!queue.isEmpty()){
        	Entry node = queue.remove();
            System.out.print(node.element + " ");
            if(node.left != null) queue.add(node.left);
            if(node.right != null) queue.add(node.right);
         }
   
    }
    
    // Stackoverflow helped in the creation of this code 
    // added by Alan Easdale and is used as a wrapper to find the height of a binary search tree  
    public int findHeightofTree()
    {
        if(this.isEmpty())
        {
            return -1;
        }
        else
        {
            Entry<E> newNode = root;
            return findHeightofTree(newNode);
        }
    }
    // Stackoverflow helped in the creation of this code this method was added by Alan Easdale 
    // this method is used to find the height of a binary search tree
    private int findHeightofTree(Entry<E> nNode)
    {
    	 if(nNode == null || (nNode.left == null && nNode.right == null))
    	      return 0;
    	    return Math.max(findHeightofTree(nNode.left), findHeightofTree(nNode.right)) + 1;
    }
    // end of help by Stackoverflow 
    
    

    //Daily free code helped in the creation of the next four methods and their url is below
    // http://www.dailyfreecode.com/UserLogin.aspx?qsRefCode=DownloadArticle
	public void findSmallestNode(Entry<E> ele)
	{
		if (ele == null || ele.left == null)
			System.out.println(ele.element);
		else
			findSmallestNode(ele.left);
	}
	void findLargestNode(Entry<E> ele) {
		if (ele == null || ele.right == null)
			System.out.println(ele.element);
		else
			findLargestNode(ele.right);
	}

	
	public int internalNodes(Entry<E> ele) {
		if ((ele == null) || (ele.left == null  && ele.right == null))
			return 0;
		else
			return(internalNodes(ele.left) + internalNodes(ele.right) + 1);
	}

	int externalNodes(Entry<E> ele) {
		if (ele == null)
			return 0;
		else if(ele.left == null  && ele.right == null)
			return 1;
		else
			return(externalNodes(ele.left) + externalNodes(ele.right));
	}
	// End of daily free code help
	
    protected class TreeIterator implements Iterator<E>
    {

        protected Entry<E> lastReturned = null,
                           next;
        
        protected int modCountOnCreation;

        /**
         *  Positions this TreeIterator to the smallest element, according to the 
         *  Comparable interface, in the BinarySearchTree object.
         *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
         *
         */
        protected TreeIterator() 
        {             
            next = root;

            if (next != null)
                while (next.left != null)
                {
 //               	queue.add(next.element);
                    next = next.left;
                }
            modCountOnCreation = modCount;
        } // default constructor

        /**
         *  Determines if there are still some elements, in the BinarySearchTree 
         *  object this TreeIterator object is iterating over, that have not been
         *  accessed by this TreeIterator object.
         *
         *  @return true - if there are still some elements that have not been
         *                accessed by this TreeIterator object; otherwise, 
         *                return false.
         *
         */ 
        public boolean hasNext() 
        {
            return next != null;
        } // method hasNext


        /**
         *  Returns the element in the Entry this TreeIterator object was positioned at 
         *  before this call, and advances this TreeIterator object.
         *  The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         *  @return the element this TreeIterator object was positioned at before
         *  				this call.
         *
         *  @throws NoSuchElementException - if this TreeIterator object was not 
         *                 positioned at an Entry before this call.
         *  @throws IllegalStateException - if the tree has been modified by a call
         *  			   to the tree's add() or remove() method since this TreeIterator 
         *                 was created.
         *
         */
        public E next() 
        {
        	if (modCountOnCreation != modCount) {
        		throw new IllegalStateException();
        	}
            if (next == null)
                throw new NoSuchElementException();
            lastReturned = next;
            next = successor (next);             
            return lastReturned.element;
        } // method next

        /**
         *  Removes the element returned by the most recent call to this TreeIterator
         *  object’s next() method.
         *  The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         *  @throws IllegalStateException - if this TreeIterator’s next() method was not
         *                called before this call, or if this TreeIterator’s remove() method
         *                was called between the call to the next() method and this call,
         *                or if the tree has been modified by a call to the tree's add() or
         *                remove() method since this TreeIterator was created.
         *
         */ 
        public void remove() 
        {
            if (lastReturned == null || modCountOnCreation != modCount)
                throw new IllegalStateException();
 
            if (lastReturned.left != null && lastReturned.right != null)
                next = lastReturned;
            deleteEntry(lastReturned);
            lastReturned = null; 
        } // method remove     

    } // class TreeIterator

    protected static class Entry<E> 
    {
        protected E element;

        protected Entry<E> left = null,
                           right = null,
                           parent;
         

        /**
         *  Initializes this Entry object.
         *
         *  This default constructor is defined for the sake of subclasses of
         *  the BinarySearchTree class. 
         */
        public Entry() { }


        /**
         *  Initializes this Entry object from element and parent.
         *
         */ 
         public Entry (E element, Entry<E> parent) 
         {
             this.element = element;
             this.parent = parent;
         } // constructor


         
      
    } // class Entry


} // class BinarySearchTree

