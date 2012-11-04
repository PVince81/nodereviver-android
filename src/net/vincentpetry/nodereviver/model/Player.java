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
    private PlayerState state;

    public Player(Node startNode){
        super(startNode);
        this.speed = 2;
        this.markedLength = 0;
        this.state = new PlayerState();
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

    @Override
    public void update(){
        if ( !this.dead ){
            super.update();
        }
        state.update();
    }

    public PlayerState getState(){
        return state;
    }

    public void die(){
        super.die();
        state.setState(PlayerState.STATE_DYING, 1000, PlayerState.STATE_DEAD);
    }

    public boolean canMove(){
        return this.getState().getState() == PlayerState.STATE_NORMAL;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
