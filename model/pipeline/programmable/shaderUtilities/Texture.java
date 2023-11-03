package model.pipeline.programmable.shaderUtilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {
    public Texture(String filePath) throws IOException {readTexture(filePath);}
    private Color[] textureData;
    private int texHeight, texWidth;
    public int getWidth() { return texWidth; }
    public int getHeight() { return texHeight; }
    public Color getPixel(int x, int y) { return textureData[x+y*texWidth]; }
    public Color[] readTexture(String filePath) throws IOException
    {
        //loads texture data from a .png, inverted vertically
        //TODO error handling : try catch
        BufferedImage textureReader = ImageIO.read(new File(filePath));
        texHeight= textureReader.getHeight();
        texWidth=textureReader.getWidth();
        textureData = new Color[textureReader.getHeight()*textureReader.getWidth()];
        for (int i =textureReader.getHeight()-1;i>=0;i--)
        {
            for (int j =0; j<textureReader.getWidth();j++)
            {
                textureData[(textureReader.getHeight()-i-1)*textureReader.getWidth()+j]=new Color(textureReader.getRGB(j,i));
            }
        }
        return textureData;
    }
}
