package Worlds;

import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Statics.*;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;

/**
 * Created by Elemental on 1/2/2017.
 */
public class World1 extends BaseWorld{

    private Handler handler;
    private BaseWorld caveWorld;

    public World1(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;
        currentWorld = 1;
        caveWorld = new CaveWorld(handler,"res/Maps/caveMap.map",player);
        // Entities in World
        entityManager.addEntity(new Tree(handler, 100, 250,"Tree"));
        entityManager.addEntity(new Rock(handler, 100, 450,"Rock"));
        entityManager.addEntity(new Tree(handler, 533, 276,"Tree"));
        entityManager.addEntity(new Rock(handler, 684, 1370,"Rock"));
        entityManager.addEntity(new Tree(handler, 765, 888,"Tree"));
        entityManager.addEntity(new Rock(handler, 88, 1345,"Rock"));
        entityManager.addEntity(new Tree(handler, 77, 700,"Tree"));
        entityManager.addEntity(new Rock(handler, 700, 83,"Rock"));
        entityManager.addEntity(new Bush(handler,445,185,"Bush"));
        entityManager.addEntity(new Bush(handler, 960, 320, "Bush"));
        entityManager.addEntity(new Bush(handler, 320, 960, "Bush"));
        entityManager.addEntity(new Chest(handler,250,0,"Chest"));
        entityManager.addEntity(new Bush(handler,700,185,"Bush"));
        entityManager.addEntity(new Bush(handler,575,895,"Bush"));
        entityManager.addEntity(new Door(handler, 100, 0, caveWorld, "Door"));
        entityManager.addEntity(new SkelyEnemy(handler, 1250, 500));
        entityManager.addEntity(new SkelyEnemy(handler, 500, 700));
        entityManager.addEntity(new SkelyEnemy(handler, 600, 1250));
        
        //Player Spawn Point
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
         
    }
    @Override
    public void addQuestItems() { //adds quest Items (Runs only one time. Called in BaseWorld)
    	for (Item item : handler.getWorld().getEntityManager().getPlayer().getQuest1().getQuest1Items()) {
			QuestItems.addItem(item);
		}
    }
    @Override 
    public void setNextWorld(){ //Sets next world (makes skip possible)
    	nextWorld = caveWorld;
    }
}