package view;

import java.awt.Graphics;
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
		final int panelWidth = getWidth();
		final int panelHeight = getHeight();
		final int imageWidth = 700;
		final int imageHeight = 700;
		// center
		final int x = (panelWidth - imageWidth) / 2;
		final int y = (panelHeight - imageHeight) / 2;
		g.drawImage(image, x, y, imageWidth, imageHeight, this);
	}
}