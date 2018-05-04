package Game.Entities.Creatures;

import Game.Entities.EntityBase;
import Game.Tiles.Tile;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.DuplicateFormatFlagsException;

/**
 * Created by Elemental on 1/1/2017.
 */


public abstract class CreatureBase extends EntityBase {

    protected static final float DEFAULT_SPEED = 3.0f;
    protected static final int DEFAULT_CREATURE_WIDTH = 64, DEFAULT_CREATURE_HEIGHT = 64;

    protected int attack = 3;

    protected float speed;
    protected float xMove, yMove;

    protected long lastAttackTimer, attackCooldown = 800, attackTimer = attackCooldown;

    protected boolean lookingDown = true, lookingLeft = false, lookingRight = false, lookingUp = false;

    public CreatureBase(Handler handler, float x, float y, int height, int width) {
        super(handler,x, y,height,width);
        speed = DEFAULT_SPEED;
        xMove = 0;
        yMove = 0;
    }

    public BufferedImage getCurrentAnimationFrame( Animation animDown, Animation animUp, Animation animLeft, Animation animRight, BufferedImage[] front,BufferedImage[] back,BufferedImage[] left,BufferedImage[] right) {
        if(xMove < 0){
            lookingLeft=true;
            lookingDown=false;
            lookingRight=false;
            lookingUp=false;
            return animLeft.getCurrentFrame();
        }else if(xMove > 0){
            lookingLeft=false;
            lookingDown=false;
            lookingRight=true;
            lookingUp=false;
            return animRight.getCurrentFrame();
        }else if(yMove < 0){
            lookingLeft=false;
            lookingDown=false;
            lookingRight=false;
            lookingUp=true;
            return animUp.getCurrentFrame();
        }else if(yMove > 0){
            lookingLeft=false;
            lookingDown=true;
            lookingRight=false;
            lookingUp=false;
            return animDown.getCurrentFrame();
        }else{
            if(lookingDown){
                return front[0];
            }else if(lookingUp){
                return back[0];
            }else if(lookingLeft){
                return left[0];
            }else if(lookingRight){
                return right[0];
            }else{
                return front[0];
            }
        }
    }

    public void move(){
        if(!checkEntityCollisions(xMove, 0f))
            moveX();
        if(!checkEntityCollisions(0f, yMove))
            moveY();
    }

    public void moveX(){
        if(xMove > 0){//Moving right
            int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;

            if(!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)){
                x += xMove;
            }else{
                x = tx * Tile.TILEWIDTH - bounds.x - bounds.width - 1;
            }

        }else if(xMove < 0){//Moving left
            int tx = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;

            if(!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)){
                x += xMove;
            }else{
                x = tx * Tile.TILEWIDTH + Tile.TILEWIDTH - bounds.x;
            }

        }
    }

    public void moveY(){
        if(yMove < 0){//Up
            int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;

            if(!collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty)){
                y += yMove;
            }else{
                y = ty * Tile.TILEHEIGHT + Tile.TILEHEIGHT - bounds.y;
            }

        }else if(yMove > 0){//Down
            int ty = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;

            if(!collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty)){
                y += yMove;
            }else{
                y = ty * Tile.TILEHEIGHT - bounds.y - bounds.height - 1;
            }

        }
    }

    @SuppressWarnings("Duplicates")
    public void checkAttacks(){
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if(attackTimer < attackCooldown)
            return;

        Rectangle cb = getCollisionBounds(0, 0);
        Rectangle ar = new Rectangle();
        int arSize = 20;
        ar.width = arSize;
        ar.height = arSize;

        if(lookingUp){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y - arSize;
        }else if(lookingDown){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y + cb.height;
        }else if(lookingLeft){
            ar.x = cb.x - arSize;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else if(lookingRight){
            ar.x = cb.x + cb.width;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else{
            return;
        }

        attackTimer = 0;

        for(EntityBase e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(this))
                continue;
            if(e.getCollisionBounds(0, 0).intersects(ar)){
                e.hurt(attack);
                System.out.println(e + " has " + e.getHealth() + " lives.");
                return;
            }
        }

    }

    protected boolean collisionWithTile(int x, int y){
        return handler.getWorld().getTile(x, y).isSolid();
    }


    //getters and setters
    public float getxMove() {
        return xMove;
    }

    public void setxMove(float xMove) {
        this.xMove = xMove;
    }

    public float getyMove() {
        return yMove;
    }

    public void setyMove(float yMove) {
        this.yMove = yMove;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

}
