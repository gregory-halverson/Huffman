import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Created by Gregory on 2/21/14.
 */
@SuppressWarnings("unchecked")
public class LatinFrequencyAnalyzer extends FrequencyAnalyzer<Character>
{
    FrequencyDistribution<Character> caseSensitiveSet;
    FrequencyDistribution<Character> characterFrequencyDistribution;

    long fileSize;
    int characterCount;
    int letterCount;
    int lowerCaseCount;
    int upperCaseCount;

    LatinFrequencyAnalyzer()
    {
        occurrenceMap = null;
        caseSensitiveSet = null;
        characterCount = 0;
        letterCount = 0;
        lowerCaseCount = 0;
        upperCaseCount = 0;
    }

    LatinFrequencyAnalyzer(URL url) throws IOException
    {
        BufferedReader reader = null;

        try
        {
            InputStream inputStream = url.openStream();

            fileSize = inputStream.available();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            reader = new BufferedReader(inputStreamReader);
            String input = "";
            String line = "";

            while ((line = reader.readLine()) != null)
                input += line;

            if (input.length() > 0)
                analyze(input);
            else
                throw new IOException();

            reader.close();
            inputStreamReader.close();
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    LatinFrequencyAnalyzer(File file) throws IOException
    {
        try
        {
            analyze(file);
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    LatinFrequencyAnalyzer(String input)
    {
        analyze(input);
    }

    public void analyze(File file) throws IOException
    {
        try
        {
            analyze(new Scanner(file).useDelimiter("\\Z").next());
            FileInputStream inputStream = new FileInputStream(file);
            fileSize = inputStream.getChannel().size();
            inputStream.close();
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    public void analyze(String input)
    {
        super.analyze(StringConversion.toCharacterArray(input));

        letterCount = 0;
        lowerCaseCount = 0;
        upperCaseCount = 0;

        for (int i = 0; i < input.length(); i++)
        {
            Character c = input.charAt(i);

            if (c >= 'a' && c <= 'z')
            {
                lowerCaseCount++;
                letterCount++;
            }
            if (c >= 'A' && c <= 'Z')
            {
                upperCaseCount++;
                letterCount++;
            }
        }

        caseSensitiveSet = new FrequencyDistribution<Character>();

        for (Map.Entry<Character, Integer> entry : occurrenceMap.entrySet())
        {
            if (entry.getKey() >= 'a' && entry.getKey() <= 'z')
                caseSensitiveSet.put(entry.getKey(), (double) entry.getValue() / lowerCaseCount);
            else if (entry.getKey() >= 'A' && entry.getKey() <= 'Z')
                caseSensitiveSet.put(entry.getKey(), (double) entry.getValue() / upperCaseCount);
        }

        characterFrequencyDistribution = new FrequencyDistribution<Character>();

        for (Map.Entry<Character, Integer> entry : occurrenceMap.entrySet())
        {
            //System.out.println("key: " + entry.getKey());

            if (entry.getKey() >= 'a' && entry.getKey() <= 'z')
            {
                if (occurrenceMap.containsKey(Character.toUpperCase(entry.getKey())))
                    characterFrequencyDistribution.put(entry.getKey(), (double)(entry.getValue() + occurrenceMap.get(Character.toUpperCase(entry.getKey()))) / lowerCaseCount);
                else
                    characterFrequencyDistribution.put(entry.getKey(), (double)entry.getValue() / letterCount);
            }
            else if (entry.getKey() >= 'A' && entry.getKey() <= 'Z')
            {
                if (!occurrenceMap.containsKey(Character.toLowerCase(entry.getKey())))
                    characterFrequencyDistribution.put(Character.toLowerCase(entry.getKey()), (double)(entry.getValue() + occurrenceMap.get(Character.toLowerCase(entry.getKey()))) / upperCaseCount);
                else
                    characterFrequencyDistribution.put(Character.toLowerCase(entry.getKey()), (double)entry.getValue() / letterCount);
            }
        }
    }

    public FrequencyDistribution<Character> getCaseSensitiveSet()
    {
        return caseSensitiveSet;
    }

    public static class Variation implements Comparable<Variation>
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

    public Variation[] variation(CharacterFrequencyTable table)
    {
        ArrayList<Variation> variationList = new ArrayList<Variation>();

        FrequencyDistribution<Character> case_insensitive_set = getCaseInsensitiveSet();

        for (String language : table.getHeaders())
        {
            variationList.add(new Variation(language, case_insensitive_set.variation(table.getFrequencyDistribution(language))));
        }

        Variation[] variations = new Variation[variationList.size()];

        variationList.toArray(variations);

        return (variations);
    }

/*
    public HashMap<String, Double> variation(CharacterFrequencyTable table)
    {
        HashMap<String, Double> variation = new HashMap<String, Double>();

        FrequencyDistribution<Character> case_insensitive_set = getCaseInsensitiveSet();

        for (String language : table.getHeaders())
        {
            variation.put(language, case_insensitive_set.variation(table.getFrequencyDistribution(language)));
        }

        return variation;
    }
*/
    public String closestMatch(CharacterFrequencyTable table)
    {
        String detected_language = "";
        Double lowest_variation = Double.MAX_VALUE;

        Variation[] variations = variation(table);

        for (int i = 0; i < variations.length; i++)
        {
            if (variations[i].getValue() < lowest_variation)
            {
                detected_language = variations[i].getName();
                lowest_variation = variations[i].getValue();
            }
        }

        return detected_language;
    }

    public FrequencyDistribution<Character> getCaseInsensitiveSet()
    {
        return characterFrequencyDistribution;
    }

    public long getFileSize()
    {
        return fileSize;
    }
}
