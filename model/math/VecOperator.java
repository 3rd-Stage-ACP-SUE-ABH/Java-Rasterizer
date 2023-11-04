package model.math;

import java.awt.*;

import static java.lang.Math.*;

public final class VecOperator
{
    private VecOperator()
    {
    }
    public static Color mulColor (Color u, Color v)
    {   //returns color equal to scaling u by v, clipping to 255
        return new Color( (int)(min(u.getRed()*(float)v.getRed()/255, 255))
                ,(int) (min(u.getGreen()*(float)v.getGreen()/255, 255)),
                (int) (min(u.getBlue()*(float)v.getBlue()/255, 255)));
    }
    public static Color sumColor (Color u, Color v)
    {   //returns plain sum clipping to 255
        return new Color(min (u.getRed()+v.getRed(), 255), min(u.getGreen()+v.getGreen(), 255), min(u.getBlue()+v.getBlue(), 255));
    }
    public static Vec3f minus (Vec3f u, Vec3f v)
    {   //non ideal implementation
        return new Vec3f(u.x()-v.x(), u.y()-v.y(), u.z()-v.z());
    }
    public static float dot (Vec3f u, Vec3f v)
    {
        return u.x()*v.x()+u.y()*v.y()+u.z()*v.z();
    }
    public static  <V extends Vector> float dot (V u, V v)
    {
        float sum = 0;
        int minDimensions = Math.min(u.getDimensions(), v.getDimensions());
        for (int i=0;i<minDimensions;i++)
        {
            sum+=u.raw[i].floatValue()*v.raw[i].floatValue();
        }
        return sum;
    }
    public static Vec3f cross (Vec3f u, Vec3f v)
    {
        return new Vec3f(u.y() * v.z() - u.z() * v.y(), u.z() * v.x() - u.x() * v.z(), u.x() * v.y() - u.y() * v.x());
    }
    public static Pixel transpose(Pixel p)
    {
       return new Pixel(p.y(), p.x());
    }


    //matrix
    public static Matrix mul(Matrix l, Matrix r)
    {   //TODO error handling : throw error here
        if (l.nColumns()!=r.nRows())
        {
            //throw error
            System.out.println("Error : incompatible matrices");
        }
        Matrix resultMatrix = new Matrix(l.nRows(), r.nColumns());
        for (int i = 0; i< l.nRows(); i++)
        {
            for (int j = 0; j<r.nColumns();j++)
            {   //init value with 0
                float sum = 0;
                //add dot product of left row and right column
                for (int k = 0; k<l.nColumns();k++)
                {
                    sum += l.getElement(i, k)*r.getElement(k,j);
                }
                resultMatrix.setElement(i, j, sum);
            }
        }
        return resultMatrix;
    }
    public static Matrix modelOffset (Vec3f offset)
    {
        Matrix translation = Matrix.getIdentityMatrix(4);
        translation.setElement(0,3,offset.x());
        translation.setElement(1,3,offset.y());
        translation.setElement(2,3,offset.z());
        return translation;
    }
    public static Matrix yRotationMatrix (float angle)
    {   //this is the model transform
        Matrix rotation = Matrix.getIdentityMatrix(4);
        rotation.setElement(0,0, (float)cos(angle));
        rotation.setElement(0,2, (float)sin(angle));
        rotation.setElement(2,0, (float)-sin(angle));
        rotation.setElement(2,2, (float)cos(angle));
        return rotation;
    }
    public static float rotationAngle = 0;
    public static Matrix lookAt(Vec3f eye, Vec3f center, Vec3f up)
    {   //this view transform is a bit of a mess and needs some work.
        //we need to make a camera class.
        Vec3f z = minus(eye, center).getNormalized();
        Vec3f x = cross(up, z).getNormalized();
        Vec3f y = cross(z, x).getNormalized();
        Matrix minv = Matrix.getIdentityMatrix(4);
        Matrix tr = Matrix.getIdentityMatrix(4);
        for (int i = 0; i<3; i++)
        {
            minv.setElement(0, i, x.raw[i].floatValue());
            minv.setElement(1, i, y.raw[i].floatValue());
            minv.setElement(2, i, z.raw[i].floatValue());
            tr.setElement(i, 3, -center.raw[i].floatValue());
        }
        //rotate the object before lookAt
        Matrix lookAt = mul(minv, tr);
        return lookAt;
    }
    public static int depthResolution = 255;
    //this depth resolution conveniently allows us to view the depth map without doing any mappings
    public static Matrix mapToNDC (float maxCoords)
    {   //maps object data from range [-maxCoords, maxCoords] into normalized device coordinate range [-1, 1]
        Matrix resultMatrix = Matrix.getIdentityMatrix(4);

        resultMatrix.setElement(0,0, 1/maxCoords);
        resultMatrix.setElement(1,1, 1/maxCoords);
        resultMatrix.setElement(2,2, 1/maxCoords);

        return resultMatrix;
    }
    public static Matrix viewportTransform (int horizontalPXOffset, int verticalPXOffset, int w, int h)
    {   //maps NDCs to viewport of specified width and height and with specified offset
        Matrix resultMatrix = Matrix.getIdentityMatrix(4);

        resultMatrix.setElement(0,3, horizontalPXOffset + w/2.f);
        resultMatrix.setElement(1,3, verticalPXOffset + h/2.f);
        resultMatrix.setElement(2,3, depthResolution /2.f);

        resultMatrix.setElement(0,0, w/2);
        resultMatrix.setElement(1,1, h/2);
        resultMatrix.setElement(2,2, depthResolution /2);

        return resultMatrix;
    }
    public static Vec3f getBarycentricCoords(Vec3f A, Vec3f B, Vec3f C, Vec3f P)
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
    public static float interpolate(Vec3f data, Vec3f barycentricCoords)
    {   //returns interpolated value for a point P inside a triangle from its vertex data using P's barycentric coordinates
        return dot(data, barycentricCoords);
    }
    public static Color scaleColor(Color color, float scalar)
    {
        return new Color((int) min(255, max(0.0, (color.getRed()*scalar))),
                (int) min(255, max(0.0, (color.getGreen()*scalar))),
                (int) min(255, max(0.0, (color.getBlue()*scalar))));
    }
    //below functions have been temporarily relocated here
    public static  <N extends Number> Double map (N srcMin, N srcMax, N destMin, N destMax, N value)
    {
        //maps a Number from range [srcMin, srcMax] to [destMin, destMAX]

        //it's always safe to cast float or int to double then cast back
        //mapping value to [0, 1]
        double ratio = (value.doubleValue()-srcMin.doubleValue())/(srcMax.doubleValue()-srcMin.doubleValue());
        //TODO error handling: what if value is outside the bounds? what if max-min==0?
        //TODO structure : write map() as a functional interface for stream maps
        return ((destMax.doubleValue() * ratio) + ((1 - ratio) * destMin.doubleValue()));
    }
    public static Vec3f mapToNDC(Vec3f u, float maxCoordinateSize)
    {
        Matrix transformMatrix = new Matrix(u, true);
        transformMatrix = mul(VecOperator.mapToNDC(maxCoordinateSize), transformMatrix);
        return new Vec3f(transformMatrix);
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
}