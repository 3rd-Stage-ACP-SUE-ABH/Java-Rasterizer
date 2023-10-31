package pipeline.fixed;

public abstract class Shader
{   //interface between rasterizer and shader types
    //all shader types are agnostic to the rendering context
    public abstract void vertex();
    //vertex is responsible for transforming the vertex data and sending processed data to fragment
    public abstract boolean fragment();
    //fragment is responsible for determining screen space fragment color and discarding fragments.
    //It expects screen space data from vertex.
    public static float interpolate()
    {
        return 0;
    }
}
