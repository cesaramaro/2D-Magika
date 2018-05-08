package Game.Items;

import Resources.Images;
import Main.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Elemental on 1/2/2017.
 */
public class Item {
    public static int ITEMWIDTH = 32, ITEMWEIGHT = 32,DEFAULTCOUNT=1;

    protected int width = 32, height = 32;
    protected Handler handler;
    protected BufferedImage texture;
    protected String name;
    protected final int id;

    protected int x,y,count;
    protected boolean pickedUp = false;

    protected Rectangle bounds;
    //handler

    public static Item[] items = new Item[256];

    public static Item woodItem = new Item(Images.items[1],"Wood",0);
    public static Item stickItem = new Item(Images.items[0],"Stick",3);
    public static Item rockItem = new Item(Images.blocks[14],"Rock",1);
    public static Item fireRuneItem = new Item(Images.Runes[2],"Fire Rune",2);
    public static Item boneItem = new Item(Images.items[2],"Bone",4);
    public static Item potionItem = new Item(Images.items[3],"Potion",5);
    public static Item brainItem = new Item(Images.items[4],"Brain",6);
    public static Item medkitItem = new Item(Images.items[5],"Medkit",7);
    public static Item boss = new Item(Images.bossEnemy_front[0],"Boss",8);
    
    //class

    public Item(BufferedImage texture, String name, int id){
        this.texture=texture;
        this.id=id;
        this.name=name;
        count = DEFAULTCOUNT;

        bounds = new Rectangle(x,y,ITEMWIDTH,ITEMWEIGHT);

        items[id]=this;
    }
    public Item(BufferedImage texture, String name, int id,int height,int width){
        this.texture=texture;
        this.id=id;
        this.name=name;
        count = DEFAULTCOUNT;
        this.width=width;
        this.height=height;

        bounds = new Rectangle(x,y,ITEMWIDTH,ITEMWEIGHT);

        items[id]=this;
    }


    public void tick(){
        if(handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0f,0f).intersects(bounds)){
            pickedUp=true;
            goodieDetect();
        }
    }
    public void goodieDetect() {
    	if(this.name.equals("Medkit")) {
    		handler.getWorld().getEntityManager().getPlayer().setHealth(handler.getWorld().getEntityManager().getPlayer().getMaxHealth());
    	}else if(this.name.equals("Potion")) {
    		System.out.println("Potion");
    		handler.getWorld().getEntityManager().getPlayer().setPotionActive(true);
    	}else {
    		handler.getWorld().getEntityManager().getPlayer().getInventory().addItem(this);
    	}
    }
    public void render(Graphics g){
        if(handler == null){
            return;
        }
        render(g,(int)(x-handler.getGameCamera().getxOffset()),(int)(y - handler.getGameCamera().getyOffset()));
    }

    public void render(Graphics g ,int x, int y){
        g.drawImage(texture,x,y,ITEMWEIGHT,ITEMWEIGHT,null);

    }

    public Item createNew(int x, int y,int count){
        Item i = new Item(texture,name,id);
        i.setCount(count);
        i.setPosition(x,y);
        return i;
    }

    public void setPosition(int x,int y){
        this.x=x;
        this.y=y;
        bounds.x=x;
        bounds.y=y;
    }


    //get and set

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
