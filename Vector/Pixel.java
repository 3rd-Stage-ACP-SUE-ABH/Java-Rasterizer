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
    public Integer x(){return super.x().intValue();}
    public Integer y(){return super.y().intValue();}
    public Integer z(){return super.z().intValue();}
}
