// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

import java.lang.reflect.Array;

// Binary tree implemented as array
// Preconditions: element type E must implement compareTo and toString
public class CompleteBinaryTree<E extends Comparable<? super E>>
{
    // Constants
    protected static final int TERMINAL = -1; // Indicates end of branch
    protected static final int SPACING = 3; // Spacing between items on printout

    // Member data
    protected E [] array; // Memory allocated for data structure
    protected int size; // Number of items in use in data structure

    // Constructors

    // Default constructor: empty tree
    CompleteBinaryTree()
    {
        size = 0;
        array = null;
    }

    // Allocate enough memory in a new array to contain an extra level on the tree
    @SuppressWarnings("unchecked")
    void allocateLevel()
    {
        // Store data
        E [] temp = array;

        // Allocate memory
        array = (E[]) Array.newInstance(temp[0].getClass(), (int) Math.pow(2, getHeight() + 1) - 1);

        // Copy data to new array
        for (int i = 0; i < size; i++)
            array[i] = temp[i];
    }

    // Copy from array
    @SuppressWarnings("unchecked")
    CompleteBinaryTree(E[] init)
    {
        // Copy size from source array
        size = init.length;

        // Allocate memory for array
        array = (E[]) Array.newInstance(init[0].getClass(), (int)Math.pow(2, getHeight()) - 1);

        // Initialize values of array
        for (int i = 0; i < size; i++)
            array[i] = init[i];
    }

    // Size of array
    int getAllocatedSize()
    {
        return array.length;
    }

    // Size of tree
    int getSize()
    {
        return size;
    }

    // Check if the tree is full
    boolean full()
    {
        if (array != null)
            return size == array.length;
        else
            return true;
    }

    // Calculate height of tree
    int getHeight()
    {
        return (int)Math.ceil(Math.log(size + 1) / Math.log(2));
    }

    // Find parent if it exists
    int parentIndex(int index)
    {
        if (index == 1)
            return TERMINAL;
        else
            return index / 2;
    }

    // Find sibling if it exists
    int siblingIndex(int index)
    {
        if (hasSibling(index))
        {
            if (index % 2 == 0)
            {
                // This is a left child
                return rightIndex(parentIndex(index));
            }
            else
            {
                // This is a right child
                return leftIndex(parentIndex(index));
            }
        }
        else
        {
            return TERMINAL;
        }
    }

    // Find left child if it exists
    int leftIndex(int index)
    {
        int left_index = 2 * index;

        if (left_index > size)
            return TERMINAL;
        else
            return left_index;
    }

    // Find right child if it exists
    int rightIndex(int index)
    {
        int right_index = 2 * index + 1;

        if (right_index > size)
            return -1;
        else
            return right_index;
    }

    // Return value at 1-based index
    E value(int index)
    {
        return array[index - 1];
    }

    // Return value of parent if it exists
    E parentValue(int index)
    {
        if (hasParent(index))
            return array[parentIndex(index) - 1];
        else
            return null;
    }

    // Return value of sibling if it exists
    E siblingValue(int index)
    {
        if (hasSibling(index))
        {
            if (index % 2 == 0)
            {
                // This is a left child
                return rightValue(parentIndex(index));
            }
            else
            {
                // This is a right child
                return leftValue(parentIndex(index));
            }
        }
        else
        {
            return null;
        }
    }

    // Return value of left child if it exists
    E leftValue(int index)
    {
        int left_index = leftIndex(index);

        if (left_index == TERMINAL)
            return null;
        else
            return array[leftIndex(index) - 1];
    }

    // Return value of right child if it exists
    E rightValue(int index)
    {
        int right_index = rightIndex(index);

        if (right_index == TERMINAL)
            return null;
        else
            return array[rightIndex(index) - 1];
    }

    // Check if node has a parent
    boolean hasParent(int index)
    {
        return parentIndex(index) != TERMINAL;
    }

    // Check if node has a sibling
    boolean hasSibling(int index)
    {
        if (index % 2 == 0)
        {
            // This is a left child
            return hasRight(parentIndex(index));
        }
        else
        {
            // This is a right child
            return hasLeft(parentIndex(index));
        }
    }

    // Check if node has children
    boolean hasChildren(int index)
    {
        return hasLeft(index) || hasRight(index);
    }

    // Check if node has a left child
    boolean hasLeft(int index)
    {
        return leftIndex(index) != TERMINAL;
    }

    // Check if node has a right child
    boolean hasRight(int index)
    {
        return rightIndex(index) != TERMINAL;
    }

    // Find smallest child, if it exists
    int smallerChild(int index)
    {
        if (!hasChildren(index))
            return TERMINAL;
        else if (hasLeft(index) && !hasRight(index))
            return leftIndex(index);
        else if (!hasLeft(index) && hasRight(index))
            return rightIndex(index);
        else if (leftValue(index).compareTo(rightValue(index)) <= 0)
            return leftIndex(index);
        else
            return rightIndex(index);
    }

    // Find largest child, if it exists
    int largerChild(int index)
    {
        if (!hasChildren(index))
            return TERMINAL;
        else if (hasLeft(index) && !hasRight(index))
            return leftIndex(index);
        else if (!hasLeft(index) && hasRight(index))
            return rightIndex(index);
        else if (leftValue(index).compareTo(rightValue(index)) >= 0)
            return leftIndex(index);
        else
            return rightIndex(index);
    }

    // Insert a value at the end of the tree
    @SuppressWarnings("unchecked")
    public void insert(E value)
    {
        // Check if array is full and allocate memory if necessary
        if (full())
        {
            // Pass type information to allocator
            if (array == null)
            {
                array = (E[]) Array.newInstance(value.getClass(), 1);
                array[0] = value;
            }

            allocateLevel();
        }

        // Insert value to end of tree
        array[size++] = value;
    }

    // Swap two values in the tree
    public void swap(int index1, int index2)
    {
        E temp = array[index1 - 1];
        array[index1 - 1] = array[index2 - 1];
        array[index2 - 1] = temp;
    }

    // Set value of parent
    void setParent(int index, E value)
    {
        if (hasParent(index))
            array[parentIndex(index) - 1] = value;
    }

    // Set value of node
    void setValue(int index, E value)
    {
        if (index <= size)
            array[index - 1] = value;
    }

    // Set value of sibling
    void setSibling(int index, E value)
    {
        if (hasSibling(index))
            array[siblingIndex(index) - 1] = value;
    }

    // Set value of left child
    void setLeft(int index, E value)
    {
        if (hasLeft(index))
            array[leftIndex(index) - 1] = value;
    }

    // Set value of right child
    void setRight(int index, E value)
    {
        if (hasRight(index))
            array[rightIndex(index) - 1] = value;
    }

    // Clear tree (set size to zero)
    void clear()
    {
        size = 0;
    }

    // Merge with another tree
    @SuppressWarnings("unchecked")
    void merge(CompleteBinaryTree<E> other)
    {
        if (other.getSize() > 0)
        {
            // Allocate memory for array
            E [] new_array = (E[]) Array.newInstance(other.array[0].getClass(), (int) Math.pow(2, (int) Math.ceil(Math.log(size + other.getSize() + 1) / Math.log(2))) - 1);

            // Copy values from old array
            for (int i = 0; i < size; i++)
                new_array[i] = array[i];

            // Copy values from other array
            for (int i = 0; i < other.getSize(); i++)
                new_array[size + i] = other.array[i];

            // Assign combined array to this tree
            array = new_array;

            // Accumulate size
            size += other.getSize();
        }
    }

    // Print data in it's raw order in memory
    String printArray()
    {
        String output_string = new String();

        output_string += "(";

        // Iterate array
        for (int i = 0; i < size; i++)
        {
            output_string += array[i];

            if (i != size - 1)
                output_string += ", ";
        }

        output_string += ")";

        return output_string;
    }

    // Get width of output strings for each element
    public SumTree getOutputWidthTree()
    {
       SumTree tree = new SumTree();

        // Iterate array
        for (int i = 0; i < size; i++)
            if (array[i] == null || array[i].toString().length() == 0)
                tree.insert(SPACING);
            else
                tree.insert(array[i].toString().length() + SPACING);

        if (size % 2 == 0)
            tree.insert(SPACING);

        return tree;
    }

    public CompleteBinaryTree<Integer> getBranches(SumTree output_width)
    {
        // Get placement of branches
        CompleteBinaryTree<Integer> branches = new CompleteBinaryTree<Integer>();

        for (int i = 1; i <= output_width.getSize(); i++)
            branches.insert(StringFormat.center(output_width.value(i)));

        return branches;
    }

    // Print tree to string
    public String printTree()
    {
        // Get widths of strings of values of the tree
        SumTree output_width = getOutputWidthTree();

        // Get placement of branches
        CompleteBinaryTree<Integer> branches = getBranches(output_width);

        //System.out.print(branches);

        int index = 1;
        int revert = 0;

        String output_string = new String();

        // Iterate levels
        for (int level = 0; level < getHeight(); level++)
        {
            revert = index;

            // Print elements
            for (int element = 0; element < Math.pow(2, level) && index <= getSize(); element++)
            {
                String element_string = "";

                if (value(index) != null)
                    element_string = value(index).toString();

                if (hasLeft(index) && leftValue(index) != null && leftValue(index).toString().length() != 0 && hasRight(index) && rightValue(index) != null && rightValue(index).toString().length() != 0 && !(element_string.length() > (output_width.leftValue(index) + output_width.rightValue(index)) / 2))
                {
                    // Pad with underscores
                    output_string +=    StringFormat.padText(
                                            StringFormat.padText(element_string,
                                                    output_width.leftValue(index) + branches.rightValue(index) - branches.leftValue(index) - 1,
                                                    branches.value(index) - branches.leftValue(index) - StringFormat.center(element_string.length()), '_'),
                                        (int)output_width.value(index),
                                        branches.leftValue(index) + 1, ' ');
                }
                else if (hasLeft(index) && leftValue(index) != null && (!hasRight(index) || rightValue(index) == null))
                {
                    // Pad with underscores on the left
                    output_string += StringFormat.padText(
                            StringFormat.padText(element_string,
                                    output_width.leftValue(index) - branches.leftValue(index) - 1,
                                    branches.value(index) - branches.leftValue(index) - StringFormat.center(element_string.length()), '_'),
                            (int)output_width.value(index),
                            branches.leftValue(index) + 1, ' ');
                }
                else if (leftValue(index) == null && hasRight(index) && rightValue(index) != null)
                {
                    // Pad with underscores on the right
                    output_string += StringFormat.padText(
                            StringFormat.padText(element_string,
                                    output_width.rightValue(index) - branches.rightValue(index),
                                    StringFormat.Alignment.left, '_'),
                            (int)output_width.value(index),
                            branches.value(index), ' ');
                }
                else
                {
                    // Center
                    output_string += StringFormat.padText(element_string, (int)output_width.value(index), branches.value(index) + 1 - StringFormat.center(element_string.length()), ' ');
                }

                index++;
            }

            index = revert;
            output_string += System.getProperty("line.separator");

            // Print connecting lines
            for (int element = 0; element < Math.pow(2, level) && index <= getSize(); element++)
            {
                if (hasLeft(index))
                    output_string += StringFormat.padText((leftValue(index) != null && leftValue(index).toString().length() != 0) ? "/" : "", (int)output_width.leftValue(index), branches.leftValue(index), ' ');
                if (hasRight(index))
                    output_string += StringFormat.padText((rightValue(index) != null && rightValue(index).toString().length() != 0) ? "\\" : "", (int)output_width.rightValue(index), branches.rightValue(index), ' ');

                index++;
            }

            output_string += System.getProperty("line.separator");
        }

        String [] lines = output_string.split(System.getProperty("line.separator"));
        Character [] column = new Character[lines.length];

        String [] output_lines = new String[lines.length];

        for (int r = 0; r < lines.length; r++)
            output_lines[r] = "";

        for (int c = 0; c < lines[0].length(); c++)
        {
            boolean empty_column = true;

            for (int r = 0; r < lines.length; r++)
            {
                if (c < lines[r].length())
                    column[r] = lines[r].charAt(c);
                else
                    column[r] = ' ';

                if (!(column[r] == ' ' || column[r] == '_'))
                    empty_column = false;

                if (c > 0)
                {
                    Character left = lines[r].charAt(c - 1);

                    if (!(left == ' ' || left == '_'))
                        empty_column = false;
                }

                if (c < lines[0].length() - 1)
                {
                    Character right = lines[r].charAt(c + 1);

                    if (!(right == ' ' || right == '_'))
                        empty_column = false;
                }
            }

            if (!empty_column)
                for (int r = 0; r < lines.length; r++)
                    output_lines[r] += column[r];
        }

        output_string = "";

        for (int r = 0; r < output_lines.length; r++)
            output_string += output_lines[r] + System.getProperty("line.separator");

        return output_string;
    }

    @Override public String toString()
    {
        return printTree();
    }
}