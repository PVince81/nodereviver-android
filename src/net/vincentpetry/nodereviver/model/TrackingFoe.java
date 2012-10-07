package net.vincentpetry.nodereviver.model;

/**
 * Foe that tracks another entity (the player) and finds the shortest path.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class TrackingFoe extends Entity {
    private Entity trackedEntity;
    
    public TrackingFoe(Node currentNode, Entity trackedEntity){
        super(currentNode);
        this.trackedEntity = trackedEntity;
        this.speed = 1;
    }
    
    
    @Override
    public void update() {
        // TODO: implement
        if ( this.isMoving() ){
            super.update();
        }
    }


    @Override
    public void onMoving(int oldX, int oldY, int newX, int newY) {
    }

    @Override
    public void onEdgeComplete(Edge edge) {
    }

    @Override
    public void onStopMoving() {
    }

}
