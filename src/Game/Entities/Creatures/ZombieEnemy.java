package Game.Entities.Creatures;

import Game.Entities.EntityBase;
import Game.Inventories.Inventory;
import Game.Items.Item;
import Game.Tiles.RockTile;
import Game.Tiles.Tile;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ZombieEnemy extends CreatureBase  {

    private Animation animDown, animUp, animLeft, animRight;

    private int animWalkingSpeed = 150;
    private Inventory zombieInventory;
    private Rectangle zombieCam;
    public static int zombieSpawnX = 600;
    public static int zombieSpawnY = 700;

    private int healthcounter = 0;

    private Random randint;
    private int moveCount = 0;
    private int direction;
    private int zombieX = 0;
    private int zombieY = 0;

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

    @Override
    public void move() {
        if(!checkEntityCollisions(xMove, 0f))
            moveX();
        if(!checkEntityCollisions(0f, yMove))
            moveY();
    }

    @Override
    public void moveX() {
        int tileX = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
        boolean condition = collisionWithTile(tileX, (int) (y + bounds.y) / Tile.TILEHEIGHT);
        boolean secondCondition = collisionWithTile(tileX, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT);
        
        if (xMove > 0) { // Moving right
            if (!condition && !secondCondition) {
                x += xMove;
            } else {
                zombieX = (int) ((x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH) - 1;
                zombieY = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT);
                //System.out.println("RIGHT Y " + zombieY + "RIGHT X" +  zombieX);
                System.out.println("PLAYER X" + handler.getWorld().getEntityManager().getPlayer().getX());
                System.out.println("PLAYER X MOVE" + handler.getWorld().getEntityManager().getPlayer().getxMove());
                //checkEmptyTrajectories(zombieX, zombieY); // TODO
            }

        } else if (xMove < 0) { // Moving left
            tileX = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;
            condition = collisionWithTile(tileX, (int) (y + bounds.y) / Tile.TILEHEIGHT);
            secondCondition = collisionWithTile(tileX, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT);
            
            if (!condition && !secondCondition) {
                x += xMove;
            } else {
                x = tileX * Tile.TILEWIDTH + Tile.TILEWIDTH - bounds.x;
                zombieX = (int) ((x + xMove + bounds.x) / Tile.TILEWIDTH) + 1;
                zombieY = (int) ((y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT);
                //System.out.println("LEFT Y " + zombieY + "LEFT X" +  zombieX);
                System.out.println("PLAYER X" + handler.getWorld().getEntityManager().getPlayer().getX());
                System.out.println("PLAYER X MOVE" + handler.getWorld().getEntityManager().getPlayer().getxMove());
                //checkEmptyTrajectories(zombieX, zombieY); // TODO
            }

        }
    }
    
    @Override
    public void moveY() {
        int tileY = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
        boolean condition = collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, tileY);
        boolean secondCondition = collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, tileY);
        
        if (yMove < 0) { //Up
            if (!condition && !secondCondition) {
                y += yMove;
            } else {
                y = tileY * Tile.TILEHEIGHT + Tile.TILEHEIGHT - bounds.y;
                zombieX = (int) ((x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH);
                zombieY = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT) + 1;
                //System.out.println("UP Y " + zombieY + "UP X" +  zombieX);
                System.out.println("PLAYER Y" + handler.getWorld().getEntityManager().getPlayer().getY());
                System.out.println("PLAYER Y MOVE" + handler.getWorld().getEntityManager().getPlayer().getyMove());
                //checkEmptyTrajectories(zombieX, zombieY); // TODO
            }

        } else if (yMove > 0) { //Down
            tileY = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;
            condition = collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, tileY);
            secondCondition = collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, tileY);
            
            if (!condition && !secondCondition) {
                y += yMove;
            } else {
                y = tileY * Tile.TILEHEIGHT - bounds.y - bounds.height - 1;
                zombieX = (int) ((x + xMove + bounds.x) / Tile.TILEWIDTH);
                zombieY = (int) ((y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT) - 1;
                //System.out.println("DOWN Y " + zombieY + "DOWN X" +  zombieX);
                System.out.println("PLAYER Y" + handler.getWorld().getEntityManager().getPlayer().getY());
                System.out.println("PLAYER Y MOVE" + handler.getWorld().getEntityManager().getPlayer().getyMove());
                //checkEmptyTrajectories(zombieX, zombieY); // TODO
            }

        }
    }
    
    /*
     * Checks for empty trajectories around the zombie if it's
     * colliding with a solid item
     */
    // Method to check for empty blocks around the zombie and if it's empty,
    // check which is closer to the current player coordinates and move there. Keep checking
    // until there is no solid tile around or until the player is out of reach
    private void checkEmptyTrajectories(int zombieX, int zombieY) {
        // if going down or right 
            // if player x - zombie x > 10
                // return
            // if player y - zombie y > 10
                // return
        // if going up or left
            // if zombie x - player x > 10
                // return
            // if zombie y - player y > 10
                // return
        
        // if blocks all around are empty
            // return
        
        // if this is dead
            // return

        //Array list of coords instead of tiles
        ArrayList<Point> tiles = new ArrayList<Point>();
        Point topLeft = new Point(zombieX - 1, zombieY - 1);
        Point topMid = new Point(zombieX, zombieY - 1);
        Point topRight = new Point(zombieX + 1, zombieY - 1);
        Point midLeft = new Point(zombieX - 1, zombieY);
        Point midRight = new Point(zombieX + 1, zombieY);
        Point bottomLeft = new Point(zombieX - 1, zombieY + 1);
        Point bottomMid = new Point(zombieX, zombieY + 1);
        Point bottomRight = new Point(zombieX + 1, zombieY + 1);
        tiles.add(topLeft); tiles.add(topMid); tiles.add(topRight);
        tiles.add(midLeft); tiles.add(midRight); tiles.add(bottomLeft);
        tiles.add(bottomMid); tiles.add(bottomRight);
        
        int count = 0;
        for (Point tileCoords : tiles) {
            int tileX = (int) tileCoords.getX(); 
            int tileY = (int) tileCoords.getY();
            
            if (!handler.getWorld().getTile(tileX, tileY).isSolid()) { tiles.remove(count); }
            else {
                float test = tileX - handler.getWorld().getEntityManager().getPlayer().getX();
            }
            count++;
        }
        
        // TODO Check for entities (trees, bushes)

        // get blocks all around
            // if instance of RockTile or Tree or rock, bush, etc
                // do nothing
            // else 
                // put in a list
                // for item in list
                    // minus player coords (which is closest to him or her)
                    // get minimum (which is closest to player)
                    // move there

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
        float cameraX = handler.getGameCamera().getxOffset();
        float cameraY = handler.getGameCamera().getyOffset();
        
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

        if (isBeinghurt() && healthcounter <= 120) {
            g.setColor(Color.WHITE);
            g.drawString("Zombie Health: " + getHealth(),
                    (int) (x-handler.getGameCamera().getxOffset()),
                    (int) (y-handler.getGameCamera().getyOffset()-20));
        }
    }

    /*
     * Drops a zombie brain when it dies
     */
    @Override
    public void die() {
        randint = new Random();
        //RNGR = randint.nextInt(3) + 1;
       // handler.getWorld().getItemManager().addItem(Item.boneItem.createNew((int)x + bounds.x,(int)y + bounds.y,RNGR));
    }
}
