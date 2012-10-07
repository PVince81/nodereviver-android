package net.vincentpetry.nodereviver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.vincentpetry.nodereviver.util.Vector;

public class Node {
    private static int lastId = 1;
    
    public static final int TYPE_SQUARE = 0;
    public static final int TYPE_JOINT = 1;
    
    private int id;
    private Level level;
    int x;
    int y;
    int type;
    private boolean deleted;
    private boolean marked;
    private List<Edge> edges;
    private List<Edge> outgoingEdges;
    
    public Node(Level level, int x, int y, int nodeType){
        this.id = lastId;
        lastId++;
        this.level = level;
        this.x = x;
        this.y = y;
        this.type = nodeType;
        this.deleted = false;
        this.marked = false;
        this.edges = new ArrayList<Edge>(4);
        this.outgoingEdges = new ArrayList<Edge>(4);
    }
    
    /**
     * Connects the current node to the other node
     * and returns the generated edge.
     * @param targetNode target node
     * @return generated edge
     */
    public Edge connect(Node targetNode){
        return connect(targetNode, false);
    }

    /**
     * Connects the current node to the other node
     * and returns the generated edge.
     * @param targetNode target node
     * @param oneWay whether the new edge is directed
     * @return generated edge
     **/
    public Edge connect(Node targetNode, boolean oneWay){
        for (Edge edge: this.edges){
            if (edge.getOtherNode(this) == targetNode){
                // already connected
                return edge;
            }
        }
        Edge edge = new Edge(this.level, this, targetNode, oneWay);
        this.edges.add(edge);
        targetNode.edges.add(edge);
        
        this.outgoingEdges.add(edge);
        if ( !oneWay ) {
            targetNode.outgoingEdges.add(edge);
        }
        return edge;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    /**
     * Returns the list of all outgoing edges which are
     * the edges that can be navigated to from this node.
     * This excludes one way edges which have this node
     * as target.
     * @return
     */
    public List<Edge> getOutgoingEdges(){
        return Collections.unmodifiableList(outgoingEdges);
    }

    /**
     * Returns the edge going in the given direction and is an outgoing edge
     * or None if none found.
     * @param direction: direction vector
     * @return edge or null if none found
     */
    public Edge getEdgeByDirection(int vx, int vy){
        // TODO: there might be a better way
        // where directions are cached into a four-index array
        // where each index corresponds to a fix direction
        Vector direction = new Vector(vx, vy);
        direction.unit();
        for ( Edge edge: this.edges ){
            if (edge.isOneWay() && edge.getTargetNode().equals(this) ){
                continue;
            }
            Node destNode = edge.getOtherNode(this);
            Vector diff = new Vector(destNode.x, destNode.y);
            diff.subst(this.x, this.y).unit();
            if (direction.x == diff.x && direction.y == diff.y){
                return edge;
            }
        }
        return null;
    }

    /**
     * Returns any adge that is not sourceEdge
     * @param sourceEdge
     * @return edge or null if none found
     */
    public Edge getOtherEdge(Edge sourceEdge){
        for (Edge edge: this.edges){
            if (edge != sourceEdge){
                return edge;
            }
        }
        return null;
    }
    
    /**
     * Returns the next non-joint node following the path
     * along the given edge.
     * If the next node is a joint, the node after that will
     * be considered.
     * @param edge edge to use as direction 
     * @return target node
     */
    public Node getNextNode(Edge edge){
        Node nextNode = edge.getOtherNode(this);
        while (nextNode.type != TYPE_JOINT && nextNode != this){
            nextNode = nextNode.getNextNode(nextNode.getOtherEdge(edge));
        }
        return nextNode;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString(){
        String typeString = "S";
        if (this.type == TYPE_JOINT){
            typeString = "J";
        }
        return "Node{id=" + id + ",x="+x+",y="+y+",type="+typeString+"}";
    }

    public int getType() {
        return type;
    }
}
