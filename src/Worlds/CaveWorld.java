package Worlds;
import Game.Entities.Creatures.Player;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;
import Resources.Images;

/**
 * Created by Elemental on 2/10/2017.
 */
public class CaveWorld extends BaseWorld{
    private Handler handler;
    private Player player;
    
    public CaveWorld(Handler handler, String path, Player player) {
        super(handler,path,player);
        this.handler = handler;
        this.player=player;
    }

    @Override
    public void addQuestItems() {
    	for (Item item : handler.getWorld().getEntityManager().getPlayer().getQuest2().getQuest2Items()) {
			QuestItems.addItem(item);
		}
    }
}