import java.io.*;
import java.util.*;

/**
 * Created by Gregory on 2/21/14.
 */
public class CSVParser
{
    private Map<String, Map<String, String>> data;
    private Map<Integer, String> headers;
    private ArrayList<String> fields;

    CSVParser(String file_name) throws IOException
    {
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(file_name));
            String line = "";
            String [] tokens;
            data = new HashMap<String, Map<String, String>>();
            headers = new HashMap<Integer, String>();
            fields = new ArrayList<String>();
            String field = "";

            // Read headers
            if ((line = file.readLine()) != null)
            {
                tokens = line.split(",");

                for (int i = 1; i < tokens.length; i++)
                {
                    data.put(tokens[i], new HashMap<String, String>());
                    headers.put(i, tokens[i]);
                }
            }

            while ((line = file.readLine()) != null)
            {
                tokens = line.split(",");

                field = tokens[0];
                fields.add(field);

                for (int i = 1; i < tokens.length; i++)
                {
                    data.get(headers.get(i)).put(field, tokens[i]);
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
        String[] header_strings = new String[headers.size()];

        for (Integer i = 1; i <= headers.size(); i++)
        {
            header_strings[i - 1] = headers.get(i);
        }

        return header_strings;
    }

    public String [] getFields()
    {
        String[] field_strings = new String[fields.size()];

        for (Integer i = 0; i < fields.size(); i++)
        {
            field_strings[i] = fields.get(i);
        }

        return field_strings;
    }

    public String get(String header, String field)
    {
        if (data.containsKey(header))
            if (data.get(header).containsKey(field))
                return data.get(header).get(field);

        return "";
    }
}
