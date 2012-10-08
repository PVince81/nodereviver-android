package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Player;
import android.graphics.Canvas;
import android.graphics.Rect;

public class PlayerView extends View {
    private Color PARTICLES_COLOR = new Color(0, 255, 255);
    
    private Player player;
    private Rect rect;
    private SpriteManager spriteManager;
    private ParticlesView particlesView;
    
    public PlayerView(Player player, SpriteManager spriteManager){
        this.player = player;
        this.spriteManager = spriteManager;
        this.rect = new Rect();
        this.particlesView = null;
    }

    @Override
    public void update(){
        Edge currentEdge = player.getCurrentEdge();
        if ( currentEdge != null && !currentEdge.isMarked() ){
            this.particlesView.makeParticles(this.player.getX(), this.player.getY(), PARTICLES_COLOR, 1);
        }
        this.rect.left = this.player.getX() - 10;
        this.rect.top = this.player.getY() - 10;
        this.rect.bottom = this.rect.top + 20;
        this.rect.right = this.rect.left + 20;
        
    }
    
    @Override
    public void render(Canvas c) {
        spriteManager.draw(SpriteManager.SPRITE_PLAYER, rect, 255, c);
    }

    public void setParticlesView(ParticlesView particlesView) {
        this.particlesView = particlesView;
    }

}
