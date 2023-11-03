package view;

import model.renderer.Renderer;



public class Window {
    Renderer ourRenderer;
    ImageDisplay img;
    public Window(String title, Renderer yourRenderer)
    {
        //init window
        ourRenderer=yourRenderer;
        buttone c = new buttone(yourRenderer);
        img = new ImageDisplay(yourRenderer.height,yourRenderer.width,title, yourRenderer.getPixelBuffer());
        img.setSize(yourRenderer.width,yourRenderer.height);
        img.setVisible(true);
        img.add(c);
    }
    public void update()
    {
        img.imagePanel.repaint();
    }
}
