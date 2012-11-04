package net.vincentpetry.nodereviver.model;

public class PlayerState extends State {

    public static int STATE_NORMAL = 0;
    public static int STATE_DYING = 1;
    public static int STATE_DEAD = 2;
    public static int STATE_APPEAR = 3;
    public static int STATE_DISAPPEAR = 4;

    public PlayerState(){
        super();
        this.state = STATE_NORMAL;
    }

}
