package model.pipeline.fixed;

import model.math.Vec3f;

import java.awt.*;

public abstract class Shader
{   //interface between rasterizer and shader types.
    //I wanted to make shaders completely agnostic to the rendering context,
    //but to do screen-space processing we need the viewport data.
    public abstract void vertex(Vec3f[] vertexData);
    //vertex shaders are responsible for transforming the raw vertex data into screen space and "sending" processed data to the fragment shader.
    //it expects data in NDC range
    public abstract Color fragment(Vec3f fragment, Vec3f bar);
    //fragment shaders are responsible for determining screen-space pixel color and discarding unwanted pixels. returns null if pixel is discarded.
    //note: fragment = pixel. typically a fragment is a result of interpolating many data points between 3 vertices of a triangle.
}
