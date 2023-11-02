package pipeline.programmable;

import math.Vec3f;

import java.awt.*;
import java.util.ArrayList;

import static math.VecOperator.dot;
import static math.VecOperator.sumColor;

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
        return new Color((int) Math.min(intensity * diffuse.lightColor.getRed(), 255),
                (int) Math.min(intensity * diffuse.lightColor.getGreen(), 255),
                (int) Math.min(intensity * diffuse.lightColor.getBlue(), 255), 255);
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
