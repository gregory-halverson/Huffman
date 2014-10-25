/**
 * Created by Gregory on 2/19/14.
 */
public class SumTree extends CompleteBinaryTree<Integer>
{
    SumTree()
    {
        super();
    }

    boolean isSum(int index)
    {
        if (hasChildren(index))
        {
            Integer sum = 0;

            if (hasLeft(index))
                sum += leftValue(index);

            if (hasRight(index))
                sum += rightValue(index);

            return value(index) == sum;
        }
        else
        {
            return true;
        }
    }

    public void insert(Integer value)
    {
        super.insert(value);
        siftUp(size);
    }

    public void changeValue(int index, Integer value)
    {
        setValue(index, value);
        siftUp(index);
    }

    protected void siftUp(int index)
    {
        while (hasParent(index) && !isSum(parentIndex(index)))
        {
            setParent(index, value(index) + (hasSibling(index) ? siblingValue(index) : 0));

            index = parentIndex(index);
        }
    }
}
