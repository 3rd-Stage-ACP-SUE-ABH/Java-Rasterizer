public abstract class Vector
{
    Vector(Number...params)
    {
        raw = new Number[params.length];
        System.arraycopy(params, 0, raw, 0, params.length);
    }
    public int getDimensions()
    {
        return raw.length;
    }
    @Override
    public String toString()
    {
        String returnValue = "";
        for (int i =0; i<this.getDimensions(); i++)
        {
            returnValue += raw[i]+" ";
        }
        return returnValue;
    }
    //operators

    //fields
    protected final Number[] raw;
    public Number x(){return raw[0];}
    public Number y(){return raw[1];}
    public Number z(){return raw[2];}
    public Number w(){return raw[3];}
}
class Pixel extends Vector
{
    Pixel()
    {
        super(0, 0 );
        raw[0] = raw[0].intValue();
        raw[1] = raw[1].intValue();
    }
    Pixel(int x, int y)
    {
        super(x, y);
        raw[0] = raw[0].intValue();
        raw[1] = raw[1].intValue();
    }
}
class ColorRGB extends Vector
{
    ColorRGB ()
    {
        super(0.0, 0.0, 0.0);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
    ColorRGB (float R, float G, float B)
    {
        super(R, G, B);
        raw[0]=raw[0].floatValue();
        raw[1]=raw[1].floatValue();
        raw[2]=raw[2].floatValue();
    }
}