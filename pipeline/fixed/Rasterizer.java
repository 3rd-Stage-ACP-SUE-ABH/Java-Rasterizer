package pipeline.fixed;

import math.Pixel;
import math.Vec3f;
import math.VecOperator;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.*;

public class Rasterizer
{   //responsible for rasterizing triangles
    //this class is agnostic to the type of shader it calls
    public Rasterizer(int viewportWidth, int viewportHeight)
    {
        setDimensions(viewportWidth, viewportHeight);
    }
    private Shader activeShader;
    private int[] pixelBuffer;
    private int viewportWidth, viewportHeight;
    public int[] getPixelBuffer()
    {
        return pixelBuffer;
    }
    private void setDimensions(int viewportWidth, int viewportHeight)
    {
        this.viewportWidth=viewportWidth;
        this.viewportHeight=viewportHeight;
        pixelBuffer= new int[viewportHeight*viewportHeight];
    }
    public void setActiveShader(Shader yourShader)
    {
        activeShader=yourShader;
    }
    private Vec3f getBarycentricCoords(Vec3f A, Vec3f B, Vec3f C, Vec3f P)
    //should probably move this function to VecOperator
    {   //this function is a bit of a black box
        Vec3f[] intervals = new Vec3f[2];
        intervals[0]=new Vec3f(C.x()-A.x(), B.x()-A.x(), A.x()-P.x());
        intervals[1]=new Vec3f(C.y()-A.y(), B.y()-A.y(), A.y()-P.y());
        Vec3f orthogonalVector = VecOperator.cross(intervals[0], intervals[1]);

        if (abs(orthogonalVector.z())>1e-2)
        {
            return new Vec3f(1.0f-((orthogonalVector.x()+orthogonalVector.y())/orthogonalVector.z()),
                    orthogonalVector.y()/ orthogonalVector.z(), orthogonalVector.x()/ orthogonalVector.z());
        }
        //return this if triangle is degenerate
        return new Vec3f(-1.f,1.f,1.f);
    }
    private Vec3f[] viewportTransform(Vec3f[] rawData)
    {
        Transform myTransform = new Transform();
        //in real applications this should map to the larger of the two
        int scale = Math.min(viewportWidth, viewportHeight);
        myTransform.addTransform(VecOperator.viewport(0,0, scale, scale));
        //TODO misc : possible inefficiency by making two distinct arrays.
        Vec3f[] processedData= new Vec3f[rawData.length];
        for (int i = 0; i<rawData.length; i++)
            processedData[i]=myTransform.transform(rawData[i]);
        return processedData;
    }
    public void rasterize(Vec3f[] triangleCoords)
    //this function rasterizes triangles by setting pixels in a pixel buffer. It expects raw NDCs.
    {
        //call vertex shader on raw data. Processing happens here.
        activeShader.vertex(triangleCoords);

        //from here downward triangleCoords should be specified in screen space.

        Vec3f[] screenSpaceCoords= viewportTransform(triangleCoords);

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
                Vec3f test = getBarycentricCoords(screenSpaceCoords[0], screenSpaceCoords[1], screenSpaceCoords[2], P);
                //first test, is this pixel inside the triangle?
                if (test.x()<0||test.y()<0||test.z()<0)
                    continue;
                Color fragmentColor = activeShader.fragment();
                //second test, is this pixel discarded?
                if (fragmentColor==null)
                    continue;
                pixelBuffer[P.x().intValue()+viewportWidth*(viewportHeight-P.y().intValue()-1)]=fragmentColor.getRGB();
            }
        }
    }
}
