package ex2.src.api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class DWGraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();

        JsonObject vertexObj = jsonObject.get("vertex").getAsJsonObject();

        for (Map.Entry<String, JsonElement> set : vertexObj.entrySet()) {

            JsonElement jsonValueElement = set.getValue();
            int id = jsonValueElement.getAsJsonObject().get("idNode").getAsInt();
            node_data n = new NodeData(id);
            graph.addNode(n);
        }

        JsonObject edgesObj = jsonObject.get("edges").getAsJsonObject();

        for (Map.Entry<String, JsonElement> set : edgesObj.entrySet()) {

            JsonElement jsonValueElement = set.getValue();
            int src = jsonValueElement.getAsJsonObject().get("src").getAsInt();
            int dest = jsonValueElement.getAsJsonObject().get("dest").getAsInt();
            double weight = jsonValueElement.getAsJsonObject().get("weight").getAsDouble();
            graph.connect(src, dest, weight);
        }

        return graph;
    }
}
