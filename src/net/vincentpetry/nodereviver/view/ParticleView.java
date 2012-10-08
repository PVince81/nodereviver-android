package net.vincentpetry.nodereviver.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ParticleView extends View {
    private float x;
    private float y;
    private boolean visible;
    private float vx;
    private float vy;
    private int lifeTime;
    private float size;
    private Color color;
    private Paint paint;
    private Rect rect;

    public ParticleView(float x, float y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.lifeTime = 0;
        this.visible = false;
        this.size = 3;
        this.color = new Color();
        this.paint = new Paint();
        this.rect = new Rect();
    }

    @Override
    public void update() {
        float colorValue;
        this.lifeTime -= 1;
        if (this.lifeTime <= 0) {
            this.visible = false;
            return;
        }
        this.x += this.vx;
        this.y += this.vy;
        this.vx *= 0.95f;
        this.vy *= 0.95f;
        this.size *= 0.9f;
        colorValue = this.lifeTime / 60.0f;
        this.paint.setARGB(255,
                (int) (this.color.r * colorValue),
                (int) (this.color.g * colorValue),
                (int) (this.color.b * colorValue));
        this.rect.top = (int)(this.y - this.size);
        this.rect.bottom = (int)(this.y + this.size);
        this.rect.left = (int)(this.x - this.size);
        this.rect.right = (int)(this.x + this.size);
    }

    @Override
    public void render(Canvas c) {
        c.drawRect(this.rect, this.paint);
    }
    
    public void reset(){
        this.lifeTime = 60;
        //this.size = 3.0f;
        this.size = 2.0f;
        this.visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMovement(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
