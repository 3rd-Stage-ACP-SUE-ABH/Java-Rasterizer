package view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import connection.ConnectionManager;

public class ImageDisplay extends JFrame {

	BufferedImage image;
	private ConnectionManager connection;
	private buttone c;
	public Panel imagePanel;

	public ImageDisplay(ConnectionManager connection, int width, int height, String frameTitle, BufferedImage pixelBuffer) {
		this.connection = connection;
		this.image = pixelBuffer;
		
		c = new buttone(this.connection, this);
		imagePanel = new Panel(image);

		setTitle(frameTitle);
		setSize(1500, 900);
		// setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1, 3));
		
		add(imagePanel);
		add(c);
		addListeners();

		setVisible(true);
	}

	private void addListeners() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				imagePanel.repaint();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Perform actions when the frame is closing
				System.out.println("Frame is closing");
				connection.sendData("client::Disconnected");
				// You can include any cleanup or additional actions here before closing the frame
				connection.closeConnection();
			}
        });
	}
}