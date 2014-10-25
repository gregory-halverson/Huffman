import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Gregory on 2/21/14.
 */
@SuppressWarnings("unchecked")
public class CharacterFrequencyTable
{
    private Map<String, FrequencyAnalyzer.FrequencyDistribution<Character>> data;
    String [] headers;
    Character [] fields;

    CharacterFrequencyTable(String file_name) throws IOException
    {
        try
        {
            CSVParser file = new CSVParser(file_name);
            String [] field_strings = file.getFields();

            headers = file.getHeaders();

            fields = new Character[field_strings.length];

            for (int i = 0; i < field_strings.length; i++)
                fields[i] = field_strings[i].charAt(0);

            NumberFormat percent = NumberFormat.getPercentInstance();

            data = new HashMap<String, FrequencyAnalyzer.FrequencyDistribution<Character>>();

            for (int h = 0; h < headers.length; h++)
            {
                //System.out.println(headers[h]);
                data.put(headers[h], new FrequencyAnalyzer.FrequencyDistribution<Character>());

                for (int c = 0; c < fields.length; c++)
                {
                    //System.out.println(fields[c] + ": " + file.get(headers[h], Character.toString(fields[c])));
                    try
                    {
                        data.get(headers[h]).put(fields[c], (Double)percent.parse(file.get(headers[h], Character.toString(fields[c]))));
                    }
                    catch (Exception e)
                    {
                        data.get(headers[h]).put(fields[c], (Double) 0.0);
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    public String [] getHeaders()
    {
        return headers;
    }

    public Character [] getFields()
    {
        return fields;
    }

    public FrequencyAnalyzer.FrequencyDistribution<Character> getFrequencyDistribution(String language)
    {
        return data.get(language);
    }

    public Double get(String language, Character character)
    {
        if (data.containsKey(language))
            if (data.get(language).containsKey(character))
                return data.get(language).get(character);

        return (Double)0.0;
    }
}