package Worlds;

import Game.Entities.Creatures.BossEnemy;
import Game.Entities.Creatures.Player;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;

public class DarkWorld extends BaseWorld {
    
    private Handler handler;
    private Player player;
    
    private int bossSpawnX = 600;
    private int bossSpawnY = 700;
    
    public DarkWorld(Handler handler, String path, Player player) {
        super(handler, path, player);
        this.handler = handler;
        this.player = player;
        
        currentWorld = 3;
        entityManager.addEntity(new BossEnemy(handler, bossSpawnX, bossSpawnY));
        
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }
    
    @Override
    public void addQuestItems() {
    	for (Item item : handler.getWorld().getEntityManager().getPlayer().getQuest3().getQuest3Items()) {
			QuestItems.addItem(item);
		}
    }
    
    @Override 
    public void setNextWorld() { // Sets next world (makes skip possible)
    	nextWorld = this;
    }

}