package net.minecraft.skintest.gfx;

import java.util.Arrays;
import net.minecraft.skintest.math.Vertex;

public class Bitmap3d extends Bitmap
{
  private double[] zBuffer;
  private static Vertex[] left = new Vertex[256];
  private static Vertex[] right = new Vertex[256];

  public Bitmap3d(int width, int height)
  {
    super(width, height);
    zBuffer = new double[width * height];
  }

  public void clearZBuffer()
  {
    Arrays.fill(zBuffer, 1.7976931348623157E+308D);
  }

  public void render(Polygon polygon, Bitmap texture, Mode mode, int color)
  {
    if (polygon.vertexCount < 3) return;

    int top = 0;
    int bottom = 0;
    for (int i = 1; i < polygon.vertexCount; i++)
    {
      if (polygon.vertices[i].yt < polygon.vertices[top].yt) top = i;
      if (polygon.vertices[i].yt < polygon.vertices[bottom].yt) continue; bottom = i;
    }

    double yy0 = polygon.vertices[top].yt;
    double yy1 = polygon.vertices[bottom].yt;
    if ((yy1 <= yy0) || (yy0 >= height) || (yy1 <= 0.0D)) return;

    int lc = 0;
    left[(lc++)] = polygon.vertices[top];
    for (int i = 1; ; i++)
    {
      Vertex v0 = polygon.vertices[((top + i) % polygon.vertexCount)];
      Vertex v1 = polygon.vertices[((top + i + 1) % polygon.vertexCount)];
      if (v1.yt < v0.yt)
        break;
      left[(lc++)] = v0;
    }

    left[(lc++)] = polygon.vertices[bottom];

    int rc = 0;
    right[(rc++)] = polygon.vertices[top];
    for (int i = polygon.vertexCount - 1; ; i--)
    {
      Vertex v0 = polygon.vertices[((top + i) % polygon.vertexCount)];
      Vertex v1 = polygon.vertices[((top + i + 1) % polygon.vertexCount)];
      if (v1.yt > v0.yt)
        break;
      right[(rc++)] = v0;
    }

    right[(rc++)] = polygon.vertices[bottom];

    int y0 = (int)yy0;
    int y1 = (int)yy1;
    lc = rc = 0;

    if (y0 < 0) y0 = 0;
    if (y1 > height) y1 = height;

    int aCol = color >> 24 & 0xFF;
    int rCol = color >> 16 & 0xFF;
    int gCol = color >> 8 & 0xFF;
    int bCol = color & 0xFF;
    int tw = texture.width;
    int twm = texture.width - 1;
    int thm = texture.height - 1;

    Vertex l0 = left[lc];
    Vertex l1 = left[(lc + 1)];
    Vertex r0 = right[rc];
    Vertex r1 = right[(rc + 1)];
    double lLen = 1.0D / (l1.yt - l0.yt);
    double rLen = 1.0D / (r1.yt - r0.yt);

    for (int y = y0; y < y1; y++)
    {
      while (y >= (int)left[(lc + 1)].yt)
      {
        lc++;
        l0 = l1;
        l1 = left[(lc + 1)];
        lLen = 1.0D / (l1.yt - l0.yt);
      }
      while (y >= (int)right[(rc + 1)].yt)
      {
        rc++;
        r0 = r1;
        r1 = right[(rc + 1)];
        rLen = 1.0D / (r1.yt - r0.yt);
      }

      double lp = (y - l0.yt + 1.0D) * lLen;
      double rp = (y - r0.yt + 1.0D) * rLen;

      double xx0 = l0.xt + (l1.xt - l0.xt) * lp;
      double xx1 = r0.xt + (r1.xt - r0.xt) * rp;
      if (xx1 <= xx0) return;

      if ((xx0 >= width) || (xx1 <= 0.0D))
        continue;
      int x0 = (int)xx0;
      int x1 = (int)xx1;
      if (x0 < 0) x0 = 0;
      if (x1 > width) x1 = width;

      double u0 = l0.ut + (l1.ut - l0.ut) * lp;
      double ua = r0.ut + (r1.ut - r0.ut) * rp - u0;

      double v0 = l0.vt + (l1.vt - l0.vt) * lp;
      double va = r0.vt + (r1.vt - r0.vt) * rp - v0;

      double iz0 = l0.iz + (l1.iz - l0.iz) * lp;
      double iza = r0.iz + (r1.iz - r0.iz) * rp - iz0;

      double ix = 1.0D / (xx1 - xx0);
      ua *= ix;
      va *= ix;
      iza *= ix;
      u0 -= (xx0 - x0 - 1.0D) * ua;
      v0 -= (xx0 - x0 - 1.0D) * va;
      iz0 -= (xx0 - x0 - 1.0D) * iza;

      int p = y * width + x0;

      if (mode == Mode.straight)
      {
        for (int x = x0; x < x1; x++)
        {
          double z = 1.0D / iz0;
          if (z > zBuffer[p])
          {
            p++;
          }
          else
          {
            int u = (int)(u0 * z) & twm;
            int v = (int)(v0 * z) & thm;

            int sourceCol = texture.pixels[(u + v * tw)];
            if ((sourceCol >> 24 & 0xFF) == 255)
            {
              zBuffer[p] = z;
              pixels[(p++)] = sourceCol;
            }
            else
            {
              p++;
            }
          }
          u0 += ua;
          v0 += va;
          iz0 += iza;
        }
      }
      else if (mode == Mode.color)
      {
        for (int x = x0; x < x1; x++)
        {
          double z = 1.0D / iz0;
          if (z > zBuffer[p])
          {
            p++;
          }
          else
          {
            zBuffer[p] = z;
            int u = (int)(u0 * z) & twm;
            int v = (int)(v0 * z) & thm;

            int col = texture.pixels[(u + v * tw)];
            int r = (col >> 16 & 0xFF) * rCol >> 8;
            int g = (col >> 8 & 0xFF) * gCol >> 8;
            int b = (col & 0xFF) * bCol >> 8;
            pixels[(p++)] = (r << 16 | g << 8 | b);
          }
          u0 += ua;
          v0 += va;
          iz0 += iza;
        }

      }
      else if (mode == Mode.blend)
      {
        for (int x = x0; x < x1; x++)
        {
          double z = 1.0D / iz0;
          if (z > zBuffer[p])
          {
            p++;
          }
          else
          {
            zBuffer[p] = z;
            int u = (int)(u0 * z) & twm;
            int v = (int)(v0 * z) & thm;

            int col = texture.pixels[(u + v * tw)];
            int a = aCol;
            int r = col >> 16 & 0xFF;
            int g = col >> 8 & 0xFF;
            int b = col & 0xFF;
            int ia = 255 - a;
            int sCol = pixels[p];
            r = r * a + (sCol >> 16 & 0xFF) * ia >> 8;
            g = g * a + (sCol >> 8 & 0xFF) * ia >> 8;
            b = b * a + (sCol & 0xFF) * ia >> 8;
            pixels[(p++)] = (r << 16 | g << 8 | b);
          }

          u0 += ua;
          v0 += va;
          iz0 += iza;
        }

      }
      else if (mode == Mode.colorblend)
      {
        for (int x = x0; x < x1; x++)
        {
          double z = 1.0D / iz0;
          if (z > zBuffer[p])
          {
            p++;
          }
          else
          {
            zBuffer[p] = z;
            int u = (int)(u0 * z) & twm;
            int v = (int)(v0 * z) & thm;

            int col = texture.pixels[(u + v * tw)];
            int a = (col >> 24 & 0xFF) * aCol >> 8;
            int r = (col >> 16 & 0xFF) * rCol >> 8;
            int g = (col >> 8 & 0xFF) * gCol >> 8;
            int b = (col & 0xFF) * bCol >> 8;
            int ia = 255 - a;
            int sCol = pixels[p];
            r = r * a + (sCol >> 16 & 0xFF) * ia >> 8;
            g = g * a + (sCol >> 8 & 0xFF) * ia >> 8;
            b = b * a + (sCol & 0xFF) * ia >> 8;
            pixels[(p++)] = (r << 16 | g << 8 | b);
          }

          u0 += ua;
          v0 += va;
          iz0 += iza;
        }

      }
      else if (mode == Mode.add)
      {
        for (int x = x0; x < x1; x++)
        {
          double z = 1.0D / iz0;
          if (z > zBuffer[p])
          {
            p++;
          }
          else
          {
            zBuffer[p] = z;
            int u = (int)(u0 * z) & twm;
            int v = (int)(v0 * z) & thm;

            int col = texture.pixels[(u + v * tw)];
            int sCol = pixels[p];
            int r = (col >> 16 & 0xFF) + (sCol >> 16 & 0xFF);
            int g = (col >> 8 & 0xFF) + (sCol >> 8 & 0xFF);
            int b = (col & 0xFF) + (sCol & 0xFF);
            if (r > 255) r = 255;
            if (g > 255) g = 255;
            if (b > 255) b = 255;
            pixels[(p++)] = (r << 16 | g << 8 | b);
          }

          u0 += ua;
          v0 += va;
          iz0 += iza;
        }
      }
      else {
        if (mode != Mode.coloradd)
          continue;
        for (int x = x0; x < x1; x++)
        {
          double z = 1.0D / iz0;
          if (z > zBuffer[p])
          {
            p++;
          }
          else
          {
            zBuffer[p] = z;
            int u = (int)(u0 * z) & twm;
            int v = (int)(v0 * z) & thm;

            int col = texture.pixels[(u + v * tw)];
            int sCol = pixels[p];
            int r = ((col >> 16 & 0xFF) * rCol >> 8) + (sCol >> 16 & 0xFF);
            int g = ((col >> 8 & 0xFF) * gCol >> 8) + (sCol >> 8 & 0xFF);
            int b = ((col & 0xFF) * bCol >> 8) + (sCol & 0xFF);

            if (r > 255) r = 255;
            if (g > 255) g = 255;
            if (b > 255) b = 255;

            pixels[(p++)] = (r << 16 | g << 8 | b);
          }

          u0 += ua;
          v0 += va;
          iz0 += iza;
        }
      }
    }
  }

  public static enum Mode
  {
    straight, color, blend, colorblend, add, coloradd;
  }
}