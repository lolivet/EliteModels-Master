package com.lolivetgt.elitemodels.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lolivetgt.elitemodels.model.Element;
import com.lolivetgt.elitemodels.model.Model;

public class ModelJSONGenerator {

    public static String generateModelJSON(Model model) {
        JsonObject root = new JsonObject();

        JsonArray elementsArray = new JsonArray();
        for (Element element : model.getElements()) {
            JsonObject elementJson = new JsonObject();
            elementJson.addProperty("name", element.getName());
            elementJson.add("from", jsonArrayFromFloatArray(element.getFrom()));
            elementJson.add("to", jsonArrayFromFloatArray(element.getTo()));
            elementJson.addProperty("rotation", element.getRotation());
            elementJson.addProperty("scale", element.getScale());
            elementJson.addProperty("texture", element.getTexture());

            JsonObject facesJson = new JsonObject();
            int[] faces = element.getFaces();
            for (int i = 0; i < faces.length; i++) {
                JsonObject faceJson = new JsonObject();
                faceJson.add("uv", jsonArrayFromFloatArray(element.getUV()));
                faceJson.addProperty("texture", "#" + faces[i]);
                facesJson.add("face" + i, faceJson);
            }
            elementJson.add("faces", facesJson);

            elementsArray.add(elementJson);
        }
        root.add("elements", elementsArray);

        return root.toString();
    }

    private static JsonArray jsonArrayFromFloatArray(float[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (float value : arr) {
            jsonArray.add(value);
        }
        return jsonArray;
    }
}
