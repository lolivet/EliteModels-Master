package com.lolivetgt.elitemodels.model;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private String name;
    private int length;
    private List<Keyframe> keyframes;

    public Animation(String name, int length) {
        this.name = name;
        this.length = length;
        this.keyframes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public List<Keyframe> getKeyframes() {
        return keyframes;
    }

    public void addKeyframe(Keyframe keyframe) {
        if (keyframes == null) {
            keyframes = new ArrayList<>();
        }
        keyframes.add(keyframe);
    }

}
