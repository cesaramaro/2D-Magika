package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Elemental on 1/31/2017.
 */
public class UIQuest1 extends UIObject {
    private BufferedImage images;
    private ClickListlener clicker;


    public UIQuest1(float x, float y, int width, int height, BufferedImage images,ClickListlener clicker ) {
        super(x, y, width, height);
        this.images=images;
        this.clicker=clicker;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        if(active){
            g.drawImage(images,(int)x,(int)y,width,heith,null);
        }

    }


    @Override
    public void onClick()
    {
        clicker.onClick();
    }
}
