import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * Created by Gregory on 2/19/14.
 */
public class StringFormat
{
    enum Alignment {left, center, right}

    public static String padText(String text, int width, Alignment alignment, Character fill)
    {
        String result = new String();
        int leftPad = 0, rightPad = 0;

        switch (alignment)
        {
            case left:
                leftPad = 0;
                rightPad = width - text.length();
                break;

            default:
            case center:
                leftPad = center(width - text.length());
                rightPad = (width - text.length()) / 2;
                //leftPad = rightPad = (width - text.length()) / 2;
                //leftPad += width - leftPad - text.length() - rightPad;
                break;

            case right:
                leftPad = width - text.length();
                rightPad = 0;
        }

        // Print left pad
        for (int i = 0; i < leftPad; i++)
            result += fill;

        // Print text
        result += text;

        // Print right pad
        for (int i = 0; i < rightPad; i++)
            result += fill;

        return result;
    }

    public static String padText(String text, int width, int starting_point, Character fill)
    {
        String result = new String();
        int leftPad = 0, rightPad = 0;

        leftPad = starting_point;
        rightPad = width - text.length() - starting_point;

        // Print left pad
        for (int i = 0; i < leftPad; i++)
            result += fill;

        // Print text
        result += text;

        // Print right pad
        for (int i = 0; i < rightPad; i++)
            result += fill;

        return result;
    }

    public static String fill(Character fill, int width)
    {
        String result = "";

        for (int i = 0; i < width; i++)
            result += fill;

        return result;
    }

    public static int center(int width)
    {
        return width / 2 + width % 2;
    }

    public static String percent(Double value)
    {
        return MessageFormat.format("{0,number,#.##%}", value);
    }

    public static String joinBlocks(String[] blocks, String separator)
    {
        String result = "";
        String [][] lines = new String[blocks.length][];
        int [] blockWidths = new int[blocks.length];
        int longestBlock = 0;

        for (int b = 0; b < blocks.length; b++)
        {
            lines[b] = blocks[b].split(System.getProperty("line.separator"));

            blockWidths[b] = 0;

            for (int l = 0; l < lines[b].length; l++)
                if (lines[b][l].length() > blockWidths[b])
                    blockWidths[b] = lines[b][l].length();

            if (lines[b].length > longestBlock)
                longestBlock = lines[b].length;
        }

        for (int l = 0; l < longestBlock; l++)
        {
            for (int b = 0; b < blocks.length; b++)
            {
                if (l < lines[b].length)
                    result += lines[b][l];
                else
                    result += StringFormat.fill(' ', blockWidths[b]);

                if (b < blocks.length - 1)
                    result += separator;
            }

            result += System.getProperty("line.separator");
        }

        return result;
    }

    public static String fileSize(long size)
    {
        if (size <= 0) return "0 bytes";

        final String[] units = new String[]{"bytes", "kb", "mb", "gb", "tb"};

        int digitGroups = (int)(Math.log10(size)/Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}