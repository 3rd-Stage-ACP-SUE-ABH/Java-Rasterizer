package renderer;
import math.*;

import java.awt.*;

public class Light {
    public Light()
    {
        pos = new Vec3f(0,0,0);
        direction = pos;
        lightColor = Color.black;
    }
    public Vec3f pos;
    public Vec3f direction;
    public Color lightColor;
}
