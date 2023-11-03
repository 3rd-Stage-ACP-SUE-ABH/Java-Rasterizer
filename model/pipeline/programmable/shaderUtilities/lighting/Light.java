package model.pipeline.programmable.shaderUtilities.lighting;

import model.math.Vec3f;

import java.awt.*;

public class Light
{
    public Vec3f direction;
    public Vec3f position;
    public Color lightColor;
    public Light()
    {
        direction=new Vec3f(0,0,0);
        lightColor = new Color(0,0,0);
        position = null;
    }
    public Light(Vec3f direction, Color color)
    {
        this.direction=direction;
        lightColor=color;
    }
}
