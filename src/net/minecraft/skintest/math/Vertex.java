package net.minecraft.skintest.math;

public class Vertex
{
  public Vec3 pos;
  public Vec3 tPos;
  public double u;
  public double v;
  public double xt;
  public double yt;
  public double zt;
  public double ut;
  public double vt;
  public double iz;

  public Vertex(double x, double y, double z, double u, double v)
  {
    this(new Vec3(x, y, z), u, v);
  }

  public Vertex remap(double u, double v)
  {
    return new Vertex(this, u, v);
  }

  public Vertex(Vertex vertex, double u, double v)
  {
    pos = vertex.pos;
    tPos = vertex.tPos;
    this.u = u;
    this.v = v;
  }

  public Vertex(Vec3 pos, double u, double v)
  {
    this.pos = pos;
    tPos = new Vec3(pos.x, pos.y, pos.z);
    this.u = u;
    this.v = v;
  }

  public Vertex interpolateTo(Vertex t, double p)
  {
    Vec3 vec = tPos.interpolateTo(t.tPos, p);
    double ut = u + (t.u - u) * p;
    double vt = v + (t.v - v) * p;

    return new Vertex(vec, ut, vt).project();
  }

  public Vertex transform(Matrix3 m)
  {
    m.mul(pos, tPos);
    return this;
  }

  public Vertex project()
  {
    xt = (tPos.x / tPos.z * 100.0D + 80.0D);
    yt = (tPos.y / tPos.z * 100.0D + 80.0D);
    zt = tPos.z;

    iz = (1.0D / zt);
    ut = (u / zt);
    vt = (v / zt);
    return this;
  }
}