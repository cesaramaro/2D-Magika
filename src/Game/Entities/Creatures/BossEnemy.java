package Game.Entities.Creatures;

import Game.Entities.EntityBase;
import Game.GameStates.State;
import Game.Inventories.Inventory;
import Game.Tiles.Tile;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.util.Random;

import Display.DisplayScreen;

public class BossEnemy extends CreatureBase  {

    private Animation animDown, animUp, animLeft, animRight;

    private Inventory bossInventory;
    private Rectangle bossCam;
    private int animWalkingSpeed = 150;

    private int healthcounter = 0;

    private Random randint;
    private int moveCount = 0;
    private int direction;
    private boolean teleporting = false;

    public BossEnemy(Handler handler, float x, float y) {
        super(handler, x, y, CreatureBase.DEFAULT_CREATURE_WIDTH, CreatureBase.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 8 * 2;
        bounds.y = 18 * 2;
        bounds.width = 16 * 2;
        bounds.height = 14 * 2;
        speed = 2f;
        health = 75;

        bossCam = new Rectangle();

        randint = new Random();
        direction = randint.nextInt(4) + 1;

        animDown = new Animation(animWalkingSpeed, Images.bossEnemy_front);
        animLeft = new Animation(animWalkingSpeed, Images.bossEnemy_left);
        animRight = new Animation(animWalkingSpeed, Images.bossEnemy_right);
        animUp = new Animation(animWalkingSpeed, Images.bossEnemy_back);

        bossInventory = new Inventory(handler);
    }

    @Override
    public void tick() {
        if (!teleporting) {
            animDown.tick();
            animUp.tick();
            animRight.tick();
            animLeft.tick();
            move();
            checkIfMove();
        }

        moveCount++;
        if (moveCount >= 60) {
            moveCount = 0;
            direction = randint.nextInt(4) + 1;
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
        bossInventory.tick();
    }

    /*
     * Moves the boss if it's not colliding with an entity
     */
    @Override
    public void move() {
        if(!checkEntityCollisions(xMove, 0f))
            moveX();
        if(!checkEntityCollisions(0f, yMove))
            moveY();
    }

    /*
     * Moves the boss either left or right
     */
    @Override
    public void moveX() {
        int tileX = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
        boolean condition = collisionWithTile(tileX, (int) (y + bounds.y) / Tile.TILEHEIGHT);
        boolean secondCondition = collisionWithTile(tileX, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT);

        if (xMove > 0) { // Moving right
            if (!condition && !secondCondition) {
                x += xMove;
            } else {
                x = (tileX * Tile.TILEWIDTH - bounds.x - bounds.width);
                this.teleportNearPlayer("right");
            }

        } else if (xMove < 0) { // Moving left
            tileX = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;
            condition = collisionWithTile(tileX, (int) (y + bounds.y) / Tile.TILEHEIGHT);
            secondCondition = collisionWithTile(tileX, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT);

            if (!condition && !secondCondition) {
                x += xMove;
            } else {
                x = (tileX * Tile.TILEWIDTH + Tile.TILEWIDTH - bounds.x);
                this.teleportNearPlayer("left");
            }
        }
    }

    /*
     * Moves the boss either up or down
     */
    @Override
    public void moveY() {
        int tileY = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
        boolean condition = collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, tileY);
        boolean secondCondition = collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, tileY);

        if (yMove < 0) { //Up
            if (!condition && !secondCondition) {
                y += yMove;
            } else {
                y = (tileY * Tile.TILEHEIGHT + Tile.TILEHEIGHT - bounds.y);
                this.teleportNearPlayer("up");
            }

        } else if (yMove > 0) { //Down
            tileY = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;
            condition = collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, tileY);
            secondCondition = collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, tileY);

            if (!condition && !secondCondition) {
                y += yMove;
            } else {
                y = (tileY * Tile.TILEHEIGHT - bounds.y - bounds.height);
                this.teleportNearPlayer("down");
            }
        }
    }

    /*
     * Teleports the boss near the player
     * @param String - direction in which the boss was looking before teleporting
     * "up", "down", "left" or "right"
     */
    private void teleportNearPlayer(String direction) {
        teleporting = true;
        float teleportToX = handler.getWorld().getEntityManager().getPlayer().getX();
        float teleportToY = handler.getWorld().getEntityManager().getPlayer().getY();

        if (direction == "down") {
            teleportToY += 50;
        } else if (direction == "up") {
            teleportToY += -50;
        } else if (direction == "right") {
            teleportToX += 50;
        } else if (direction == "left") {
            teleportToX += -50;
        }

        teleportToX += -50;
        teleportToY += -50;
        if (teleportToX < 0 || teleportToY < 0) {
            // Don't teleport
        } else {
            x = teleportToX;
            y = teleportToY;
            teleporting = false;
        }
    }

    private void checkIfMove() {
        xMove = 0;
        yMove = 0;

        bossCam.x = (int) (x - handler.getGameCamera().getxOffset() - (64 * 3));
        bossCam.y = (int) (y - handler.getGameCamera().getyOffset() - (64 * 3));
        bossCam.width = 64 * 7;
        bossCam.height = 64 * 7;

        double playerWidth = handler.getWorld().getEntityManager().getPlayer().getWidth();
        double playerHeight = handler.getWorld().getEntityManager().getPlayer().getHeight();
        double playerX = handler.getWorld().getEntityManager().getPlayer().getX();
        double playerY = handler.getWorld().getEntityManager().getPlayer().getY();
        float cameraX = handler.getGameCamera().getxOffset();
        float cameraY = handler.getGameCamera().getyOffset();

        if (bossCam.contains(playerX - cameraX, playerY - cameraY) 
                || bossCam.contains(playerX - cameraX + playerWidth, playerY - cameraY + playerHeight)) {

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

            // Don't move if the boss is already near the player (i.e attacking them)
            // Otherwise, move either to the left or the right
            if ((x >= playerX - 8) && (x <= playerX + 8)) xMove = 0; // Don't move
            else xMove = (x < playerX ? speed : -speed);

            if ((y >= playerY - 8) && (y <= playerY + 8)) yMove = 0; // Don't move
            else yMove = (y < playerY ? speed : -speed);

        } else {
            // Move the boss in random directions 
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
     * Renders the boss' graphics and health bar
     * @param Graphics g
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentAnimationFrame(animDown, animUp, animLeft, animRight, 
                Images.bossEnemy_front, Images.bossEnemy_back,  
                Images.bossEnemy_left, Images.bossEnemy_right), 
                (int) (x - handler.getGameCamera().getxOffset()), 
                (int) (y - handler.getGameCamera().getyOffset()), 
                width, height, null);
        double xOffSet = handler.getGameCamera().getxOffset();
        double yOffSet = handler.getGameCamera().getyOffset();

        g.setColor(Color.BLACK);
        g.drawRect((int) (x - xOffSet - 1), (int) (y - yOffSet - 21), 76, 11);

        if (this.getHealth() > 50) {
            g.setColor(Color.GREEN);
        } else if (this.getHealth() >= 15 && getHealth() <= 50) {
            g.setColor(Color.YELLOW);
        } else if (this.getHealth() < 15) {
            g.setColor(Color.RED);
        }
        g.fillRect((int) (x - xOffSet), (int) (y - yOffSet - 20), getHealth(), 10);
        g.setColor(Color.white);
        g.drawString("Health: " + getHealth(), (int) (x - xOffSet), (int) (y - yOffSet - 10));
    }

    /*
     * Wins the game when the boss is defeated
     */
    @Override
    public void die() {
        State.setState(handler.getGame().menuState);
        DisplayScreen display = handler.getGame().getDisplay();
        display.showWindow("You won! Congratulations!");
    }
}
