package Worlds;

import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Creatures.ZombieEnemy;
import Game.Entities.Statics.*;
import Game.GameStates.State;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Main.Handler;
import Quests.Quest1;
import Resources.Images;

/**
 * Created by Elemental on 1/2/2017.
 */
public class World1 extends BaseWorld{

    private Handler handler;
    private BaseWorld caveWorld;
    public static Item woodItem = new Item(Images.items[1],"Wood",0);
    public static Item stickItem = new Item(Images.items[0],"Stick",3);
    public static Item rockItem = new Item(Images.blocks[14],"Rock",1);
    public static Item fireRuneItem = new Item(Images.Runes[2],"Fire Rune",2);
    public static Item boneItem = new Item(Images.items[2],"Bone",4);

    public World1(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;
        caveWorld = new CaveWorld(handler,"res/Maps/caveMap.map",player);
        entityManager.addEntity(new Tree(handler, 100, 250,"Tree"));
        entityManager.addEntity(new Rock(handler, 100, 450,"Rock"));
        entityManager.addEntity(new Tree(handler, 533, 276,"Tree"));
        entityManager.addEntity(new Rock(handler, 684, 1370,"Rock"));
        entityManager.addEntity(new Tree(handler, 765, 888,"Tree"));
        entityManager.addEntity(new Rock(handler, 88, 1345,"Rock"));
        entityManager.addEntity(new Tree(handler, 77, 700,"Tree"));
        entityManager.addEntity(new Rock(handler, 700, 83,"Rock"));
        entityManager.addEntity(new Door(handler, 100, 0,caveWorld,"Door"));
        entityManager.addEntity(new SkelyEnemy(handler, 1250, 500));
        entityManager.addEntity(new ZombieEnemy(handler, spawnX + 300, spawnY + 300));
        entityManager.addEntity(new Bush(handler,445,185,"Bush"));
        entityManager.addEntity(new Chest(handler,250,0,"Chest"));
        entityManager.addEntity(new Bush(handler,700,185,"Bush"));
        entityManager.addEntity(new Bush(handler,575,895,"Bush"));
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
        stickItem.setCount(3);
        QuestItems.addItem(stickItem);
        boneItem.setCount(3);
        QuestItems.addItem(boneItem);
        
        nextWorld = caveWorld;
    }
}