package net.vincentpetry.nodereviver.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Node;

/**
 * Path finder algorithm using A*
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class PathFinder {
    private Set<Node> closedSet;
    private Set<Node> openSet;
    private Map<Node,NodeData> nodeData;
    
    private List<Edge> path;
    
    private class NodeData{
        Node node;
        Edge cameFrom;
        int gScore;
        int hScore;
        int fScore;
        
        public NodeData(Node node){
            this.node = node;
            reset();
        }
        
        public void reset(){
            cameFrom = null;
            gScore = 0;
            hScore = 0;
            fScore = 0;
        }
        
        public String toString(){
            return "NodeData{node=" + node.toString() + 
                    ",fScore=" + fScore +
                    ",gScore=" + gScore +
                    ",hScore=" + hScore +
                    ",cameFrom=" + cameFrom +
                    "}";
        }
    }
    
    public PathFinder(Level level) {
        init(level);
    }
    
    public void setLevel(Level level){
        init(level);
    }

    private void init(Level level){
        // pre-init sets to prevent garbage collection
        int maxNodes = level.getNodes().size();
        path = new ArrayList<Edge>(level.getEdges().size());
        closedSet = new HashSet<Node>(maxNodes);
        openSet = new HashSet<Node>(maxNodes);
        nodeData = new HashMap<Node,NodeData>(maxNodes);
        for ( Node node: level.getNodes() ){
            nodeData.put(node, new NodeData(node));
        }
    }
    
    private void clear(){
        closedSet.clear();
        openSet.clear();
        path.clear();
        for ( NodeData data: nodeData.values() ){
            data.reset();
        }
    }

    private int dist(Node source, Node target) {
        return Math.abs(source.getX() - target.getX() + source.getY()
                - target.getY());
    }

    private NodeData lowest(Collection<Node> nodes) {
        int min = Integer.MAX_VALUE;
        NodeData curNode = null;
        for (Node node : nodes) {
            NodeData data = nodeData.get(node);
            if (data.fScore < min) {
                curNode = data;
                min = data.fScore;
            }
        }
        return curNode;
    }
    
    /**
     * Finds the shortest path from start node to goal.
     * @param start
     * @param goal
     * @return list of edges to travel along from node to node
     * (excluding joint nodes)
     */
    public List<Edge> findShortestPath( Node start, Node goal, List<Edge> path ){
        // THANKS WIKIPEDIA FOR THE A* ALGORITHM!
        if ( path == null ){
            path = this.path;
        }
        clear();
        
        openSet.add(start);
        NodeData startData = nodeData.get(start);
        // Cost from start along best known path
        startData.gScore = 0;
        startData.hScore = dist(start, goal);
        // Estimated total cost from start to goal through y.
        startData.fScore = startData.gScore + startData.hScore;
        while ( openSet.size() > 0 ){
            // the node in openset having the lowest fScore value
            NodeData currentData = lowest(openSet);
            if ( currentData.node == goal ){
                return reconstructPath(goal, path);
            }
            
            openSet.remove(currentData.node);
            closedSet.add(currentData.node);
            
            for( Edge edge: currentData.node.getOutgoingEdges()){
                boolean tentativeIsBetter = false;
                int tentativeGScore;
                NodeData neighborData;
                Node neighbor = edge.getOtherNode(currentData.node);
                if ( closedSet.contains(neighbor)) {
                    continue;
                }
                neighborData = nodeData.get(neighbor);
                tentativeGScore = currentData.gScore + edge.getLength();
                
                if (!openSet.contains(neighbor)){
                    openSet.add(neighbor);
                    neighborData.hScore = dist(neighbor, goal);
                    tentativeIsBetter = true;
                }
                else if ( tentativeGScore < neighborData.gScore ){
                    tentativeIsBetter = true;
                }
                else{
                    tentativeIsBetter = false;
                }
                
                if ( tentativeIsBetter ){
                    neighborData.cameFrom = edge;
                    neighborData.gScore = tentativeGScore;
                    neighborData.fScore = neighborData.gScore + neighborData.hScore; 
                }
            }
        }
        return null;
    }
    
    /**
     * Reconstructs the path from start to node
     * @param node goal node
     * @return list of edges to travel along from the start node
     */
    private List<Edge> reconstructPath(Node node, List<Edge> path){
        path.clear();
        Edge edge;
        NodeData data = nodeData.get(node);
        while ( data.cameFrom != null ){
            edge = data.cameFrom;
            node = edge.getOtherNode(node);
            data = nodeData.get(node);
            // exclude joint nodes
            if ( node.getType() != Node.TYPE_JOINT ){
                path.add(0, edge);
            }
        }
        return path;
    }
}
