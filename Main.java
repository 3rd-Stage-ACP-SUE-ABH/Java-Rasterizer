import java.awt.*;
import java.io.IOException;
import model.math.Vec3f;
import model.pipeline.programmable.*;
import model.pipeline.programmable.shaderUtilities.CommonTransformations;
import model.pipeline.programmable.shaderUtilities.lighting.Light;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;
import controller.renderer.*;
import view.Window;
import static model.math.VecOperator.*;
import static model.pipeline.programmable.shaderUtilities.CommonTransformations.updateMatrices;

public class Main
{
    public static void main(String[]args) throws IOException
    {
        // init renderer
        Renderer myRenderer = new Renderer(pix_width, pix_height);
        myRenderer.setShader(new PhongShader());

        Window myWindow = new Window("Java Rasterizer", myRenderer);

        // default global light settings
        Light wlight = new Light();
        wlight.lightColor = new Color(255,255,0);
        wlight.position = new Vec3f(1.0f, 1.45f, 0.f);
        LightShader.addLight(wlight);

        // TODO structure : make breaking of while loop dependent on user input
        // render loop
        while (true) {
            long start = System.nanoTime();
            // clear buffers at the beginning of the frame
            myRenderer.clearDepthBuffer();
            myRenderer.clearColorBuffer(new Color(50, 50, 50));

            // upate global geometric variables
            updateMatrices();
          
            //do the magic
            myRenderer.renderModel();

            // write to the buffer after doing all processing.
            // avoid writing multiple times per frame as tearing happens.
            myRenderer.writePixelBuffer();
            myWindow.update();
            long time = System.nanoTime() - start;
        //     System.out.println("processing time : " + (((double) time / 1_000_000) + "ms/frame"));
        }
    }
    public static final int pix_height = 350;
    public static final int pix_width = 350;
}