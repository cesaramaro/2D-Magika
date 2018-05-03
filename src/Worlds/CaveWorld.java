package Worlds;
import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Creatures.ZombieEnemy;
import Game.Entities.Statics.Door;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;
import Resources.Images;

/**
 * Created by Elemental on 2/10/2017.
 */
public class CaveWorld extends BaseWorld {
    private Handler handler;
    private Player player;
    private BaseWorld darkWorld;
    
    public CaveWorld(Handler handler, String path, Player player) {
        super(handler,path,player);
        this.handler = handler;
        this.player = player;
        darkWorld = new DarkWorld(handler, "res/Maps/darkMap.map", player);
        
        entityManager.addEntity(new Door(handler, 100, 0, darkWorld, "Door"));
        entityManager.addEntity(new ZombieEnemy(handler, ZombieEnemy.zombieSpawnX, ZombieEnemy.zombieSpawnY));
        entityManager.addEntity(new SkelyEnemy(handler, spawnX, spawnY + 300));
        
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
        nextWorld = darkWorld;
    }

    @Override
    public void addQuestItems() {
    	for (Item item : handler.getWorld().getEntityManager().getPlayer().getQuest2().getQuest2Items()) {
			QuestItems.addItem(item);
		}
    }
}