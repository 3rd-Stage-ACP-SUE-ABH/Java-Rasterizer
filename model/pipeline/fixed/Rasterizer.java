package model.pipeline.fixed;

import model.math.Vec3f;
import model.math.VecOperator;
import model.pipeline.programmable.shaderUtilities.Transform;

import java.awt.*;
import java.util.Arrays;

import static java.lang.Math.*;


public class Rasterizer
{   //responsible for rasterizing triangles
    //this class is agnostic to the type of shader it calls
    public Rasterizer(int viewportWidth, int viewportHeight)
    {
        this.viewportWidth=viewportWidth;
        this.viewportHeight=viewportHeight;
        initDepthBuffer();
        initPixelBuffer();
    }
    public Shader activeShader;
    private int viewportWidth, viewportHeight;
    private int[] pixelBuffer;
    private float[] depthBuffer;

    public void clearDepthBuffer()
    {
        Arrays.fill(depthBuffer, -Float.MAX_VALUE);
    }
    public void clearPixelBuffer(Color clearColor)
    {
        Arrays.fill(pixelBuffer, clearColor.getRGB());
    }

    private void initDepthBuffer() 
    {
        depthBuffer = new float[viewportWidth*viewportHeight];
        clearDepthBuffer();
    }
    private void initPixelBuffer()
    {
        pixelBuffer= new int[viewportHeight*viewportHeight];
    }

    public int[] getPixelBuffer()
    {
        return pixelBuffer;
    }
    public void setActiveShader(Shader yourShader)
    {
        activeShader=yourShader;
    }
    private Vec3f[] viewportTransform(Vec3f[] rawData)
    {
        Transform myTransform = new Transform();
        //in real applications this should map to the larger of the two
        int scale = Math.min(viewportWidth, viewportHeight);
        myTransform.addTransform(VecOperator.viewportTransform(0,0, scale, scale));
        //TODO misc : possible inefficiency by making two distinct arrays.
        Vec3f[] processedData= new Vec3f[rawData.length];
        for (int i = 0; i<rawData.length; i++)
            processedData[i]=myTransform.transform(rawData[i]);
        return processedData;
    }
    public void rasterize(Vec3f[] triangleCoords)
    //this function rasterizes triangles by setting pixels in a pixel buffer. It expects raw 3D triangle data in NDC range.
    {
        //call vertex shader on raw data. Processing happens here.
        activeShader.vertex(triangleCoords);

        //from here downward triangleCoords should be specified in screen space.
        Vec3f[] screenSpaceCoords = viewportTransform(new Vec3f[]{triangleCoords[0], triangleCoords[1], triangleCoords[2]});

        //define bounding box of triangle
        float minX = viewportWidth-1, minY = viewportHeight-1;
        float maxX = 0, maxY = 0;
        for (int i =0;i<3;i++)
        {
            minX= max(0, min(minX, screenSpaceCoords[i].x()));
            minY= max(0, min(minY, screenSpaceCoords[i].y()));
            maxX = min(viewportWidth-1, max(maxX, screenSpaceCoords[i].x()));
            maxY = min(viewportHeight-1, max(maxY, screenSpaceCoords[i].y()));
        }
        //test every pixel in the bounding box and render those which are inside the triangle.
        Vec3f P;
        int traversalX, traversalY;
        for (traversalX=(int)minX; traversalX<maxX; traversalX++)
        {
            for (traversalY=(int) minY; traversalY<maxY; traversalY++)
            {
                P = new Vec3f(traversalX, traversalY, 0.0f);
                Vec3f test = VecOperator.getBarycentricCoords(screenSpaceCoords[0], screenSpaceCoords[1], screenSpaceCoords[2], P);
                //first test: is this pixel inside the triangle?
                if (test.x()<0||test.y()<0||test.z()<0)
                    continue;
                P.setDepth(VecOperator.interpolate(new Vec3f(triangleCoords[0].z(), triangleCoords[1].z(), triangleCoords[2].z()), test));
                //second test : is this pixel behind another pixel?
                if(P.z()<depthBuffer[(int)(P.x()+P.y()*viewportWidth)])
                    continue;
                depthBuffer[(int)(P.x()+P.y()*viewportWidth)]=P.z();
                Color fragmentColor = activeShader.fragment(P, test);
                //third test: is this pixel discarded by the shader?
                if (fragmentColor==null)
                    continue;
                pixelBuffer[P.x().intValue()+viewportWidth*(viewportHeight-P.y().intValue()-1)]=fragmentColor.getRGB();
            }
        }
    }
}
