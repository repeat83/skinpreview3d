package net.minecraft.skintest.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageBitmap extends Bitmap3d
{
  private BufferedImage image;
  private static Map<String, Bitmap> loadedBitmaps = new HashMap<String, Bitmap>();

  public ImageBitmap(int width, int height)
  {
    super(width, height);
    image = new BufferedImage(width, height, 1);
    setPixels(((DataBufferInt)image.getRaster().getDataBuffer()).getData());
  }

  public BufferedImage getImage()
  {
    return image;
  }

  public static Bitmap load(String name)
  {
    Bitmap bm = (Bitmap)loadedBitmaps.get(name);
    if (bm != null) return bm;

    try
    {
      BufferedImage img = ImageIO.read(ImageBitmap.class.getResource(name));
      Bitmap bitmap = new Bitmap(img.getWidth(), img.getHeight());
      int[] pixels = new int[img.getWidth() * img.getHeight()];
      bitmap.setPixels(img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth()));
      loadedBitmaps.put(name, bitmap);
      return bitmap;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }return null;
  }

  public static Bitmap load(BufferedImage img)
  {
    Bitmap bitmap = new Bitmap(img.getWidth(), img.getHeight());
    int[] pixels = new int[img.getWidth() * img.getHeight()];
    bitmap.setPixels(img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth()));
    return bitmap;
  }

  public static Bitmap[][] cut(String name, int width, int height)
  {
    try
    {
      BufferedImage img = ImageIO.read(ImageBitmap.class.getResource(name));
      int xTiles = img.getWidth() / width;
      int yTiles = img.getHeight() / height;

      Bitmap[][] bitmaps = new Bitmap[xTiles][yTiles];
      for (int x = 0; x < xTiles; x++)
        for (int y = 0; y < yTiles; y++)
        {
          int[] pixels = new int[width * height];
          bitmaps[x][y] = new Bitmap(width, height);
          bitmaps[x][y].setPixels(img.getRGB(x * width, y * height, width, height, pixels, 0, width));
        }
      return bitmaps;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }return null;
  }
}