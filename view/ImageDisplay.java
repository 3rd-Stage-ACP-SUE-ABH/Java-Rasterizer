package view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import controller.renderer.Renderer;

public class ImageDisplay extends JFrame {

	public BufferedImage image;
	public static Panel imagePanel;
	public buttone c;
	public int width;
	public int height;

	private String frameTitle;
	private Color backgroundColor = new Color(50, 50, 50);

	public ImageDisplay(int width, int height, String frameTitle, BufferedImage pixelBuffer) {
		this.width = width;
		this.height = height;
		this.frameTitle = frameTitle;
		image = pixelBuffer;
		setTitle(frameTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		imagePanel = new Panel(image);

		setLayout(new GridLayout(1, 3));
		add(imagePanel);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				imagePanel.repaint();
			}
		});

	}
}