package  window;

import Renderer.Renderer;

import java.awt.image.BufferedImage;

public class Window {
    ImageDisplay img;
    public Window(String title, Renderer yourRenderer)
    {
        //init window
        buttone c = new buttone(yourRenderer);
        img = new ImageDisplay(yourRenderer.height,yourRenderer.width,title, yourRenderer.getPixelBuffer());
        img.setSize(yourRenderer.width,yourRenderer.height);
        img.setVisible(true);
        img.add(c);
    }
    public void update()
    {
        ImageDisplay.imagePanel.repaint();
    }
}
