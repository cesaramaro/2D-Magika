package Worlds;

import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Creatures.ZombieEnemy;
import Game.Entities.Statics.*;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;

public class DarkWorld extends BaseWorld    {
    
    private Handler handler;
    private Player player;
    
    public DarkWorld(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;
        this.player = player;
        
        currentWorld = 3;
        entityManager.addEntity(new Rock(handler, 100, 250, "Rock"));
        entityManager.addEntity(new Tree(handler, 100, 450, "Tree"));
        entityManager.addEntity(new Rock(handler, 528, 276, "Rock"));
        entityManager.addEntity(new Tree(handler, 684, 1370, "Tree"));
        entityManager.addEntity(new Rock(handler, 765, 888, "Rock"));
        entityManager.addEntity(new Tree(handler, 88, 1345, "Tree"));
        entityManager.addEntity(new Rock(handler, 77, 700, "Rock"));
        entityManager.addEntity(new Tree(handler, 700, 83, "Tree"));
        entityManager.addEntity(new ZombieEnemy(handler, 300, 500));
        entityManager.addEntity(new SkelyEnemy(handler, 400, 300));

        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }
//    @Override
//    public void addQuestItems() {
//    	for (Item item : handler.getWorld().getEntityManager().getPlayer().getQuest3().getQuest3Items()) {
//			QuestItems.addItem(item);
//		}
//    }
    @Override 
    public void setNextWorld(){ //Sets next world (makes skip possible)
    	nextWorld = this;
    }

}