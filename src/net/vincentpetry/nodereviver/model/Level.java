package net.vincentpetry.nodereviver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.vincentpetry.nodereviver.util.PathFinder;

/**
 * Level model.
 * 
 * Contains nodes, edges and entities.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class Level {
    private PathFinder pathFinder;
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Entity> entities;
    boolean dirty;
    int markedEdges;
    private String title;
    private String subtitle;
    private String endtext;
    private Node playerStartNode;

    public Level() {
        this.nodes = new ArrayList<Node>(50);
        this.edges = new ArrayList<Edge>(50);
        this.entities = new ArrayList<Entity>(10);
        this.markedEdges = 0;
        this.title = null;
        this.subtitle = null;
        this.endtext = null;
        this.dirty = false;
        this.playerStartNode = null;
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }
    
    public void addEntity(Entity entity){
        entities.add(entity);
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(this.edges);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getEndtext() {
        return endtext;
    }

    public void setEndtext(String endtext) {
        this.endtext = endtext;
    }

    public Node createNode(int x, int y, int nodeType) {
        Node node = new Node(this, x, y, nodeType);
        this.nodes.add(node);
        this.dirty = true;
        return node;
    }

    public Edge connectNodes(Node node1, Node node2) {
        Edge edge = node1.connect(node2);
        this.edges.add(edge);
        this.dirty = true;
        return edge;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void resetDirty() {
        this.dirty = false;
    }

    /**
     * Translate the coordinates of all nodes and entities.
     */
    public void translate(int dx, int dy) {
        this.dirty = true;
        for (Node node : this.nodes) {
            node.x += dx;
            node.y += dy;
        }
        for (Entity entity : this.entities) {
            entity.x += dx;
            entity.y += dy;
        }
    }

    public void centerInView(int width, int height) {
        int[] rect = getRect();
        int offsetX = (rect[2] - rect[0]) / 2;
        int offsetY = (rect[3] - rect[1]) / 2;
        offsetX = width / 2 - offsetX - rect[0];
        offsetY = height / 2 - offsetY - rect[1];
        // offset must be a multiple of 20
        offsetX = offsetX / 20 * 20;
        offsetY = offsetY / 20 * 20;
        this.translate(offsetX, offsetY);
    }

    /**
     * Returns whether all edges have been marked.
     * 
     * @return
     */
    public boolean hasAllEdgesMarked() {
        return this.markedEdges == this.edges.size();
    }

    /**
     * Returns the smallest possible rectangle that includes
     * all nodes. 
     * @return array of 4 ints representing the top-left and
     * bottom-right x-y coordinates
     */
    private int[] getRect() {
        int[] rect = new int[4];
        for (int i = 0; i < rect.length; i++) {
            rect[i] = 0;
        }

        if (this.nodes.size() == 0) {
            return rect;
        }
        
        Iterator<Node> nodeIt = this.nodes.iterator();
        Node firstNode = nodeIt.next();
        rect[0] = rect[2] = firstNode.x;
        rect[1] = rect[3] = firstNode.y;
        while (nodeIt.hasNext()){
            Node aNode = nodeIt.next();
            if (aNode.x < rect[0]){
                rect[0] = aNode.x;
            }
            if (aNode.x > rect[2]){
                rect[2] = aNode.x;
            }
            if (aNode.y < rect[1]){
                rect[1] = aNode.y;
            }
            if (aNode.y > rect[3]){
                rect[3] = aNode.y;
            }            
        }

        return rect;
    }

    public void setPlayerStartNode(Node startNode) {
        if ( startNode == null ){
            throw new IllegalArgumentException("Argument startNode must not be null");
        }
        this.playerStartNode = startNode;
    }

    public Node getPlayerStartNode() {
        return playerStartNode;
    }

    public Entity createSimpleFoe(Node startNode){
        if ( startNode == null ){
            throw new IllegalArgumentException("Argument startNode must not be null");
        }
        Entity entity = new SimpleFoe(startNode);
        this.entities.add(entity);
        this.dirty = true;
        return entity;
    }

    public Entity createTrackingFoe(Node startNode){
        if ( startNode == null ){
            throw new IllegalArgumentException("Argument startNode must not be null");
        }
        Entity entity = new TrackingFoe(startNode, null, this);
        this.entities.add(entity);
        this.dirty = true;
        return entity;
    }
    
    /**
     * Returns the shortest path between start node and end node.
     * @param startNode start node
     * @param endNode end node
     * @return list of edges to travel along from node to node
     * (excluding joint nodes)
     */
    public List<Edge> findShortestPath(Node startNode, Node endNode, List<Edge> path){
        if ( pathFinder == null ){
            pathFinder = new PathFinder(this);
        }
        return pathFinder.findShortestPath(startNode, endNode, path);
    }
}
