package model.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import model.object3D.Object3D;
import model.math.*;
import model.pipeline.fixed.Rasterizer;
import model.pipeline.fixed.Shader;

import static model.math.VecOperator.*;

public class Renderer {
    BufferedImage pixelBuffer;
    Object3D modelObject;
    //model data. stored such that index 0 corresponds to the 3 3D coordinates specified by face 0.
    //TODO structure : accommodate loading of multiple models
    //TODO error handling : null handling
    //TODO structure : implement a way to handle objects that have no texture coords, or false texture coords. Also implement a texture loading function
    Vec3f[][] vertexCoords;
    Vec3f[][] textureCoords;
    Vec3f[][] normalCoords;
    public int width, height;
    public int[] colorBuffer;        //pixel buffer
    public Color[] textureData;
    public int texHeight, texWidth;
    public boolean buttonFlag = false;
    public boolean modelLoaded = false;
    public Rasterizer myRasterizer;
    public Shader myShader;

    //TODO structure : probably better to use an entire array of lights. Improve the general structure.
    public Renderer(int screenWidth, int screenHeight)
    {
        width=screenWidth;
        height=screenHeight;
        colorBuffer = new int[width*height];
        pixelBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        myRasterizer= new Rasterizer(width, height);
    }
    public void loadModelData(Object3D modelObject)
    {
        System.out.println("FUNCTION CALL : model.renderer.loadModelData ");
        //loads vertex, normal, and texture coords in the order specified by faces
        this.modelObject=modelObject;
        vertexCoords = new Vec3f[modelObject.nFaces()][3];
        textureCoords = new Vec3f[modelObject.nFaces()][3];
        normalCoords = new Vec3f[modelObject.nFaces()][3];
        System.out.println("FACES : "+modelObject.nFaces());;
        for (int i = 0; i<modelObject.nFaces(); i++)
        {
            for (int j = 0; j<3;j++)
            {   //iterate through every face 3 times to get every index
                //indexV is a single index, representing one Vec3f object
                int indexV = modelObject.getVertexIndices(i)[j];
                vertexCoords[i][j] = VecOperator.mapToNDC(new Vec3f (modelObject.getVertexCoords(indexV)), modelObject.maxCoord) ;
                if (modelObject.nTextures()!=0)
                {
                    int indexT = modelObject.getTextureIndices(i)[j];
                    textureCoords[i][j] = new Vec3f(modelObject.getTexCoords(indexT));
                }
                if (modelObject.nNormals()!=0)
                {   //TODO bug fixing : transform normals in their own way
                    int indexN = modelObject.getNormalIndices(i)[j];
                    normalCoords[i][j] = new Vec3f(modelObject.getNormalCoords(indexN));
                }
            }
        }
        modelLoaded=true;
    }
    public void setShader(Shader yourShader)
    {
        myShader=yourShader;
        myRasterizer.setActiveShader(myShader);
    }
    public void renderModel()
    {
        //calls drawTriangle function on loaded model data
        //TODO error handling : assumes model data is loaded
        //TODO error handling : this function assumes nFaces() always matches coords size
        if (buttonFlag || !modelLoaded)
            return;
        System.out.println("FUNCTION CALL : model.renderer.rendererModel");
        for (int i = 0; i<modelObject.nFaces(); i++)
        {
            //future tip, don't change the coordinates values, only copy them, especially if you're updating every frame :)
            //deliver data
            //TODO structure : implement this as an optional?
            if (vertexCoords[i][0]==null||vertexCoords[i][1]==null||vertexCoords[i][2]==null)
            {
                return;
            }
            //deliver data.
            myRasterizer.rasterize(new Vec3f[]{vertexCoords[i][0], vertexCoords[i][1], vertexCoords[i][2],
                    normalCoords[i][0], normalCoords[i][1], normalCoords[i][2],
                    textureCoords[i][0], textureCoords[i][1], textureCoords[i][2]});
            copyRasterizer();
        }
    }
    private void copyRasterizer()
    {
        colorBuffer= myRasterizer.getPixelBuffer();
    }

    private void testTexture () throws IOException
    {
        ppmWriter myWriter = new ppmWriter(texWidth, texHeight);
        int[] colorBuffer = new int[texHeight*texWidth];
        for (int i = 0;i<texHeight*texWidth;i++)
        {
            colorBuffer[i]=textureData[i].getRGB();
        }
        myWriter.setTitle("TEXTURE_TEST");
        myWriter.writeToPPM(colorBufferToString(colorBuffer));
    }
    public void printBuffer()
    {
        pixelBuffer.setRGB(0,0, width, height, colorBuffer,0, width);
    }

    public int[] getColorBuffer() {
        return colorBuffer;
    }
    public BufferedImage getPixelBuffer(){return pixelBuffer;}
    public void resize(int newHeight, int newWidth)
    {
        System.out.println("FUNCTION CALL : model.renderer.resize()" + newHeight + " " + newWidth);
        height=newHeight;
        width=newWidth;
        colorBuffer= new int[height*width];
        pixelBuffer= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
}
