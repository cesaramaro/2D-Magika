package Game.SpellCast;

import Game.Items.Item;
import Main.Handler;
import Resources.Images;
import UI.*;
import UI.UIManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Elemental on 2/10/2017.
 */
public class SpellCastUI {

    private Handler handler;
    private boolean active = false,newItem =false;
    private UIManager uiManager;
    private ArrayList<Spells> spellList;
    private int spellSlot=1;

    public SpellCastUI(Handler handler){
        this.handler=handler;
        spellList = new ArrayList<>();

        uiManager = new UIManager(handler);

        uiManager.addObjects(new UISpellGUI(598,556, 202, 44, Images.spellGUI, new ClickListlener() {
            @Override
            public void onClick() {
                if(((handler.getMouseManager().getMouseX()>=598+7 && handler.getMouseManager().getMouseX()<=598+38)&&
                        (handler.getMouseManager().getMouseY()>=556+6 && handler.getMouseManager().getMouseY()<=556+37))&&(spellList.size()>=1)){
                    spellList.get(0).Fire();
                }


            }
        }));



    }
    public void tick() {
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_F)){
            active=!active;
            handler.getWorld().getEntityManager().getPlayer().getInventory().setActive(false);
            handler.getWorld().getEntityManager().getPlayer().getQuestItems().setActive(false);
        }
        if(!active){
            return;
        }

        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();






    }

    public void render(Graphics g) {

        if (!active) {
            uiManager.isActive(uiManager.getObjects(), false);
            return;
        }


        uiManager.isActive(uiManager.getObjects(), true);
        uiManager.Render(g);
        renderSpells(g);
    }

    private void renderSpells(Graphics g) {
        if(spellList.size()==1){
            g.drawImage(spellList.get(0).textture,598+10,556+7,24,30,null);
        }
    }

    public void addSpell(Spells spell){
        for(Spells s : spellList){

            if(s.getId() == spell.getId()){
                return;
            }

        }
        spellList.add(spell);
    }



    //GET SET
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ArrayList<Spells> getSpellList() {
        return spellList;
    }

    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;


    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
