package Game.Entities.Statics;

import Game.Entities.Creatures.Player;
import Game.Items.Item;
import Game.Tiles.Tile;
import Resources.Images;
import Main.Handler;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Elemental on 1/2/2017.
 */
public class Chest extends StaticEntity {

    private File audioFile;
    private AudioInputStream audioStream;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip audioClip;
    private Random randint;
    private int RNGR;
    private boolean opened = false;
    private Rectangle ir = new Rectangle();

    public Chest(Handler handler, float x, float y, String type) {
        super(handler, x, y, Tile.TILEWIDTH, Tile.TILEHEIGHT);

        bounds.x=0;
        bounds.y=0;
        bounds.width = 64;
        bounds.height = 64;
        health=1000000;
        this.type = type;
        
        try {
            audioFile = new File("res/music/Pickaxe.wav");
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            format = audioStream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);



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
        if(isBeinghurt()) { 
        	if(opened) {
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		opened =!opened;
        	}else if (!opened) {
        		opened =!opened;
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	this.beinghurt = false;
        	health = 1000000;
        }
    }
    

    @Override
    public void render(Graphics g) {
        if(!opened) {
        g.drawImage(Images.chest[0],(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()),width,height,null);
        }
        else {
        	g.drawImage(Images.chest[1],(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()),width,height,null);
        }
    }

    @Override
    public void die() {
    	for(int i = 0; i < handler.getWorld().getEntityManager().getEntities().size();i++) {
        if(handler.getWorld().getEntityManager().getEntities().get(i).getType().equals("Door")) {
        	handler.getWorld().getEntityManager().getEntities().get(i).setVisible(true);
        }
    	}
    }
}
