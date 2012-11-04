package net.vincentpetry.nodereviver.model;

/**
 * Game state with transitions.
 *
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class GameState extends State {

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

    private boolean paused;

    public GameState() {
        super();
        this.state = STATE_TITLE;
        this.paused = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return this.paused;
    }
}
