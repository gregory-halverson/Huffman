import java.util.Arrays;

/**
 * Created by Gregory on 2/28/14.
 */
public class Entropy
{
    public static void main(String[] args)
    {
        Byte[] data = new Byte[1024];

        for (int i = 0; i < data.length; i++)
            data[i] = (byte)(Math.random() * 256);

        System.out.println(Arrays.asList(data));

        FrequencyAnalyzer<Byte> analyzer = new FrequencyAnalyzer<Byte>(data);

        System.out.println(analyzer.getFrequencyDistribution());
        System.out.println(analyzer.getFrequencyDistribution().entropy());

        String string = "this is just some English text";

        FrequencyAnalyzer<Character> string_analyzer = new FrequencyAnalyzer<Character>(StringConversion.toCharacterArray(string));

        System.out.println();
        System.out.println(string);
        System.out.println(string_analyzer.getFrequencyDistribution());
        System.out.println(string_analyzer.getFrequencyDistribution().entropy());

    }
}
