package model.pipeline.programmable;

import model.math.Matrix;
import model.math.Vec3f;
import model.math.VecOperator;
import model.pipeline.fixed.Shader;
import model.pipeline.programmable.shaderUtilities.Transform;
import model.pipeline.programmable.shaderUtilities.lighting.Light;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;

import java.awt.*;

import static model.math.VecOperator.*;

public class FlatShader extends Shader
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
        LightShader.addLight(new Light(new Vec3f(-1,0,0), Color.WHITE));
    }
    private Vec3f calculateNormal(Vec3f[] vertices)
    {
        return cross(minus(vertices[1], vertices[0]), minus(vertices[2], vertices[0])).getNormalized();
    }
    private Vec3f normal;
    public FlatShader() {}
    @Override
    public void vertex(Vec3f[] objectData)
    {
        for(int i = 0; i<3; i++)
        {
            objectData[i] = transformMatrix.transform(objectData[i]);
        }
        normal = calculateNormal(objectData);
    }
    @Override
    public Color fragment(Vec3f fragment, Vec3f bar)
    {
        return sumColor(LightShader.shade(normal, null), new Color(50,0,0));
    }

}
