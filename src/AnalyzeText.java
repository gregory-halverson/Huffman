import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gregory on 3/1/14.
 */
public class AnalyzeText
{
    protected static final int LABEL_FIELD = 20;

    protected static PrintWriter output = null;

    protected static LatinFrequencyAnalyzer analysis = null;
    protected static CharacterFrequencyTable table = null;
    protected static LatinFrequencyAnalyzer.Variation[] variation = null;

    protected static int tableHeaders = 0;
    protected static int fileSize = 0;

    protected static String fileName = "";
    protected static String tableFileName = "";

    protected static void analyze()
    {
        try
        {
            if (tableFileName != null)
                table = new CharacterFrequencyTable(tableFileName);
            else
                table = null;

            try
            {
                analysis = new LatinFrequencyAnalyzer(new URL(fileName));
            }
            catch (IOException e)
            {
                try
                {
                    analysis = new LatinFrequencyAnalyzer(new File(fileName));
                }
                catch (FileNotFoundException e1)
                {
                    output.println(fileName + " not found");
                    analysis = null;
                }
            }

            if (analysis != null && table != null)
            {
                variation = analysis.variation(table);
                Collections.sort(Arrays.asList(variation));
            }
        }
        catch (IOException e)
        {
            output.println("Frequency table " + tableFileName + " not found");
        }
    }

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();

        StringWriter outputString = new StringWriter();
        output = new PrintWriter(outputString);

        if (args.length > 0)
        {
            fileName = args[0];

            if (args.length > 1)
                tableFileName = args[1];
            else
                tableFileName = null;

            analyze();
        }
        else
        {
            output.println();
            output.println("use: AnalyzeText \"source file\" (\"frequency distribution table\")");
        }

        if (analysis != null)
        {
            output.println();
            output.println(StringFormat.padText("Source file: ", LABEL_FIELD, StringFormat.Alignment.right, ' ') + "\"" + fileName + "\" (" + StringFormat.fileSize(analysis.getFileSize()) + ")");

            if (tableFileName != null)
                output.println(StringFormat.padText("Frequency table: ", LABEL_FIELD, StringFormat.Alignment.right, ' ') + "\"" + tableFileName + "\" (" + table.getHeaders().length + " frequency distribution sets)");

            output.println();

            int characterField = 10;
            int frequencyField = 10;
            int[] headerFields = null;

            if (table != null)
            {
                headerFields = new int[(table.getHeaders().length)];

                tableHeaders = headerFields.length;

                for (int i = 0; i < tableHeaders; i++)
                    headerFields[i] = variation[i].getName().length() + 2;
            }

            output.print(StringFormat.padText("Character", characterField, StringFormat.Alignment.left, ' '));
            output.print(StringFormat.padText("Frequency", frequencyField, StringFormat.Alignment.left, ' '));

            for (int i = 0; i < tableHeaders; i++)
                output.print(StringFormat.padText(variation[i].getName(), headerFields[i], StringFormat.Alignment.left, ' '));

            output.println();

            for (Map.Entry<Character, Double> entry : analysis.getCaseInsensitiveSet().descendingByValue().entrySet())
            {
                output.print(StringFormat.padText("\'" + entry.getKey() + "\'", characterField, StringFormat.Alignment.left, ' '));
                output.print(StringFormat.padText(StringFormat.percent(entry.getValue()), frequencyField, StringFormat.Alignment.left, ' '));

                for (int i = 0; i < tableHeaders; i++)
                    output.print(StringFormat.padText(StringFormat.percent(Math.abs(entry.getValue() - table.get(variation[i].getName(), entry.getKey()))), headerFields[i], StringFormat.Alignment.left, ' '));

                output.println();
            }

            output.println();

            if (table != null)
            {
                output.println();
                output.print(StringFormat.padText("Total variation: ", characterField + frequencyField, StringFormat.Alignment.right, ' '));

                for (int i = 0; i < tableHeaders; i++)
                    output.print(StringFormat.padText(StringFormat.percent(variation[i].getValue()), headerFields[i], StringFormat.Alignment.left, ' '));

                output.println();
                output.println();
                output.println(StringFormat.padText("Closest match: ", LABEL_FIELD, StringFormat.Alignment.right, ' ') + analysis.closestMatch(table));
                output.println();
            }

            //output.println();
            output.println(StringFormat.padText("Entropy: ", LABEL_FIELD, StringFormat.Alignment.right, ' ') + (new DecimalFormat("##.##").format(analysis.entropy())) + " bits per character");
            output.println(StringFormat.padText("Entropy size: ", LABEL_FIELD, StringFormat.Alignment.right, ' ') + StringFormat.fileSize(analysis.entropySize()) + " (" + StringFormat.percent((double) analysis.entropySize() / (double) analysis.getFileSize()) + " of file size)");

            output.println();
            output.println("Analysis completed in " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " milliseconds");
        }

        System.out.print(outputString);
    }
}