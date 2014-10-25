import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Gregory on 2/21/14.
 */
public class DetectLanguage
{
    public static void main(String [] args)
    {
        CharacterFrequencyTable table = null;
        try {
            table = new CharacterFrequencyTable("Letter Frequencies.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();

        for (int f = 0; f < args.length; f++)
        {
            LatinFrequencyAnalyzer analysis = null;

            try
            {
                analysis = new LatinFrequencyAnalyzer(new URL(args[f]));
                System.out.println(args[f] + ": " + analysis.closestMatch(table));
            }
            catch (IOException e)
            {
                try
                {
                    analysis = new LatinFrequencyAnalyzer(new File(args[f]));
                    System.out.println(args[f] + ": " + analysis.closestMatch(table));
                }
                catch (IOException e1)
                {
                    System.out.println(args[f] + " not found");
                }
            }
        }
    }
}
