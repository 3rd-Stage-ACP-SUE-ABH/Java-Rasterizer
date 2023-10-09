package Vector;

public final class VecOperator
{
    private VecOperator()
    {

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
        return new Vec3f(u.y().floatValue()*v.z().floatValue() - u.z().floatValue()*v.y().floatValue(), u.z().floatValue()*v.x().floatValue()-u.x().floatValue()*v.z().floatValue(), u.x().floatValue()*v.y().floatValue()- u.y().floatValue()*v.x().floatValue());
    }
}