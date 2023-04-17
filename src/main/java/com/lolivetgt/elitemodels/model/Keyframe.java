package com.lolivetgt.elitemodels.model;

public class Keyframe {
    private int time;
    private int bone;
    private float[] position;
    private float[] rotation;

    public Keyframe(int time, int bone, float[] position, float[] rotation) {
        this.time = time;
        this.bone = bone;
        this.position = position;
        this.rotation = rotation;
    }

    // Agrega un getter para el campo 'time'
    public int getTime() {
        return time;
    }

    public int getBone() {
        return bone;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getRotation() {
        return rotation;
    }
}
