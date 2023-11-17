package model.pipeline.programmable;

import model.math.Vec3f;
import model.pipeline.fixed.Shader;
import model.pipeline.programmable.shaderUtilities.Texture;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;
import java.awt.*;
import java.io.IOException;
import static model.math.VecOperator.*;
import static model.pipeline.programmable.shaderUtilities.CommonTransformations.*;

public class PhongShader extends Shader
{
    public PhongShader() throws IOException {}
    private Vec3f[] normals;
    private Vec3f[] texCoords;
    private Vec3f[] positions = new Vec3f[3];

    @Override
    public void vertex(Vec3f[] objectData)
    {   //expects at least 3 vertex positions.Optionally 3 normals and 3 texture coordinates.
        normals = new Vec3f[3];
        texCoords = new Vec3f[3];
        for(int i = 0; i<3; i++)
        {
            objectData[i] = applyTransform((objectData[i]));
            positions[i]=objectData[i];
            //do null handling here
            if(objectData[i+3]!=null)
                normals[i] = applyNormalTransform(objectData[i+3]);
            if (objectData[i+6]!=null)
                texCoords[i] = objectData[i+6];
        }
    }
    Texture diffuseMap = new Texture("assets/african_head_diffuse.png");
    Texture normalMap = new Texture("assets/african_head_nm.png");
    Texture specMap = new Texture("assets/african_head_spec.png");

    @Override
    public Color fragment(Vec3f fragment, Vec3f bar) {
        float posX = interpolate(new Vec3f(positions[0].x(), positions[1].x(), positions[2].x()), bar);
        float posY = interpolate(new Vec3f(positions[0].y(), positions[1].y(), positions[2].y()), bar);
        float posZ = interpolate(new Vec3f(positions[0].z(), positions[1].z(), positions[2].z()), bar);
        Vec3f interpolatedPosition = new Vec3f(posX, posY, posZ);

        //we have texture coordinates?
        if (texCoords[0]==null||texCoords[1]==null||texCoords[2]==null)
            return fragmentNoTex(fragment, bar, interpolatedPosition);

        //interpolate texture and normal map
        float texturePixelX = interpolate(new Vec3f(texCoords[0].x(), texCoords[1].x(), texCoords[2].x()), bar);
        float texturePixelY = interpolate(new Vec3f(texCoords[0].y(), texCoords[1].y(), texCoords[2].y()), bar);

        //retrieve normal from normal map
        float normalsX = texturePixelX;
        float normalsY = texturePixelY;
        normalsX=map(0,1,0,normalMap.getWidth(), normalsX).floatValue();
        normalsY=map(0,1,0,normalMap.getHeight(), normalsY).floatValue();
        Color normalColor = normalMap.getPixel((int)normalsX, (int)normalsY);
        Vec3f normal = new Vec3f((float) normalColor.getRed()/255, (float) normalColor.getGreen()/255, (float) normalColor.getGreen()/255);
        normal=applyNormalTransform(normal).getNormalized();

        //retrieve diffuse color from diffuse map
        float diffuseTexLocY = map(0, 1, 0, diffuseMap.getHeight(), texturePixelY).floatValue();
        float diffuseTexLocX = map(0, 1, 0, diffuseMap.getWidth(), texturePixelX).floatValue();
        Color fragmentTextureColor = diffuseMap.getPixel((int) diffuseTexLocX, (int) diffuseTexLocY);

        float specTexLocY = map(0, 1, 0, specMap.getHeight(), texturePixelY).floatValue();
        float specTexLocX = map(0, 1, 0, diffuseMap.getWidth(), texturePixelX).floatValue();
        Color fragmentSpecColor = specMap.getPixel((int)specTexLocX, (int) specTexLocY);

        return LightShader.shade(normal, interpolatedPosition, fragmentSpecColor.getRed(), fragmentTextureColor);
    }


    private Color fragmentNoTex (Vec3f fragment, Vec3f bar, Vec3f interpolatedPosition)
    {
        //We have normal coordinates?
        if (normals[0]==null||normals[1]==null||normals[2]==null)
            return fragmentNoNormal(fragment, bar, interpolatedPosition);
        //Interpolate normal
        float normalX = interpolate(new Vec3f(normals[0].x(), normals[1].x(), normals[2].x()), bar);
        float normalY = interpolate(new Vec3f(normals[0].y(), normals[1].y(), normals[2].y()), bar);
        float normalZ = interpolate(new Vec3f(normals[0].z(), normals[1].z(), normals[2].z()), bar);
        Vec3f normal = new Vec3f(normalX, normalY, normalZ);

        return LightShader.shade(normal, interpolatedPosition, 0);
    }
    private Color fragmentNoNormal (Vec3f fragment, Vec3f bar, Vec3f interpolatedPosition)
    {
        //Flat shading.
        Vec3f normal = cross(minus(positions[1], positions[0]), minus(positions[2], positions[0])).getNormalized();
        return LightShader.shade(normal, interpolatedPosition, 0);
    }
}
