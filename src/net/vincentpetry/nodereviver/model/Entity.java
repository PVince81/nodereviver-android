package net.vincentpetry.nodereviver.model;

import net.vincentpetry.nodereviver.util.Vector;

/**
 * Game entity.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public abstract class Entity {

    int x;
    int y;
    protected boolean dead;
    protected Node currentNode;
    protected Edge currentEdge;
    protected Node targetNode;
    protected int targetX;
    protected int targetY;
    protected int speed;
    protected int vx;
    protected int vy;
    
    protected Entity(Node currentNode){
        this.x = currentNode.x;
        this.y = currentNode.y;
        this.currentNode = currentNode;
        this.dead = false;
        this.speed = 1;
        this.targetX = 0;
        this.targetY = 0;
    }
    
    public void setCurrentNode(Node currentNode){
        this.x = currentNode.x;
        this.y = currentNode.y;
        this.currentNode = currentNode;
    }

    public void die(){
        this.dead = true;
    }
    
    public boolean isDead(){
        return this.dead;
    }
    
    public void moveTo(Node targetNode){
        this.targetNode = targetNode;
        this.targetX = targetNode.x;
        this.targetY = targetNode.y;
        Vector dir = new Vector(this.targetX, this.targetY);
        dir.subst(this.x, this.y).unit();
        this.vx = dir.x;
        this.vy = dir.y;
    }
    
    public void moveAlong(Edge targetEdge){
        this.currentEdge = targetEdge;
        this.moveTo( targetEdge.getOtherNode(this.currentNode) );
    }
    
    public void moveToDirection(int vx, int vy){
        Edge edge = this.currentNode.getEdgeByDirection(vx, vy);
        if (edge != null){
            this.moveAlong(edge);
        }
    }
    
    public boolean isMoving(){
        return vx != 0 || vy != 0;
    }
    
    public void update(){
        if ( vx != 0 || vy != 0 ){
            int oldX = x;
            int oldY = y;
            x += vx * speed;
            y += vy * speed;
            this.onMoving(oldX, oldY, x, y);

            // assuming that only one of vx or vy is nonzero
            int distanceX = Math.abs(targetX - x);
            int distanceY = Math.abs(targetY - y);
            if ( vx != 0 && distanceX < speed || vy != 0 && distanceY < speed ){
                // arrived
                x = targetNode.x;
                y = targetNode.y;
                this.currentNode = this.targetNode;
                this.onEdgeComplete(this.currentEdge);
                if ( this.currentNode.type == Node.TYPE_JOINT ){
                    // continue along the next edge
                    Edge edge = this.currentNode.getOtherEdge(this.currentEdge);
                    this.moveAlong(edge);
                }
                else{
                    // arrived on a square node, stop moving
                    this.onStopMoving();
                    this.vx = 0;
                    this.vy = 0;
                    this.targetNode = null;
                    this.currentEdge = null;
                }
            }
        }
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    /**
     * 
     * @param oldX
     * @param oldY
     * @param newX
     * @param newY
     */
    public abstract void onMoving(int oldX, int oldY, int newX, int newY);
    
    /**
     * Called whenever the entity has finished traversing an edge.
     * @param edge edge that has been traversed
     */
    public abstract void onEdgeComplete(Edge edge);
    
    /**
     * Called whenever the entity has stopped moving.
     */
    public abstract void onStopMoving();
}
