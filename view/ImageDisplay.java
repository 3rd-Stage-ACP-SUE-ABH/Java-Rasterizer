package view;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class ImageDisplay extends JFrame {

	public BufferedImage image;
	public static Panel imagePanel;
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

		getContentPane().setLayout(new GridLayout(3, 3));
		getContentPane().add(imagePanel, BorderLayout.CENTER);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				imagePanel.repaint();
			}
		});

	}
	/*
	 * public void loadBufferedImage (BufferedImage pixelBuffer)
	 * {
	 * image=pixelBuffer;
	 * }
	 */
	/*
	 * public void loadBuffer(int[] colorBuffer) { //TODO improve this whole class
	 * if(pixelBuffer.length == colorBuffer.length)
	 * {
	 * pixelBuffer= colorBuffer;
	 * }
	 * else
	 * {
	 * System.out.println("Error: unexpected color buffer size");;
	 * }
	 * }
	 */

	/*
	 * public void updateBufferedImage() {
	 * for (int i = 0; i < height; i++) {
	 * for (int j = 0; j < width ; j++) {
	 * image.setRGB(j, i, pixelBuffer[j + i * width]);
	 * }
	 * }
	 * }
	 */
}
