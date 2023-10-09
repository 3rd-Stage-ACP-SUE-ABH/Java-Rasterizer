package Vector;

public class Vector
{
    public Vector(Number...params)
    {
        raw = new Number[params.length];
        System.arraycopy(params, 0, raw, 0, params.length);
    }
    public int getDimensions()
    {
        return raw.length;
    }

    @Override
    public String toString()
    {
        String returnValue = "";
        for (int i =0; i<this.getDimensions(); i++)
        {
            returnValue += raw[i]+" ";
        }
        return returnValue;
    }
    //fields
    protected final Number[] raw;
    protected Number x(){return raw[0];}
    protected Number y(){return raw[1];}
    protected Number z(){return raw[2];}
    protected Number w(){return raw[3];}
}

