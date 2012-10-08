package net.vincentpetry.nodereviver.model;

/**
 * Player entity.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class Player extends Entity {

    /**
     * Length of the edge being currently marked.
     */
    private int markedLength;
    
    public Player(Node startNode){
        super(startNode);
        this.speed = 2;
        this.markedLength = 0;
    }
    
    @Override
    public void onMoving(int oldX, int oldY, int newX, int newY) {
        if ( !this.currentEdge.isMarked() ){
            if ( oldX != newX ){
                markedLength += Math.abs(newX - oldX);
            }
            else{
                markedLength += Math.abs(newY - oldY);
            }
        }
    }

    @Override
    public void onEdgeComplete(Edge edge) {
        if ( !edge.isMarked() ){
            edge.mark();
            markedLength = 0;
            // TODO: play sound
        }
    }

    @Override
    public void onStopMoving() {
     // TODO: play sound
    }
    
    public int getMarkedLength(){
        return markedLength;
    }

}
