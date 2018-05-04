package Game.Inventories;

import Game.Items.Item;
import Game.SpellCast.FireBallSpell;
import Resources.Images;
import UI.UIManager;
import UI.UIQuestItems;
import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Elemental on 1/3/2017.
 */
public class QuestItems {

    private static Handler handler;
    private boolean active = false;
    private UIManager uiManager;
    private static ArrayList<Item> QuestItems;

    public QuestItems(Handler handler){

        this.handler=handler;
        QuestItems = new ArrayList<>();

        uiManager = new UIManager(handler);

        uiManager.addObjects(new UIQuestItems(470, 0, 293, 166, Images.quest[0],() -> {
        }));
        
    }

    public void tick() {

        for(Item i : QuestItems){
            if(i.getCount()==0){
                QuestItems.remove(QuestItems.indexOf(i));
                return;
            }
        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) && !handler.getWorld().getEntityManager().getPlayer().getQuest1().isActive()){
            active=!active;
            handler.getWorld().getEntityManager().getPlayer().getSpellGUI().setActive(false);

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
        renderItems(g);


    }

    //Inventory Methods
    private void renderItems(Graphics g) {

        if (QuestItems.size() >= 1) {
            g.drawImage(QuestItems.get(0).getTexture(), 504, 83, QuestItems.get(0).getWidth(), QuestItems.get(0).getHeight(), null);
            g.drawString(String.valueOf(QuestItems.get(0).getCount()), 504+34,83+35);
        } if(QuestItems.size() >= 2) {
            g.drawImage(QuestItems.get(1).getTexture(), 562, 83, QuestItems.get(1).getWidth(), QuestItems.get(1).getHeight(), null);
            g.drawString(String.valueOf(QuestItems.get(1).getCount()), 562+34,83+35);
        } if(QuestItems.size() >= 3) {
        	if(QuestItems.get(2).getName().equals("Brain")) {
        		g.drawImage(QuestItems.get(2).getTexture(), 617, 83, QuestItems.get(2).getWidth(), QuestItems.get(2).getHeight(), null);
                g.drawString(String.valueOf(QuestItems.get(2).getCount()), 617+34,83+35);
        	}else {
        		g.drawImage(QuestItems.get(2).getTexture(), 622, 83, QuestItems.get(2).getWidth(), QuestItems.get(2).getHeight(), null);
        		g.drawString(String.valueOf(QuestItems.get(2).getCount()), 622+34,83+35);
        	}
        } if(QuestItems.size() >= 4) {
            g.drawImage(QuestItems.get(3).getTexture(), 679, 83, QuestItems.get(3).getWidth(), QuestItems.get(3).getHeight(), null);
            g.drawString(String.valueOf(QuestItems.get(3).getCount()), 679+34,83+35);
        } if(QuestItems.size() >= 5) {
            return;
        }


    }

    public static void addItem(Item item){
        for(Item i : QuestItems){
            if(i.getId() == item.getId()){
                i.setCount(i.getCount()+item.getCount());
                return;
            }
        }
        if(item.getId()==2){
            handler.getWorld().getEntityManager().getPlayer().getSpellGUI().addSpell(new FireBallSpell(handler));
        }
        QuestItems.add(item);

    }

    //GET SET
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ArrayList<Item> getQuestItems(){
        return QuestItems;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
