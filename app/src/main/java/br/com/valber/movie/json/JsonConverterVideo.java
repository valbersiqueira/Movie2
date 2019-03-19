package br.com.valber.movie.json;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class JsonConverterVideo implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement popular =    json.getAsJsonObject();
        if (json.getAsJsonObject() != null) {
            popular = json.getAsJsonObject();
        }

        return (new Gson().fromJson(popular, ResultVideo.class));
    }
}
