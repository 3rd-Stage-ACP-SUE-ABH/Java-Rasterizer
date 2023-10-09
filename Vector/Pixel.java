package Vector;

public class Pixel extends Vector
{
    public Pixel()
    {
        super(0, 0 );
        raw[0] = raw[0].intValue();
        raw[1] = raw[1].intValue();
    }
    public Pixel(int x, int y)
    {
        super(x, y);
        raw[0] = raw[0].intValue();
        raw[1] = raw[1].intValue();
    }
    //field
    public Number x(){return super.x().intValue();}
    public Number y(){return super.y().intValue();}
    public Number z(){return super.z().intValue();}
}
