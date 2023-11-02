package pipeline.programmable;

import math.Matrix;
import math.Vec3f;
import math.VecOperator;
import pipeline.fixed.Shader;
import pipeline.fixed.Transform;

import java.awt.*;

import static math.VecOperator.*;

public class PhongShader extends Shader
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
    {
        LightShader.addLight(new Light(new Vec3f(-1,0,0), Color.cyan));
        LightShader.ambient.lightColor=new Color(0.1f,0,0);
    //    LightShader.addLight(new Light(new Vec3f(0f,-0.8f,0.2f), new Color(0.5f,0.1f,0.35f)));
    }
    public PhongShader() {}
    private Vec3f[] normals = new Vec3f[3];
    @Override
    public void vertex(Vec3f[] objectData)
    {   //expects 3 vertex positions and 3 normals. This shader only works if the model has normal coordinates!
        for(int i = 0; i<3; i++)
        {
            objectData[i] = transformMatrix.transform(objectData[i]);
            normals[i] = transformMatrix.transformNormals(objectData[i+3]);
        }
    }
    @Override
    public Color fragment(Vec3f fragment, Vec3f bar)
    {
        float normalsX = interpolate(new Vec3f(normals[0].x(), normals[1].x(), normals[2].x()), bar);
        float normalsY = interpolate(new Vec3f(normals[0].y(), normals[1].y(), normals[2].y()), bar);
        float normalsZ = interpolate(new Vec3f(normals[0].z(), normals[1].z(), normals[2].z()), bar);
        Vec3f interpolatedNormal = new Vec3f(normalsX, normalsY, normalsZ);
        return LightShader.shadeChunky(interpolatedNormal, 5);
    }
}
