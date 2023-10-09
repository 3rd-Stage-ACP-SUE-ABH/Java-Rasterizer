package Renderer;

import java.awt.*;
import java.util.Arrays;

public class Renderer {
    public Renderer(int screenWidth, int screenHeight)
    {
        width=screenWidth;
        height=screenHeight;
        colorBuffer = new int[width*height];
    }
    public void setPixel(int x, int y, Color RGBcolor)
    {
        colorBuffer[y*width+x]= RGBcolor.getRGB();
    }
    public void fill (Color fillColor)
    {
        Arrays.fill(colorBuffer, fillColor.getRGB());
    }
    public int[] getColorBuffer() {
        return colorBuffer;
    }
    //fields
    public int width, height;
    public int[] colorBuffer;        //pixel buffer
}
