import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Gregory on 3/2/14.
 */
public class RandomBytes
{
    private static String fileName = null;
    private static int length = 0;
    private static Random random = new Random(System.nanoTime());
    private static byte[] bytes = null;

    public static void main(String[] args)
    {
        if (args.length > 1)
        {
            length = Integer.parseInt(args[0]);
            fileName = args[1];

            try
            {
                File file = new File(fileName);
                FileOutputStream outputStream = new FileOutputStream(file);

                bytes = new byte[length];
                random.nextBytes(bytes);
                outputStream.write(bytes);

                outputStream.close();

                System.out.println();
                System.out.println(length + " bytes written to " + fileName);
            }
            catch (IOException e)
            {
                System.out.println();
                System.out.println("Cannot write to " + fileName);
            }
        }
    }
}
