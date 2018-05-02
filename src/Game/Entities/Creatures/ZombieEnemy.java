package Game.Entities.Creatures;

import Game.Entities.EntityBase;
import Game.Inventories.Inventory;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.util.Random;

public class ZombieEnemy extends CreatureBase  {

    private Animation animDown, animUp, animLeft, animRight;

    private int animWalkingSpeed = 150;
    private Inventory zombieInventory;
    private Rectangle zombieCam;

    private int healthcounter = 0;

    private Random randint;
    private int moveCount = 0;
    private int direction;

    public ZombieEnemy(Handler handler, float x, float y) {
        super(handler, x, y, CreatureBase.DEFAULT_CREATURE_WIDTH, CreatureBase.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 8 * 2;
        bounds.y = 18 * 2;
        bounds.width = 16 * 2;
        bounds.height = 14 * 2;
        speed = 1f;
        health = 50;

        zombieCam = new Rectangle();

        randint = new Random();
        direction = randint.nextInt(4) + 1;

        animDown = new Animation(animWalkingSpeed, Images.zombieEnemy_front);
        animLeft = new Animation(animWalkingSpeed, Images.zombieEnemy_left);
        animRight = new Animation(animWalkingSpeed, Images.zombieEnemy_right);
        animUp = new Animation(animWalkingSpeed, Images.zombieEnemy_back);

        zombieInventory = new Inventory(handler);
    }

    @Override
    public void tick() {
        animDown.tick();
        animUp.tick();
        animRight.tick();
        animLeft.tick();

        moveCount++;
        if (moveCount >= 60) {
            moveCount = 0;
            direction = randint.nextInt(4) + 1;
        }
        checkIfMove();
        move();

        if (isBeinghurt()) {
            healthcounter++;
            if (healthcounter >= 120) {
                setBeinghurt(false);
                System.out.print(isBeinghurt());
            }
        }
        
        if (healthcounter >= 120 && !isBeinghurt()) {
            healthcounter = 0;
        }
        zombieInventory.tick();
    }

    private void checkIfMove() {
        xMove = 0;
        yMove = 0;

        zombieCam.x = (int) (x - handler.getGameCamera().getxOffset() - (64 * 3));
        zombieCam.y = (int) (y - handler.getGameCamera().getyOffset() - (64 * 3));
        zombieCam.width = 64 * 7;
        zombieCam.height = 64 * 7;
        
        double playerWidth = handler.getWorld().getEntityManager().getPlayer().getWidth();
        double playerHeight = handler.getWorld().getEntityManager().getPlayer().getHeight();
        double playerX = handler.getWorld().getEntityManager().getPlayer().getX();
        double playerY = handler.getWorld().getEntityManager().getPlayer().getY();
        double cameraX = handler.getGameCamera().getxOffset();
        double cameraY = handler.getGameCamera().getyOffset();
        
        if (zombieCam.contains(playerX - cameraX, playerY - cameraY) 
                || zombieCam.contains(playerX - cameraX + playerWidth, playerY - cameraY + playerHeight)) {

            Rectangle cb = getCollisionBounds(0, 0);
            Rectangle ar = new Rectangle();
            int arSize = 13;
            ar.width = arSize;
            ar.height = arSize;

            if (lookingUp) {
                ar.x = cb.x + cb.width / 2 - arSize / 2;
                ar.y = cb.y - arSize;
            } else if (lookingDown) {
                ar.x = cb.x + cb.width / 2 - arSize / 2;
                ar.y = cb.y + cb.height;
            } else if (lookingLeft) {
                ar.x = cb.x - arSize;
                ar.y = cb.y + cb.height / 2 - arSize / 2;
            } else if (lookingRight) {
                ar.x = cb.x + cb.width;
                ar.y = cb.y + cb.height / 2 - arSize / 2;
            }

            for (EntityBase entity : handler.getWorld().getEntityManager().getEntities()) {
                if (entity.equals(this)) continue;
                if (entity.getCollisionBounds(0, 0).intersects(ar) && entity.equals(handler.getWorld().getEntityManager().getPlayer())) {
                    checkAttacks();
                    return;
                }
            }
            
            // Don't move if the zombie is already near the player (i.e attacking them)
            // Otherwise, move either to the left or the right
            if ((x >= playerX - 8) && (x <= playerX + 8)) xMove = 0; // Don't move
            else xMove = (x < playerX ? speed : -speed);
            
            if ((y >= playerY - 8) && (y <= playerY + 8)) yMove = 0; // Don't move
            else yMove = (y < playerY ? speed : -speed);
            
        } else {
            // Move the zombie in random directions 
            // when the player is out of its view
            switch (direction) {
            case 1://up
                yMove = -speed;
                break;
            case 2://down
                yMove = speed;
                break;
            case 3://left
                xMove = -speed;
                break;
            case 4://right
                xMove = speed;
                break;
            }
        }
    }


    /*
     * Add description
     * @param
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(
                getCurrentAnimationFrame(animDown, animUp, animLeft, animRight, 
                        Images.zombieEnemy_front, Images.zombieEnemy_back,  
                        Images.zombieEnemy_left, Images.zombieEnemy_right), 
                (int) (x - handler.getGameCamera().getxOffset()), 
                (int) (y - handler.getGameCamera().getyOffset()), 
                width, height, null);

        g.setColor(Color.BLACK);
        g.drawRect((int)(x-handler.getGameCamera().getxOffset()-1),(int)(y-handler.getGameCamera().getyOffset()-21),76,11);
        if(this.getHealth()>35){
            g.setColor(Color.GREEN);
            g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),(int)(getHealth()*1.5),10);

        }else if(this.getHealth()>=15 && getHealth()<=35){
            g.setColor(Color.YELLOW);
            g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),(int)(getHealth()*1.5),10);

        }else if(this.getHealth() < 15){
            g.setColor(Color.RED);
            g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),(int) (getHealth()*1.5),10);

        }
        g.setColor(Color.white);
        g.drawString("Health: " + getHealth(),(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-10));
    }

    /*
     * Add description
     * @param
     */
    @Override
    public void die() {
        // Make it drop an item when it dies
    }
}
