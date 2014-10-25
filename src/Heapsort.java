/**
 * Created by Gregory on 2/19/14.
 */
public class Heapsort
{
    // Use Heap.Min to sort an array in ascending order
    public static <E extends Comparable<? super E>>  E [] sortAscending(E [] unsorted_array)
    {
        return (E[]) new Heap.Min<E>(unsorted_array).sort();
    }

    // Use Heap.Max to sort an array in descending order
    public static <E extends Comparable<? super E>> E [] sortDescending(E [] unsorted_array)
    {
        return (E[]) new Heap.Max<E>(unsorted_array).sort();
    }
}
