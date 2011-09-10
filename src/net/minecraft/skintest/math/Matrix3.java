package net.minecraft.skintest.math;

public class Matrix3
{
  public double m0;
  public double m1;
  public double m2;
  public double m3;
  public double m4;
  public double m5;
  public double m6;
  public double m7;
  public double m8;
  public double m9;
  public double ma;
  public double mb;

  public Matrix3()
  {
    this(1.0D, 0.0D, 0.0D, 0.0D, 
      0.0D, 1.0D, 0.0D, 0.0D, 
      0.0D, 0.0D, 1.0D, 0.0D);
  }

  public Matrix3(double _m0, double _m1, double _m2, double _m3, double _m4, double _m5, double _m6, double _m7, double _m8, double _m9, double _ma, double _mb)
  {
    m0 = _m0; m1 = _m1; m2 = _m2; m3 = _m3;
    m4 = _m4; m5 = _m5; m6 = _m6; m7 = _m7;
    m8 = _m8; m9 = _m9; ma = _ma; mb = _mb;
  }

  public Matrix3 set(double _m0, double _m1, double _m2, double _m3, double _m4, double _m5, double _m6, double _m7, double _m8, double _m9, double _ma, double _mb)
  {
    m0 = _m0; m1 = _m1; m2 = _m2; m3 = _m3;
    m4 = _m4; m5 = _m5; m6 = _m6; m7 = _m7;
    m8 = _m8; m9 = _m9; ma = _ma; mb = _mb;
    return this;
  }

  public Matrix3 clone()
  {
    return new Matrix3(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9, ma, mb);
  }

  public Vec3 mul(double xs, double ys, double zs)
  {
    double _x = xs * m0 + ys * m1 + zs * m2 + 1.0D * m3;
    double _y = xs * m4 + ys * m5 + zs * m6 + 1.0D * m7;
    double _z = xs * m8 + ys * m9 + zs * ma + 1.0D * mb;

    return new Vec3(_x, _y, _z);
  }

  public Vec3 mul(Vec3 o)
  {
    double _x = o.x * m0 + o.y * m1 + o.z * m2 + 1.0D * m3;
    double _y = o.x * m4 + o.y * m5 + o.z * m6 + 1.0D * m7;
    double _z = o.x * m8 + o.y * m9 + o.z * ma + 1.0D * mb;

    return new Vec3(_x, _y, _z);
  }

  public void mul(Vec3 o, Vec3 t)
  {
    double _x = o.x * m0 + o.y * m1 + o.z * m2 + m3;
    double _y = o.x * m4 + o.y * m5 + o.z * m6 + m7;
    double _z = o.x * m8 + o.y * m9 + o.z * ma + mb;

    t.set(_x, _y, _z);
  }

  public Matrix3 mul(double _m0, double _m1, double _m2, double _m3, double _m4, double _m5, double _m6, double _m7, double _m8, double _m9, double _ma, double _mb)
  {
    return set(
      m0 * _m0 + m1 * _m4 + m2 * _m8 + m3 * 0.0D, 
      m0 * _m1 + m1 * _m5 + m2 * _m9 + m3 * 0.0D, 
      m0 * _m2 + m1 * _m6 + m2 * _ma + m3 * 0.0D, 
      m0 * _m3 + m1 * _m7 + m2 * _mb + m3 * 1.0D, 
      m4 * _m0 + m5 * _m4 + m6 * _m8 + m7 * 0.0D, 
      m4 * _m1 + m5 * _m5 + m6 * _m9 + m7 * 0.0D, 
      m4 * _m2 + m5 * _m6 + m6 * _ma + m7 * 0.0D, 
      m4 * _m3 + m5 * _m7 + m6 * _mb + m7 * 1.0D, 
      m8 * _m0 + m9 * _m4 + ma * _m8 + mb * 0.0D, 
      m8 * _m1 + m9 * _m5 + ma * _m9 + mb * 0.0D, 
      m8 * _m2 + m9 * _m6 + ma * _ma + mb * 0.0D, 
      m8 * _m3 + m9 * _m7 + ma * _mb + mb * 1.0D);
  }

  public Matrix3 rotX(double r)
  {
    double s = Math.sin(r);
    double c = Math.cos(r);

    return mul(
      1.0D, 0.0D, 0.0D, 0.0D, 
      0.0D, c, -s, 0.0D, 
      0.0D, s, c, 0.0D);
  }

  public Matrix3 rotY(double r)
  {
    double s = Math.sin(r);
    double c = Math.cos(r);

    return mul(
      c, 0.0D, s, 0.0D, 
      0.0D, 1.0D, 0.0D, 0.0D, 
      -s, 0.0D, c, 0.0D);
  }

  public Matrix3 rotZ(double r)
  {
    double s = Math.sin(r);
    double c = Math.cos(r);

    return mul(
      c, -s, 0.0D, 0.0D, 
      s, c, 0.0D, 0.0D, 
      0.0D, 0.0D, 1.0D, 0.0D);
  }

  public Matrix3 scale(double xs, double ys, double zs)
  {
    return mul(
      xs, 0.0D, 0.0D, 0.0D, 
      0.0D, ys, 0.0D, 0.0D, 
      0.0D, 0.0D, zs, 0.0D);
  }

  public Matrix3 translate(double x, double y, double z)
  {
    return mul(
      1.0D, 0.0D, 0.0D, x, 
      0.0D, 1.0D, 0.0D, y, 
      0.0D, 0.0D, 1.0D, z);
  }
}