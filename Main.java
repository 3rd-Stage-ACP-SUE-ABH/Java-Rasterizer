import Vector.ColorRGB;
import Vector.Pixel;
import static Vector.VecOperator.*;
import Renderer.Renderer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.BufferedWriter;
public class Main
{
    public static void main(String[]args) throws IOException {
        createPPM(title);
        //init PPM
        String init = ("P3\n"+pix_width+" "+pix_height+"\n255\n");
        //process pixelBuffer
        long start = System.nanoTime();

        Renderer myRenderer = new Renderer(pix_width, pix_height);
        for (int i =0;i<55;i++)
        {
            myRenderer.drawTriangle(new Pixel(10+10*i, 10+10*i), new Pixel( 10*i, 30*i), new Pixel(30*i, 10*i), new Color(Math.min(15*i, 255), Math.min(5*i, 255), Math.min(20*i, 255)));
        }

        long time = System.nanoTime() - start;
        System.out.println("Processing time :  " + (((double) time/1_000_000_000) + "s"));
        //write pixelBuffer
        StringBuilder bufferData=new StringBuilder(init);
        int[] pixelBuffer = myRenderer.getColorBuffer();
        for (int i =0; i<pixelBuffer.length;i++)
        {
            Color tempColor=new Color(pixelBuffer[i]);
            //concatting strings is extremely slow, since each time we concat the string has to be copied, meaning
            //we are copying the same string hundreds of thousands of times.
            //for building strings in for loops, use string builder or string buffer.
            bufferData.append(tempColor.getRed() + " " + tempColor.getGreen() + " " + tempColor.getBlue()+"\n");
        }
        writeToPPM(bufferData.toString());
    }
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