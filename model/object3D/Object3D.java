package model.object3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class Object3D {
    private final ArrayList<float[]> vertexCoords;
    private final ArrayList<float[]> normalCoords;
    private final ArrayList<float[]> texCoords;
    private final List<Integer> vertexIndices;
    private final List<Integer> textureIndices;
    private final List<Integer> normalIndices;
    private final ArrayList<int[]> Vindices;
    private final ArrayList<int[]> Nindices;
    private final ArrayList<int[]> Tindices;
    private final String filePath;
    public float minCoord=Float.MAX_VALUE;
    public float maxCoord=-Float.MAX_VALUE;
    public Object3D(String filePath) {
        this.filePath = filePath;
        normalIndices = new ArrayList<>();
        textureIndices = new ArrayList<>();
        vertexIndices = new ArrayList<>();
        texCoords = new ArrayList<>();
        normalCoords = new ArrayList<>();
        vertexCoords = new ArrayList<>();
        //load data AFTER instantiating the fields
        loadData();
        Vindices = new ArrayList<>();
        Nindices = new ArrayList<>();
        Tindices = new ArrayList<>();
        organizeIndices();
    }
    public void printFaces()
    {
        for (int i =0; i<Vindices.size();i++)
        {
            for (int j = 0;j<3;j++)
            {
                System.out.print(Vindices.get(i)[j]);
                if (!Tindices.isEmpty())
                    System.out.print("/"+Tindices.get(i)[j]);
                if (!Nindices.isEmpty())
                    System.out.print("/"+Nindices.get(i)[j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    private void organizeIndices ()
    {       //inserting data into arrays of 3 rather than having everything in one array
        if (vertexIndices.size()%3==0)
        {
            for (int i =0; i<vertexIndices.size(); i++)
            {
                Vindices.add(new int[]{vertexIndices.get(i++), vertexIndices.get(i++), vertexIndices.get(i)});
                if (!normalIndices.isEmpty())
                    Nindices.add(new int[]{normalIndices.get(i-2), normalIndices.get(i-1), normalIndices.get(i)});
                if (!textureIndices.isEmpty())
                    Tindices.add(new int[]{textureIndices.get(i-2), textureIndices.get(i-1), textureIndices.get(i)});
            }
        }
        else
        {
            System.out.println("Critical error : faces not divisible by 3");
        }
    }

    public boolean hasTextureCoords()
    {
        return !Tindices.isEmpty();
    }
    public boolean hasNormalCoords()
    {
        return !Nindices.isEmpty();
    }


    public float[] getVertexCoords(int idx)
    {
        return vertexCoords.get(idx);
    }
    public float[] getTexCoords(int idx)
    {
        return texCoords.get(idx);
    }
    public float[] getNormalCoords(int idx)
    {
        return normalCoords.get(idx);
    }
    public int[] getVertexIndices(int idx)
    {
        return Vindices.get(idx);
    }
    public int[] getNormalIndices(int idx)
    {
        return Nindices.get(idx);
    }
    public int[] getTextureIndices(int idx)
    {
        return Tindices.get(idx);
    }

    public int nVerts()
    {
        return vertexCoords.size();
    }
    public int nNormals()
    {
        return normalCoords.size();
    }
    public int nTextures()
    {
        return texCoords.size();
    }
    public int nFaces()
    {
        return Vindices.size();
    }

    private void loadData() {
        try {
            FileReader f = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(f);
            String Line;
            String[] P;
            while ((Line = reader.readLine()) != null) {
                P = Line.split("\\s+");

                if (P.length == 0) {
                    continue;
                }

                if (P[0].equals("v")) {
                    float x = Float.parseFloat(P[1]);
                    float y = Float.parseFloat(P[2]);
                    float z = Float.parseFloat(P[3]);
                    float minCandidate = Math.min(x,Math.min(y,z));
                    minCoord = Math.min(minCoord, minCandidate);
                    float maxCandidate = Math.max(x,Math.max(y,z));
                    maxCoord = Math.max(maxCoord, maxCandidate);
                    vertexCoords.add(new float[]{x, y, z});
                }
                if (P[0].equals("vn")) {
                    float x = Float.parseFloat(P[1]);
                    float y = Float.parseFloat(P[2]);
                    float z = Float.parseFloat(P[3]);
                    float[] Vs = {x, y, z};
                    normalCoords.add(Vs);
                }
                if (P[0].equals("vt")) {
                    float x = Float.parseFloat(P[1]);
                    float y = Float.parseFloat(P[2]);
                    float z = Float.parseFloat(P[3]);
                    float[] Vs = {x, y, z};
                    texCoords.add(Vs);
                }
                String[] parts;

                if (P[0].equals("f"))
                {
                    //TODO bug fixing :  as far as I'm aware this condition is unnecessary,
                    // and can miss faces in some formats. Check with team.
                    if (true)   // previous condition : P[1].contains("/")
                    {
                        for (int i = 1; i < P.length; i++)
                        {
                            int coordCounter = 0;
                            parts = P[i].split("/"); // '1 1 1'
                            //-1 because indices in .obj format start from 1
                            vertexIndices.add(Integer.parseInt(parts[coordCounter++])-1);
                            if (!texCoords.isEmpty())
                                textureIndices.add(Integer.parseInt(parts[coordCounter++])-1);
                            if (!normalCoords.isEmpty())
                                 normalIndices.add(Integer.parseInt(parts[coordCounter++])-1);
                        }
                    }
                }

            }
            reader.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}