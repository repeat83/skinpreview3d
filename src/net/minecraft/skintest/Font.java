package net.minecraft.skintest;

import net.minecraft.skintest.gfx.Bitmap;
import net.minecraft.skintest.gfx.ImageBitmap;

public class Font
{
  private Bitmap[][] sheet;
  private static final int[] map = new int[256];
  private final int w;
  private final int h;

  static
  {
    for (int i = 0; i < "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!=,'\"-=+/\\[]*:;<>()@      ".length(); i++)
    {
      char ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!=,'\"-=+/\\[]*:;<>()@      ".charAt(i);
      map[ch] = i;
      map[java.lang.Character.toLowerCase(ch)] = i;
    }
  }

  public Font(String resourceName, int w, int h)
  {
    this.w = (w + 1);
    this.h = (h + 1);
    sheet = ImageBitmap.cut(resourceName, w, h);
  }

  public void draw(String str, Bitmap target, int x, int y)
  {
    if ((y < -h) || (y > target.height)) return;
    int i0 = 0;
    int i1 = str.length();
    if (x < -w) i0 -= x / w;
    if (x + i1 * w > target.width) i1 -= (x + i1 * w - target.width) / w;
    for (int i = i0; i < i1; i++)
    {
      int index = map[str.charAt(i)];
      target.draw(sheet[(index % 21)][(index / 21)], x + i * w, y);
    }
  }

  public void draw(String str, Bitmap target, int x, int y, int color)
  {
    if ((y < -h) || (y > target.height)) return;
    int i0 = 0;
    int i1 = str.length();
    if (x < -w) i0 -= x / w;
    if (x + i1 * w > target.width) i1 -= (x + i1 * w - target.width) / w;
    for (int i = i0; i < i1; i++)
    {
      int index = map[str.charAt(i)];
      target.color(sheet[(index % 21)][(index / 21)], x + i * w, y, color);
    }
  }

  public void drawShadow(String str, Bitmap target, int x, int y, int color)
  {
    draw(str, target, x + 1, y + 1, 0);
    draw(str, target, x, y, color);
  }
}