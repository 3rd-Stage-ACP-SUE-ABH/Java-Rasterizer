package model.pipeline.programmable;

import model.math.Matrix;
import model.math.Vec3f;
import model.math.VecOperator;
import model.pipeline.fixed.Shader;
import model.pipeline.programmable.shaderUtilities.Texture;
import model.pipeline.programmable.shaderUtilities.Transform;
import model.pipeline.programmable.shaderUtilities.lighting.Light;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;

import java.awt.*;
import java.io.IOException;

import static model.math.VecOperator.*;

public class PhongShader extends Shader
{
    public Vec3f camPos = new Vec3f(0,0,2);
    public Vec3f lookAt = new Vec3f(0,0,0);
    public Vec3f cameraUp = new Vec3f(0,1,0);
    public float rotationAngle = 0;
    private Matrix modelTransform = VecOperator.yRotationMatrix(rotationAngle);
    private Matrix viewTransform = VecOperator.lookAt(camPos, lookAt, cameraUp);
    private Matrix projectionTransform = Matrix.getIdentityMatrix(4);
    {
        projectionTransform.setElement(3,2, -1.f/camPos.z());
    }
    private Transform transformMatrix = new Transform();
    {
        transformMatrix.addTransform(modelTransform);
        transformMatrix.addTransform(viewTransform);
        transformMatrix.addTransform(projectionTransform);
    }
    public void updateMatrices()
    {
        modelTransform = VecOperator.yRotationMatrix(rotationAngle);
        viewTransform = VecOperator.lookAt(camPos, lookAt, cameraUp);
        projectionTransform.setElement(3,2, -1.f/camPos.z());

        transformMatrix.clear();
        transformMatrix.addTransform(modelTransform);
        transformMatrix.addTransform(viewTransform);
        transformMatrix.addTransform(projectionTransform);
    }
    {
     //   LightShader.addLight(new Light(new Vec3f(-1,0,0.5f),new Color(200,50,100)));
        Light wlight = new Light();
        wlight.lightColor = Color.white;
        wlight.position = new Vec3f(1.5f, 2.f, -1.5f);
        LightShader.addLight(wlight);
    //    LightShader.ambient.lightColor=new Color(0.1f,0,0);
    }
    public PhongShader() throws IOException {}
    private Vec3f[] normals = new Vec3f[3];
    private Vec3f[] texCoords = new Vec3f[3];
    private Vec3f[] positions = new Vec3f[3];
    @Override
    public void vertex(Vec3f[] objectData)
    {   //expects 3 vertex positions, 3 normals, and 3 texture coordinates. This shader only works if the model has all those coordinates!
        for(int i = 0; i<3; i++)
        {
            objectData[i] = transformMatrix.transform(objectData[i]);
            positions[i]=objectData[i];
            normals[i] = transformMatrix.transformNormals(objectData[i+3]);
            texCoords[i] = objectData[i+6];
        }
    }
    Texture diffuseMap= new Texture("C:/Users/msi/Desktop/african_head_diffuse.png");
    Texture normalMap = new Texture("C:/Users/msi/Desktop/african_head_nm.png");
    @Override
    public Color fragment(Vec3f fragment, Vec3f bar)
    {
        float posX = interpolate(new Vec3f(positions[0].x(), positions[1].x(), positions[2].x()), bar);
        float posY = interpolate(new Vec3f(positions[0].y(), positions[1].y(), positions[2].y()), bar);
        float posZ = interpolate(new Vec3f(positions[0].z(), positions[1].z(), positions[2].z()), bar);
        Vec3f interpolatedPosition = new Vec3f(posX, posY, posZ);

        float texturePixelX = interpolate(new Vec3f(texCoords[0].x(), texCoords[1].x(), texCoords[2].x()), bar);
        float texturePixelY = interpolate(new Vec3f(texCoords[0].y(), texCoords[1].y(), texCoords[2].y()), bar);

        float normalsX = texturePixelX;
        float normalsY = texturePixelY;
        normalsX=map(0,1,0,normalMap.getWidth(), normalsX).floatValue();
        normalsY=map(0,1,0,normalMap.getHeight(), normalsY).floatValue();
        Color normalColor = normalMap.getPixel((int)normalsX, (int)normalsY);
        Vec3f normal = new Vec3f((float) normalColor.getRed()/255, (float) normalColor.getGreen()/255, (float) normalColor.getGreen()/255);
        normal=transformMatrix.transformNormals(normal).getNormalized();

        texturePixelY=map(0,1,0,diffuseMap.getHeight(), texturePixelY).floatValue();
        texturePixelX=map(0,1,0,diffuseMap.getWidth(), texturePixelX).floatValue();
        Color fragmentTextureColor = diffuseMap.getPixel((int)texturePixelX, (int)texturePixelY);

        return mulColor(LightShader.shade(normal, interpolatedPosition), fragmentTextureColor) ;
    }
}
