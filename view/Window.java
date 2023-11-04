
import controller.renderer.Renderer;

public class Window {
    Renderer ourRenderer;
    ImageDisplay img;
    buttone c;

    public Window(String title, Renderer yourRenderer) {
        // init window
        ourRenderer = yourRenderer;
        c = new buttone(yourRenderer);

        img = new ImageDisplay(yourRenderer.height, yourRenderer.width, title, yourRenderer.getPixelBuffer());
        img.setSize(yourRenderer.width, yourRenderer.height);
        img.setVisible(true);
        img.add(c);
    }

    public void update() {
        img.imagePanel.repaint();
        c.updateInput();
    }
}
