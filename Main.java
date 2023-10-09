import Vector.ColorRGB;
import Vector.Pixel;
import static Vector.VecOperator.*;
public class Main {
    public static void main(String[] args) {
        Pixel p0 = new Pixel(3, 4);
        Pixel p1 = new Pixel(0, 1);
        ColorRGB red = new ColorRGB(1.0F, 0.0F,0.0F);
        System.out.println(dot(p0, red));
    }
}