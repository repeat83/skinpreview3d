package net.minecraft.skintest;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import net.minecraft.skintest.gfx.Bitmap3d;
import net.minecraft.skintest.gfx.ImageBitmap;
import net.minecraft.skintest.math.Matrix3;
import net.minecraft.skintest.math.Zombie;

public class ModelPreview extends Canvas
  implements Runnable
{
  private static final long serialVersionUID = 1L;
  private int scale;
  private Thread thread;
  private volatile boolean running = false;
  private ImageBitmap screenBitmap;
  private Zombie zombie;
  private boolean paused = false;
  private int xOld;
  private int yOld;
  long lastTime = System.nanoTime();
  float time = 0.0F;
  float yRot = 0.0F;
  float xRot = 0.0F;

  public ModelPreview(int scale, String name)
  {
    this.scale = scale;
    int w = 160 * scale;
    int h = 160 * scale;
    setPreferredSize(new Dimension(w, h));
    setMaximumSize(new Dimension(w, h));
    setMinimumSize(new Dimension(w, h));

    screenBitmap = new ImageBitmap(160, 160);
    zombie = new Zombie(name);

    addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent e)
      {
        paused = (!paused);
      }

      public void mouseEntered(MouseEvent e)
      {
      }

      public void mouseExited(MouseEvent e)
      {
      }

      public void mousePressed(MouseEvent e)
      {
        xOld = e.getX();
        yOld = e.getY();
      }

      public void mouseReleased(MouseEvent e)
      {
      }
    });
    addMouseMotionListener(new MouseMotionListener()
    {
      public void mouseDragged(MouseEvent e)
      {
        yRot -= (e.getX() - xOld) / 80.0F;
        xRot += (e.getY() - yOld) / 80.0F;
        xOld = e.getX();
        yOld = e.getY();

        float max = 1.570796F;
        if (xRot < -max) xRot = (-max);
        if (xRot > max) xRot = max;
      }

      public void mouseMoved(MouseEvent e)
      {
      }
    });
  }

  public void render(Bitmap3d bitmap)
  {
    long now = System.nanoTime();
    if (!paused)
    {
      time -= (float)(now - lastTime) / 1.0E+009F;
    }
    lastTime = now;

    bitmap.clearZBuffer();
    bitmap.fill(0, 0, bitmap.width, bitmap.height, 10531040);
    Matrix3 m = new Matrix3().translate(0.0D, 0.0D, 30.0D).rotX(xRot).rotY(yRot);
    zombie.render(m, bitmap, time);
  }

  public void paint(Graphics g)
  {
  }

  public void update(Graphics g)
  {
  }

  public synchronized void start()
  {
    if (thread == null)
    {
      thread = new Thread(this);
      running = true;
      thread.start();
    }
  }

  public synchronized void stop()
  {
    if (thread != null)
    {
      running = false;
      try
      {
        thread.join();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      thread = null;
    }
  }

  public void run()
  {
    while (running)
    {
      render(screenBitmap);

      Graphics g = getGraphics();
      g.drawImage(screenBitmap.getImage(), 0, 0, 160 * scale, 160 * scale, 0, 0, 160, 160, null);
      g.dispose();
      try
      {
        Thread.sleep(5L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args)
  {
    ModelPreview modelPreview = new ModelPreview(2, "http://www.minecraft.net/skin/Notch.png");
    JFrame frame = new JFrame("ModelPreview");
    frame.setLayout(new BorderLayout());
    frame.add(modelPreview, "Center");
    frame.pack();
    frame.setDefaultCloseOperation(3);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    modelPreview.start();
  }
}