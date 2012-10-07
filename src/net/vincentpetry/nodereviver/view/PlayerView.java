package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.model.Player;
import android.graphics.Canvas;
import android.graphics.Rect;

public class PlayerView extends EntityView {
    private Player player;
    private Rect rect;
    private GameContext context;
    private SpriteManager spriteManager;
    
    public PlayerView(Player player, SpriteManager spriteManager, GameContext context){
        this.player = player;
        this.spriteManager = spriteManager;
        this.context = context;
        this.rect = new Rect();
    }
    
    @Override
    public void update(){
        this.rect.left = this.player.getX() - 10;
        this.rect.top = this.player.getY() - 10;
        this.rect.bottom = this.rect.top + 20;
        this.rect.right = this.rect.left + 20;
    }
    
    @Override
    public void render(Canvas c) {
        spriteManager.draw(SpriteManager.SPRITE_PLAYER, rect, 255, c);
    }

}
