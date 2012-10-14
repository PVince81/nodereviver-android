package net.vincentpetry.nodereviver.model;

import java.util.ArrayList;
import java.util.List;

import net.vincentpetry.nodereviver.util.Random;

/**
 * Foe that tracks another entity (the player) and finds the shortest path.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class TrackingFoe extends Entity {
    private Entity trackedEntity;
    private Node trackedTarget;
    private int sleepTicks;
    private List<Edge> path;
    private int pathIndex;
    private Level level;

    public TrackingFoe(Node currentNode, Entity trackedEntity, Level level){
        super(currentNode);
        this.level = level;
        this.trackedEntity = trackedEntity;
        this.speed = 1;
        this.sleepTicks = 0;
        this.path = new ArrayList<Edge>(level.getEdges().size());
        this.pathIndex = 0;
    }
    
    public void setTrackedEntity(Entity entity){
        this.trackedEntity = entity;
        this.trackedTarget = null;
        this.pathIndex = 0;
    }

    @Override
    public void update() {
        if ( sleepTicks > 0 ){
            sleepTicks--;
            return;
        }
        
        if ( this.isMoving() ){
            super.update();
            return;
        }
        
        if ( trackedEntity == null ){
            return;
        }
        
        // find path
        if ( this.path != null && this.path.size() > 0 &&
                pathIndex < this.path.size() &&
                ( trackedEntity.targetNode == trackedTarget ||
                trackedEntity.currentNode == trackedTarget)){
            this.moveAlong(path.get(pathIndex));
            pathIndex++;
        }
        else if ( Random.randInt(0, 5) == 0 ){
            // sleep for a bit
            sleepTicks = 60;
        }
        else if ( trackedEntity.currentNode != currentNode &&
                trackedEntity.currentNode != targetNode){
            // target has moved or changed direction
            targetNode = trackedEntity.currentNode;
            if ( trackedEntity.isMoving() ){
                targetNode = trackedEntity.getFinalTargetNode();
            }
            if ( targetNode != currentNode ){
                pathIndex = 0;
                level.findShortestPath(currentNode, targetNode, path);
                trackedTarget = targetNode;
            }
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
