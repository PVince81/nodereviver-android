package net.vincentpetry.nodereviver.model;

/**
 * Game state with transitions.
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class GameState {
    
    public static final int STATE_TITLE = 0;
    public static final int STATE_GAME = 1;
    public static final int STATE_DEAD = 2;
    public static final int STATE_LEVEL_START = 3;
    public static final int STATE_LEVEL_END = 4;
    public static final int STATE_NEXT_LEVEL = 5;
    public static final int STATE_RESTART_LEVEL = 6;
    public static final int STATE_EDITOR = 7;
    public static final int STATE_STORY = 8;
    public static final int STATE_ENDGAME = 9;
    public static final int STATE_QUIT = 10;
    
    private int state;
    private boolean paused;
    private int duration;
    private int maxDuration;
    private int nextState;
    private boolean dirty;
    
    public GameState(){
	this.state = STATE_TITLE;
	this.dirty = true;
	this.paused = false;
	this.nextState = -1;
	this.duration = 0;
	this.maxDuration = 0;
    }
    
    public int getState(){
	return this.state;
    }
    
    public void setState( int newState ){
	this.state = newState;
	this.duration = 0;
	this.nextState = -1;
    }
    
    public void setState( int newState, int duration, int nextState ){
	this.state = newState;
	if ( duration > 0 ) {
	    this.duration = (int)(duration * 60.0 / 1000.0);
	}
	else {
	    this.duration = 0;
	}
	this.maxDuration = this.duration;
	this.nextState = nextState;
	this.dirty = true;
    }
    
    public void setPaused( boolean paused ){
	this.paused = paused;
    }
    
    public boolean isPaused(){
	return this.paused;
    }
    
    public void update(){
	if ( this.nextState <= 0 ){
	    return;
	}
	if ( this.duration >= 0 ){
	    this.duration -= 1;
	}
	else {
	    this.state = this.nextState;
	    this.dirty = true;
	    this.duration = 0;
	    this.maxDuration = 0;
	}
    }
    
    /**
      * Returns the transition ratio between two states S1 and S2.
      * 0 is S1
      * 0.5 is exactly between S1 and S2
      * 1 is S2
     */
    public float getProgress(){
	if ( this.nextState >= 0 ) {
	    return 1.0f;
	}
	return 1.0f - (float)this.duration / this.maxDuration; 
    }
    
    public boolean isDirty(){
	return dirty;
    }
    
    public void resetDirty(){
	this.dirty = false;
    }

}
