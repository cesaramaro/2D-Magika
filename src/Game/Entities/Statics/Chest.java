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
import java.util.ArrayList;
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
    private int timeOpen;
    public Boolean EP = false;

    public Chest(Handler handler, float x, float y, String type) {
        super(handler, x, y, Tile.TILEWIDTH, Tile.TILEHEIGHT);
        bounds.x=0;
        bounds.y=0;
        bounds.width = 64;
        bounds.height = 64;
        health=1000000;
        this.type = type;
        
        ir.width = bounds.width;
        ir.height = bounds.height;
        int irx=(int)(bounds.x-handler.getGameCamera().getxOffset()+x);
        int iry= (int)(bounds.y-handler.getGameCamera().getyOffset()+height);
        ir.y=iry;
        ir.x=irx;

    }

    @Override
    public void tick() {
    	if(opened) {
    		timeOpen++;
    	}
    	if(timeOpen>=300) {
    		opened = false;
    		timeOpen = 0;
    	}
        if(isBeinghurt()) { 
        	deliverItems();
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
        if(handler.getKeyManager().attbut){
            EP=true;

        }else if(!handler.getKeyManager().attbut){
            EP=false;
        }
        if(handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().isEmpty()&& !handler.getWorld().getWorldStart()) {
        	active = false;
    		die();
        }
    }
    
    public void deliverItems() {
    	for(int i = 0 ; i< handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().size();i++) {
    		for(int j = 0 ; j<handler.getWorld().getEntityManager().getPlayer().getInventory().getInventoryItems().size();j++) {
    			if(handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().get(i).getName().equals(handler.getWorld().getEntityManager().getPlayer().getInventory().getInventoryItems().get(j).getName())) {
    				while(handler.getWorld().getEntityManager().getPlayer().getInventory().getInventoryItems().get(j).getCount()>0&& handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().get(i).getCount()>0) {
    					handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().get(i).setCount(handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().get(i).getCount()-1);
    					handler.getWorld().getEntityManager().getPlayer().getInventory().getInventoryItems().get(j).setCount(handler.getWorld().getEntityManager().getPlayer().getInventory().getInventoryItems().get(j).getCount()-1);
    				}
    				
    			}
    		}
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
        checkForPlayer(g, handler.getWorld().getEntityManager().getPlayer());
    }
    private void checkForPlayer(Graphics g, Player p) {
        Rectangle pr = p.getCollisionBounds(0,0);

        if (ir.contains(pr) && !EP) {
            g.drawImage(Images.E,(int) x+width,(int) y+10+10,32,32,null);
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
