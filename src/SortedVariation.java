import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Gregory on 3/3/14.
 */
public class SortedVariation
{
    private static class Variation implements Comparable<Variation>
    {
        private String name;
        private Double value;

        Variation(String name, Double value)
        {
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public Double getValue()
        {
            return value;
        }

        public int compareTo(Variation other)
        {
            return value.compareTo(other.getValue());
        }
    }

    public static void main(String[] args)
    {
        ArrayList<Variation> variationList = new ArrayList<Variation>();

        variationList.add(new Variation("English", 0.1));
        variationList.add(new Variation("French", 0.05));
        variationList.add(new Variation("Italian", 0.2));

        Collections.sort(variationList);

        Variation[] variations = variationList.toArray(new Variation[variationList.size()]);

        for (int i = 0; i < variations.length; i++)
        {
            System.out.println(variations[i].getName() + ": " + variations[i].getValue());
        }
    }
}
