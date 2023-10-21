
import javax.imageio.ImageIO;
import Vector.Pixel;
import Vector.*;
import Renderer.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import Model.*;
import window.ImageDisplay;
import static Renderer.Renderer.*;
import static java.lang.Math.*;
import static Vector.VecOperator.*;
public class Main
{
    public static void main(String[]args) throws IOException {
        //load texture inverted around x-axis
        BufferedImage textureReader = ImageIO.read(new File("C:/Users/msi/Desktop/african_head_diffuse.png"));
        int[] textureData = new int[textureReader.getHeight()*textureReader.getWidth()];
        for (int i =textureReader.getHeight()-1;i>=0;i--)
        {
            for (int j =0; j<textureReader.getWidth();j++)
            {
                textureData[(textureReader.getHeight()-i-1)*textureReader.getWidth()+j]=(textureReader.getRGB(j,i));
            }
        }
        Model africanHead = new Model("C:/Users/msi/Desktop/african_head.obj");
        //process pixelBuffer
        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.objectAmp=1;
        myRenderer.fill(new Color(50,50,50));

        myRenderer.textureData = new Color[textureData.length];
        for (int i =0; i<textureData.length;i++)
        {
            myRenderer.textureData[i]=new Color (textureData[i]);
        }
        myRenderer.texHeight=textureReader.getHeight();
        myRenderer.texWidth=textureReader.getWidth();

        myRenderer.loadModelData(africanHead);

        //init window
        ImageDisplay img = new ImageDisplay(pix_width,pix_height,"test display", myRenderer.getPixelBuffer());
        img.setSize(pix_width,pix_height);
        img.setVisible(true);

        //TODO make breaking of while loop dependent on user input
        myRenderer.diffuse.lightColor = new Color(0.65f, 0.85f,1.0f);
    //  myRenderer.diffuse.direction = new Vec3f(0.45f, -0.3f, -0.15f);
        myRenderer.ambient.lightColor = new Color(0.05F,0.075F,0.15F);
        int i = 0;
        Color[] colorBufferWindow= new Color[myRenderer.colorBuffer.length];
        //render loop
        while (true)
        {
            long start = System.nanoTime();

            //clear buffers at the beginning of the frame. 10-15ms
            myRenderer.fill(new Color(50,50,50));
            myRenderer.clearDepthBuffer();

            //as proof of concept, render something cool
       /*     int radius = 50+(int)((sin((double) System.currentTimeMillis() /250)+1)/2*25);
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

                        myRenderer.setPixel(point, new Color((int)((sin((double) System.currentTimeMillis() /1200)+1)/2*255),
                                (int)((sin((double) System.currentTimeMillis() /900)+1)/2*255), (int)((sin((double) System.currentTimeMillis() /800)+1)/2*255), 255));
                    }
                }
            } */
            myRenderer.diffuse.direction = new Vec3f( (float)sin((double) System.currentTimeMillis() /100),
                      -0.5f,
                      (float)cos((double) System.currentTimeMillis() /100));
            myRenderer.diffuse.lightColor = new Color((int)(random()*255), (int)(random()*255), (int)(random()*255), 255);
            myRenderer.renderModel();

            //write to the buffer after doing all processing.
            //avoid writing multiple times per frame as tearing happens.
            myRenderer.printBuffer();
            ImageDisplay.imagePanel.repaint();
            i++;
            long time = System.nanoTime() - start;
        //    System.out.println("processing time :  " + (((double) time/1_000_000_000) + "s"));
        }
    }
    public static Pixel mapToScreen (float x, float y, float min, float max)
    {
        return new Pixel(map(min, max,0, pix_width-1, x).intValue(), map(min,max,0, pix_height-1, y).intValue());
    }
    public static final int pix_height = 500;
    public static final int pix_width = 500;
}