package Vector;

public class ColorRGB extends Vector
{
    public ColorRGB ()
    {
        super(0.0F, 0.0F, 0.0F);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
    public ColorRGB (float R, float G, float B)
    {
        super(R, G, B);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
    //field
    public Number R(){return super.x().floatValue();}
    public Number G(){return super.y().floatValue();}
    public Number B(){return super.z().floatValue();}
}
