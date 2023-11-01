import java.awt.*;
import java.io.IOException;

import pipeline.programmable.CellShader;
import renderer.Renderer;
import window.Window;

import static math.VecOperator.*;
public class Main
{
    public static void main(String[]args) throws IOException
    {
        //init renderer
        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.readTexture("C:/Users/msi/Desktop/african_head_diffuse.png");
        CellShader myCellShader = new CellShader();
        myRenderer.setShader(myCellShader);

        Window myWindow = new Window("Java Rasterizer", myRenderer);

        int i = 0;
        //TODO structure : make breaking of while loop dependent on user input
        //render loop
        while (true)
        {
            long start = System.nanoTime();
            //clear buffers at the beginning of the frame. 10-15ms
            myRenderer.myRasterizer.clearDepthBuffer();
            myRenderer.myRasterizer.clearPixelBuffer(new Color(50,50,50));

            //specify rotation angle of object around y-axis in radians
            rotationAngle = (float)i/5;

            //do the magic
            myCellShader.rotationAngle = rotationAngle;
            myCellShader.updateMatrices();
            myRenderer.renderModel();

            //write to the buffer after doing all processing.
            //avoid writing multiple times per frame as tearing happens.
            myRenderer.printBuffer();
            myWindow.update();
            i++;

            long time = System.nanoTime() - start;
            System.out.println("processing time :  " + (((double) time/1_000_000) + " ms/frame"));
        }
    }
    public static final int pix_height = 250;
    public static final int pix_width = 250;
}