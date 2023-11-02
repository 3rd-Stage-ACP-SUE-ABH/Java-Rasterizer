package pipeline.programmable;

import math.Matrix;
import math.Vec3f;

import java.awt.*;
import java.util.ArrayList;

import static math.VecOperator.*;

public class LightShader {
    public static Vec3f surfaceNormal;
    private static ArrayList<Light> diffuseLights = new ArrayList<>();
    public static Light ambient = new Light();

    {
        ambient.direction = null;
    }

    public static void addLight(Light yourLight) {
        diffuseLights.add(yourLight);
    }

    public static void clearLights() {
        diffuseLights.clear();
    }

    private static Color shadeDiffuseLight(Light diffuse) {
        //returns diffuse color scaled by intensity according to angle with respect to normal
        float intensity = Math.max(dot(surfaceNormal.getNormalized(), diffuse.direction.getNormalized().neg()), 0.0f);
        return scaleColor(diffuse.lightColor, intensity);
    }
    public static Color shadeChunky(Vec3f surfaceNormal, int numberOfChunks)
    {
        Color result = Color.black;
        for (int i = 0; i < diffuseLights.size(); i++)
        {
            float intensity = Math.max(dot(surfaceNormal.getNormalized(), diffuseLights.get(i).direction.getNormalized().neg()), 0.0f);
            intensity = map(0,1,0,numberOfChunks,intensity).floatValue();
            for (int j = 0; j < numberOfChunks; j++)
            {
               if(Math.abs(j-intensity)<0.5)
                   intensity= j;
            }
            intensity = map(0,numberOfChunks, 0,1, intensity).floatValue();
            result = sumColor(result, scaleColor(diffuseLights.get(i).lightColor, intensity));
        }
        return sumColor(result, ambient.lightColor);
    }
    public static Color shade(Vec3f normal) {
        surfaceNormal = normal;
        Color result = Color.black;
        for (int i = 0; i < diffuseLights.size(); i++) {
            //sum every diffuse into result
            result = sumColor(result, shadeDiffuseLight(diffuseLights.get(i)));
        }
        return sumColor(result, ambient.lightColor);
    }
}
