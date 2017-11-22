package com.projects.p8.intelligent_workout;

public class Tuple
{
    public final int ix;
    public final int iy;
    public final double dx;
    public final double dy;

    public Tuple(int x, int y)
    {
        this.ix = x;
        this.iy = y;
        this.dx = (double) x;
        this.dy = (double) y;
    }

    public Tuple(double x, double y)
    {
        this.dx = x;
        this.dy = y;
        this.ix = (int) x;
        this.iy = (int) y;
    }

    public int getIx () { return ix; }
    public int getIy () { return iy; }
    public double getDx () { return dx; }
    public double getDy () { return dy; }

}
