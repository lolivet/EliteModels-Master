package com.lolivetgt.elitemodels.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BBModelReader {

    public static Model readModel(File file) throws IOException {
        Model model = new Model();
        List<Bone> bones = new ArrayList<>();
        List<Element> elements = new ArrayList<>();
        List<Texture> textures = new ArrayList<>();
        List<Animation> animations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("bone")) {
                    Bone bone = parseBone(line);
                    bones.add(bone);
                } else if (line.startsWith("cube")) {
                    Element element = parseElement(line);
                    elements.add(element);
                } else if (line.startsWith("texture")) {
                    Texture texture = parseTexture(line);
                    textures.add(texture);
                } else if (line.startsWith("animation")) {
                    Animation animation = parseAnimation(line);
                    animations.add(animation);
                }
            }
        }

        model.setBones(bones);
        model.setElements(elements);
        model.setTextures(textures);
        model.setAnimations(animations);
        return model;
    }

    private static Bone parseBone(String line) {
        String[] parts = line.split(" ");
        int boneId = Integer.parseInt(parts[1]);
        String boneName = parts[2];
        int parentBoneId = Integer.parseInt(parts[3]);
        float posX = Float.parseFloat(parts[4]);
        float posY = Float.parseFloat(parts[5]);
        float posZ = Float.parseFloat(parts[6]);
        float rotX = Float.parseFloat(parts[7]);
        float rotY = Float.parseFloat(parts[8]);
        float rotZ = Float.parseFloat(parts[9]);
        Bone bone = new Bone(boneId, boneName, parentBoneId, posX, posY, posZ, rotX, rotY, rotZ);
        return bone;
    }

    private static Element parseElement(String line) {
        String[] tokens = line.split("\\s+"); // Separa la línea en palabras
        if (tokens.length < 8) {
            throw new IllegalArgumentException("Invalid cube format: " + line);
        }

        // Obtiene los datos de la posición y tamaño del cubo
        float x1 = Float.parseFloat(tokens[2]);
        float y1 = Float.parseFloat(tokens[3]);
        float z1 = Float.parseFloat(tokens[4]);
        float x2 = Float.parseFloat(tokens[5]);
        float y2 = Float.parseFloat(tokens[6]);
        float z2 = Float.parseFloat(tokens[7]);

        // Crea el elemento
        Element element = new Element(null, null, null, null, null, false);
        element.setFrom(x1, y1, z1);
        element.setTo(x2, y2, z2);

        // Si se especifica la rotación, la añade al elemento
        if (tokens.length >= 9) {
            int rotation = Integer.parseInt(tokens[8]);
            element.setRotation(rotation);
        }

        // Si se especifica la escala, la añade al elemento
        if (tokens.length >= 10) {
            float scale = Float.parseFloat(tokens[9]);
            element.setScale(scale);
        }

        // Si se especifica la textura, la añade al elemento
        if (tokens.length >= 11) {
            String texture = tokens[10];
            element.setTexture(texture);
        }

        return element;
    }

    private static Texture parseTexture(String line) {
        String[] parts = line.split(" ");
        String name = parts[1];

        Texture texture = new Texture(name);

        for (int i = 2; i < parts.length; i++) {
            String[] subParts = parts[i].split("=");
            String key = subParts[0];
            String value = subParts[1];

            switch (key) {
                case "width":
                    texture.setWidth(Integer.parseInt(value));
                    break;
                case "height":
                    texture.setHeight(Integer.parseInt(value));
                    break;
                case "u":
                    texture.setU(Float.parseFloat(value));
                    break;
                case "v":
                    texture.setV(Float.parseFloat(value));
                    break;
                case "scale":
                    texture.setScale(Float.parseFloat(value));
                    break;
                case "mirror":
                    texture.setMirror(Boolean.parseBoolean(value));
                    break;
                default:
                    break;
            }
        }

        return texture;
    }

    private static Animation parseAnimation(String line) {
        String[] parts = line.split(" ");
        String name = parts[1];
        int length = Integer.parseInt(parts[2]);

        Animation animation = new Animation(name, length);

        for (int i = 3; i < parts.length; i++) {
            String[] keyframeParts = parts[i].split("=");
            int time = Integer.parseInt(keyframeParts[0]);

            String[] poseParts = keyframeParts[1].split(",");
            float[] pose = new float[poseParts.length - 1]; // Ajusta el tamaño del array
            int boneId = Integer.parseInt(poseParts[0]); // Extrae el ID de hueso

            // Llena el array pose
            for (int j = 1; j < poseParts.length; j++) {
                pose[j - 1] = Float.parseFloat(poseParts[j]);
            }

            animation.addKeyframe(new Keyframe(time, boneId, pose, pose)); // Cambia esta línea para incluir 'time'

        }

        return animation;
    }

}
