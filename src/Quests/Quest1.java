package Quests;

import Game.Items.Item;
import Game.SpellCast.FireBallSpell;
import Resources.Images;
import UI.UIInventory;
import UI.UIManager;
import UI.UIQuest1;
import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Elemental on 1/3/2017.
 */
public class Quest1 {

    private Handler handler;
    private boolean active = true;
    private UIManager uiManager;
    private boolean readQuest = false;
    public Quest1(Handler handler){

        this.handler=handler;
        
        uiManager = new UIManager(handler);

        uiManager.addObjects(new UIQuest1(470, 0, 288, 473, Images.quest[1],() -> {
        }));
    }

    public void tick() {
    	
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) && readQuest == false){
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

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
