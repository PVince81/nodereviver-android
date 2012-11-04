package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Player;
import net.vincentpetry.nodereviver.model.PlayerState;
import net.vincentpetry.nodereviver.util.Random;
import android.graphics.Canvas;

public class PlayerView extends View {
    private Color PARTICLES_COLOR = new Color(0, 255, 255);

    private Player player;
    private SpriteManager spriteManager;
    private ParticlesView particlesView;

    public PlayerView(Player player, ViewContext viewContext){
        this.player = player;
        this.spriteManager = viewContext.getSpriteManager();
        this.particlesView = null;
    }

    @Override
    public void update(){
        if (player.getState().getState() != PlayerState.STATE_NORMAL &&
                player.getState().getState() != PlayerState.STATE_DEMO){
            return;
        }
        Edge currentEdge = player.getCurrentEdge();
        if ( currentEdge != null && !currentEdge.isMarked() ){
            this.particlesView.makeParticles(this.player.getX(), this.player.getY(), PARTICLES_COLOR, 1);
        }

    }

    @Override
    public void render(Canvas c) {
        int alpha = 255;
        int offsetX = 0;
        int offsetY = 0;
        PlayerState state = player.getState();
        int stateVal = state.getState();

        if (stateVal == PlayerState.STATE_DEAD){
            return;
        }
        else if (stateVal == PlayerState.STATE_APPEAR){
            alpha = (int)(state.getProgress() * 255);
        }
        else if (stateVal == PlayerState.STATE_DISAPPEAR){
            alpha = 255 - (int)(state.getProgress() * 255);
        }
        else if (stateVal == PlayerState.STATE_DYING){
            offsetX = Random.randInt(-3, 3);
            offsetY = Random.randInt(-3, 3);
            alpha = 255 - (int)(state.getProgress() * 255);
        }
        spriteManager.setAlpha(alpha);
        spriteManager.draw(SpriteManager.SPRITE_PLAYER, this.player.getX() + offsetX, this.player.getY() + offsetY, c);
        spriteManager.setAlpha(255);
    }

    public void setParticlesView(ParticlesView particlesView) {
        this.particlesView = particlesView;
    }

}
