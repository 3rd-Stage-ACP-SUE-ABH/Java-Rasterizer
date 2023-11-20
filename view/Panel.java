package view;

import java.awt.Graphics;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Panel extends JPanel {
	public BufferedImage image;

	public Panel(BufferedImage image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int imageWidth = 700;
		int imageHeight = 700;
		// center
		int x = (panelWidth - imageWidth) / 2;
		int y = (panelHeight - imageHeight) / 2;
		g.drawImage(image, x, y, imageWidth, imageHeight, this);
	}
}