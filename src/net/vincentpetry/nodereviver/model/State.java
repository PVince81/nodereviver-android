package net.vincentpetry.nodereviver.model;

public class State {

    protected int state;
    protected boolean dirty;
    private int duration;
    private int maxDuration;
    private int nextState;

    public State() {
        this.dirty = true;
        this.nextState = -1;
        this.duration = 0;
        this.maxDuration = 0;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int newState) {
        this.state = newState;
        this.duration = 0;
        this.nextState = -1;
    }

    public void setState(int newState, int duration, int nextState) {
        this.state = newState;
        if (duration > 0) {
            this.duration = (int) (duration * 60.0f / 1000.0f);
        }
        else {
            this.duration = 0;
        }
        this.maxDuration = this.duration;
        this.nextState = nextState;
        this.dirty = true;
    }

    public void update() {
        if (this.nextState < 0) {
            return;
        }
        if (this.duration >= 1) {
            this.duration -= 1;
        }
        else {
            this.state = this.nextState;
            this.nextState = -1;
            this.dirty = true;
            this.duration = 0;
            this.maxDuration = 0;
        }
    }

    /**
     * Returns the transition ratio between two states S1 and S2. 0 is S1 0.5 is
     * exactly between S1 and S2 1 is S2
     */
    public float getProgress() {
        if (this.nextState < 0) {
            return 1.0f;
        }
        return 1.0f - (float) this.duration / this.maxDuration;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void resetDirty() {
        this.dirty = false;
    }
}
