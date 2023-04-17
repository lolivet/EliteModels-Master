package com.lolivetgt.elitemodels.util;

import com.google.gson.JsonObject;
import com.lolivetgt.elitemodels.model.Texture;

public class TextureJSONGenerator {

    public static String generateTextureJSON(Texture texture) {
        JsonObject root = new JsonObject();
        root.addProperty("path", texture.getPath());
        root.addProperty("width", texture.getWidth());
        root.addProperty("height", texture.getHeight());
        root.addProperty("u", texture.getU());
        root.addProperty("v", texture.getV());
        root.addProperty("scale", texture.getScale());
        root.addProperty("mirror", texture.isMirror());

        return root.toString();
    }
}
