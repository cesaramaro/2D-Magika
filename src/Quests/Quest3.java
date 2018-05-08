package Quests;

import Game.Items.Item;
import Game.SpellCast.FireBallSpell;
import Resources.Images;
import UI.UIInventory;
import UI.UIManager;
import UI.UIQuest;
import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Elemental on 1/3/2017.
 */
public class Quest3 {

    private Handler handler;
    private boolean active = false;
    private UIManager uiManager;
    private boolean readQuest = false;
    private static ArrayList<Item> Quest3Items;
    public static Item woodItem = new Item(Images.items[1],"Wood",0);
    public static Item stickItem = new Item(Images.items[0],"Stick",3);
    public static Item rockItem = new Item(Images.blocks[14],"Rock",1);
    public static Item fireRuneItem = new Item(Images.Runes[2],"Fire Rune",2);
    public static Item boneItem = new Item(Images.items[2],"Bone",4);
    public static Item potionItem = new Item(Images.items[3],"Potion",5);
    public static Item brainItem = new Item(Images.items[4],"Brain",6);
    public static Item medkitItem = new Item(Images.items[5],"Medkit",7);
    public static Item boss = new Item(Images.bossEnemy_front[0],"Boss",8);
    
    public Quest3(Handler handler){
    	Quest3Items = new ArrayList<>();
		boss.setCount(1);
		Quest3Items.add(boss);
        this.handler=handler;
        uiManager = new UIManager(handler);

        uiManager.addObjects(new UIQuest(470, 0, 288, 473, Images.quest[3],() -> {
        }));
    }

    public void tick() {
    	
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) && readQuest == false && active == true){
            active=!active;
            readQuest = true;
            handler.getWorld().getEntityManager().getPlayer().getSpellGUI().setActive(false);
            handler.getWorld().getEntityManager().getPlayer().getInventory().setActive(false);
            
        }
        if(!active){
            return;
        }

        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();



    }

    public void render(Graphics g) {
    	
        if(!active){
            uiManager.isActive(uiManager.getObjects(),false);
            return;
        }


        uiManager.isActive(uiManager.getObjects(),true);
        uiManager.Render(g);
        g.setColor(Color.white);
    	

    }

    //GET SET
    public Handler getHandler() {
        return handler;
    }
    public boolean isActive() {
        return active;
    }
    public ArrayList<Item> getQuest3Items() {
		return Quest3Items;
    }
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
