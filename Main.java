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

        //configure light settings
        Light wlight = new Light();
        wlight.lightColor = Color.white;
        wlight.position = new Vec3f(1.0f, 1.45f, 0.f);
        LightShader.addLight(wlight);

        // TODO structure : make breaking of while loop dependent on user input
        // render loop
        int i = 0;
        while (true) {
            long start = System.nanoTime();
            // clear buffers at the beginning of the frame
            myRenderer.myRasterizer.clearDepthBuffer();
            myRenderer.myRasterizer.clearPixelBuffer(new Color(50, 50, 50));

            // specify rotation angle of object around y-axis in radians
            rotationAngle = (float) i / 5;
            CommonTransformations.rotationAngle = rotationAngle;
            CommonTransformations.offset= new Vec3f(0.5f,0,0);
            updateMatrices();
          
            //do the magic
            myRenderer.renderModel();

            // write to the buffer after doing all processing.
            // avoid writing multiple times per frame as tearing happens.
            myRenderer.printBufferOutput();
            myWindow.update();
            i++;

             long time = System.nanoTime() - start;
             System.out.println("processing time : " + (((double) time / 1_000_000) + "ms/frame"));
        }
    }
    public static final int pix_height = 300;
    public static final int pix_width = 300;
}