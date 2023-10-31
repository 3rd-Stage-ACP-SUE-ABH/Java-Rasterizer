package pipeline.fixed;

import math.Pixel;
import math.Vec3f;

import java.awt.*;

public abstract class Shader
{   //interface between rasterizer and shader types
    //all shader types are agnostic to the rendering context
    protected float[] depthBuffer;
    public void setDepthBuffer(float[] depthBuffer) {
        this.depthBuffer = depthBuffer;
    }

    protected boolean depthTest(Pixel point)
    {   //returns true if point passes depth test, otherwise updates buffer with new value
        return false;
    }
    public abstract void vertex(Vec3f[] vertexData);
    //vertex is responsible for transforming the vertex data and sending processed data to fragment
    //it expects NDCs
    public abstract Color fragment();
    //fragment is responsible for determining screen space fragment color and discarding fragments. returns null if fragment is discarded
    protected float interpolate()
    {
        return 0;
    }
}
