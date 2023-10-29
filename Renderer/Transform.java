package Renderer;

import Vector.Matrix;
import Vector.Vec3f;
import Vector.VecOperator;

import static Vector.VecOperator.*;
import static Vector.VecOperator.mul;

public class Transform {
    public Transform (int screenHeight, int screenWidth)
    {
        this.screenHeight=screenHeight;
        this.screenWidth=screenWidth;
    }
    int screenHeight, screenWidth;
    public Vec3f cameraPos = new Vec3f(0,0.5f,3);
    public Vec3f lookAtCenter = new Vec3f(0,0,0);
    public Matrix modelView, projection, viewPort;
    public void updateMatrices()
    {
        modelView = lookAt(cameraPos, lookAtCenter, new Vec3f(0,1,0));

        projection = Matrix.getIdentityMatrix(4);
        projection.setElement(3,2, -1.f/cameraPos.z());

        viewPort = viewport(0,0, screenWidth, screenHeight);
    }
    public Vec3f transform(Vec3f u)
    {   //TODO structure : can we make this more efficient by reducing calls to mul()?
        updateMatrices();
        Matrix transformMatrix = new Matrix(u, true);
        transformMatrix = mul(modelView, transformMatrix);
        transformMatrix= mul(projection, transformMatrix);
        transformMatrix = mul(viewPort, transformMatrix);
        return new Vec3f(transformMatrix);
    }
    public Vec3f mapToNDC(Vec3f u, float maxCoordinateSize)
    {
        Matrix transformMatrix = new Matrix(u, true);
        transformMatrix = mul(VecOperator.mapToNDC(maxCoordinateSize), transformMatrix);
        return new Vec3f(transformMatrix);
    }
}
