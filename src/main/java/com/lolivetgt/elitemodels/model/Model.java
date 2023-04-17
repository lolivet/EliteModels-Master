package com.lolivetgt.elitemodels.model;

import java.util.List;

public class Model {
    private List<Bone> bones;
    private List<Element> elements;
    private List<Texture> textures;
    private List<Animation> animations;

    public List<Bone> getBones() {
        return bones;
    }

    public void setBones(List<Bone> bones) {
        this.bones = bones;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public void setTextures(List<Texture> textures) {
        this.textures = textures;
    }

    public List<Animation> getAnimations() {
        return animations;
    }

    public void setAnimations(List<Animation> animations) {
        this.animations = animations;
    }
}
