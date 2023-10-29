import Vector.*;
import Renderer.*;
import java.awt.*;
import java.io.IOException;
import window.ImageDisplay;
import window.Window;
import window.buttone;

import static Renderer.Renderer.*;
import static java.lang.Math.*;
import static Vector.VecOperator.*;
public class Main
{
    public static void main(String[]args) throws IOException
    {
        //init renderer
        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.readTexture("C:/Users/msi/Desktop/african_head_diffuse.png");

        Window myWindow = new Window("Java Rasterizer", myRenderer);

        //configure our own light settings
        PrimitiveShader.clearLights();
        Light diffuse1 = new Light();
        diffuse1.lightColor = new Color(0.75f,0.2f,0.2f);
        diffuse1.direction = new Vec3f(0.5f, 0,0);
        PrimitiveShader.addLight(diffuse1);
        Light diffuse2 = new Light();
        diffuse2.lightColor = new Color(0.7f, 0.7f, 0.7f);
        diffuse2.direction = new Vec3f(-0.5f, -0.3f, -0.2f);
        PrimitiveShader.addLight(diffuse2);
    //    PrimitiveShader.ambient.lightColor= new Color(0.15f, 0.09f, 0.18f);
        int i = 0;
        //TODO structure : make breaking of while loop dependent on user input
        //render loop
        while (true)
        {
            long start = System.nanoTime();

            //clear buffers at the beginning of the frame. 10-15ms
            myRenderer.fill(new Color(50,50,50));
            myRenderer.clearDepthBuffer();

            //specify rotation angle of object around y-axis in radians
            rotationAngle = (float)i/5;
            //do the magic
            myRenderer.renderModel();

            //write to the buffer after doing all processing.
            //avoid writing multiple times per frame as tearing happens.
            myRenderer.printBuffer();
            myWindow.update();
            i++;

            long time = System.nanoTime() - start;
        //    System.out.println("processing time :  " + (((double) time/1_000_000) + " ms/frame"));
        }
    }
    public static Pixel mapToScreen (float x, float y, float min, float max)
    {
        return new Pixel(map(min, max,0, pix_width-1, x).intValue(), map(min,max,0, pix_height-1, y).intValue());
    }
    public static void drawBouncyCircle(Renderer yourRenderer, int i)
    {   //this was cluttering the loop
        int radius = 50+(int)((sin((double) System.currentTimeMillis() /250)+1)/2*25);
        Pixel center = new Pixel(i%(pix_width-1)+(int)(((sin((double) System.currentTimeMillis() /1000)+1)/2)*100),
                i%(pix_height)+(int)(((sin((double) System.currentTimeMillis() /1000)+1)/2)*100));
        for (int j =center.y()-radius;j<center.y()+radius;j++)
        {
            for (int k = center.x()-radius;k<center.x()+radius;k++)
            {
                Pixel candidate = new Pixel(k,j);
                if ( new Pixel(candidate.x()-center.x(),candidate.y()-center.y()).magnitude()<radius )
                {
                    Pixel point = new Pixel (abs (candidate.x()%(pix_width-1)), abs(candidate.y()%(pix_height-1)));

                    yourRenderer.setPixel(point, new Color((int)((sin((double) System.currentTimeMillis() /1200)+1)/2*255),
                            (int)((sin((double) System.currentTimeMillis() /900)+1)/2*255), (int)((sin((double) System.currentTimeMillis() /800)+1)/2*255), 255));
                }
            }
        }
    }
    public static final int pix_height = 500;
    public static final int pix_width = 500;
}