package com.lolivetgt.elitemodels.model;

public class Texture {
    private String name;
    private String path;
    private int width;
    private int height;
    private float u;
    private float v;
    private float scale;
    private boolean mirror;

    public Texture(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Texture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setU(float u) {
        this.u = u;
    }

    public void setV(float v) {
        this.v = v;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    // Agrega métodos getter para los campos nuevos
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getScale() {
        return scale;
    }

    public boolean isMirror() {
        return mirror;
    }
}
