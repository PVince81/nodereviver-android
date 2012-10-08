package net.vincentpetry.nodereviver.view;

import android.graphics.Canvas;

public class ParticlesView extends View {
    private int frequency;
    private int freqCounter;
    private int activeParticles;
    private ParticleView[] particles;
    
    public ParticlesView(int amount, int frequency){
        this.frequency = frequency;
        this.freqCounter = frequency;
        this.activeParticles = 0;
        this.particles = new ParticleView[amount];
        // pre-buffer particles
        for ( int i = 0; i < amount; i++ ){
            this.particles[i] = new ParticleView(0f, 0f);
        }
    }
    
    public void makeParticles(int x, int y, Color color, int amount){
        int toRevive = Math.min(amount, this.particles.length);
        if ( this.freqCounter > 0 ){
            this.freqCounter -= 1;
            return;
        }
        this.freqCounter = this.frequency;
        for ( ParticleView particle : this.particles ){
            if ( toRevive <= 0 ){
                break;
            }
            if (!particle.isVisible()){
                particle.reset();
                particle.setColor(color);
                particle.setMovement((float)Math.random() * 4.0f - 2.0f, (float)Math.random() * 4.0f - 2.0f);
                particle.setPosition(x, y);
                toRevive -= 1;
            }
        }
    }
    
    @Override
    public void update(){
        this.activeParticles = 0;
        for ( ParticleView particle : this.particles ){
             if ( !particle.isVisible() ){
                 continue;
             }
             particle.update();
             this.activeParticles++;
        }
    }
    
    @Override
    public void render(Canvas c) {
        if ( this.activeParticles == 0 ){
            return;
        }

        for ( ParticleView particle : this.particles ){
            particle.render(c);
        }
    }
    
    public boolean isActive(){
        return this.activeParticles > 0;
    }
}
