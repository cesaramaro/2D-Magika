package Game.Entities.Statics;

import Game.Items.Item;
import Game.Tiles.Tile;
import Resources.Images;
import Main.Handler;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Elemental on 1/1/2017.
 */
public class Tree extends StaticEntity {
    private File audioFile;
    private AudioInputStream audioStream;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip audioClip;

    public Tree(Handler handler, float x, float y, String type) {
        super(handler, x, y, Tile.TILEHEIGHT * 2, Tile.TILEWIDTH);
        bounds.x=14;
        bounds.y=85;
        bounds.width = 36;
        bounds.height = 48;
        health=16;
        this.type = type;

        try {
            audioFile = new File("res/music/Chopping.wav");
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            format = audioStream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.setMicrosecondPosition(2000);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void tick() {
        if(isBeinghurt()){
            audioClip.start();
        }
        if(!isBeinghurt() && !handler.getKeyManager().attbut){
            audioClip.stop();
        }
        if(!isActive()){
            audioClip.stop();

        }

    }

    @Override
    public void render(Graphics g) {
        renderLife(g);
        g.drawImage(Images.blocks[13],(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()),width,height,null);

    }



    @Override
    public void die() {
        handler.getWorld().getItemManager().addItem(Item.woodItem.createNew((int)x + bounds.x,(int)y + bounds.y,1));


    }

    public void renderLife(Graphics g) {
        if (beinghurt && count <=8){
            if(count == 8){
                count = 0;
                beinghurt=false;
            }

            g.drawImage(Images.numbers[getHealth()],(int)(x-handler.getGameCamera().getxOffset()+bounds.x),(int)(y-handler.getGameCamera().getyOffset()-getHeight()+(bounds.height+32)),42,42,null);
            count++;

        }
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
