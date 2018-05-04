package Worlds;

import Game.Entities.Creatures.Player;
import Game.Entities.EntityManager;
import Game.GameStates.State;
import Game.Items.Item;
import Game.Items.ItemManager;
import Game.Tiles.Tile;
import Main.Handler;
import Quests.Quest1;
import Resources.Images;
import Resources.Utils;

import java.awt.*;

/**
 * Created by Elemental on 2/10/2017.
 */
public class BaseWorld {

    protected Handler handler;
    protected int width, height;
    protected int spawnX, spawnY;
    protected int[][] tiles;
    protected int countP = 0;
    protected static BaseWorld nextWorld;
    protected EntityManager entityManager;
    protected Quest1 quest1;
    protected boolean worldStart;
//    public int currentWorld;

    //Item
    protected ItemManager itemManager;



    public BaseWorld(Handler handler, String path, Player player) {
    	
        this.handler=handler;
        entityManager = new EntityManager(handler,player);
        itemManager=new ItemManager(handler);
        worldStart = false;
        loadWorld(path);

    }

    public void tick(Graphics g){
        entityManager.tick();
        itemManager.tick();
        countP++;
        if(countP>=30){
            countP=30;
        }

        if(handler.getKeyManager().pbutt && countP>=30){
            handler.getMouseManager().setUimanager(null);
            countP=0;
            State.setState(handler.getGame().pauseState);
        }
        if(handler.getKeyManager().skipbut){
        	if(!handler.getWorld().equals(nextWorld)) {
        	g.drawImage(Images.loading,0,0,800,600,null);
        	handler.getWorld().getEntityManager().getPlayer().getQuestItems().setActive(false);
//            if (currentWorld == 1) {
//                handler.getWorld().getEntityManager().getPlayer().getQuest2().setActive(true);
//            } else if (currentWorld == 2) {
//                handler.getWorld().getEntityManager().getPlayer().getQuest2().setActive(false);
//            }
            handler.getWorld().getEntityManager().getPlayer().getQuest2().setActive(true);
            handler.getWorld().getEntityManager().getPlayer().getQuestItems().getQuestItems().clear();
            entityManager.getPlayer().setX(spawnX);
            entityManager.getPlayer().setY(spawnY);
        	handler.setWorld(nextWorld);
        	handler.getWorld().setWorldStart(true);
        	}
      }
        if (worldStart) {
        	addQuestItems();
        	worldStart = false;
        }
        
    }

    public void render(Graphics g){
        int xStart = (int) Math.max(0, handler.getGameCamera().getxOffset() / Tile.TILEWIDTH);
        int xEnd = (int) Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILEWIDTH + 1);
        int yStart = (int) Math.max(0, handler.getGameCamera().getyOffset() / Tile.TILEHEIGHT);
        int yEnd = (int) Math.min(height, (handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILEHEIGHT + 1);

        for(int y = yStart;y < yEnd;y++){
            for(int x = xStart;x < xEnd;x++){
                getTile(x, y).render(g, (int) (x * Tile.TILEWIDTH - handler.getGameCamera().getxOffset()),
                        (int) (y * Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
            }
        }

        //Item
        itemManager.render(g);
        //Entities
        entityManager.render(g);
        
        

        entityManager.getPlayer().getInventory().render(g);
        entityManager.getPlayer().getSpellGUI().render(g);
        entityManager.getPlayer().getQuest1().render(g);
        entityManager.getPlayer().getQuest2().render(g);
        entityManager.getPlayer().getQuestItems().render(g);

    }

    public Tile getTile(int x, int y){
        if(x < 0 || y < 0 || x >= width || y >= height)
            return Tile.grassTile;

        Tile t = Tile.tiles[tiles[x][y]];
        if(t == null)
            return Tile.dirtTile;
        return t;
    }

    protected void loadWorld(String path){
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        width = Utils.parseInt(tokens[0]);
        height = Utils.parseInt(tokens[1]);
        spawnX = Utils.parseInt(tokens[2]);
        spawnY = Utils.parseInt(tokens[3]);

        tiles = new int[width][height];
        for(int y = 0;y < height;y++){
            for(int x = 0;x < width;x++){
                tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 4]);
            }
        }
        

    }
    public void addQuestItems() {
    	
    }
    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }
    public void setWorldStart(boolean status) {
    	this.worldStart = status;
    }
    public boolean getWorldStart() {
    	return this.worldStart;
    }

}
