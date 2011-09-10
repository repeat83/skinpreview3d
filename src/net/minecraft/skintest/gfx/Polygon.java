package net.minecraft.skintest.gfx;

import net.minecraft.skintest.math.Matrix3;
import net.minecraft.skintest.math.Vec3;
import net.minecraft.skintest.math.Vertex;

public class Polygon
{
  private Vertex[] orgVertices;
  private Vertex[] tmpVertices;
  public Vertex[] vertices;
  public int vertexCount = 0;

  static int ab = 0;
  static int nc = 0;
  static int pc = 0;
  static int tot = 0;

  public Polygon(Vertex[] vertices)
  {
    orgVertices = vertices;
    tmpVertices = new Vertex[vertices.length + 1];

    this.vertices = orgVertices;
    vertexCount = vertices.length;
  }

  public Polygon(Vertex[] vertices, int u0, int v0, int u1, int v1)
  {
    this(vertices);

    vertices[0] = vertices[0].remap(u1, v0);
    vertices[1] = vertices[1].remap(u0, v0);
    vertices[2] = vertices[2].remap(u0, v1);
    vertices[3] = vertices[3].remap(u1, v1);
  }

  public static void dumpStats()
  {
    System.out.println("Stats for " + tot + " clips");
    System.out.println("  All behind: " + ab * 10000 / tot / 100.0D + "%");
    System.out.println("     No clip: " + nc * 10000 / tot / 100.0D + "%");
    System.out.println("Partial clip: " + pc * 10000 / tot / 100.0D + "%");

    ab = Polygon.nc = Polygon.pc = Polygon.tot = 0;
  }

  public final void clipZ(double zd)
  {
    tot += 1;
    boolean shouldClip = false;
    boolean allBehind = true;
    for (int i = 0; (i < orgVertices.length) && ((!shouldClip) || (allBehind)); i++)
    {
      if (orgVertices[i].tPos.z < zd) shouldClip = true; else {
        allBehind = false;
      }
    }
    if (allBehind)
    {
      vertices = tmpVertices;
      vertexCount = 0;
      ab += 1;
      return;
    }

    if (!shouldClip)
    {
      vertices = orgVertices;
      vertexCount = vertices.length;
      nc += 1;
      return;
    }

    pc += 1;

    vertices = tmpVertices;
    vertexCount = 0;

    Vertex v0 = orgVertices[(orgVertices.length - 1)];
    for (int i = 0; i < orgVertices.length; i++)
    {
      Vertex v1 = orgVertices[i];
      Vec3 p0 = v0.tPos;
      Vec3 p1 = v1.tPos;

      if ((p0.z >= zd) && (p1.z >= zd))
      {
        vertices[(vertexCount++)] = v1;
      }
      else if ((p0.z >= zd) || (p1.z >= zd))
      {
        double d = (zd - p0.z) / (p1.z - p0.z);
        vertices[(vertexCount++)] = v0.interpolateTo(v1, d);

        if ((p0.z < zd) && (p1.z >= zd))
        {
          vertices[(vertexCount++)] = v1;
        }

      }

      v0 = v1;
    }
  }

  public void project()
  {
    for (int i = 0; i < vertexCount; i++)
    {
      vertices[i].project();
    }
  }

  public void transform(Matrix3 m)
  {
    for (int i = 0; i < orgVertices.length; i++)
    {
      Vertex v = orgVertices[i];
      v.transform(m);
    }
    clipZ(1.0D);
    for (int i = 0; i < vertexCount; i++)
    {
      vertices[i].project();
    }
  }

  public void mirror()
  {
    Vertex[] newVertices = new Vertex[vertices.length];
    for (int i = 0; i < vertices.length; i++)
      newVertices[i] = vertices[(vertices.length - i - 1)];
    vertices = newVertices;

    orgVertices = vertices;
    tmpVertices = new Vertex[vertices.length + 1];

    vertices = orgVertices;
    vertexCount = vertices.length;
  }
}