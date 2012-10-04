package net.vincentpetry.nodereviver.model;

public class GameContext {
    private GameState gameState;
    
    public float dummyValue;
    
    public GameContext(){
        this.gameState = new GameState();
        this.dummyValue = 0.0f;
    }
    
    public GameState getGameState(){
        return this.gameState;
    }
}
