package Vector;

import java.util.Arrays;

public class Matrix {
    public static final int defaultAllocation = 4;
    private float[][] matrix;
    private int nRows, nColumns;
    private void initMatrix()
    {
        matrix = new float[nRows][nColumns];
    }
    public Matrix ()
    {
        nRows=defaultAllocation;
        nColumns=defaultAllocation;
        initMatrix();
    }
    public Matrix(int r, int c)
    {
        nRows=r;
        nColumns=c;
        initMatrix();
    }
    public Matrix (Vec3f u, boolean isPoint)
    {
        nRows = 4;
        nColumns = 1;
        initMatrix();
        matrix[0][0] = u.x();
        matrix[1][0] = u.y();
        matrix[2][0] = u.z();
        matrix[3][0] = isPoint? 1.f : 0.f;
    }
    //TODO error handling : all indices should be positive
    public float[] getRow(int i)
    {
        return matrix[i];
    }
    public float getElement(int i, int j){return matrix[i][j];}
    public void setElement(int i, int j, float val){matrix[i][j]=val;}
    public int nRows()
    {
        return nRows;
    }
    public int nColumns()
    {
        return nColumns;
    }
    public static Matrix getIdentityMatrix(int dimensions)
    {   //initializes a matrix with 1.0 on the diagonal
        Matrix I = new Matrix(dimensions, dimensions);
        for (int i = 0; i<dimensions; i++)
        {
            for (int j = 0; j<dimensions; j++)
            {
                I.matrix[i][j]= i==j? 1.0f : 0.0f;
            }
        }
        return I;
    }
    public void printMatrix()
    {
        for (int i = 0; i<nRows;i++)
        {
            for (int j = 0; j<nColumns;j++)
            {
                if (j==nColumns-1)
                    System.out.print(matrix[i][j]);
                else
                    System.out.print(matrix[i][j] + ", ");
            }
            System.out.print('\n');
        }
    }
    public Matrix transpose ()
    {
        Matrix resultMatrix = new Matrix(nColumns, nRows);
        for (int i = 0; i<nRows; i++)
        {
            for(int j = 0; j<nColumns; j++)
            {
                resultMatrix.setElement(j, i, matrix[i][j]);
            }
        }
        return resultMatrix;
    }
    public Matrix inverse()
    {   //TODO error handling : nRows == nColumns
        Matrix resultMatrix = new Matrix(nRows, nColumns*2);
        for (int i = 0; i<nRows;i++)
        {
            for (int j = 0; j<nColumns; j++)
            {
                resultMatrix.setElement(i,j, matrix[i][j]);
            }
        }
        for (int i =0; i<nRows; i++)
        {
            resultMatrix.setElement(i, i+nColumns, 1.0f);
        }
        for (int i = 0; i<nRows-1; i++)
        {
            for (int j = resultMatrix.nColumns()-1; j>=0; j--)
            {
                resultMatrix.matrix[i][j] /= resultMatrix.matrix[i][i];
            }
            for(int k = i+1;k<nRows; k++)
            {
                float coefficient = resultMatrix.getElement(k,i);
                for (int j = 0; j< resultMatrix.nColumns; j++)
                {
                    resultMatrix.matrix[k][j]-= resultMatrix.matrix[i][j]*coefficient;
                }
            }
        }
        for (int j = resultMatrix.nColumns-1; j>nRows-1; j--)
        {
            resultMatrix.matrix[nRows-1][j]/=resultMatrix.matrix[nRows-1][nRows-1];
        }
        for (int i = nRows-1; i>0;i--)
        {
            for (int k = i-1; k>=0; k--)
            {
                float coefficient = resultMatrix.getElement(k,i);
                for (int j = 0; j< resultMatrix.nColumns;j++)
                {
                    resultMatrix.matrix[k][j]-=resultMatrix.matrix[i][j]*coefficient;
                }
            }
        }
        Matrix truncate = new Matrix(nRows, nColumns);
        for (int i =0;i<nRows;i++)
        {
            for (int j = 0; j<nColumns; j++)
            {
                truncate.matrix[i][j]=resultMatrix.matrix[i][j+nColumns];
            }
        }
        return  truncate;
    }//TODO misc : add toString method
}
