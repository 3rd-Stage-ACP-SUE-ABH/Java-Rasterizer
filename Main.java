import Vector.ColorRGB;
import Vector.Pixel;
import static Vector.VecOperator.*;
import Renderer.Renderer;
import java.awt.*;
import java.io.IOException;
import ppmWriter.*;
public class Main
{
    public static void main(String[]args) throws IOException {
        //process pixelBuffer
        long start = System.nanoTime();

        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.fill(new Color(50,50,50));

        long time = System.nanoTime() - start;
        System.out.println("Processing time :  " + (((double) time/1_000_000_000) + "s"));
        //write pixelBuffer
        StringBuilder bufferData = colorBufferToString(myRenderer.getColorBuffer());
        ppmWriter myPPM = new ppmWriter(pix_width, pix_height);
        myPPM.writeToPPM(bufferData.toString());
    }

    private static StringBuilder colorBufferToString(int[] colorBuffer) {
        StringBuilder bufferData=new StringBuilder();
        for (int i =0; i<colorBuffer.length;i++)
        {
            Color tempColor=new Color(colorBuffer[i]);
            //concatting strings is extremely slow, since each time we concat the string has to be copied, meaning
            //we are copying the same string hundreds of thousands of times.
            //for building strings in for loops, use string builder or string buffer.
            bufferData.append(tempColor.getRed()).append(" ").append(tempColor.getGreen()).append(" ").append(tempColor.getBlue()).append("\n");
        }
        return bufferData;
    }

    public static final int pix_height = 1080;
    public static final int pix_width = 1920;
}