
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
        //load texture
        BufferedImage textureReader = ImageIO.read(new File("C:/Users/msi/Desktop/african_head_diffuse.png"));
        int[] textureData = new int[textureReader.getHeight()*textureReader.getWidth()];
        for (int i =0;i<textureReader.getHeight();i++)
        {
            for (int j =0; j<textureReader.getWidth();j++)
            {
                textureData[i*textureReader.getWidth()+j]=(textureReader.getRGB(j,i));
            }
        }
        /* testing
        String textureTest = colorBufferToString(textureData);
        ppmWriter textureWriter = new ppmWriter(textureReader.getWidth(), textureReader.getHeight());
        textureWriter.setTitle("texture");
        textureWriter.writeToPPM(textureTest); */

        //instantiate zBuffer
        for (int i =0; i<zBuffer.length; i++)
        {
            zBuffer[i] = -Float.MAX_VALUE;
        }
        //init window
        ImageDisplay img = new ImageDisplay(pix_width,pix_height,"test display");
        img.setSize(pix_width,pix_height);
        img.setVisible(true);


        Model africanHead = new Model("C:/Users/msi/Desktop/african_head.obj");
        //process pixelBuffer
        long start = System.nanoTime();

        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.fill(new Color(50,50,50));
        //we only need to load the texture data once
        myRenderer.textureData = new Color[textureData.length];
        for (int i =0; i<textureData.length;i++)
        {
            myRenderer.textureData[i]=new Color (textureData[i]);
        }
        myRenderer.texHeight=textureReader.getHeight();
        myRenderer.texWidth=textureReader.getWidth();
        for (int i =0; i<africanHead.nFaces();i++)
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
            }
            Vec3f AB = minus(worldCoords[2], worldCoords[0]);
            Vec3f AC = minus(worldCoords[1], worldCoords[0]);
            Vec3f normal = cross(AC, AB).getNormalized();
            myRenderer.diffuse.lightColor = new Color(0.8f,0.95f,1.0f);
            myRenderer.diffuse.direction = new Vec3f(0.5f,-1.0f,-0.2f);
            myRenderer.ambient.lightColor = new Color(0.1f,0.05f,0.0f);
            Color diffuseDirectional = myRenderer.diffuseDirectional(normal);
            Color sumColor = new Color(min(diffuseDirectional.getRed()+myRenderer.ambient.lightColor.getRed(), 255),
                    min(diffuseDirectional.getGreen()+myRenderer.ambient.lightColor.getGreen(), 255),
                    min(diffuseDirectional.getBlue()+myRenderer.ambient.lightColor.getBlue(), 255), 255
                    );
            //testing
        /*  Color randomColor = new Color((int)(random()*255), (int)(random()*255), (int)(random()*255));
            Color normalRGB = new Color (map(-1,1, 0, 255, normal.x()).intValue(),
                    map(-1,1, 0, 255, normal.y()).intValue(),
                    map(-1,1, 0, 255, normal.z()).intValue(),
                    255); */
            myRenderer.drawTriangle(screenCoords, textureCoords, zBuffer, sumColor);
        }
        //testing: displays texture onto screen
    /*    for (int i =0; i<myRenderer.texHeight;i++)
        {
            for (int j = 0; j<myRenderer.texWidth;j++)
            {
                myRenderer.setPixel(new Pixel(map(0, myRenderer.texWidth-1, 0, pix_width-1, j).intValue(),
                        map(0, myRenderer.texHeight-1, 0, pix_height-1, i).intValue()), myRenderer.textureData[j+i*myRenderer.texWidth]);
            }
        } */
        long time = System.nanoTime() - start;
        System.out.println("Processing time :  " + (((double) time/1_000_000_000) + "s"));
        Color[] colorBufferWindow= new Color[myRenderer.colorBuffer.length];
        for (int i =0;i<myRenderer.colorBuffer.length;i++)
        {
            colorBufferWindow[i]=new Color(myRenderer.getColorBuffer()[i]);
        }
        while (true)
        {
            img.loadBuffer(colorBufferWindow);
            img.updateBufferedImage();
            ImageDisplay.imagePanel.repaint();
        }
        //write pixelBuffer
    //    ppmWriter myPPM = new ppmWriter(pix_width, pix_height);
    //    myPPM.writeToPPM(colorBufferToString(myRenderer.getColorBuffer()));
    }
    public static Pixel mapToScreen (float x, float y, float min, float max)
    {
        return new Pixel(map(min, max,0, pix_width-1, x).intValue(), map(min,max,0, pix_height-1, y).intValue());
    }
    public static final int pix_height = 1000;
    public static final int pix_width = 1000;
    public static float[] zBuffer = new float[pix_height*pix_width];
}