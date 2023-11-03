package pipeline.programmable.shaderUtilities;

import math.Matrix;
import math.Vec3f;

import java.util.ArrayList;
import java.util.Stack;

import static math.VecOperator.mul;

public class Transform
{   //this class is responsible for combining multiple transformations into a single transform
    public Transform(){}
    public void clear()
    {
        matrices.clear();
    }
    private ArrayList<Matrix> matrices = new ArrayList<>();
    public void addTransform(Matrix transformMatrix)
    {
        matrices.add(transformMatrix);
    }
    public Vec3f transform(Vec3f rawVertex)
    {
        Matrix transformMatrix = new Matrix(rawVertex, true);
        for (int i = 0; i<matrices.size(); i++)
        {
            transformMatrix = mul(matrices.get(i), transformMatrix);
        }
        return new Vec3f(transformMatrix);
    }
    public Vec3f transformNormals(Vec3f normals)
    {
        Matrix transformMatrix = new Matrix(normals, true);
        for (int i = 0; i<matrices.size(); i++)
        {
            transformMatrix = mul(matrices.get(i).inverse().transpose(), transformMatrix);
        }
        return new Vec3f(transformMatrix);
    }
}
