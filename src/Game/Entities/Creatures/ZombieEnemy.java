package Game.Entities.Creatures;

import Game.Entities.EntityBase;
import Game.Inventories.Inventory;
import Game.Tiles.Tile;
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
    public static int zombieSpawnX = 600;
    public static int zombieSpawnY = 700;
    Point previousBlock = new Point();
    private boolean avoidingWall = false;
    private boolean noWallsAround = true;

    private int healthcounter = 0;

    private Random randint;
    private int moveCount = 0;
    private int direction;
    private int zombieX;
    private int zombieY;
    private int recursiveCount = 0;

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
        
        if (!avoidingWall) {
            move();
            checkIfMove();
        }
        
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
                x = tileX * Tile.TILEWIDTH - bounds.x - bounds.width - 1;
                zombieX = (int) ((x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH) - 1;
                zombieY = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT);
                avoidingWall = true;
                checkEmptyTrajectories(zombieX, zombieY);
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
                avoidingWall = true;
                checkEmptyTrajectories(zombieX, zombieY);
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
                avoidingWall = true;
                checkEmptyTrajectories(zombieX, zombieY);
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
                avoidingWall = true;
                checkEmptyTrajectories(zombieX, zombieY);
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
        // Temporary so it doesn't throw a StackOverflow error
        if (recursiveCount > 1000) {
            avoidingWall = false;
            noWallsAround = true;
            return;
        }
        int playerX = handler.getWorld().getEntityManager().getPlayer().getLocation().x;
        int playerY = handler.getWorld().getEntityManager().getPlayer().getLocation().y;
        
        avoidingWall = true;
        noWallsAround = true;
        double zombiePlayerDistance;
        
        // Distance formula => d = √((x2 -x1)^2 + (y2 - y1)^2)
        double zombiePlayerDistanceP1 = Math.pow((playerX - zombieX), 2);
        double zombiePlayerDistanceP2 = Math.pow((playerY - zombieY), 2);
        zombiePlayerDistance = Math.sqrt(zombiePlayerDistanceP1 + zombiePlayerDistanceP2);
        
        // Exit conditions
        if ((this.getHealth() < 0) || (zombiePlayerDistance > 5))  {
            avoidingWall = false;
            return;
        }
        
        // Array of nearby tiles (x, y)
        Point up = new Point(zombieX, zombieY - 1);
        Point down = new Point(zombieX, zombieY + 1);
        Point left = new Point(zombieX - 1, zombieY);
        Point right = new Point(zombieX + 1, zombieY);
        Point[] tiles = { up, down, left, right };
        
        double minDistance = 0;
        Point closestBlock = new Point();
        
        for (int i = 0; i < tiles.length; i++) {
            int tileX = (int) tiles[i].getX(); 
            int tileY = (int) tiles[i].getY();

            if (handler.getWorld().getTile(tileX, tileY).isSolid()) { noWallsAround = false; }
            else {
                // If it's the last iteration and no solid blocks were found
                // it changes noWallsAround to true and exits the method
                if ((i == tiles.length - 1) && noWallsAround) { 
                    avoidingWall = false;
                    return;
                }
                
                if (closestBlock.x == tileX && closestBlock.y == tileY) { /* Do nothing*/ }
                else {
                    // Distance formula => d = √((x2 -x1)^2 + (y2 - y1)^2)
                    double xDistance = Math.pow((playerX - tileX), 2);
                    double yDistance = Math.pow((playerY - tileY), 2);
                    double distance = Math.sqrt(xDistance + yDistance);

                    if (i == 0 || distance < minDistance) {
                        minDistance = distance;
                        closestBlock = tiles[i];
                    }
                }
            }
        }
        
        // Exit condition
        if (noWallsAround) {
            avoidingWall = false;
            return;
        }
        
        previousBlock = closestBlock;
        float xToMove = ((closestBlock.x - zombieX) * speed);
        float yToMove = ((closestBlock.y - zombieY) * speed);
        
        this.setxMove(xToMove); this.setyMove(yToMove);
        x += xMove; y += yMove; // checkIfMove();

        System.out.println("Closest empty tile is " + closestBlock);
        recursiveCount++;
        checkEmptyTrajectories(closestBlock.x, closestBlock.y);

        // TODO Check for the corners to see which is the better option
        // if they have the same distance
        // TODO Check for static entities and not just "walls" (rock tiles)
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
       handler.getWorld().getItemManager().addItem(Item.brainItem.createNew((int)x + bounds.x,(int)y + bounds.y,1));
    }
}
