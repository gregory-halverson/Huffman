import java.io.*;
import java.util.concurrent.TimeUnit;

// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

public class HuffmanEncode
{
    private static final int labelField = 15;

    public static void main(String[] args)
    {
        Byte[] byteArray = null;
        int inputFileSize = 0;
        HuffmanCodec.FileSize outputFileSize;

        long startTime = System.nanoTime();

        if (args.length >= 1)
        {
            String inputFilename = args[0];

            try
            {
                FileInputStream inputFile = new FileInputStream(inputFilename);

                inputFileSize = inputFile.available();

                byte[] inputBytes = new byte[inputFileSize];

                inputFile.read(inputBytes);

                byteArray = new Byte[inputFileSize];

                for (int b = 0; b < inputBytes.length; b++)
                    byteArray[b] = inputBytes[b];

                System.out.println();
                System.out.println(StringFormat.padText("Source file: ", labelField, StringFormat.Alignment.right, ' ') + inputFilename + " (" + StringFormat.fileSize(inputFileSize) + ")");
            }
            catch (IOException e)
            {
                System.out.println("Cannot open file " + inputFilename);
            }

            String outputFilename;

            if (args.length >= 2)
                outputFilename = args[1];
            else
                outputFilename = args[0] + ".huff";

            HuffmanCodec<Byte> codec = new HuffmanCodec<Byte>();

            try
            {
                outputFileSize = codec.encodeFile(byteArray, outputFilename);

                System.out.println(StringFormat.padText("Encoded file: ", labelField, StringFormat.Alignment.right, ' ') + outputFilename + " " + outputFileSize);
                System.out.println(StringFormat.padText("Code length: ", labelField, StringFormat.Alignment.right, ' ') + StringFormat.fileSize(outputFileSize.code) + " (" + StringFormat.percent((double) outputFileSize.code / codec.getAnalyzer().entropySize() - 1.0) + " over entropy)");
                System.out.println(StringFormat.padText("Overhead: ", labelField, StringFormat.Alignment.right, ' ') + StringFormat.percent((outputFileSize.total - outputFileSize.code) / (double)outputFileSize.total));
                System.out.println(StringFormat.padText("Compression: ", labelField, StringFormat.Alignment.right, ' ') + StringFormat.percent(1.0 - ((double) outputFileSize.total / (double) inputFileSize)));

                System.out.println();

                //System.out.println("Data storage " + StringFormat.percent(outputFileSize.data / (codec.getCaseSensitiveSet().entropy() * outputFileSize.data / 8.0) - 1.0) + " over entropy");

                System.out.println("Compression completed in " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " milliseconds");
            }
            catch (IOException e)
            {
                System.out.println("Cannot write to file " + outputFilename);
            }
        }
    }
}
