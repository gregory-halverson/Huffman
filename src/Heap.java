// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

import java.lang.reflect.*;

// Abstract class for Heap, implemented by Heap.Max and Heap.Min
public abstract class Heap<E extends Comparable<? super E>> extends CompleteBinaryTree<E>
{

    // Heap with maximum value at top
    public static class Max<E extends Comparable<? super E>> extends Heap<E>
    {
        // Default constructor
        Max()
        {
            super();
        }

        // Copy from an array
        Max(E[] init)
        {
            super(init);
        }

        // Traverse the tree down and swap with the largest child on each iteration
        @Override void siftDown(int index)
        {
            int larger_child;

            while (hasChildren(index) && value(index).compareTo(value(larger_child = largerChild(index))) <= 0)
            {
                swap(index, larger_child);
                index = larger_child;
            }
        }

        // Traverse the tree up and swap with parent until larger value is found
        @Override void siftUp(int index)
        {
            while (hasParent(index) && parentValue(index).compareTo(value(index)) <= 0)
            {
                swap(index, parentIndex(index));
                index = parentIndex(index);
            }
        }
    }

    // Heap with minimum value at top
    public static class Min<E extends Comparable<? super E>> extends Heap<E>
    {
        // Default constructor
        Min()
        {
            super();
        }

        // Copy from an array
        Min(E[] init)
        {
            super(init);
        }

        // Traverse the tree down and swap with the smallest child on each iteration
        @Override void siftDown(int index)
        {
            int smaller_child;

            while (hasChildren(index) && value(index).compareTo(value(smaller_child = smallerChild(index))) >= 0)
            {
                swap(index, smaller_child);
                index = smaller_child;
            }
        }

        // Traverse the tree up and swap with parent until smaller value is found
        @Override void siftUp(int index)
        {
            while (hasParent(index) && parentValue(index).compareTo(value(index)) >= 0)
            {
                swap(index, parentIndex(index));
                index = parentIndex(index);
            }
        }
    }

    // Default constructor
    Heap()
    {
        super();
    }

    // Copy from an array
    Heap(E [] init)
    {
        super(init);
        heapify();
    }

    // Implemented in Heap.Max and Heap.Min
    void siftDown(int index) {}

    // Implemented in Heap.Max and Heap.Min
    void siftUp(int index) {}

    // Sift down all nodes to make the tree a heap
    void heapify()
    {
        for (int i = size / 2; i > 0; i--)
        {
            siftDown(i);
        }
    }

    // Insert a value and sift up to restore the heap
    public void insert(E value)
    {
        super.insert(value);

        // Resort tree
        siftUp(size);
    }

    // Remove top value of tree and resort
    E pop()
    {
        E result = (E)value(1);

        // Hide value at the end of the tree
        swap(1, size);
        size--;
        siftDown(1);

        return result;
    }

    // Produce sorted array and restore the tree
    @SuppressWarnings("unchecked")
    E [] sort()
    {
        int original_size = size;
        E [] result = (E[]) Array.newInstance(array[0].getClass(), size);

        // Pop each value into an array
        for (int i = 0; i < original_size; i++)
            result[i] = pop();

        // Restore heap
        size = original_size;
        heapify();

        return result;
    }

    // Merge with another tree
    @SuppressWarnings("unchecked")
    void merge(CompleteBinaryTree<E> other)
    {
        super.merge(other);
        heapify();
    }
}
