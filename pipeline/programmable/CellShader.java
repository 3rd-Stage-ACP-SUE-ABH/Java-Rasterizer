package pipeline.programmable;

import math.Matrix;
import math.Vec3f;
import math.VecOperator;
import pipeline.fixed.Shader;
import pipeline.fixed.Transform;

import java.awt.*;
import java.util.ArrayList;
import static math.VecOperator.*;

public class CellShader extends Shader
{
    public Vec3f camPos = new Vec3f(0,0,2);
    public Vec3f lookAt = new Vec3f(0,0,0);
    public Vec3f cameraUp = new Vec3f(0,1,0);
    public float rotationAngle = 0;
    private Matrix modelTransform = VecOperator.yRotationMatrix(rotationAngle);
    private Matrix viewTransform = VecOperator.lookAt(camPos, lookAt, cameraUp);
    private Matrix projectionTransform = Matrix.getIdentityMatrix(4);
    {
        projectionTransform.setElement(3,2, -1.f/camPos.z());
    }
    private Transform transformMatrix = new Transform();
    {
        transformMatrix.addTransform(modelTransform);
        transformMatrix.addTransform(viewTransform);
        transformMatrix.addTransform(projectionTransform);
    }
    public void updateMatrices()
    {
        modelTransform = VecOperator.yRotationMatrix(rotationAngle);
        viewTransform = VecOperator.lookAt(camPos, lookAt, cameraUp);
        projectionTransform.setElement(3,2, -1.f/camPos.z());

        transformMatrix.clear();
        transformMatrix.addTransform(modelTransform);
        transformMatrix.addTransform(viewTransform);
        transformMatrix.addTransform(projectionTransform);
    }
    public static class Light
    {
        public Vec3f direction;
        public Color lightColor;
        public Light()
        {
            direction=new Vec3f(0,0,0);
            lightColor = new Color(0,0,0);
        }
        public Light(Vec3f direction, Color color)
        {
            this.direction=direction;
            lightColor=color;
        }
    }
    public static class LightShader
    {
        public static Vec3f surfaceNormal;
        private static ArrayList<Light> diffuseLights = new ArrayList<>();
        public static Light ambient = new Light();
        {
            ambient.direction=null;
        }
        public static void addLight(Light yourLight)
        {
            diffuseLights.add(yourLight);
        }
        public static void clearLights(){diffuseLights.clear();}
        private static Color shadeDiffuseLight(Light diffuse)
        {
            //returns diffuse color scaled by intensity according to angle with respect to normal
            float intensity =Math.max(dot(surfaceNormal.getNormalized(), diffuse.direction.getNormalized().neg()), 0.0f);
            return new Color((int) Math.min(intensity*diffuse.lightColor.getRed(), 255),
                    (int) Math.min(intensity*diffuse.lightColor.getGreen(), 255),
                    (int) Math.min(intensity*diffuse.lightColor.getBlue(), 255), 255);
        }
        public static Color shade(Vec3f normal)
        {
            surfaceNormal = normal;
            Color result = Color.black;
            for (int i = 0; i<diffuseLights.size(); i++)
            {
                //sum every diffuse into result
                result = sumColor(result, shadeDiffuseLight(diffuseLights.get(i)));
            }
            return sumColor(result, ambient.lightColor);
        }
    }
    {
        LightShader.addLight(new Light(new Vec3f(-1,0,0), Color.WHITE));
    }
    private Vec3f calculateNormal(Vec3f[] vertices)
    {
        return cross(minus(vertices[1], vertices[0]), minus(vertices[2], vertices[0])).getNormalized();
    }
    private Vec3f normal;
    public CellShader() {}
    @Override
    public void vertex(Vec3f[] objectData)
    {
        for(int i = 0; i<objectData.length; i++)
        {
            objectData[i] = transformMatrix.transform(objectData[i]);
        }
        normal = calculateNormal(objectData);
    }
    @Override
    public Color fragment()
    {
        return sumColor(LightShader.shade(normal), new Color(50,0,0));
    }

}
