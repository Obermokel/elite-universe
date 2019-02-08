package borg.ed.galaxy.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import borg.ed.galaxy.data.Coord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * GsonCoord
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class GsonCoord implements JsonSerializer<Coord>, JsonDeserializer<Coord> {

    static final Logger logger = LoggerFactory.getLogger(GsonCoord.class);

    @Override
    public JsonElement serialize(Coord src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(src.getX());
        jsonArray.add(src.getY());
        jsonArray.add(src.getZ());
        return jsonArray;
    }

    @Override
    public Coord deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        float x = jsonArray.get(0).getAsFloat();
        float y = jsonArray.get(1).getAsFloat();
        float z = jsonArray.get(2).getAsFloat();
        return new Coord(x, y, z);
    }

}
