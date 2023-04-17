package com.lolivetgt.elitemodels.model;


public class Element {
    private String name;
    private float[] from;
    private float[] to;
    private int[] faces;
    private float[] uv;
    private boolean shade;
    private int rotation;
    private float scale;
    private String texture;

    public Element(String name, float[] from, float[] to, int[] faces, float[] uv, boolean shade) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.faces = faces;
        this.uv = uv;
        this.shade = shade;
    }

    public String getName() {
        return name;
    }

    public float[] getFrom() {
        return from;
    }

    public float[] getTo() {
        return to;
    }

    public int[] getFaces() {
        return faces;
    }

    public float[] getUV() {
        return uv;
    }

    public boolean getShade() {
        return shade;
    }

    public void setFrom(float x1, float y1, float z1) {
        this.from = new float[] { x1, y1, z1 };
    }

    public void setTo(float x2, float y2, float z2) {
        this.to = new float[] { x2, y2, z2 };
    }

    public void setRotation(int rotation) {
        // Asumiendo que tienes un miembro "rotation" en la clase Element
        this.rotation = rotation;
    }

    public void setScale(float scale) {
        // Asumiendo que tienes un miembro "scale" en la clase Element
        this.scale = scale;
    }

    public void setTexture(String texture) {
        // Asumiendo que tienes un miembro "texture" en la clase Element
        this.texture = texture;
    }

    public int getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public String getTexture() {
        return texture;
    }

}
