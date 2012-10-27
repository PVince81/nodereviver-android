package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Level;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.text.TextPaint;

public class HudView extends View {

    private Level level;
    private ViewContext viewContext;
    private TextView titleView;
    private TextView subtitleView;

    public HudView(ViewContext viewContext){
        this.viewContext = viewContext;
        TextPaint paint = new TextPaint();
        paint.setARGB(255, 0, 255, 0);
        paint.setTypeface(viewContext.getTypeface());
        paint.setTextSize(viewContext.getFontHeightBig());
        paint.setTextAlign(Align.CENTER);

        TextPaint paint2 = new TextPaint();
        paint2.setARGB(255, 0, 192, 0);
        paint2.setTypeface(viewContext.getTypeface());
        paint2.setTextSize(viewContext.getFontHeightNormal());
        paint2.setTextAlign(Align.CENTER);

        titleView = new TextView(paint);
        subtitleView = new TextView(paint2);

        this.level = null;
    }

    public void setLevel(Level level){
        if ( this.level != level ){
            this.level = level;
            titleView.setText(level.getTitle(), viewContext.getWidth());
            subtitleView.setText(level.getSubtitle(), viewContext.getWidth());
        }
    }

    @Override
    public void render(Canvas c) {
        titleView.render(c, 0.0f, 0.0f);
        subtitleView.render(c, 0.0f, viewContext.getHeight() - subtitleView.getHeight());
    }
}
