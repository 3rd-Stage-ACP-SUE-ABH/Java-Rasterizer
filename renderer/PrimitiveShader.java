package renderer;

import math.Vec3f;

import java.awt.*;
import java.util.ArrayList;

import static math.VecOperator.dot;
import static math.VecOperator.sumColor;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class PrimitiveShader
{
    //this class will serve as a primitive shader until we implement a more permanent one
    private PrimitiveShader()
    {
    }
    private static Vec3f surfaceNormal;
    private static ArrayList<Light> diffuseDirectionalLights = new ArrayList<>();
    public static Light ambient = new Light();
    public static Color shade(Vec3f normal)
    {
        surfaceNormal = normal;
        Color result = Color.black;
        for (int i = 0; i<diffuseDirectionalLights.size(); i++)
        {
            //sum every diffuse into result
            result = sumColor(result, diffuseByIntensity(diffuseDirectionalLights.get(i)));
        }
        return sumColor(result, ambient.lightColor);
    }
    public static void addLight (Light diffuseLight)
    {
        diffuseDirectionalLights.add(diffuseLight);
    }
    public static void clearLights()
    {
        diffuseDirectionalLights.clear();
        ambient=new Light();
    }
    private static Color diffuseByIntensity (Light diffuse)
    {
        //returns diffuse color scaled by intensity according to angle with respect to normal
        float intensity =max(dot(surfaceNormal.getNormalized(), diffuse.direction.getNormalized().neg()), 0.0f);
        return new Color((int) min(intensity*diffuse.lightColor.getRed(), 255),
                (int) min(intensity*diffuse.lightColor.getGreen(), 255),
                (int) min(intensity*diffuse.lightColor.getBlue(), 255), 255);
    }
}
