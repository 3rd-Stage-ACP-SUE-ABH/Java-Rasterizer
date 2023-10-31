package pipeline.programmable;

import math.Vec3f;
import pipeline.fixed.Shader;

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
    public Color fragment() {
        return Color.red;
    }

}
