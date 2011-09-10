package net.minecraft.skintest.gfx;

public class Bitmap
{
  public final int width;
  public final int height;
  public int[] pixels;

  public Bitmap(int width, int height)
  {
    this.width = width;
    this.height = height;
    pixels = new int[width * height];
  }

  protected void setPixels(int[] pixels)
  {
    this.pixels = pixels;
  }

  public void draw(Bitmap bitmap, int xPos, int yPos)
  {
    int x0 = xPos;
    int y0 = yPos;
    int x1 = xPos + bitmap.width;
    int y1 = yPos + bitmap.height;

    if (x0 < 0) x0 = 0;
    if (y0 < 0) y0 = 0;
    if (x1 > width) x1 = width;
    if (y1 > height) y1 = height;

    int[] inPixels = bitmap.pixels;

    for (int y = y0; y < y1; y++)
    {
      int tp = y * width;
      int sp = (y - yPos) * bitmap.width - xPos;
      for (int x = x0; x < x1; x++)
      {
        if (inPixels[(sp + x)] == 0) continue; pixels[(tp + x)] = inPixels[(sp + x)];
      }
    }
  }

  public void color(Bitmap bitmap, int xPos, int yPos, int color)
  {
    int x0 = xPos;
    int y0 = yPos;
    int x1 = xPos + bitmap.width;
    int y1 = yPos + bitmap.height;

    if (x0 < 0) x0 = 0;
    if (y0 < 0) y0 = 0;
    if (x1 > width) x1 = width;
    if (y1 > height) y1 = height;

    int[] inPixels = bitmap.pixels;

    for (int y = y0; y < y1; y++)
    {
      int tp = y * width;
      int sp = (y - yPos) * bitmap.width - xPos;
      for (int x = x0; x < x1; x++)
      {
        if (inPixels[(sp + x)] == 0) continue; pixels[(tp + x)] = color;
      }
    }
  }

  public void draw(Bitmap bitmap, int xPos, int yPos, int xx0, int yy0, int xx1, int yy1)
  {
    int x0 = xPos;
    int y0 = yPos;
    int x1 = xPos + xx1 - xx0;
    int y1 = yPos + yy1 - yy0;

    if (xx0 < 0) x0 -= xx0;
    if (yy0 < 0) y0 -= yy0;
    if (xx1 > bitmap.width) x1 -= xx1 - bitmap.width;
    if (yy1 > bitmap.height) y1 -= yy1 - bitmap.height;

    if (x0 < 0) x0 = 0;
    if (y0 < 0) y0 = 0;
    if (x1 > width) x1 = width;
    if (y1 > height) y1 = height;

    int[] inPixels = bitmap.pixels;

    for (int y = y0; y < y1; y++)
    {
      int tp = y * width;
      int sp = (y - yPos + yy0) * bitmap.width - xPos + xx0;
      for (int x = x0; x < x1; x++)
      {
        if (inPixels[(sp + x)] == 0) continue; pixels[(tp + x)] = inPixels[(sp + x)];
      }
    }
  }

  public void fill(int x0, int y0, int x1, int y1, int col)
  {
    if (x0 < 0) x0 = 0;
    if (y0 < 0) y0 = 0;
    if (x1 > width) x1 = width;
    if (y1 > height) y1 = height;

    for (int y = y0; y < y1; y++)
    {
      int tp = y * width + x0;
      for (int x = x0; x < x1; x++)
      {
        pixels[(tp + x)] = col;
      }
    }
  }
}