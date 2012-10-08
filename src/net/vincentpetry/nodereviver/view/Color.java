package net.vincentpetry.nodereviver.view;

public class Color {
    int r;
    int g;
    int b;
    int a;
    
    public Color(){
        this(0, 0, 0 ,255);
    }
    
    public Color(int r, int g, int b){
        this(r, g, b, 255);
    }
    
    public Color(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
