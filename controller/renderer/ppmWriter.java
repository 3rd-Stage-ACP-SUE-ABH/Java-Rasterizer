package controller.renderer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ppmWriter {
    //this class outputs a simple .ppm image file.
    //mainly used for testing purposes.
    public ppmWriter(int width, int height)
    {
        pix_width = width;
        pix_height = height;
    }
    private void createPPM()
    {
        try{
            ppmImage = new File(this.title+".PPM");
            if (ppmImage.createNewFile())
            {
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
    }
    public void writeToPPM (String colorBuffer) throws IOException
    {
        loadBuffer(colorBuffer);
        createPPM();
        String init = ("P3\n" + pix_width + " " + pix_height + "\n255\n");
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(title+".PPM"));
        myWriter.write(init);
        myWriter.write(data);
        myWriter.close();
    }
    private void loadBuffer(String colorBuffer)
    {
        //TODO bad input handling
        data = colorBuffer;
    }
    public void setTitle(String title)
    {
        this.title= title;
    }
    File ppmImage;
    private String data;
    private String title ="output";
    private final int pix_height;
    private final int pix_width;
}
