package Worlds;
import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Creatures.ZombieEnemy;
import Game.Entities.Statics.Box;
import Game.Entities.Statics.Bush;
import Game.Entities.Statics.Chest;
import Game.Entities.Statics.Door;
import Game.Entities.Statics.Rock;
import Game.Entities.Statics.Tree;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;

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
        currentWorld = 2;
        darkWorld = new DarkWorld(handler, "res/Maps/darkMap.map", player);
        
        entityManager.addEntity(new Bush(handler, 100, 450, "Bush"));
        entityManager.addEntity(new Tree(handler, 110, 270, "Tree"));
        entityManager.addEntity(new Tree(handler, 88, 1345, "Tree"));
        entityManager.addEntity(new Tree(handler, 200, 400, "Tree"));
        entityManager.addEntity(new Tree(handler, 400, 320, "Tree"));
        entityManager.addEntity(new Tree(handler, 100, 620, "Tree"));
        entityManager.addEntity(new Tree(handler, 600, 500, "Tree"));
        entityManager.addEntity(new Rock(handler, 700, 500, "Rock"));
        entityManager.addEntity(new Rock(handler, 700, 83, "Rock"));
        entityManager.addEntity(new Rock(handler, 528, 276, "Rock"));
        entityManager.addEntity(new Rock(handler, 684, 1370, "Rock"));
        entityManager.addEntity(new Rock(handler, 765, 888, "Rock"));
        entityManager.addEntity(new Chest(handler, 250, 0, "Chest"));
        entityManager.addEntity(new Door(handler, 100, 0, darkWorld, "Door"));
        entityManager.addEntity(new Box(handler, 700, 575, "Box"));
        entityManager.addEntity(new Box(handler, 130, 775, "Box"));
        entityManager.addEntity(new Box(handler, 960, 315, "Box"));
        entityManager.addEntity(new ZombieEnemy(handler, ZombieEnemy.zombieSpawnX, ZombieEnemy.zombieSpawnY));
        entityManager.addEntity(new SkelyEnemy(handler, spawnX + 350, spawnY + 550));
        
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }

    @Override
    public void addQuestItems() {
    	for (Item item : handler.getWorld().getEntityManager().getPlayer().getQuest2().getQuest2Items()) {
			QuestItems.addItem(item);
		}
    }
    @Override 
    public void setNextWorld(){ //Sets next world (makes skip possible)
    	nextWorld = darkWorld;
    }
}