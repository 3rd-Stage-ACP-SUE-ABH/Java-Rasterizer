package controller.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import model.object3D.Object3D;
import model.math.*;
import model.pipeline.fixed.Rasterizer;
import model.pipeline.fixed.Shader;
//TODO structure : accommodate loading of multiple models
//TODO error handling : null handling

public class Renderer {
    BufferedImage pixelBuffer;
    // model data. stored such that index 0 corresponds to the 3 coordinates specified by the first face.
    Object3D modelObject;
    private Vec3f[][] vertexCoords;
    private Vec3f[][] textureCoords;
    private Vec3f[][] normalCoords;
    // render data
    public int width, height;
    public boolean buttonFlag = false;
    private boolean modelLoaded = false;
    private Rasterizer myRasterizer;
    private Shader myShader;

    public Renderer(int screenWidth, int screenHeight)
    {
        width=screenWidth;
        height=screenHeight;
        pixelBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        myRasterizer= new Rasterizer(width, height);
    }
    public void setShader(Shader yourShader)
    {
        myShader=yourShader;
        myRasterizer.setActiveShader(myShader);
    }
    public void clearDepthBuffer()
    {
        myRasterizer.clearDepthBuffer();
    }
    public void clearColorBuffer(Color clearColor)
    {
        myRasterizer.clearPixelBuffer(clearColor);
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
                {
                    int indexN = modelObject.getNormalIndices(i)[j];
                    normalCoords[i][j] = new Vec3f(modelObject.getNormalCoords(indexN));
                }
            }
        }
        modelLoaded=true;
    }
    public void renderModel()
    {
        //TODO error handling : this function assumes nFaces() always matches coords size
        if (buttonFlag || !modelLoaded)
            return;
        System.out.println("FUNCTION CALL : model.renderer.rendererModel");
        for (int i = 0; i<modelObject.nFaces(); i++)
        {
            //future tip, don't change the coordinates values, only copy them, especially if you're updating every frame :)
            //TODO structure : implement these as optionals ?
            if (vertexCoords[i][0]==null||vertexCoords[i][1]==null||vertexCoords[i][2]==null)
            {
                return;
            }
            //deliver data.
            myRasterizer.rasterize(
                    new Vec3f[]{
                    vertexCoords[i][0], vertexCoords[i][1], vertexCoords[i][2],
                    normalCoords[i][0], normalCoords[i][1], normalCoords[i][2],
                    textureCoords[i][0], textureCoords[i][1], textureCoords[i][2]}
                    );
        }
    }
    public void writePixelBuffer()
    {   //writes content of the virtual buffer inside the rasterizer to the buffered image.
        pixelBuffer.setRGB(0,0, width, height, myRasterizer.getPixelBuffer(),0, width);
    }
    public BufferedImage getPixelBuffer(){return pixelBuffer;}
}
