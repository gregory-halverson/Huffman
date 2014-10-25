import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

public class HuffmanDecode
{
    public static void main(String[] args)
    {
        byte[] byteArray = null;
        long inputFileSize = 0;
        long outputFileSize = 0;

        long startTime = System.nanoTime();

        if (args.length >= 1)
        {
            String inputFilename = args[0];

            try
            {
                FileInputStream inputFile = new FileInputStream(inputFilename);

                inputFileSize = inputFile.getChannel().size();

                System.out.println(StringFormat.padText("Encoded file: ", 14, StringFormat.Alignment.right, ' ') + inputFilename + " (" + StringFormat.fileSize(inputFileSize) + ")");

                HuffmanCodec<Byte> codec = new HuffmanCodec<Byte>();

                ArrayList<Byte> byteList = codec.decodeFile(inputFilename);

                outputFileSize = byteList.size();

                byteArray = new byte[(int)outputFileSize];

                for (int i = 0; i < outputFileSize; i++)
                    byteArray[i] = byteList.get(i);
            }
            catch (IOException e)
            {
                System.out.println("Cannot open file " + inputFilename);
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Unable to decode " + inputFilename);
            }

            String outputFilename;

            if (args.length >= 2)
                outputFilename = args[1];
            else
                outputFilename = args[0].substring(0, args[0].lastIndexOf('.'));

            try
            {
                FileOutputStream outputFile = new FileOutputStream(outputFilename);

                outputFile.write(byteArray);

                outputFile.close();

                System.out.println(StringFormat.padText("Decoded file: ", 14, StringFormat.Alignment.right, ' ') + outputFilename + " (" + StringFormat.fileSize(outputFileSize) + ")");
                System.out.println(StringFormat.padText("Expansion: ", 14, StringFormat.Alignment.right, ' ') + StringFormat.percent(1 - ((double)inputFileSize/(double)outputFileSize)));

                System.out.println();

                System.out.println("Expansion completed in " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " milliseconds");
            }
            catch (IOException e)
            {
                System.out.println("Unable to write to file " + outputFilename);
            }
        }
    }
}