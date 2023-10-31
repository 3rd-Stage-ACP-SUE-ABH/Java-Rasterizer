package math;

import java.awt.*;

import static java.lang.Math.*;

public final class VecOperator
{
    private VecOperator()
    {
    }
    public static Color mulColor (Color u, Color v)
    {   //returns color equal to scaling u by v, clipping to 255
        return new Color( min(u.getRed()*v.getRed()/255, 255),  min(u.getGreen()*v.getGreen()/255, 255), min(u.getBlue()*v.getBlue()/255, 255));
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
    public static Matrix yRotationMatrix (float angle)
    {
        Matrix rotation = Matrix.getIdentityMatrix(4);
        rotation.setElement(0,0, (float)cos(angle));
        rotation.setElement(0,2, (float)sin(angle));
        rotation.setElement(2,0, (float)-sin(angle));
        rotation.setElement(2,2, (float)cos(angle));
        return rotation;
    }
    public static float rotationAngle = 0;
    public static Matrix lookAt(Vec3f eye, Vec3f center, Vec3f up)
    {
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
        return mul(lookAt, yRotationMatrix(rotationAngle));
    }
    public static int depth = 255;
    public static Matrix mapToNDC(float maxCoords)
    {
        Matrix resultMatrix = Matrix.getIdentityMatrix(4);

        resultMatrix.setElement(0,0, 1/maxCoords);
        resultMatrix.setElement(1,1, 1/maxCoords);
        resultMatrix.setElement(2,2, 1/maxCoords);

        return resultMatrix;
    }
    public static Matrix viewport (int horizontalPXOffset, int verticalPXOffset, int w, int h)
    {
        Matrix resultMatrix = Matrix.getIdentityMatrix(4);

        resultMatrix.setElement(0,3, horizontalPXOffset + w/2.f);
        resultMatrix.setElement(1,3, verticalPXOffset + h/2.f);
        resultMatrix.setElement(2,3, depth/2.f);

        resultMatrix.setElement(0,0, w/2);
        resultMatrix.setElement(1,1, h/2);
        resultMatrix.setElement(2,2, depth/2);

        return resultMatrix;
    }
}