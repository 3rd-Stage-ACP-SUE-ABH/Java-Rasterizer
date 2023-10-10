package Renderer;

import java.awt.*;
import java.util.Arrays;
import Vector.*;
import static java.lang.Math.*;

public class Renderer {
    public Renderer(int screenWidth, int screenHeight)
    {
        width=screenWidth;
        height=screenHeight;
        colorBuffer = new int[width*height];
        fill(new Color(0.2f,0.2f,0.2f));
    }
    public Renderer(int screenWidth, int screenHeight, Color backgroundFill)
    {
        width=screenWidth;
        height=screenHeight;
        colorBuffer = new int[width*height];
        fill(backgroundFill);
    }
    //pixel manipulation
    public void setPixel(Pixel p, Color RGBcolor)
    {
        colorBuffer[p.y()*width+p.x()]= RGBcolor.getRGB();
    }
    public void drawLine (Pixel p0, Pixel p1, Color color)
    {
        int x0 = p0.x(), y0=p0.y(), x1=p1.x(), y1=p1.y();
        boolean steep = false;
        if (Math.abs(x0-x1)<Math.abs(y0-y1))    //if height>width
        {
            //transpose both points
            int temp = x0;
            x0=y0;
            y0=temp;
            temp = x1;
            x1=y1;
            y1=temp;
            steep = true;
        }
        if (x0>x1)  //swap the points if x0 is to the right of x1
        {
            int temp = x0;
            x0=x1;
            x1=temp;
            temp = y0;
            y0=y1;
            y1=temp;
        }
        int y = y0;
        int dy = y1-y0;
        int dx = x1-x0;
        int derror = Math.abs(2*dy);
        int error = 0;
        if (steep)
        {
            for (int x = x0; x<=x1; x++)
            {
                setPixel(new Pixel(y,x), color);
                error+=derror;
                if (error>dx)
                {
                    y+=y1>y0? 1:-1;
                    error -=2*dx;
                }
            }
        }
        else
        {
            for (int x = x0; x<=x1; x++)
            {
                setPixel(new Pixel(x,y), color);
                error+=derror;
                if (error>dx)
                {
                    y+=y1>y0? 1:-1;
                    error -=2*dx;
                }
            }
        }
    }
    public void drawTriangle (Pixel p0, Pixel p1, Pixel p2, Color color)
    {
        Pixel[] pixArr = {p0,p1,p2};
        int minX = width, minY = height;
        int maxX = 0, maxY = 0;
        for (int i =0;i<3;i++)
        {
            minX= max(0, min(minX, pixArr[i].x()));
            minY= max(0, min(minY, pixArr[i].x()));
            maxX = min(width, max(maxX, pixArr[i].x()));
            maxY = min(height, max(maxY, pixArr[i].y()));
        }
        int traversalX, traversalY;
        for (traversalY=minY; traversalY<maxY; traversalY++)
        {
            for (traversalX=minX; traversalX<maxX; traversalX++)
            {
                Pixel P = new Pixel(traversalX,traversalY);
                Vec3f test = barycentric(p0,p1,p2, P);
                if (test.x()<0||test.y()<0||test.z()<0)
                {
                    continue;
                }
                setPixel(P, color);
            }
        }
    }
    public Vec3f barycentric (Pixel p0, Pixel p1, Pixel p2, Pixel P)
    {
        Vec3f yIntervals = new Vec3f(p2.y()-p0.y(), p1.y()-p0.y(), p0.y()- P.y());
        Vec3f xIntervals = new Vec3f(p2.x()-p0.x(), p1.x()-p0.x(), p0.x()- P.x());
        Vec3f orthogonalVector = VecOperator.cross(yIntervals, xIntervals);
        if (abs(orthogonalVector.z())<1.f)
        {
            return new Vec3f(-1.f,1.f,1.f);
        }
        return new Vec3f(1.0f-((orthogonalVector.x()+orthogonalVector.y())/orthogonalVector.z()), orthogonalVector.y()/ orthogonalVector.z(), orthogonalVector.x()/ orthogonalVector.z());
    }
    public void fill (Color fillColor)
    {
        Arrays.fill(colorBuffer, fillColor.getRGB());
    }
    public int[] getColorBuffer() {
        return colorBuffer;
    }
    //fields
    public int width, height;
    public int[] colorBuffer;        //pixel buffer
}
