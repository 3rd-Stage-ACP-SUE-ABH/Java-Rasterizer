import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ppmWriter {
    public static File createPPM(String title)
    {
        try{
            File image = new File(title+".PPM");
            if (image.createNewFile())
            {
                return image;
            }
            else
            {
                System.out.println("FILE EXISTS");
            }
        }
        catch (IOException e)
        {
            System.out.println("FAILED TO CREATE FILE");
        }
        return null;
    }
    public static void writeToPPM (String writeable) throws IOException
    {
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(title+".PPM"));
        myWriter.write(writeable);
        myWriter.close();
    }
    public static final String title ="output";
    public static final int pix_height = 1000;
    public static final int pix_width = 1000;
}
