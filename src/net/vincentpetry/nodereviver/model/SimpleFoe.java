package net.vincentpetry.nodereviver.model;

import java.util.List;

import net.vincentpetry.nodereviver.util.Random;

/**
 * Simple foe that randomly changes direction.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class SimpleFoe extends Entity {
    private Edge lastEdge;
    
    public SimpleFoe(Node currentNode){
        super(currentNode);
        this.speed = 1;
        this.lastEdge = null;
    }
    
    
    
    @Override
    public void update() {
        if ( this.isMoving() ){
            super.update();
        }
        else {
            // find path
            List<Edge> nextEdges = this.currentNode.getOutgoingEdges();
            int index = Random.randInt(0, nextEdges.size() - 1);
            Edge nextEdge = nextEdges.get(index);
            if ( nextEdge.equals(this.lastEdge) ){
                // don't go back to the previous edge
                // unless there is no other choice
                if ( index >= nextEdges.size() - 1 ){
                    nextEdge = nextEdges.get(0);
                }
                else {
                    nextEdge = nextEdges.get(index + 1);
                }
            }
            this.moveAlong(nextEdge);
        }
    }

    @Override
    public void onMoving(int oldX, int oldY, int newX, int newY) {
    }

    @Override
    public void onEdgeComplete(Edge edge) {
        this.lastEdge = edge;
    }

    @Override
    public void onStopMoving() {
    }

}
