package model.math;

public abstract class Vector {
    public Vector(Number... params) {
        raw = new Number[params.length];
        System.arraycopy(params, 0, raw, 0, params.length);
    }

    public int getDimensions() {
        return raw.length;
    }

    public abstract Vector neg();

    public abstract Vector getNormalized();

    protected float[] getNormalizedCoords() { // not the ideal implementation but I had to code this up quickly
        float[] normalizedCoords = new float[getDimensions()];
        for (int i = 0; i < getDimensions(); i++) {
            normalizedCoords[i] = raw[i].floatValue() / magnitude();
        }
        return normalizedCoords;
    }

    public float magnitude() {
        float sum = 0;
        for (int i = 0; i < getDimensions(); i++) {
            sum += raw[i].floatValue() * raw[i].floatValue();
        }
        return (float) Math.sqrt(sum);
    }

    @Override
    public String toString() {
        String returnValue = "";
        for (int i = 0; i < this.getDimensions(); i++) {
            returnValue += raw[i] + " ";
        }
        return returnValue;
    }

    // fields
    // TODO find a way to make these final
    protected Number[] raw;

    protected Number x() {
        return raw[0];
    }

    protected Number y() {
        return raw[1];
    }

    protected Number z() {
        return raw[2];
    }

    protected Number w() {
        return raw[3];
    }
}
