import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

import connection.ConnectionManager;
import view.ImageDisplay;

public class Client {
    public static void main(String[] args) {
        // client-server related
        final String serverAddress = "127.0.0.1"; // Change this to the server's IP if necessary
        final int serverPort = 5075;

        final int width = 350;
        final int height = 350;
        final String title = "Java Rasterizer";
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        final ConnectionManager connection = new ConnectionManager(serverAddress, serverPort);
        
        SwingUtilities.invokeLater(() -> {
            new ImageDisplay(connection, width, height, title, image);
        });
    }
}