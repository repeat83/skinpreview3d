package net.minecraft.skintest;

import java.awt.BorderLayout;
import javax.swing.JApplet;

public class ModelPreviewApplet extends JApplet
{
  private static final long serialVersionUID = 1L;
  ModelPreview modelPreview;

  public void init()
  {
    String url = "http://pereulok.net.ru/Minecraft_Skins/" + getParameter("name") + ".png";
    System.out.println(url);
    modelPreview = new ModelPreview(2, url);
    setLayout(new BorderLayout());
    add(modelPreview, "Center");
  }

  public void start()
  {
    modelPreview.start();
  }

  public void stop()
  {
    modelPreview.stop();
  }
}