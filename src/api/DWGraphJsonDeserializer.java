package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;

public class DWGraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();

        JsonArray vertexObj = jsonObject.get("Nodes").getAsJsonArray();

        for (int i=0; i<vertexObj.size(); i++) {

            JsonElement jsonValueElement = vertexObj.get(i);
            int id = jsonValueElement.getAsJsonObject().get("id").getAsInt();
            String s = jsonValueElement.getAsJsonObject().get("pos").getAsString();
            String [] arr= s.split(",");

            double x=Double.valueOf(arr[0]);
            double y=Double.valueOf(arr[1]);
            double z=Double.valueOf(arr[2]);

            geo_location ge= new GeoLocation(x,y,z);
            node_data n = new NodeData(id,ge);
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
