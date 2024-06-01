import java.util.*;

// Red-Black Tree implementation
class RedBlackTree {
    // Two hash maps to store color information for each node
    public static Map<Integer, Color> hm1 = new HashMap<>();
    public static Map<Integer, Color> hm2 = new HashMap<>();

    // Enumeration for node colors
    static
    public enum Color {RED, BLACK}
    private final Book nullBook = new Book(-1);
    public int flipCount;

    // Inner class representing a book node in the Red-Black Tree
    public class Book {
        public int bookId;
        public Color color;
        public Book left, right, parent;
        public String bookName;
        public String authorName;
        public boolean availabilityStatus;
        public int borrowedBy;
        public ReservationHeap reservationHeap;

        // toString method for printing book information
        @Override
        public String toString() {
            return
                    "BookID = " + bookId +
                            "\nTitle = \"" + bookName +
                            "\"\nAuthor = \"" + authorName +
                            "\"\nAvailability = \"" + (availabilityStatus?"Yes":"No") +
                            "\"\nBorrowedBy = " + (borrowedBy==-1?"None":borrowedBy) +
                            "\nReservations = [" + reservationHeap+"]\n";
        }

        // Constructor for creating a book node
        public Book(int bookId, String bookName, String authorName, boolean availabilityStatus, int borrowedBy, ReservationHeap reservationHeap){
            this.bookId = bookId;
            this.left= nullBook;
            this.right= nullBook;
            this.parent= nullBook;
            this.color=Color.BLACK;
            this.bookName=bookName;
            this.authorName=authorName;
            this.availabilityStatus=availabilityStatus;
            this.borrowedBy=borrowedBy;
            this.reservationHeap=reservationHeap;
        }
        // Constructor for creating a null or empty book node
        public Book(int bookId) {
            this.bookId =bookId;
            this.color = Color.BLACK;
        }


    }
    // Inner class representing a reservation heap
    public static class ReservationHeap{

        public BinaryHeap<HeapNode> heap;

        // Constructor for creating a reservation heap
        public ReservationHeap(){
            heap=new BinaryHeap<HeapNode>((x, y) -> {
                if(x.priorityNumber==y.priorityNumber)
                    return x.timeOfReservation.compareTo(y.timeOfReservation);
                return x.priorityNumber-y.priorityNumber;
            });
        }

        // Method to add a reservation to the heap
        public void addToHeap(int patronId,int priorityNumber){
            HeapNode heapNode=new HeapNode(patronId,priorityNumber,new Date());
            this.heap.offer(heapNode);
        }

        // Method to get the patron with the highest priority from the heap
        public int getTop(){
            if(this.isEmpty())
                return -1;
            return this.heap.peek().patronId;
        }

        // Method to delete the patron with the highest priority from the heap
        public int deleteFromHeap(){
            if(!this.isEmpty())
                return this.heap.poll().patronId;
            return -1;
        }

        // Method to check if the heap is empty
        public boolean isEmpty(){
            return this.heap.size()==0;
        }

        // toString method for printing heap information
        @Override
        public String toString(){
            String res="";
            List<HeapNode> tempList=new ArrayList<>();
            while(this.heap.size()>1){
                HeapNode heapNode=this.heap.poll();
                tempList.add(heapNode);
                res+=heapNode.patronId+",";
            }
            if(!this.heap.isEmpty()) {
                HeapNode heapNode = this.heap.poll();
                tempList.add(heapNode);
                res+=heapNode.patronId;
            }
            for(HeapNode heapNode:tempList)
                this.heap.offer(heapNode);
            return res;
        }
        // Inner class representing a node in the reservation heap
        private class HeapNode{
            public int patronId;
            public int priorityNumber;
            public Date timeOfReservation;
            // Constructor for creating a heap node
            public HeapNode(int patronId, int priorityNumber, Date timeOfReservation) {
                this.patronId = patronId;
                this.priorityNumber = priorityNumber;
                this.timeOfReservation = timeOfReservation;
            }
        }

        // Inner class representing a binary heap
        public class BinaryHeap<E> {

            private static final int DEFAULT_INITIAL_CAPACITY = 11;
            transient Object[] queue;
            private int size = 0;
            private final Comparator<? super E> comparator;
            transient int modCount = 0;
            // Constructor for creating a binary heap with a default initial capacity
            public BinaryHeap(Comparator<? super E> comparator) {
                this(DEFAULT_INITIAL_CAPACITY, comparator);
            }
            // Constructor for creating a binary heap with a specified initial capacity and comparator
            public BinaryHeap(int initialCapacity,
                              Comparator<? super E> comparator) {
                if (initialCapacity < 1)
                    throw new IllegalArgumentException();
                this.queue = new Object[initialCapacity];
                this.comparator = comparator;
            }
            private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
            // Method to grow the heap capacity 
            private void grow(int minCapacity) {
                int oldCapacity = queue.length;
                int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                        (oldCapacity + 2) :
                        (oldCapacity >> 1));
                if (newCapacity - MAX_ARRAY_SIZE > 0)
                    newCapacity = hugeCapacity(minCapacity);
                queue = Arrays.copyOf(queue, newCapacity);
            }
            // Method to handle huge capacity for the heap
            private int hugeCapacity(int minCapacity) {
                if (minCapacity < 0) // overflow
                    throw new OutOfMemoryError();
                return (minCapacity > MAX_ARRAY_SIZE) ?
                        Integer.MAX_VALUE :
                        MAX_ARRAY_SIZE;
            }
            // Method to add an element to the heap
            public boolean offer(E e) {
                if (e == null)
                    throw new NullPointerException();
                modCount++;
                int i = size;
                if (i >= queue.length)
                    grow(i + 1);
                size = i + 1;
                if (i == 0)
                    queue[0] = e;
                else
                    siftUp(i, e);
                return true;
            }
            // Method to peek at the element with the highest priority in the heap
            //@SuppressWarnings("unchecked")
            public E peek() {
                return (size == 0) ? null : (E) queue[0];
            }
            // Method to remove and return the element with the highest priority from the heap
           // @SuppressWarnings("unchecked")
            public E poll() {
                if (size == 0)
                    return null;
                int s = --size;
                modCount++;
                E result = (E) queue[0];
                E x = (E) queue[s];
                queue[s] = null;
                if (s != 0)
                    siftDown(0, x);
                return result;
            }
            // Sift up the element at index k
            private void siftUp(int k, E x) {
                if (comparator != null)
                    siftUpUsingComparator(k, x);
                else
                    siftUpComparable(k, x);
            }

            // Sift up using Comparable interface
            //@SuppressWarnings("unchecked")
            private void siftUpComparable(int k, E x) {
                Comparable<? super E> key = (Comparable<? super E>) x;
                while (k > 0) {
                    int parent = (k - 1) >>> 1;
                    Object e = queue[parent];
                    if (key.compareTo((E) e) >= 0)
                        break;
                    queue[k] = e;
                    k = parent;
                }
                queue[k] = key;
            }

            // Sift up using custom comparator
            //@SuppressWarnings("unchecked")
            private void siftUpUsingComparator(int k, E x) {
                while (k > 0) {
                    int parent = (k - 1) >>> 1;
                    Object e = queue[parent];
                    if (comparator.compare(x, (E) e) >= 0)
                        break;
                    queue[k] = e;
                    k = parent;
                }
                queue[k] = x;
            }

            // Sift down the element at index k
            private void siftDown(int k, E x) {
                if (comparator != null)
                    siftDownUsingComparator(k, x);
                else
                    siftDownComparable(k, x);
            }
            // Sift down using Comparable interface
            //@SuppressWarnings("unchecked")
            private void siftDownComparable(int k, E x) {
                Comparable<? super E> key = (Comparable<? super E>)x;
                int half = size >>> 1;
                while (k < half) {
                    int child = (k << 1) + 1;
                    Object c = queue[child];
                    int right = child + 1;
                    if (right < size &&
                            ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                        c = queue[child = right];
                    if (key.compareTo((E) c) <= 0)
                        break;
                    queue[k] = c;
                    k = child;
                }
                queue[k] = key;
            }
            // Sift down using custom comparator
            //@SuppressWarnings("unchecked")
            private void siftDownUsingComparator(int k, E x) {
                int half = size >>> 1;
                while (k < half) {
                    int child = (k << 1) + 1;
                    Object c = queue[child];
                    int right = child + 1;
                    if (right < size &&
                            comparator.compare((E) c, (E) queue[right]) > 0)
                        c = queue[child = right];
                    if (comparator.compare(x, (E) c) <= 0)
                        break;
                    queue[k] = c;
                    k = child;
                }
                queue[k] = x;
            }
            // Heapify the heap
            //@SuppressWarnings("unchecked")
            private void heapify() {
                for (int i = (size >>> 1) - 1; i >= 0; i--)
                    siftDown(i, (E) queue[i]);
            }
            // Get the size of the heap
            public int size() {
                return size;
            }
            // Check if the heap is empty
            public boolean isEmpty(){return size==0;}
        }
    }
    // Constructor for initializing the Red-Black Tree
    private Book root;
    public RedBlackTree() {
        this.root = nullBook;
        this.flipCount=0;
    }

    // Method to insert a book into the Red-Black Tree
    public void insertBook(int bookId,String bookName,String authorName,String availabilityStatus,int borrowedBy,ReservationHeap reservationHeap){
        Book book = new Book(bookId,bookName,authorName,availabilityStatus.equals("Yes")?true:false,borrowedBy,reservationHeap);
        initializeHashMaps();
        if(root == nullBook){
            hm1.put(bookId, Color.BLACK);
        }
        else{
            hm1.put(bookId,Color.RED);
        }
        insert(book);
        populateHm2();
        calculateFlipCounts();
    }
    // Method to calculate the number of color flips after insertions
    private void calculateFlipCounts(){
        for(Map.Entry<Integer, Color> entry: hm1.entrySet()){
            if(entry.getValue() != hm2.get(entry.getKey())){
                this.flipCount++;
            }
        }
    }
    // Method to populate hm2 with the colors after an inorder traversal
    private void populateHm2(){
        inorderTraversal(root);
    }
    // Inorder traversal to populate hm2
    private void inorderTraversal(Book root){
        if(root == nullBook){
            return;
        }
        inorderTraversal(root.left);
        hm2.put(root.bookId, root.color);
        inorderTraversal(root.right);
    }
    // Method to initialize hash maps before insertions
    private void initializeHashMaps(){
        hm1.clear();
        hm1= new HashMap<>(hm2);
        hm2.clear();
    }

    // Method to insert a book into the Red-Black Tree
    private void insert(Book book) {

        Book temp = root;
        if (root == nullBook) {
            root = book;
            book.color = Color.BLACK;
            book.parent = nullBook;
        } else {
            book.color = Color.RED;
            while (true) {
                if (book.bookId < temp.bookId) {
                    if (temp.left == nullBook) {
                        temp.left = book;
                        book.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (book.bookId == temp.bookId) {
                    return; // Book with the same ID already exists
                } else {
                    if (temp.right == nullBook) {
                        temp.right = book;
                        book.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixupInsert(book);
        }
    }
    // Method to fix the Red-Black Tree properties after an insert
    private void fixupInsert(Book book) {
        while (book.parent.color == Color.RED) {
            Book uncle = nullBook;
            if (book.parent == book.parent.parent.left) {
                uncle = book.parent.parent.right;

                if (uncle != nullBook && uncle.color == Color.RED) {
                    // Case 1: Recoloring
                    book.parent.color = Color.BLACK;
                    if(book.parent.parent.color != Color.RED && book.parent.parent != root) {
                        book.parent.parent.color = Color.RED;
                    }
                    uncle.color = Color.BLACK;
                    book = book.parent.parent;
                    continue;
                }
                if (book == book.parent.right) {
                    // Case 2: Left Rotation
                    book = book.parent;
                    leftRotate(book);
                }
                // Case 3: Recoloring and Right Rotation
                book.parent.color = Color.BLACK;
                book.parent.parent.color = Color.RED;
                rightRotate(book.parent.parent);
            } else {
                uncle = book.parent.parent.left;
                if (uncle != nullBook && uncle.color == Color.RED) {
                    // Case 1: Recoloring
                    book.parent.color = Color.BLACK;
                    book.parent.parent.color = Color.RED;
                    uncle.color = Color.BLACK;
                    book = book.parent.parent;
                    continue;
                }
                if (book == book.parent.left) {
                    // Case 2: Right Rotation
                    book = book.parent;
                    rightRotate(book);
                }
                // Case 3: Recoloring and Left Rotation
                book.parent.color = Color.BLACK;
                book.parent.parent.color = Color.RED;
                leftRotate(book.parent.parent);
            }
        }
        root.color = Color.BLACK; // Ensure the root is always black
    }
    // Left rotation operation in the Red-Black Tree
    private void leftRotate(Book book) {
        if (book.parent != nullBook) {
            // Adjust the parent's child pointer based on the rotation direction
            if (book == book.parent.left) {
                book.parent.left = book.right;
            } else {
                book.parent.right = book.right;
            }
            // Update the parent pointers
            book.right.parent = book.parent;
            book.parent = book.right;
            // Update the left child of the rotated node
            if (book.right.left != nullBook) {
                book.right.left.parent = book;
            }
            book.right = book.right.left;
            book.parent.left = book;
        } else {
            // Perform a root rotation if the current node is the root
            Book right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nullBook;
            root = right;
        }
    }
    // Right rotation operation in the Red-Black Tree
    private void rightRotate(Book book) {
        if (book.parent != nullBook) {
            // Adjust the parent's child pointer based on the rotation direction
            if (book == book.parent.left) {
                book.parent.left = book.left;
            } else {
                book.parent.right = book.left;
            }
            // Update the parent pointers
            book.left.parent = book.parent;
            book.parent = book.left;
            // Update the right child of the rotated node
            if (book.left.right != nullBook) {
                book.left.right.parent = book;
            }
            book.left = book.left.right;
            book.parent.right = book;
        } else {
            // Perform a root rotation if the current node is the root
            Book left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nullBook;
            root = left;
        }
    }
    // Search for a book with a given bookId and return the corresponding node
    public Book printBook(int bookId) {
        Book temp = root;
        // Check if the tree is empty
        if(root.bookId == -1)
            return null;
        // Traverse the tree to find the node with the given bookId
        while (true) {
            if (bookId < temp.bookId) {
                if (temp.left == nullBook) {
                    // Book not found
                    return null;
                } else {
                    temp = temp.left;
                }
            }else if (bookId == temp.bookId) {
                // Book found   
                return temp;
            } else {
                if (temp.right == nullBook) {
                    // Book not found
                    return null;
                } else {
                    temp = temp.right;
                }
            }
        }
    }

    // Delete a book with a given bookId from the Red-Black Tree
    public void deleteBook(int bookId) {
        // Find the book to be deleted
        Book delBook = printBook(bookId);
        if(delBook == null) {
            // Book not found
            System.out.println("Book "+bookId+" is no longer available.");
            return;
        }
        // Initialize hash maps and remove bookId from hm1
        initializeHashMaps();
        hm1.remove(bookId);
        // Delete the book from the tree
        delete(delBook);
        // Update hm2 and calculate flip counts
        populateHm2();
        calculateFlipCounts();
        if(delBook.reservationHeap.isEmpty()){
            // Book has no reservations
            System.out.println("Book "+bookId+" is no longer available.");
        } else{
            // Book has reservations; additional logic may be added here
        System.out.println("Book "+bookId+" is no longer available. Reservations made by Patrons "+ delBook.reservationHeap+" have been cancelled!");
        }
    }
    // Method to delete a book from the Red-Black Tree
    private boolean delete(Book z){
        Book y = z;
        Color y_original_color = y.color;
        Book x;
        if(z.left == nullBook){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nullBook){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = treeMaximum(z.left);  // Changed to treeMaximum
            y_original_color = y.color;
            x = y.left;  // Changed to left child
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.left);  // Changed to y.left
                y.left = z.left;  // Changed to left child
                y.left.parent = y;
            }
            transplant(z, y);
            y.right = z.right;  // Adjust right child
            y.right.parent = y;
            y.color = z.color;
        }
        if(y_original_color==Color.BLACK){
            fixupDelete(x);
        }
        return true;
    }

    // Method to replace the subtree rooted at node u with the subtree rooted at node v
    private void transplant(Book u, Book v){
        if(u.parent == nullBook){
            root = v;
        }else if(u == u.parent.left){
            u.parent.left = v;
        }else
            u.parent.right = v;
        v.parent = u.parent;
    }
    // Method to find the node with the minimum key in the subtree rooted at node z
    private Book treeMinimum(Book z){
        while(z.left!= nullBook){
            z = z.left;
        }
        return z;
    }
    // Method to find the node with the maximum key in the subtree rooted at node z
    private Book treeMaximum(Book z){
        while(z.right != nullBook){
            z= z.right;
        }
        return z;
    }
    // Method to fix violations after delete operation in the Red-Black Tree
    private void fixupDelete(Book x){
        while(x!=root && x.color == Color.BLACK){
            if(x == x.parent.left){
                Book w = x.parent.right;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == Color.BLACK && w.right.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == Color.BLACK){
                    w.left.color = Color.BLACK;
                    w.color = Color.RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }else{
                Book w = x.parent.left;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == Color.BLACK && w.left.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == Color.BLACK){
                    w.right.color = Color.BLACK;
                    w.color = Color.RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                if(w.left.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }

    // Method to print books in the specified bookId range
    public void printBooks(int bookId1,int bookId2){
        List<Book> listOfBooks=new ArrayList<>();
        inorder(this.root,bookId1,bookId2,listOfBooks,true);
        for(Book book: listOfBooks)
            System.out.println(book);
    }

    // Method to find the closest book to a target bookId
    public void findClosestBook(int targetId){
        int minDiff=Integer.MAX_VALUE;
        List<Book> listOfBooks=new ArrayList<>();
        inorder(this.root,-1,-1,listOfBooks,false);
        List<Book> res=new ArrayList<>();
        for(Book book:listOfBooks){
            int diff=Math.abs(targetId-book.bookId);
            if(minDiff>diff){
                minDiff=diff;
                res=new ArrayList<>();
                res.add(book);
            }else if(minDiff==diff)
                res.add(book);
        }
        Collections.sort(res,(x, y) -> {
            return x.bookId -y.bookId;
        });
        for(Book book:res)
            System.out.println(book);
    }

    // Inorder traversal to collect books in the specified range or all books
    private void inorder(Book book, int lower, int upper, List<Book> listOfBooks, boolean flag){
        if(book== nullBook)
            return;
        inorder(book.left,lower,upper,listOfBooks,flag);
        if(flag) {
            if (book.bookId >= lower && book.bookId <= upper)
                listOfBooks.add(book);
        }
        else {
            listOfBooks.add(book);
        }
        inorder(book.right,lower,upper,listOfBooks,flag);
    }

    // Method to display color flip count
    public void colorFlipCount(){
        System.out.println("Color Flip Count : "+this.flipCount);
    }

    // Method to borrow a book by a patron
    public void borrowBook(int patronId,int bookId,int patronPriority){
        Book book=printBook(bookId);
        if(book==null)
            return;
        if(book.availabilityStatus){
            book.borrowedBy=patronId;
            book.availabilityStatus=false;
            System.out.println("Book "+bookId+" Borrowed by Patron "+patronId);
        }else {
            System.out.println("Book " + bookId + " Reserved by Patron " + patronId);
            book.reservationHeap.addToHeap(patronId,patronPriority);
        }
    }

    // Method to return a book by a patron
    public void returnBook(int patronId,int bookId){
        Book book=printBook(bookId);
        if(book==null)
            return;
        if(book.borrowedBy!=patronId)
            return;
        if(book.availabilityStatus)
            return;
        book.borrowedBy=-1;
        book.availabilityStatus=true;
        System.out.println("Book "+bookId+" Returned by Patron "+patronId);
        if(!book.reservationHeap.isEmpty()){
            int reservedPatronId=book.reservationHeap.deleteFromHeap();
            if(reservedPatronId==-1)
                return;
            book.borrowedBy=reservedPatronId;
            book.availabilityStatus=false;
            System.out.println("\nBook "+bookId+" Allotted to Patron "+reservedPatronId);
        }
    }

    // Method to terminate the program
    public void quit(){
        System.out.println("Program Terminated!!");
        this.root=null;
    }

}






