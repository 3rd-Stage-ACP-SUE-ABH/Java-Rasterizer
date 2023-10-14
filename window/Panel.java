package window;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel{
	 private BufferedImage image;

	    public Panel(BufferedImage image) {
	        this.image = image;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int panelWidth = getWidth();
	        int panelHeight = getHeight();
	        int imageWidth = image.getWidth();
	        int imageHeight = image.getHeight();
	        // center 
	        int x = (panelWidth - imageWidth) / 2;
	        int y = (panelHeight - imageHeight) / 2;
	        g.drawImage(image, x, y, imageWidth, imageHeight, this);
	    }
}
