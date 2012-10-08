package net.vincentpetry.nodereviver.model;

import net.vincentpetry.nodereviver.util.Vector;

public class Edge {
    private static int lastId = 1;
    
    private int id;
    private Level level;
    private Node sourceNode;
    private Node targetNode;
    private boolean oneWay;
    private boolean marked;
    private int length;
    
    public Edge(Level level, Node sourceNode, Node targetNode, boolean oneWay) {
        this.id = lastId;
        lastId++;
        this.level = level;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.oneWay = oneWay;
        this.marked = false;
        Vector diff = new Vector(sourceNode.x, sourceNode.y);
        diff.subst(targetNode.x, targetNode.y);
        // assuming edge are always either horizontal or vertical
        if (diff.x == 0){
            this.length = Math.abs(diff.y);
        }
        else {
            this.length = Math.abs(diff.x);
        }
    }

    /**
     * Returns the opposite node of the given node,
     * assuming that the given node is either the source
     * or the target of this edge.
     * @param node opposite node
     * @return opposite node or null if the given node
     * isn't connected with this edge
     */
    public Node getOtherNode(Node node) {
        if ( node.equals(this.sourceNode) ){
            return this.targetNode;
        }
        else if ( node.equals(this.targetNode) ){
            return this.sourceNode;
        }
        return null;
    }
    
    public void reverse(){
        Node aux = this.sourceNode;
        this.sourceNode = this.targetNode;
        this.targetNode = aux;
    }

    public void mark() {
        if ( !this.marked ) {
            this.marked = true;
            // FIXME: do this in level directly?
            this.level.dirty = true;
            this.level.markedEdges++;
        }
    }

    public boolean isMarked(){
        return marked;
    }
    
    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public boolean isOneWay(){
        return oneWay;
    }
    
    public Node getSourceNode(){
        return sourceNode;
    }
    
    public Node getTargetNode(){
        return targetNode;
    }

    /**
     * Length of the edge.
     * @return edge length
     */
    public int getLength(){
        return length;
    }
    
    @Override
    public int hashCode() {
        final int prime = 38;
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
        Edge other = (Edge) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Edge{id=" + id +
                ",src=" + sourceNode +
                ",dst=" + targetNode +
                ",len=" + length +
                "}";
    }
}
