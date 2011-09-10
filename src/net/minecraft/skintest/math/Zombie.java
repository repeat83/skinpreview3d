package net.minecraft.skintest.math;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.skintest.gfx.Bitmap;
import net.minecraft.skintest.gfx.Bitmap3d;
import net.minecraft.skintest.gfx.ImageBitmap;

public class Zombie
{
  public Model head;
  public Model hair;
  public Model iHair;
  public Model body;
  public Model arm0;
  public Model arm1;
  public Model leg0;
  public Model leg1;
  public Bitmap texture;
  public double x;
  public double y;
  public double z;
  public double rot;
  public double size;
  public double timeOffs;
  public double speed;
  public double rotA;
  private boolean hasHair = false;

  public Zombie(String url)
  {
    rotA = 0.0D;
    timeOffs = 0.0D;
    rot = 0.0D;
    size = 1.0D;
    speed = 0.800000011920929D;

    head = new Model(0, 0);
    head.addBox(-4.0D, -8.0D, -4.0D, 8, 8, 8, 0.0F);

    hair = new Model(32, 0);
    hair.addBox(-4.0D, -8.0D, -4.0D, 8, 8, 8, 0.5F);

    iHair = new Model(32, 0);
    iHair.invert = true;
    iHair.addBox(-4.0D, -8.0D, -4.0D, 8, 8, 8, 0.5F);

    body = new Model(16, 16);
    body.addBox(-4.0D, 0.0D, -2.0D, 8, 12, 4, 0.0F);

    arm0 = new Model(40, 16);
    arm0.addBox(-3.0D, -2.0D, -2.0D, 4, 12, 4, 0.0F);
    arm0.setPos(-5.0D, 2.0D, 0.0D);

    arm1 = new Model(40, 16);
    arm1.mirror = true;
    arm1.addBox(-1.0D, -2.0D, -2.0D, 4, 12, 4, 0.0F);
    arm1.setPos(5.0D, 2.0D, 0.0D);

    leg0 = new Model(0, 16);
    leg0.addBox(-2.0D, 0.0D, -2.0D, 4, 12, 4, 0.0F);
    leg0.setPos(-2.0D, 12.0D, 0.0D);

    leg1 = new Model(0, 16);
    leg1.mirror = true;
    leg1.addBox(-2.0D, 0.0D, -2.0D, 4, 12, 4, 0.0F);
    leg1.setPos(2.0D, 12.0D, 0.0D);

    loadTexture(url);
  }

  public void loadTexture(final String _url)
  {
//	    new Thread(_url)
    new Thread()
    {
      public void run()
      {
        HttpURLConnection huc = null;
        try
        {
          URL url = new URL(_url);
          huc = (HttpURLConnection)url.openConnection();
          huc.setDoInput(true);
          huc.setDoOutput(false);
          huc.connect();
          if ((_url.isEmpty()) || (huc.getResponseCode() == 404))
          {
            System.out.println("Failed to load texture for " + _url);
            texture = ImageBitmap.load("/char.png");
            return;
          }
          System.out.println("Loading texture for " + _url);
          Bitmap newTexture = ImageBitmap.load(ImageIO.read(huc.getInputStream()));
          hasHair = Zombie.this.checkHair(newTexture);
          texture = newTexture;
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
          huc.disconnect();
        }
      }
    }
    .start();
  }

  private boolean checkHair(Bitmap b)
  {
    for (int x = 32; x < 64; x++)
      for (int y = 0; y < 16; y++)
      {
        int a = b.pixels[(x + y * 64)] >> 24 & 0xFF;
        if (a < 128) return true;
      }
    return false;
  }

  public void tick()
  {
  }

  public void render(Matrix3 m, Bitmap3d bitmap, double time)
  {
    if (texture == null)
    {
      return;
    }

    m = m.clone();

    time = time * 10.0D * speed + timeOffs;

    y = (-Math.abs(Math.sin(time * 0.6662D)) * 5.0D - 24.0D + 20.0D);
    m = m.translate(x, y, z).rotY(rot).scale(size, size, size);

    head.yRot = (Math.sin(time * 0.23D) * 1.0D);
    head.xRot = (Math.sin(time * 0.1D) * 0.8D);

    iHair.yRot = (hair.yRot = head.yRot);
    iHair.xRot = (hair.xRot = head.xRot);

    arm0.xRot = (Math.sin(time * 0.6662D + 3.141592653589793D) * 2.0D);
    arm0.zRot = ((Math.sin(time * 0.2312D) + 1.0D) * 1.0D);

    arm1.xRot = (Math.sin(time * 0.6662D) * 2.0D);
    arm1.zRot = ((Math.sin(time * 0.2812D) - 1.0D) * 1.0D);

    leg0.xRot = (Math.sin(time * 0.6662D) * 1.4D);
    leg1.xRot = (Math.sin(time * 0.6662D + 3.141592653589793D) * 1.4D);

    head.render(m, texture, bitmap);
    if (hasHair)
    {
      hair.render(m, texture, bitmap);
      iHair.render(m, texture, bitmap);
    }
    body.render(m, texture, bitmap);
    arm0.render(m, texture, bitmap);
    arm1.render(m, texture, bitmap);
    leg0.render(m, texture, bitmap);
    leg1.render(m, texture, bitmap);
  }
}