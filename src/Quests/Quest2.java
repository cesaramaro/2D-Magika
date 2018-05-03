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
public class Quest2 {

    private Handler handler;
    private boolean active = false;
    private UIManager uiManager;
    private boolean readQuest = false;
    private static ArrayList<Item> Quest2Items;
    public static Item woodItem = new Item(Images.items[1],"Wood",0);
    public static Item stickItem = new Item(Images.items[0],"Stick",3);
    public static Item rockItem = new Item(Images.blocks[14],"Rock",1);
    public static Item fireRuneItem = new Item(Images.Runes[2],"Fire Rune",2);
    public static Item boneItem = new Item(Images.items[2],"Bone",4);
    public Quest2(Handler handler){
    	Quest2Items = new ArrayList<>();
		woodItem.setCount(6);
        rockItem.setCount(5);
		Quest2Items.add(woodItem);
		Quest2Items.add(rockItem);
        this.handler=handler;
        uiManager = new UIManager(handler);

        uiManager.addObjects(new UIQuest(470, 0, 288, 473, Images.quest[2],() -> {
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
    public ArrayList<Item> getQuest2Items() {
		return Quest2Items;
    }
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}