package Game.Entities.Statics;

import Game.Items.Item;
import Game.Tiles.Tile;
import Resources.Images;
import Main.Handler;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Bush extends StaticEntity {
    private File audioFile;
    private AudioInputStream audioStream;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip audioClip;
    private Random randint;
    private int RNGR;

    public Bush(Handler handler, float x, float y, String type) {
        super(handler, x, y, Tile.TILEHEIGHT, Tile.TILEWIDTH);
        bounds.x=0;
        bounds.y=0;
        bounds.width = 64;
        bounds.height = 58;
        health=10;
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
        g.drawImage(Images.blocks[15],(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()),width,height,null);

    }

    /*
     * Drops an item when the bush is destroyed
     */
    @Override
    public void die() {
    	randint = new Random();
        RNGR = randint.nextInt(2) + 1;
        handler.getWorld().getItemManager().addItem(Item.stickItem.createNew((int) x + bounds.x, (int) y + bounds.y, RNGR));
    }

    /*
     * Gets the bush's health
     * @return int - health
     */
    public int getHealth() {
        return health;
    }

    /*
     * Sets the bush's health
     * @param int - health
     */
    public void setHealth(int health) {
        this.health = health;
    }

}
