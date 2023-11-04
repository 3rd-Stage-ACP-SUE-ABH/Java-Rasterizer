package model.pipeline.programmable.shaderUtilities;

import model.math.Matrix;
import model.math.Vec3f;
import model.math.VecOperator;

import static model.math.VecOperator.mul;

public final class CommonTransformations
{
    private CommonTransformations(){}
    //camera parameters
    public static Vec3f camPos = new Vec3f(10,0,5);
    public static Vec3f lookAt = new Vec3f(0,0,0);
    public static Vec3f cameraUp = new Vec3f(0,1.f,0);
    //model parameters
    public static float rotationAngle = 0;
    public static Vec3f offset = new Vec3f(0.0f,0.f,0.f);

    private static Matrix modelTransform = new Matrix();
    private static Matrix viewTransform = VecOperator.lookAt(camPos, lookAt, cameraUp);
    private static Matrix projectionTransform = Matrix.getIdentityMatrix(4);
    private static Transform transformMatrix = new Transform();
    {
        transformMatrix.addTransform(modelTransform);
        transformMatrix.addTransform(viewTransform);
        transformMatrix.addTransform(projectionTransform);
    }

    public static Vec3f applyTransform (Vec3f data)
    {
       return transformMatrix.transform(data);
    }
    public static Vec3f applyNormalTransform (Vec3f data)
    {
        return transformMatrix.transformNormals(data);
    }
    public static void updateMatrices()
    {
        modelTransform = mul( VecOperator.modelOffset(offset) ,VecOperator.yRotationMatrix(rotationAngle));
        viewTransform = VecOperator.lookAt(camPos, lookAt, cameraUp);
        projectionTransform.setElement(3,2, -1.f/camPos.z());

        transformMatrix.clear();
        transformMatrix.addTransform(modelTransform);
        transformMatrix.addTransform(viewTransform);
        transformMatrix.addTransform(projectionTransform);
    }
}
