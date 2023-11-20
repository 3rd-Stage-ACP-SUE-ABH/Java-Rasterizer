package model.pipeline.programmable.shaderUtilities.lighting;

import model.math.Vec3f;
import model.pipeline.programmable.shaderUtilities.CommonTransformations;
import java.awt.*;
import java.util.ArrayList;
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
    public static void addLight(Light yourLight) 
    {
        diffuseLights.add(yourLight);
    }
    private static boolean currentlyRendering = false;

    private static boolean hideDiffuse = false;
    private static boolean hideAmbient = false;
    private static boolean hideSpecular = false;
    private static boolean hideTexture = false;
    private static boolean shadeChunky = false;
    public static boolean specRdotZ = true;
    private static int numberOfChunks = 3;

    //public interface for settings 

    public static void setDefaults()
    {
        hideDiffuse = false;
        hideAmbient = false;
        hideSpecular = false;
        hideTexture = false;
        shadeChunky = false;
        specRdotZ = true;
    }
    public static void toggleChunky()
    {
        shadeChunky = !shadeChunky;
        if (shadeChunky == true)
            hideTexture = true;
    }
    public static void toggleDiffuse()
    {
        hideDiffuse = !hideDiffuse;
    }
    public static void toggleSpec()
    {
        hideSpecular = !hideSpecular;
    }
    public static void toggleAmbient()
    {
        hideAmbient = !hideAmbient;
    }
    public static void toggleTex()
    {
        hideTexture = !hideTexture;
    }
    public static void toggleSpecMode()
    {
        specRdotZ = !specRdotZ;
    }

    public static void clearLights()
    {
        if (!currentlyRendering)
            diffuseLights.clear();
    }
    public static Color shade(Vec3f normal, Vec3f interpolatedPosition, float fragSpecStregnth, Color fragDiffuseColor) 
    {
        currentlyRendering = true;
        surfaceNormal = normal;
        Color result = Color.black;
        for (int i = 0; i < diffuseLights.size(); i++) 
        {
            if(diffuseLights.get(i).position!=null)
            {
                diffuseLights.get(i).direction = minus(interpolatedPosition, diffuseLights.get(i).position);
            }
            if (shadeChunky)
            {   //TODO : this is repeating code

                float intensity = Math.max(dot(surfaceNormal.getNormalized(), diffuseLights.get(i).direction.getNormalized().neg()), 0.0f);
                float specIntensity = shadeSpecLight(diffuseLights.get(i), fragSpecStregnth, interpolatedPosition);
                intensity = map(0,1,0,numberOfChunks,intensity).floatValue();
                specIntensity = map(0,1, 0, numberOfChunks, specIntensity).floatValue();
                for (int j = 0; j < numberOfChunks; j++)
                {
                    if(Math.abs(j - intensity)<0.5)
                        intensity = j;
                    if(Math.abs(j - specIntensity)<0.5)
                        specIntensity = j;
                }
                intensity = map(0, numberOfChunks, 0, 1, intensity).floatValue();
                specIntensity = map(0, numberOfChunks, 0, 1, specIntensity).floatValue();
                result = hideDiffuse ? result : sumColor(result, scaleColor(diffuseLights.get(i).lightColor, intensity));
                result = hideSpecular ? result : sumColor(result, mulColor(diffuseLights.get(i).lightColor, specIntensity));
            }
            else 
            {
                result = hideDiffuse ? result : sumColor(result, shadeDiffuseLight(diffuseLights.get(i)));
                result = hideDiffuse ? result : mulColor(result, 0.75f);    //scale down diffuse
                result = hideSpecular ? result : sumColor(result, mulColor(diffuseLights.get(i).lightColor,
                shadeSpecLight(diffuseLights.get(i), fragSpecStregnth, interpolatedPosition)));
            }
        }
        //multiply texture once and only once. Order probably matters here.
        result = hideTexture ? result : mulColor(fragDiffuseColor, result);
        currentlyRendering = false;
        return hideAmbient ? result : sumColor(result, ambient.lightColor);
    }
    private static float shadeSpecLight (Light specular, float specStrength, Vec3f interpolatedPos)
    {
        Vec3f n = surfaceNormal.getNormalized();
        Vec3f l = specular.direction.getNormalized();
        Vec3f r = minus(l, n.scale(2.f*dot(l, n))).getNormalized();
        Vec3f v =  minus(CommonTransformations.camPos, interpolatedPos).getNormalized();
        float spec = max (specRdotZ ? r.z() : dot(r, v), 0.f);
        spec = (float)pow(spec, specStrength);
        return spec;
    }

    private static Color shadeDiffuseLight(Light diffuse) {
        //returns diffuse color scaled by intensity according to angle with respect to normal
        float intensity = Math.max(dot(surfaceNormal.getNormalized(), diffuse.direction.getNormalized().neg()), 0.0f);
        return scaleColor(diffuse.lightColor, intensity);
    }
}
