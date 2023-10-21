package Vector;

public class Vec3f extends Vector
{
    public void setDepth(float depth)
    {
        raw[2]=depth;
    }
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
    public Vec3f (float[] data)
    {      //TODO assigning raw values to their type here is probably redundant
        super(data[0], data[1], data[2]);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
    @Override
    public Vec3f getNormalized() {
        return new Vec3f(getNormalizedCoords()[0], getNormalizedCoords()[1], getNormalizedCoords()[2]);
    }
    @Override
    public Vec3f neg()
    {
        return new Vec3f(-x(), -y(), -z());
    }
    public Float x(){return super.x().floatValue();}
    public Float y(){return super.y().floatValue();}
    public Float z(){return super.z().floatValue();}
}

