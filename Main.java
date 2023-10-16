
import javax.imageio.ImageIO;
import javax.imageio.ImageIO.*;
import Vector.Pixel;
import Vector.*;
import Renderer.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import Vector.Vector;
import ppmWriter.*;
import Model.*;
import window.ImageDisplay;

import static Renderer.Renderer.colorBufferToString;
import static Renderer.Renderer.map;
import static Vector.VecOperator.*;
import static java.lang.Math.*;

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
        //init window
        ImageDisplay img = new ImageDisplay(pix_width,pix_height,"test display");
        img.setSize(pix_width,pix_height);
        img.setVisible(true);


        Model africanHead = new Model("C:/Users/msi/Desktop/african_head.obj");
        //process pixelBuffer

        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.fill(new Color(50,50,50));

        myRenderer.textureData = new Color[textureData.length];
        for (int i =0; i<textureData.length;i++)
        {
            myRenderer.textureData[i]=new Color (textureData[i]);
        }
        myRenderer.texHeight=textureReader.getHeight();
        myRenderer.texWidth=textureReader.getWidth();

        myRenderer.loadModelData(africanHead);


        //TODO make breaking of while loop dependent on user input
        myRenderer.diffuse.lightColor = new Color(0.65f, 0.85f,1.0f);
    //  myRenderer.diffuse.direction = new Vec3f(0.45f, -0.3f, -0.15f);
        myRenderer.ambient.lightColor = new Color(0.0F,0.05F,0.05F);
        int i = 0;
        Color[] colorBufferWindow= new Color[myRenderer.colorBuffer.length];
        while (true)
        {
            long start = System.nanoTime();
            //reset buffer at the beginning of the frame
            myRenderer.fill(new Color(50,50,50));
            //as proof of concept, render something cool
            int radius = 50+(int)((sin((double) System.currentTimeMillis() /1000)+1)/2*50);
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
            }
            //TODO bug fixing : model render gets removed after one frame
        //  myRenderer.renderModel(africanHead);

            //copy renderer output to screen buffer
            for (int j =0;j<myRenderer.colorBuffer.length;j++)
            {
                colorBufferWindow[j]=new Color(myRenderer.getColorBuffer()[j]);
            }
            img.loadBuffer(colorBufferWindow);
            img.updateBufferedImage();
            ImageDisplay.imagePanel.repaint();
            i++;
            long time = System.nanoTime() - start;
            System.out.println("Processing time :  " + (((double) time/1_000_000_000) + "s per frame"));
        }
    }
    public static Pixel mapToScreen (float x, float y, float min, float max)
    {
        return new Pixel(map(min, max,0, pix_width-1, x).intValue(), map(min,max,0, pix_height-1, y).intValue());
    }
    public static final int pix_height = 1000;
    public static final int pix_width = 1000;
}
// we keep this code in case of need
        /* testing
        String textureTest = colorBufferToString(textureData);
        ppmWriter textureWriter = new ppmWriter(textureReader.getWidth(), textureReader.getHeight());
        textureWriter.setTitle("texture");
        textureWriter.writeToPPM(textureTest); */
        //testing
        /*  Color randomColor = new Color((int)(random()*255), (int)(random()*255), (int)(random()*255));
            Color normalRGB = new Color (map(-1,1, 0, 255, normal.x()).intValue(),
                    map(-1,1, 0, 255, normal.y()).intValue(),
                    map(-1,1, 0, 255, normal.z()).intValue(),
                    255); */
//testing: Renderer displays its loaded texture onto screen
    /*    for (int i =0; i<myRenderer.texHeight;i++)
        {
            for (int j = 0; j<myRenderer.texWidth;j++)
            {
                myRenderer.setPixel(new Pixel(map(0, myRenderer.texWidth-1, 0, pix_width-1, j).intValue(),
                        map(0, myRenderer.texHeight-1, 0, pix_height-1, i).intValue()), myRenderer.textureData[j+i*myRenderer.texWidth]);
            }
        } */
/*    for (int i =0; i<africanHead.nFaces();i++)
        {
            int[] face = africanHead.getVertexIndices(i);
            int[] faceNormals = africanHead.getNormalIndices(i);
            int[] faceTexture = africanHead.getTextureIndices(i);
            float[][] vCoords = new float[3][3];
            float[][] nCoords = new float[3][3];
            float[][] tCoords = new float[3][3];
            Vec3f[] screenCoords = new Vec3f[3];
            Vec3f[] worldCoords = new Vec3f[3];
            Vec3f[] normalCoords = new Vec3f[3];
            Vec3f[] textureCoords = new Vec3f[3];
            for (int j =0;j<3;j++)
            {
                int indexV = face[j];
                int indexN = faceNormals[j];
                int indexT = faceTexture[j];
                vCoords[j] = new float[]{africanHead.getVertexCoords(indexV)[0], africanHead.getVertexCoords(indexV)[1], africanHead.getVertexCoords(indexV)[2]};
                screenCoords[j]=new Vec3f (mapToScreen(vCoords[j][0], vCoords[j][1], -1, 1).x(),
                        mapToScreen(vCoords[j][0], vCoords[j][1], -1, 1).y(),
                        africanHead.getVertexCoords(indexV)[2]);
                worldCoords[j] = new Vec3f(africanHead.getVertexCoords(indexV)[0],africanHead.getVertexCoords(indexV)[1], africanHead.getVertexCoords(indexV)[2]);
                nCoords[j] = new float[]{africanHead.getNormalCoords(indexN)[0], africanHead.getNormalCoords(indexN)[1], africanHead.getNormalCoords(indexN)[2]};
                normalCoords[j]= new Vec3f(nCoords[j][0], nCoords[j][1], nCoords[j][2]);
                tCoords[j]= new float[]{africanHead.getTexCoords(indexT)[0], africanHead.getTexCoords(indexT)[1], africanHead.getTexCoords(indexT)[2]};
                textureCoords[j]= new Vec3f(tCoords[j][0], tCoords[j][1], tCoords[j][2]);
            }n
            Vec3f AB = minus(worldCoords[2], worldCoords[0]);
            Vec3f AC = minus(worldCoords[1], worldCoords[0]);
            Vec3f normal = cross(AC, AB).getNormalized();
            myRenderer.diffuse.lightColor = new Color(0.8f,0.95f,1.0f);
            myRenderer.diffuse.direction = new Vec3f(0.5f,-1.0f,-0.2f);
            myRenderer.ambient.lightColor = new Color(0.0f,0.05f,0.1f);
            Color diffuseDirectional = myRenderer.diffuseDirectional(normal);
            Color sumColor = new Color(min(diffuseDirectional.getRed()+myRenderer.ambient.lightColor.getRed(), 255),
                    min(diffuseDirectional.getGreen()+myRenderer.ambient.lightColor.getGreen(), 255),
                    min(diffuseDirectional.getBlue()+myRenderer.ambient.lightColor.getBlue(), 255), 255
                    );

            myRenderer.drawTriangle(screenCoords, textureCoords, zBuffer, sumColor);
        } */