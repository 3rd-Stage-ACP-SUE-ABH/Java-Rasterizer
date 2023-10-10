package Vector;

public class Vec3f extends Vector
{
    public Vec3f ()
    {
        super(0.0F, 0.0F, 0.0F);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
    public Vec3f (float x, float y, float z)
    {
        super(x, y, z);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
    public Float x(){return super.x().floatValue();}
    public Float y(){return super.y().floatValue();}
    public Float z(){return super.z().floatValue();}
}

