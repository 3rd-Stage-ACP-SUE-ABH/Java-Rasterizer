package  window;
import java.awt.Color;

public class window {

	public static void notMain(String[] args) {
		
	
		ImageDisplay img = new ImageDisplay(1000,1000,"test display");
		Color[] colorBuffer = new Color[1000*1000]; 
		
		img.setSize(700,700);
		img.setVisible(true);
		
		while(true) {
			updateArray(colorBuffer); // works
			img.loadBuffer(colorBuffer);
			img.updateBufferedImage();
			
			img.imagePanel.repaint();
			
		}
		
	}
	 public static void updateArray(Color[] colorArray) {
		 System.out.println("frame test");
		   float x = (float) Math.sin(System.currentTimeMillis()/100); //
		   int firstARg = (int) ((x+1)/2*77);
		   int sec = (int) ((x+1)/2*114);
		   int thi = (int) ((x+1)/2*45);
		    	for(int i=0; i<colorArray.length; i++) {
		    		colorArray[i] = new Color(firstARg,sec,thi);
		    	}
	    }
}
