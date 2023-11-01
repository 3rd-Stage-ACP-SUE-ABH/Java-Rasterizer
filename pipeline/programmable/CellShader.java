package pipeline.programmable;

import math.Matrix;
import math.Vec3f;
import math.VecOperator;
import pipeline.fixed.Shader;
import pipeline.fixed.Transform;

import java.awt.*;

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
    public CellShader()
    {
    }
    @Override
    public void vertex(Vec3f[] objectData)
    {
        for(int i = 0; i<objectData.length; i++)
        {
            objectData[i] = transformMatrix.transform(objectData[i]);
        }
    }
    @Override
    public Color fragment()
    {
        return Color.PINK;
    }

}
