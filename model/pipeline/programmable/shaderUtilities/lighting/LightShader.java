package model.pipeline.programmable.shaderUtilities.lighting;

import model.math.Vec3f;
import model.pipeline.programmable.shaderUtilities.CommonTransformations;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static model.math.VecOperator.*;

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

    public static Color shade(Vec3f normal, Vec3f interpolatedPosition, float fragSpecStregnth, Color fragDiffuseColor) {
        surfaceNormal = normal;
        Color result = Color.black;
        for (int i = 0; i < diffuseLights.size(); i++) 
        {
            if(diffuseLights.get(i).position!=null)
            {
                diffuseLights.get(i).direction = minus(interpolatedPosition, diffuseLights.get(i).position);
            }
            result = sumColor(result, shadeDiffuseLight(diffuseLights.get(i)));
            result = mulColor(result, 0.75f);    //scale down diffuse
            result = sumColor(result, mulColor(diffuseLights.get(i).lightColor,
            shadeSpecLight(diffuseLights.get(i), fragSpecStregnth, interpolatedPosition)));
        }
        //multiply texture once and only once. Order matters ?
        result = mulColor(fragDiffuseColor, result);
        return sumColor(result, ambient.lightColor);
    }
    private static float shadeSpecLight (Light specular, float specStrength, Vec3f interpolatedPos)
    {
        Vec3f n = surfaceNormal.getNormalized();
        Vec3f l = specular.direction.getNormalized();
        Vec3f r = minus(l, n.scale(2.f*dot(l, n))).getNormalized();
        Vec3f v =  minus(CommonTransformations.camPos, interpolatedPos).getNormalized();
        float spec = max (r.z(), 0.f);
        spec = (float)pow(spec, specStrength);
        return spec;
    }

    private static Color shadeDiffuseLight(Light diffuse) {
        //returns diffuse color scaled by intensity according to angle with respect to normal
        float intensity = Math.max(dot(surfaceNormal.getNormalized(), diffuse.direction.getNormalized().neg()), 0.0f);
        return scaleColor(diffuse.lightColor, intensity);
    }
}
