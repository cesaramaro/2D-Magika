package Game.Entities;

import Resources.Images;
import Main.Handler;

import java.awt.*;

import Game.Entities.Creatures.Player;
import Game.Tiles.Tile;

/**
 * Created by Elemental on 1/1/2017.
 */
public abstract class EntityBase {

    //public static final int DEFAULT_HEALTH = 10;

    public static final int DEFAULT_HEALTH = 10;
    protected Handler handler;
    protected float x, y;
    protected int width, height;
    protected int health;
    protected int maxHealth;
    protected boolean active = true;
    protected boolean visible = true;
    protected Rectangle bounds;
    protected boolean beinghurt=false;
    protected int count = 0;
    protected String type = "";


    public EntityBase(Handler handler, float x, float y, int height, int width) {
        this.handler = handler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        health = DEFAULT_HEALTH;
        
        bounds = new Rectangle(0, 0, width,height);
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract void die();

    public void hurt(int amt) {
        health -= amt;
        beinghurt=true;
        if (health <= 0) {
            active = false;
            die();
        }
    }
    
    public void renderLife(Graphics g) {
        if (beinghurt && count <=8){
            if(count == 8){
                count = 0;
                beinghurt=false;
            }
            if(getHealth()>=0) {
            g.drawImage(Images.numbers[getHealth()],(int)(x-handler.getGameCamera().getxOffset()+bounds.x),(int)(y-handler.getGameCamera().getyOffset()-getHeight()+(bounds.height/3)),42,42,null);
            count++;
            }
        }
    }

    public boolean checkEntityCollisions(float xOffset, float yOffset){
        for(EntityBase e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(this))
                continue;
            if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
                return true;
        }
        return false;
    }

    public Rectangle getCollisionBounds(float xOffset, float yOffset){
        return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height);
    }
    
    /*
     * Get an x, y location if it's a player
     * @return Point - x, y location of the player relative to the blocks
     */
    public Point getLocation() {
        if (this instanceof Player) {
            double pXMove = handler.getWorld().getEntityManager().getPlayer().getxMove();
            double pYMove = handler.getWorld().getEntityManager().getPlayer().getyMove();

            int playerX = (int) ((this.x + pXMove + this.bounds.x + this.bounds.width) / Tile.TILEWIDTH);
            int playerY = (int) ((this.y + pYMove + this.bounds.y + this.bounds.height) / Tile.TILEHEIGHT);

            return new Point (playerX, playerY);
        }
        return null;
    }
    
    /*
     * Checks if the entity is being hurt
     * @return boolean - whether it's being hurt or not
     */
    public boolean isBeinghurt() {
        return beinghurt;
    }

    /*
     * Sets if the entity is being hurt
     * @param boolean - whether it should be set to being hurt or not
     */
    public void setBeinghurt(boolean beinghurt) {
        this.beinghurt = beinghurt;
    }

    /*
     * Gets the entity's x coordinate
     * @return float
     */
    public float getX() {
        return x;
    }

    /*
     * Sets the entity's x coordinate
     * @param float - x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /*
     * Gets the entity's y coordinate
     * @return float
     */
    public float getY() {
        return y;
    }

    /*
     * Sets the entity's y coordinate
     * @param float - y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /*
     * Gets the entity's width
     * @return int - width
     */
    public int getWidth() {
        return width;
    }

    /*
     * Sets the entity's width
     * @param int - width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /*
     * Gets the entity's height
     * @return int - height
     */
    public int getHeight() {
        return height;
    }

    /*
     * Sets the entity's height
     * @param int - height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /*
     * Gets the entity's health
     * @return int - health
     */
    public int getHealth() {
        return health;
    }

    /*
     * Sets the entity's health
     * @param int - health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /*
     * Checks if the entity is active
     * @return boolean
     */
    public boolean isActive() {
        return active;
    }

    /*
     * Sets if the entity is active
     * @param boolean
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /*
     * Checks if the entity is visible
     * @return boolean
     */
    public boolean isVisible() {
        return visible;
    }

    /*
     * Sets if the entity is visible
     * @param boolean
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /*
     * Gets the entity's type
     * @return String
     */
    public String getType() {
        return type;
    }

    /*
     * Sets the entity's type
     * @param String
     */
    public void setType(String type) {
        this.type = type;
    }
}
