package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Node;
import net.vincentpetry.nodereviver.model.Player;
import android.graphics.Canvas;
import android.graphics.Paint;

public class EdgeView extends View {

    private Edge edge;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Paint paint;
    private Player player;
    private ViewContext viewContext;

    public EdgeView(Player player, ViewContext viewContext){
        this.edge = null;
        this.player = player;
        this.viewContext = viewContext;
        this.paint = new Paint();
        this.paint.setStrokeWidth(3.0f);
        this.paint.setStrokeCap(Paint.Cap.SQUARE);

        // FIXME: do this properly
        if ( this.viewContext.getScaling() < 1.0f ){
            this.paint.setStrokeWidth(1);
        }
    }

    @Override
    public void update(){
        float scale = viewContext.getScaling();
        Edge playerEdge = player.getCurrentEdge();
        if ( playerEdge != this.edge ){
            this.edge = playerEdge;
            // TODO: node activation particles effect
            if ( this.edge != null ){
                if ( !this.edge.isMarked() ){
                    Node source = this.edge.getSourceNode();
                    Node target = this.edge.getTargetNode();
                    x1 = (int)(source.getX() * scale);
                    y1 = (int)(source.getY() * scale);
                    x2 = (int)(target.getX() * scale);
                    y2 = (int)(target.getY() * scale);
                }
                else {
                    this.edge = null;
                }
            }
        }

        if ( this.edge == null ){
            return;
        }

        float ratio = (float)this.player.getMarkedLength() / this.edge.getLength();
        if ( ratio > 1.0f ){
            ratio = 1.0f;
        }
        int colorValue1 = 128 * (int)(1.0f - ratio);
        int colorValue2 = 128 + (int)(127f * ratio);
        this.paint.setARGB(255, colorValue1, colorValue2, colorValue2);

    }

    @Override
    public void render(Canvas c) {
        if ( this.edge == null ){
            return;
        }

        c.drawLine(x1, y1, x2, y2, paint);
        // also draw nodes
        if ( this.edge.getSourceNode().getType() != Node.TYPE_JOINT){
            drawNode(c, this.edge.getSourceNode());
        }
        if ( this.edge.getTargetNode().getType() != Node.TYPE_JOINT){
            drawNode(c, this.edge.getTargetNode());
        }
    }

    private void drawNode(Canvas c, Node node) {
        float scale = viewContext.getScaling();
        viewContext.getSpriteManager().draw(SpriteManager.SPRITE_NODE_NORMAL,
                node.getX() * scale, node.getY() * scale, c);
    }
}
