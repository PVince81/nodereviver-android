package net.vincentpetry.nodereviver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetManager;

import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Node;

public class LevelLoader {

    public LevelLoader(){
    }

    private String fixNewlines(String s){
        // it seems StaticLayout expects \r\n instead of \n
        return s.replace("\\n", "\n");
    }

    public Level load(AssetManager assetManager, int levelNum) throws IOException{
        InputStream input = assetManager.open("levels/level" + levelNum + ".json");
        try{
            byte[] buf = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while (input.read(buf) > 0) {
                sb.append(new String(buf));
            }
            return load(sb.toString());
        }
        catch (JSONException e){
            return null;
        }
        finally{
            input.close();
        }
    }

    private Level load(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);

        Level level = new Level();

        level.setTitle(fixNewlines(root.optString("title")));
        level.setSubtitle(fixNewlines(root.optString("subtitle")));
        level.setEndtext(fixNewlines(root.optString("endtext")));

        Map<Integer,Node> nodesMap = processNodes(level, root.getJSONArray("nodes"));
        processEdges(level, nodesMap, root.getJSONArray("edges"));
        processEntities(level, nodesMap, root.getJSONArray("entities"));
        return level;
    }

    private Map<Integer,Node> processNodes(Level level, JSONArray jsonNodes) throws JSONException{
        Map<Integer,Node> nodesMap = new HashMap<Integer,Node>(jsonNodes.length());
        for ( int i = 0; i < jsonNodes.length(); i++ ){
            JSONObject jsonNode = jsonNodes.getJSONObject(i);
            String type = jsonNode.optString("type", "node");
            if ( !"node".equals(type) && !"joint".equals(type) ){
                continue;
            }
            int id = jsonNode.optInt("id", -1);
            int x = jsonNode.getInt("x");
            int y = jsonNode.getInt("y");
            int nodeType = Node.TYPE_SQUARE;
            if ("joint".equals(type)){
                nodeType = Node.TYPE_JOINT;
            }
            Node node = level.createNode(x, y, nodeType);
            if ( id != -1 ){
                nodesMap.put(id, node);
            }
        }
        return nodesMap;
    }

    private void processEdges(Level level, Map<Integer,Node> nodesMap, JSONArray jsonEdges) throws JSONException{
        for ( int i = 0; i < jsonEdges.length(); i++ ){
            JSONObject jsonEdge = jsonEdges.getJSONObject(i);
            int sourceId = jsonEdge.getInt("source");
            int targetId = jsonEdge.getInt("dest");
            Node node1 = nodesMap.get(sourceId);
            Node node2 = nodesMap.get(targetId);
            if ( node1 == null || node2 == null ) {
                continue;
            }
            boolean oneWay = jsonEdge.optBoolean("oneway", false);
            Edge edge = level.connectNodes(node1, node2);
            edge.setOneWay(oneWay);
        }
    }

    private void processEntities(Level level, Map<Integer,Node> nodesMap, JSONArray jsonEntities) throws JSONException{
        for ( int i = 0; i < jsonEntities.length(); i++ ){
            JSONObject jsonEntity = jsonEntities.getJSONObject(i);
            int startNodeId = jsonEntity.getInt("node");
            String type = jsonEntity.getString("type");
            Node startNode = nodesMap.get(startNodeId);
            if ( startNode == null ){
                continue;
            }

            if ("player".equals(type)){
                level.setPlayerStartNode(startNode);
            }
            else if ("foe".equals(type)){
                String foeType = jsonEntity.getString("foeType");
                if ( "simple".equals(foeType) || "0".equals(foeType) ){
                    level.createSimpleFoe(startNode);
                }
                else{
                    level.createTrackingFoe(startNode);
                }
            }
        }
    }
}
