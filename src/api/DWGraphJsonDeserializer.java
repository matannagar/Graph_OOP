package ex2.src.api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class DWGraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();

        JsonArray vertexObj = jsonObject.get("Nodes").getAsJsonArray();

        for (int i=0; i<vertexObj.size(); i++) {

            JsonElement jsonValueElement = vertexObj.get(i);
            int id = jsonValueElement.getAsJsonObject().get("id").getAsInt();
            node_data n = new NodeData(id);
            graph.addNode(n);
        }

        JsonArray edgesObj = jsonObject.get("Edges").getAsJsonArray();
        for (int i=0; i<edgesObj.size(); i++) {

            JsonElement jsonValueElement = edgesObj.get(i);
            int src = jsonValueElement.getAsJsonObject().get("src").getAsInt();
            int dest = jsonValueElement.getAsJsonObject().get("dest").getAsInt();
            double weight = jsonValueElement.getAsJsonObject().get("w").getAsDouble();
            graph.connect(src, dest, weight);
        }

        return graph;
    }
}
