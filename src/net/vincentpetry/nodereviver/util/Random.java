package net.vincentpetry.nodereviver.util;

/**
 * Utility class for generating pseudo-random numbers.
 *
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class Random {

    /**
     * Returns a random integer between v1 and v2,
     * v1 and v2 included.
     * @param v1 range start
     * @param v2 range end
     * @return random integer between v1 and v2
     */
    public static int randInt(int v1, int v2){
        return (int)(Math.random() * (v2 - v1)) + v1;
    }
    
}
