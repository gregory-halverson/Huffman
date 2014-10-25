import java.util.*;

// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

// Counts occurrence of unique symbols in a set and divides into frequencies
@SuppressWarnings("unchecked")
public class FrequencyAnalyzer<T extends Comparable<? super T>>
{
    // Table storing count of occurences for each unique symbol
    Map<T, Integer> occurrenceMap = null;

    // Table storing frequencies for each unique symbol in the range (0, 1)
    FrequencyDistribution frequencyDistribution = null;

    // Total number of symbols in the input set
    int count = 0;

    // Stores frequency of symbols in the range (0, 1)
    public static class FrequencyDistribution<T extends Comparable<? super T>> extends TreeMap<T, Double>
    {
        public FrequencyDistribution()
        {
            super();
        }

        public FrequencyDistribution(Comparator comparator)
        {
            super(comparator);
        }

        // Shannon entropy: the minimum number of bits required to represent each symbol
        public double entropy()
        {
            double entropy = 0;

            for (Map.Entry<T, Double> entry : entrySet())
                entropy -= entry.getValue() * Math.log(entry.getValue()) / Math.log(2);

            return entropy;
        }

        // Calculates how different this set of symbol frequencies is from another
        // Useful for guessing the language contained in a string or the type of data in a file
        public double variation(FrequencyDistribution other)
        {
            T symbol;

            double variation = 0.0;

            for (Map.Entry<T, Double> entry : (Set<Map.Entry<T, Double>>)other.entrySet())
                if (containsKey(symbol = entry.getKey()))
                    variation += Math.abs(get(symbol) - (Double)other.get(symbol));

            return variation;
        }

        public static class AscendingByValue implements Comparator
        {
            FrequencyDistribution unsorted;

            public AscendingByValue(FrequencyDistribution unsorted)
            {
                this.unsorted = unsorted;
            }

            @Override
            public int compare(Object o1, Object o2) {
                return (((Double)unsorted.get(o1)).compareTo((Double)unsorted.get(o2)) > 0 ? 1 : -1);
            }
        }

        public FrequencyDistribution<T> ascendingByValue()
        {
            FrequencyDistribution<T> sortedDistribution = new FrequencyDistribution<T>(new AscendingByValue(this));
            sortedDistribution.putAll(this);
            return sortedDistribution;
        }

        public static class DescendingByValue implements Comparator
        {
            FrequencyDistribution unsorted;

            public DescendingByValue(FrequencyDistribution unsorted)
            {
                this.unsorted = unsorted;
            }

            @Override
            public int compare(Object o1, Object o2)
            {
                return (((Double)unsorted.get(o2)).compareTo((Double)unsorted.get(o1)) > 0 ? 1 : -1);
            }
        }

        public FrequencyDistribution<T> descendingByValue()
        {
            FrequencyDistribution<T> sortedDistribution = new FrequencyDistribution<T>(new DescendingByValue(this));
            sortedDistribution.putAll(this);
            return sortedDistribution;
        }

        public String printHorizontal()
        {
            StringBuilder output = new StringBuilder();

            output.append("[");

            int e = 0;

            for (Map.Entry<T, Double> entry : entrySet())
            {
                output.append("\'" + entry.getKey() + "\'=" + StringFormat.percent(entry.getValue()));

                if (e++ < size() - 1)
                    output.append(", ");
            }

            output.append("]");

            return output.toString();
        }

        public String toString()
        {
            return printHorizontal();
        }
    }

    FrequencyAnalyzer()
    {
        occurrenceMap = null;
        frequencyDistribution = null;
        count = 0;
    }

    FrequencyAnalyzer(T[] input)
    {
        analyze(input);
    }

    public FrequencyDistribution<T> analyze(T[] input)
    {
        occurrenceMap = new TreeMap<T, Integer>();
        frequencyDistribution = new FrequencyDistribution();

        count = input.length;

        for (int i = 0; i < input.length; i++)
        {
            T symbol = input[i];

            if (occurrenceMap.containsKey(symbol))
                occurrenceMap.put(symbol, occurrenceMap.get(symbol) + 1);
            else
                occurrenceMap.put(symbol, 1);
        }

        for (Map.Entry<T, Integer> entry : occurrenceMap.entrySet())
        {
            frequencyDistribution.put(entry.getKey(), (double) entry.getValue() / count);
        }

        return frequencyDistribution;
    }

    public int getInputSymbolCount()
    {
        return count;
    }

    public FrequencyDistribution<T> getFrequencyDistribution()
    {
        return frequencyDistribution;
    }

    public double entropy()
    {
        return frequencyDistribution.entropy();
    }

    public int entropySize()
    {
        return (int)(entropy() * getInputSymbolCount() / 8.0);
    }
}
