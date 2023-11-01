package pipeline.programmable;

import math.Vec3f;
import pipeline.fixed.Shader;
import pipeline.fixed.Transform;

import java.awt.*;

public class CellShader extends Shader
{
    public CellShader()
    {
    }
    @Override
    public void vertex(Vec3f[] objectData)
    {
    }
    @Override
    public Color fragment()
    {
        return Color.PINK;
    }

}
