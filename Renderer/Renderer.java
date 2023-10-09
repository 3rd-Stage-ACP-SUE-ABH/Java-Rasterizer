package Renderer;

import java.awt.*;
import java.util.Arrays;

public class Renderer {
    public int[] getColorBuffer() {
        return colorBuffer;
    }

    public Renderer(int screenWidth, int screenHeight)
    {
        width=screenWidth;
        height=screenHeight;
    }
    public void setPixel(int x, int y, Color RGBcolor)
    {
        colorBuffer[y*width+x]= RGBcolor.getRGB();
    }
    public void fill (Color fillColor)
    {
        Arrays.fill(colorBuffer, fillColor.getRGB());
    }
    //fields
    public int width, height;
    public int[] colorBuffer = new int[width*height];        //pixel buffer
}
