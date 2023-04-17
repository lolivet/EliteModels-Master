package com.lolivetgt.elitemodels.model;

public class Bone {
    private int boneId;
    private String name;
    private int parent;
    private float[] pivot;
    private float[] rotation;

    public Bone(int boneId, String boneName, int parentBoneId, float posX, float posY, float posZ, float rotX,
            float rotY, float rotZ) {
        this.boneId = boneId;
        this.name = boneName;
        this.parent = parentBoneId;
        this.pivot = new float[] { posX, posY, posZ };
        this.rotation = new float[] { rotX, rotY, rotZ };
    }

    public int getBoneId() {
        return boneId;
    }

    public String getName() {
        return name;
    }

    public int getParent() {
        return parent;
    }

    public float[] getPivot() {
        return pivot;
    }

    public float[] getRotation() {
        return rotation;
    }
}
