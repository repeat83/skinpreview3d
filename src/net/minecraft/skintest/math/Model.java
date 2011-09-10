package net.minecraft.skintest.math;

import net.minecraft.skintest.gfx.Bitmap;
import net.minecraft.skintest.gfx.Bitmap3d;
import net.minecraft.skintest.gfx.Polygon;

public class Model
{
  private Vertex[] vertices;
  private Polygon[] polygons;
  private int xTexOffs;
  private int yTexOffs;
  public double x;
  public double y;
  public double z;
  public double xRot;
  public double yRot;
  public double zRot;
  public boolean mirror = false;
  public boolean invert = false;

  public Model(int xTexOffs, int yTexOffs)
  {
    this.xTexOffs = xTexOffs;
    this.yTexOffs = yTexOffs;
  }

  public void setTexOffs(int xTexOffs, int yTexOffs)
  {
    this.xTexOffs = xTexOffs;
    this.yTexOffs = yTexOffs;
  }

  public void addBox(double x0, double y0, double z0, int w, int h, int d, float g)
  {
    vertices = new Vertex[8];
    polygons = new Polygon[6];

    double x1 = x0 + w;
    double y1 = y0 + h;
    double z1 = z0 + d;

    x0 -= g;
    y0 -= g;
    z0 -= g;
    x1 += g;
    y1 += g;
    z1 += g;

    if (mirror)
    {
      double tmp = x1;
      x1 = x0;
      x0 = tmp;
    }

    Vertex u0 = new Vertex(x0 - g, y0 - g, z0 - g, 0.0D, 0.0D);
    Vertex u1 = new Vertex(x1 + g, y0 - g, z0 - g, 0.0D, 8.0D);
    Vertex u2 = new Vertex(x1 + g, y1 + g, z0 - g, 8.0D, 8.0D);
    Vertex u3 = new Vertex(x0 - g, y1 + g, z0 - g, 8.0D, 0.0D);

    Vertex l0 = new Vertex(x0 - g, y0 - g, z1 + g, 0.0D, 0.0D);
    Vertex l1 = new Vertex(x1 + g, y0 - g, z1 + g, 0.0D, 8.0D);
    Vertex l2 = new Vertex(x1 + g, y1 + g, z1 + g, 8.0D, 8.0D);
    Vertex l3 = new Vertex(x0 - g, y1 + g, z1 + g, 8.0D, 0.0D);

    vertices[0] = u0;
    vertices[1] = u1;
    vertices[2] = u2;
    vertices[3] = u3;
    vertices[4] = l0;
    vertices[5] = l1;
    vertices[6] = l2;
    vertices[7] = l3;

    polygons[0] = new Polygon(new Vertex[] { l1, u1, u2, l2 }, xTexOffs + d + w, yTexOffs + d, xTexOffs + d + w + d, yTexOffs + d + h);
    polygons[1] = new Polygon(new Vertex[] { u0, l0, l3, u3 }, xTexOffs + 0, yTexOffs + d, xTexOffs + d, yTexOffs + d + h);

    polygons[2] = new Polygon(new Vertex[] { l1, l0, u0, u1 }, xTexOffs + d, yTexOffs + 0, xTexOffs + d + w, yTexOffs + d);
    polygons[3] = new Polygon(new Vertex[] { u2, u3, l3, l2 }, xTexOffs + d + w, yTexOffs + 0, xTexOffs + d + w + w, yTexOffs + d);

    polygons[4] = new Polygon(new Vertex[] { u1, u0, u3, u2 }, xTexOffs + d, yTexOffs + d, xTexOffs + d + w, yTexOffs + d + h);
    polygons[5] = new Polygon(new Vertex[] { l0, l1, l2, l3 }, xTexOffs + d + w + d, yTexOffs + d, xTexOffs + d + w + d + w, yTexOffs + d + h);

    if ((mirror ^ invert))
    {
      for (int i = 0; i < polygons.length; i++)
      {
        polygons[i].mirror();
      }
    }
  }

  public void render(Matrix3 m, Bitmap texture, Bitmap3d bitmap)
  {
    m = m.clone();
    if ((x != 0.0D) || (y != 0.0D) || (z != 0.0D)) m = m.translate(x, y, z);
    if (zRot != 0.0D) m = m.rotZ(zRot);
    if (yRot != 0.0D) m = m.rotY(yRot);
    if (xRot != 0.0D) m = m.rotX(xRot);

    for (int i = 0; i < vertices.length; i++) {
      vertices[i].transform(m);
    }
    for (int i = 0; i < polygons.length; i++)
    {
      polygons[i].clipZ(1.0D);
      polygons[i].project();
      bitmap.render(polygons[i], texture, Bitmap3d.Mode.straight, -1);
    }
  }

  public void setPos(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}