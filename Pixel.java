public class Pixel
{
    public  Pixel()
    {
        x=0;
        y=0;
    }
    public  Pixel(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    public  Pixel(float x, float y)
    {
        this.x=(int)x;
        this.y=(int)y;
    }
    public Pixel(Pixel p)
    {
        this.x = p.x;
        this.y = p.y;
    }
    @Override
    public String toString ()
    {
        return  (x + " " + y);
    }
    public static Pixel add(Pixel p0, Pixel p1)
    {
        return new Pixel(p0.x+p1.x, p0.y+ p1.y);
    }
    public static Pixel neg (Pixel p)
    {
        return new Pixel(-p.x, -p.y);
    }
    public Pixel neg ()
    {
        return new Pixel(-x,-y);
    }
    public static Pixel sub (Pixel p0, Pixel p1)
    {
        return new Pixel(p0.x - p1.x, p0.y - p1.y);
    }
    public static int dot (Pixel p0, Pixel p1)
    {
        return (p0.x * p1.x) + (p0.y * p1.y);
    }
    public static Pixel mul (Pixel p0, Pixel p1)
    {
        return new Pixel(p0.x*p1.x, p0.y*p1.y);
    }
    public static Pixel mul (Pixel p, int t)
    {
        return new Pixel(p.x*t, p.y*t);
    }
    public static Pixel mul (Pixel p, float t)
    {
        return new Pixel((p.x*t), (p.y*t));
    }
    public Pixel mul (int t)
    {
        return new Pixel(x*t, y*t);
    }
    public Pixel mul (float t)
    {
        return new Pixel(x*t, y*t);
    }
    public Pixel div(float t)
    {
        return new Pixel((float)x/t, (float)y/t);
    }
    public float magnitude ()
    {
        return (float)Math.sqrt(x*x+y*y);
    }
    public static Pixel getNormalized(Pixel p)
    {
        return new Pixel(p.div(p.magnitude()));
    }
    public final int x, y;
}
class Vec2i extends Pixel
{
    public Vec2i (int x, int y)
    {
        super(x,y);
    }
}