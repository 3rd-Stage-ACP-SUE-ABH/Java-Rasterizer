package window;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class ImageDisplay extends JFrame {
	
	public static BufferedImage image;
	public static Panel imagePanel;
	public static Color[] pixelBuffer;
	public static int counter = 0;
	public int width;
	public int height;
	private String frameTitle;
	private Color backgroundColor = new Color(50,50,50);
	
	
	public ImageDisplay(int width,int height,String frameTitle) {
		this.width = width;
		this.height = height;
		this.frameTitle = frameTitle;
		pixelBuffer = new Color[width*height];
		fillBackground();
		// updateArray();
		image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		setTitle(frameTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		   imagePanel = new Panel(image);
		   
	        getContentPane().setLayout(new BorderLayout());
	        getContentPane().add(imagePanel, BorderLayout.CENTER);
	       
	        addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	            	updateBufferedImage();
	                imagePanel.repaint();
	            }
	        });
	        
	}
	
	public void loadBuffer(Color[] colorBuffer) {
		if(pixelBuffer.length == colorBuffer.length) {
			System.arraycopy(colorBuffer, 0, pixelBuffer, 0, pixelBuffer.length);
		}else {
			System.out.println("Wrong Size Bith");;
		}
	}
	
	private void fillBackground() {
		for(int i=0; i<pixelBuffer.length; i++) {
			pixelBuffer[i] = backgroundColor; 
		}
	}
	
	 public  void updateBufferedImage() {
	        for (int i = 0; i < height; i++) {
	            for (int j = 0; j < width ; j++) {
	                image.setRGB(j, i, pixelBuffer[j + i * width].getRGB());
	            }
	        }
	    }
	
}
