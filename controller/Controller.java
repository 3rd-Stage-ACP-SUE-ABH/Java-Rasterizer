package controller;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import connection.ConnectionManager;
import connection.ControllerHelpers;
import model.pipeline.programmable.PhongShader;
import controller.renderer.Renderer;
import model.math.Vec3f;
import model.object3D.Object3D;
import model.pipeline.programmable.shaderUtilities.CommonTransformations;
import model.pipeline.programmable.shaderUtilities.lighting.Light;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;

import static model.pipeline.programmable.shaderUtilities.CommonTransformations.updateMatrices;

public final class Controller {
    private final File objFolder = new File("c:/java-rasterizer-obj");
    private ConnectionManager connection;
    private Renderer myRenderer;
    private Map<String, String> tempMap;
    private Light tempLight;

    private float floatX, floatY, floatZ;
    private float directionFloat1, directionFloat2, directionFloat3;
    private float posX, posY, posZ;
    private float lookAt1, lookAt2, lookAt3;
    private float cameraIncXFloat, cameraIncYFloat, cameraIncZFloat;
    private float rotationFloat;
    private float offsetXF, offsetYF, offsetZF;

    public Controller(ConnectionManager connection, int width, int height) {
        this.connection = connection;

        this.myRenderer = new Renderer(width, height);

        try {
            myRenderer.setShader(new PhongShader());
        } catch (IOException e) {
            returnVoid(e, "couldn't set the shader");
        }

        // create obj && png folders
        objFolder.mkdir();

        tempLight = new Light();
        Light wlight = new Light();
        wlight.lightColor = new Color(255, 255, 0);
        wlight.position = new Vec3f(1.0f, 1.45f, 0.f);
        LightShader.addLight(wlight);
        wlight = null;
    }

    public void openConnectionListener() {
        String mapStr;

        try {
            while ((mapStr = this.connection.receiveData()) != null) {
                System.out.println("ACP: Line recieved");
                // System.out.println(mapStr);
                checkMapStateAndRespond(mapStr);
            }
        } catch (Exception e) {
            System.err.println("Couldn't convert the request string to map");
        }
    }

    private void checkMapStateAndRespond(String str) {
        str = str.trim();
        System.out.println(str);

        if (str.charAt(0) == '[') {
            System.out.println("Banna: wara era");
            int indexOfColon = str.indexOf(":");
            String fileName = str.substring(1, indexOfColon);
            String fileContent = str.substring(indexOfColon + 1, str.length() - 1);
            // load model
            try {
                File objFile = new File(objFolder.getAbsolutePath() + "/" + fileName);
                // objFile.createNewFile();

                if (objFile.createNewFile()) {
                    PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(objFile)), true);
                    printWriter.write(fileContent);     
                    printWriter.close();
                    printWriter = null;
                }

                Object3D objModel = new Object3D(objFile.getAbsolutePath());

                this.myRenderer.buttonFlag = true;
                this.myRenderer.loadModelData(objModel);

                this.myRenderer.buttonFlag = false;

            } catch(IOException e) {
                returnVoid(e, "couldn't write to the file\nTry again");
            }
        } else {

            try {
                this.tempMap = (HashMap<String, String>) ControllerHelpers.stringToMap(str);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                return;
            }

            if(tempMap.containsKey("shader")) {
                // { "Default", "Toggle Chunky", "Toggle Texture", 
                // "Toggle Diffuse", "Toggle Specular",
                // "Toggle Ambient", "Toggle Spec Mode"}
                // add light according to the map's "shader" entry value
                int shaderValue = Integer.parseInt(tempMap.get("shader"));
                
                switch (shaderValue) {
                    case 0:
                        LightShader.setDefaults();
                        break;
                    case 1:
                        LightShader.toggleChunky();
                        break;
                    case 2:
                        LightShader.toggleTex();
                        break;
                    case 3:
                        LightShader.toggleDiffuse();
                        break;
                    case 4:
                        LightShader.toggleSpec();
                        break;
                    case 5:
                        LightShader.toggleAmbient();
                        break;
                    case 6:
                        LightShader.toggleSpecMode();
                        break;
                    default:
                        break;
                }

            } else if(tempMap.containsKey("clearLight(s)")) {
                // clear lights
                LightShader.clearLights();
            } else if(tempMap.containsKey("addLight")) {
                String lightType = tempMap.get("addLight");
                // add light according to the map's "addLight" entry value
                if (lightType.equals("pointLight")) {
                    tempLight.direction = null;
                    floatX = Float.parseFloat(tempMap.get("floatX"));
                    floatY = Float.parseFloat(tempMap.get("floatY"));
                    floatZ = Float.parseFloat(tempMap.get("floatZ"));
                    tempLight.position = new Vec3f(floatX, floatY, floatZ);
                    System.out.println(tempLight.position);
                    LightShader.addLight(tempLight);
                } else if(lightType.equals("directionalLight")){
                    tempLight.position = null;
                    directionFloat1 = Float.parseFloat(tempMap.get("lightDirection1"));
                    directionFloat2 = Float.parseFloat(tempMap.get("lightDirection2"));
                    directionFloat3 = Float.parseFloat(tempMap.get("lightDirection3"));
                    tempLight.direction = new Vec3f(directionFloat1, directionFloat2, directionFloat3);
                    System.out.println(tempLight.direction);
                    LightShader.addLight(tempLight);
                }
            } else {
                lookAt1 = Float.parseFloat(tempMap.get("lookAtX"));
                lookAt2 = Float.parseFloat(tempMap.get("lookAtY"));
                lookAt3 = Float.parseFloat(tempMap.get("lookAtZ"));
                CommonTransformations.lookAt = new Vec3f(lookAt1, lookAt2, lookAt3);
    
                posX = Float.parseFloat(tempMap.get("posX"));
                posY = Float.parseFloat(tempMap.get("posY"));
                posZ = Float.parseFloat(tempMap.get("posZ"));
                CommonTransformations.camPos = new Vec3f(posX, posY, posZ);
    
                cameraIncXFloat = Float.parseFloat(tempMap.get("cameraIncX"));
                cameraIncYFloat = Float.parseFloat(tempMap.get("cameraIncY"));
                cameraIncZFloat = Float.parseFloat(tempMap.get("cameraIncZ"));
                CommonTransformations.cameraUp = new Vec3f(cameraIncXFloat, cameraIncYFloat, cameraIncZFloat);
    
                rotationFloat = Float.parseFloat(tempMap.get("rotation"));
                CommonTransformations.rotationAngle = rotationFloat;
    
                offsetXF = Float.parseFloat(tempMap.get("offsetX"));
                offsetYF = Float.parseFloat(tempMap.get("offsetY"));
                offsetZF = Float.parseFloat(tempMap.get("offsetZ"));
                CommonTransformations.offset = new Vec3f(offsetXF, offsetYF, offsetZF);
            }
        }

        // clear buffers at the beginning of the frame
        myRenderer.myRasterizer.clearDepthBuffer();
        myRenderer.myRasterizer.clearPixelBuffer(new Color(50, 50, 50));

        updateMatrices();
          
        //do the magic
        this.myRenderer.renderModel();

        int[] pixilBuffer = myRenderer.getRasterizerOutput();
        String a = Arrays.toString(pixilBuffer);
        System.out.println("data is ready to be sent");
        // System.out.println(a);

        this.connection.sendData(a);
    }

    private void returnVoid(IOException e, String str) {
        System.err.println(str);
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}