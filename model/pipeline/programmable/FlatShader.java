package model.pipeline.programmable;

import model.math.Matrix;
import model.math.Vec3f;
import model.math.VecOperator;
import model.pipeline.fixed.Shader;
import model.pipeline.programmable.shaderUtilities.CommonTransformations;
import model.pipeline.programmable.shaderUtilities.Transform;
import model.pipeline.programmable.shaderUtilities.lighting.Light;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;

import java.awt.*;

import static model.math.VecOperator.*;

public class FlatShader extends Shader
{
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
            objectData[i] = CommonTransformations.applyTransform(objectData[i]);
        }
        normal = calculateNormal(objectData);
    }
    @Override
    public Color fragment(Vec3f fragment, Vec3f bar)
    {   //TODO temporary.
        return null;
        // return sumColor(LightShader.shade(normal, null), new Color(50,0,0));
    }
}
