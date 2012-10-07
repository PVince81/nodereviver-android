package net.vincentpetry.nodereviver.model;

public class GameContext {
    private GameState gameState;
    private Level level;
    private Player player;
    
    private int viewportWidth;
    private int viewportHeight;
    private int levelNum;
    
    public GameContext(){
        this.gameState = new GameState();
        this.level = null;
        this.player = null;
        this.levelNum = 1;
    }
    
    public GameState getGameState(){
        return this.gameState;
    }
    
    public void setLevel(Level level){
        this.level = level;
    }
    
    public Level getLevel(){
        return this.level;
    }

    public Player getPlayer(){
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    

    public int getViewportWidth(){
        return viewportWidth;
    }

    public int getViewportHeight(){
        return viewportHeight;
    }

    public void setViewportWidth(int viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public void setViewportHeight(int viewportHeight) {
        this.viewportHeight = viewportHeight;
    }
   
    public int getLevelNum() {
        return levelNum;        
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;        
    }
}
