package Renderer;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import Vector.*;
import static java.lang.Math.*;
import static Vector.VecOperator.*;
import ppmWriter.*;
public class Renderer {
    //TODO probably better to use an entire array of lights. Improve the general structure.
    public Color[] textureData;
    public int texHeight, texWidth;
    public Light diffuse = new Light();
    public Light ambient = new Light();
    {

        diffuse.lightColor= Color.white;
        diffuse.direction = new Vec3f(0.5f,0.0f,0.3f);
        ambient.lightColor = new Color(0.3f,0.15f,0.2f);
    }
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
    public void testTexture () throws IOException
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
    public void setPixel(Pixel p, Color RGBcolor)
    {   // max range for p.x and p.y is width-1 and height-1 respectively
        colorBuffer[(height-p.y()-1)*width+p.x()]= RGBcolor.getRGB();
    }
    public void drawLine (Pixel p0, Pixel p1, Color color)
    {   // max range for p.x and p.y is width-1 and height-1 respectively
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
    public Color diffuseDirectional (Vec3f normal)
    {
        float intensity =max(dot(normal.getNormalized(), diffuse.direction.getNormalized().neg()), 0.0f);
        return new Color((int) min(intensity*diffuse.lightColor.getRed(), 255),
                (int) min(intensity*diffuse.lightColor.getGreen(), 255),
                (int) min(intensity*diffuse.lightColor.getBlue(), 255), 255);
    }
    private Vec3f barycentric (Vec3f A, Vec3f B, Vec3f C, Vec3f P)
    {
        Vec3f[] intervals = new Vec3f[2];
        intervals[0]=new Vec3f(C.x()-A.x(), B.x()-A.x(), A.x()-P.x());
        intervals[1]=new Vec3f(C.y()-A.y(), B.y()-A.y(), A.y()-P.y());
        Vec3f orthogonalVector = VecOperator.cross(intervals[0], intervals[1]);

        if (abs(orthogonalVector.z())>1e-2)
        {
            return new Vec3f(1.0f-((orthogonalVector.x()+orthogonalVector.y())/orthogonalVector.z()), orthogonalVector.y()/ orthogonalVector.z(), orthogonalVector.x()/ orthogonalVector.z());
        }
        //not returning this
        return new Vec3f(-1.f,1.f,1.f);
    }
    private float interpolate(float[]pts, Vec3f barycentric,float value)
    {//interpolates value between 3 points. Assumes value = 0 at start
        float[] screen = new float[]{barycentric.x(),barycentric.y(), barycentric.z()};
        for (int i = 0; i<pts.length; i++)
        {
            value+=pts[i]*screen[i];
        }
        return  value;
    }
    public void drawTriangle (Vec3f[] pts, Vec3f[] texPts,float[] depthBuffer,Color color)
    {
        float minX = width-1, minY = height-1;
        float maxX = 0, maxY = 0;
        for (int i =0;i<3;i++)
        {   //TODO TEST (probably works)
            minX= max(0, min(minX, pts[i].x()));
            minY= max(0, min(minY, pts[i].y()));
            maxX = min(width-1, max(maxX, pts[i].x()));
            maxY = min(height-1, max(maxY, pts[i].y()));
        }
        Vec3f P;
        int traversalX, traversalY;
        for (traversalX=(int)minX; traversalX<maxX; traversalX++)
        {
            for (traversalY=(int) minY; traversalY<maxY; traversalY++)
            {
                P = new Vec3f(traversalX, traversalY, 0.0f);
                Vec3f test = barycentric(pts[0], pts[1], pts[2], P);
                if (test.x()<0||test.y()<0||test.z()<0)
                {
                    continue;
                }
                float sum = 0;
                //interpolate sum between the 3 Z coordinates of the face
                sum = interpolate(new float[]{pts[0].z(), pts[1].z(), pts[2].z()}, test, sum);
                P.setDepth(sum);
                //interpolate texture X Y coordinate
                //TODO the texture only works when I map() the interpolated coordinates, why?
                float texX = 0, texY = 0;
                texX = interpolate(new float[]{texPts[0].x(), texPts[1].x(), texPts[2].x()}, test, texX);
                texY = interpolate(new float[]{texPts[0].y(), texPts[1].y(), texPts[2].y()}, test, texY);
                Pixel fragmentScreenSpace = new Pixel(map(0,1,0,texWidth, texX).intValue(),
                        map(0,1,0,texHeight, texY).intValue() );
                Color textureColor = textureData[fragmentScreenSpace.x()+fragmentScreenSpace.y()*texWidth];
                if (depthBuffer[(int)(P.x()+P.y()*width)]<P.z())
                {
                    depthBuffer[(int)(P.x()+P.y()*width)]=P.z();
                    setPixel(new Pixel(P.x().intValue(), P.y().intValue()), VecOperator.coloMul(textureColor, color));
                }
            }
        }
    }
    public void fill (Color fillColor)
    {
        Arrays.fill(colorBuffer, fillColor.getRGB());
    }
    public int[] getColorBuffer() {
        return colorBuffer;
    }
    public static  <N extends Number> Double map (N srcMin, N srcMax, N destMin, N destMax, N value)
    {   //maps a Number from range [srcMin, srcMax] to [destMin, destMAX]

        //it's always safe to cast float or int to double then cast back
        //mapping value to [0, 1]
        double ratio = (value.doubleValue()-srcMin.doubleValue())/(srcMax.doubleValue()-srcMin.doubleValue());
        //TODO error handling: what if value is outside the bounds? what if max-min==0?
        return ((destMax.doubleValue() * ratio) + ((1 - ratio) * destMin.doubleValue()));
    }
    public static String colorBufferToString(int[] colorBuffer) {
        StringBuilder bufferData=new StringBuilder();
        for (int i =0; i<colorBuffer.length;i++)
        {
            Color tempColor=new Color(colorBuffer[i]);
            //concatting strings is extremely slow, since each time we concat the string has to be copied, meaning
            //we are copying the same string hundreds of thousands of times.
            //for building strings in for loops, use string builder or string buffer.
            bufferData.append(tempColor.getRed()).append(" ").append(tempColor.getGreen()).append(" ").append(tempColor.getBlue()).append("\n");
        }
        return bufferData.toString();
    }
    //fields
    public int width, height;
    public int[] colorBuffer;        //pixel buffer
}
