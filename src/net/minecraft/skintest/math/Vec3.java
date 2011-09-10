package net.minecraft.skintest.math;

public class Vec3
{
  public double x;
  public double y;
  public double z;

  public Vec3(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vec3 interpolateTo(Vec3 t, double p)
  {
    double xt = x + (t.x - x) * p;
    double yt = y + (t.y - y) * p;
    double zt = z + (t.z - z) * p;

    return new Vec3(xt, yt, zt);
  }

  public void set(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}