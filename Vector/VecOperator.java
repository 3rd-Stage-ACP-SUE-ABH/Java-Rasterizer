package Vector;

public final class VecOperator
{
    private VecOperator()
    {
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
}