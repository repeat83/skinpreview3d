package skinpreviewapplet;

import java.awt.BorderLayout;
import javax.swing.JApplet;
import net.minecraft.skintest.ModelPreview;

@SuppressWarnings("serial")
public class AppletLauncher extends JApplet
{
  ModelPreview prew;

  public void init()
  {
    prew = new ModelPreview(2, getParameter("url"));
    setLayout(new BorderLayout());
    add(prew, "Center");
  }

  public void start()
  {
    prew.start();
  }

  public void stop()
  {
    prew.stop();
  }
}