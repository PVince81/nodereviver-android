package net.vincentpetry.nodereviver.util;

/**
 * Integer vector utility class
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class Vector {

    public int x;
    public int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector unit() {
        if (x != 0) {
            x = x / Math.abs(x);
        }
        if (y != 0) {
            y = y / Math.abs(y);
        }
        return this;
    }

    public Vector add(Vector v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector subst(Vector v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector subst(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public boolean equals(Object o) {
        if (o instanceof Vector) {
            Vector v = (Vector) o;
            return (v.x == this.x && v.y == this.y);
        }
        return false;
    }
}
