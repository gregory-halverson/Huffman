import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Gregory on 3/2/14.
 */
public class AnalyzeWords
{
    private static final int LIMIT = 10;

    private static FrequencyAnalyzer<String> analysis = null;
    private static String fileName = null;

    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            fileName = args[0];

            try
            {
                String[] words = new Scanner(new File(args[0])).useDelimiter("\\A").next().toLowerCase().split("\\P{L}+");

                analysis = new FrequencyAnalyzer<String>(words);

                System.out.println();
                System.out.println(analysis.getFrequencyDistribution().size() + " words found");
                System.out.println();

                int i = 0;

                for (Map.Entry<String, Double> entry : analysis.getFrequencyDistribution().descendingByValue().entrySet())
                {
                    System.out.println(entry.getKey() + ": " + StringFormat.percent(entry.getValue()));

                    if (++i == LIMIT)
                        break;
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println();
                System.out.println("File " + fileName + " not found");
            }
        }
        else
        {
            System.out.println();
            System.out.println("usage: AnalyzeWords \"file name\"");
        }
    }
}
