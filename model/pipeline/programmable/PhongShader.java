package model.pipeline.programmable;

import model.math.Vec3f;
import model.pipeline.fixed.Shader;
import model.pipeline.programmable.shaderUtilities.Texture;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;

import java.awt.*;
import java.io.IOException;

import static model.math.VecOperator.*;
import static model.pipeline.programmable.shaderUtilities.CommonTransformations.*;

public class PhongShader extends Shader {
    public PhongShader() throws IOException {
    }

    private Vec3f[] normals = new Vec3f[3];
    private Vec3f[] texCoords = new Vec3f[3];
    private Vec3f[] positions = new Vec3f[3];

    @Override
    public void vertex(Vec3f[] objectData) { // expects 3 vertex positions, 3 normals, and 3 texture coordinates. This
                                             // shader only works if the model has all those coordinates!
        for (int i = 0; i < 3; i++) {
            objectData[i] = applyTransform((objectData[i]));
            positions[i] = objectData[i];
            // do null handling here
            if (objectData[i + 3] != null)
                normals[i] = applyNormalTransform(objectData[i + 3]);
            if (objectData[i + 6] != null)
                texCoords[i] = objectData[i + 6];
        }
    }

    Texture diffuseMap = new Texture("C:/Users/GFC/Desktop/african_head_diffuse.png");
    Texture normalMap = new Texture("C:/Users/GFC/Desktop/african_head_nm.png");

    @Override
    public Color fragment(Vec3f fragment, Vec3f bar) {
        float posX = interpolate(new Vec3f(positions[0].x(), positions[1].x(), positions[2].x()), bar);
        float posY = interpolate(new Vec3f(positions[0].y(), positions[1].y(), positions[2].y()), bar);
        float posZ = interpolate(new Vec3f(positions[0].z(), positions[1].z(), positions[2].z()), bar);
        Vec3f interpolatedPosition = new Vec3f(posX, posY, posZ);

        float texturePixelX = interpolate(new Vec3f(texCoords[0].x(), texCoords[1].x(), texCoords[2].x()), bar);
        float texturePixelY = interpolate(new Vec3f(texCoords[0].y(), texCoords[1].y(), texCoords[2].y()), bar);

        float normalsX = texturePixelX;
        float normalsY = texturePixelY;
        normalsX = map(0, 1, 0, normalMap.getWidth(), normalsX).floatValue();
        normalsY = map(0, 1, 0, normalMap.getHeight(), normalsY).floatValue();
        Color normalColor = normalMap.getPixel((int) normalsX, (int) normalsY);
        Vec3f normal = new Vec3f((float) normalColor.getRed() / 255, (float) normalColor.getGreen() / 255,
                (float) normalColor.getGreen() / 255);
        normal = applyNormalTransform(normal).getNormalized();

        texturePixelY = map(0, 1, 0, diffuseMap.getHeight(), texturePixelY).floatValue();
        texturePixelX = map(0, 1, 0, diffuseMap.getWidth(), texturePixelX).floatValue();
        Color fragmentTextureColor = diffuseMap.getPixel((int) texturePixelX, (int) texturePixelY);

        return mulColor(LightShader.shade(normal, interpolatedPosition), fragmentTextureColor);
    }
}
