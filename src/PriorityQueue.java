/**
 * Created by Gregory on 2/20/14.
 */

// Priority Queue as a wrapper of Heap.Min
public class PriorityQueue<E>
{
    // Member data
    private Heap.Min<Item<E>> heap;

    // Prioritized item structure
    private class Item<E> implements Comparable<Item>
    {
        // Member data
        Double priority;
        E data;

        // Constructor
        Item(E data, Double priority)
        {
            this.data = data;
            this.priority = priority;
        }

        // Comparison
        @Override public int compareTo(Item o) throws NullPointerException
        {
            if (o == null)
                throw new NullPointerException();
            else
                return priority.compareTo(o.priority);
        }

        // Print contents
        @Override public String toString()
        {
            return data.toString();
        }
    }

    // Default constructor
    PriorityQueue()
    {
        heap = new Heap.Min<Item<E>>();
    }

    // Add an item to the queue with specified priority
    public E enqueue(E data, Double priority)
    {
        heap.insert(new Item<E>(data, priority));
        return data;
    }

    // Remove and return highest priority item from the queue
    public E dequeue()
    {
        return heap.pop().data;
    }

    // Return data of highest priority item without removing it
    public E peek()
    {
        return heap.value(1).data;
    }

    // Check if the queue is empty
    public boolean empty()
    {
        return heap.getSize() == 0;
    }

    // Get number of elements in queue
    public int size()
    {
        return heap.getSize();
    }

    // Enable random access
    public E get(int key)
    {
        return heap.sort()[key].data;
    }

    public Heap.Min<Item<E>> getHeap()
    {
        return heap;
    }
}
