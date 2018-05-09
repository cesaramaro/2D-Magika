package Game.Entities.Creatures;

import Game.Entities.EntityBase;
import Game.GameStates.State;
import Game.Inventories.Inventory;
import Game.Inventories.QuestItems;
import Game.Items.Item;
import Game.SpellCast.SpellCastUI;
import Resources.Animation;
import Resources.Images;
import Main.Handler;
import Quests.Quest1;
import Quests.Quest2;
import Quests.Quest3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import Display.DisplayScreen;

/**
 * Created by Elemental on 1/1/2017.
 */

public class Player extends CreatureBase {

    //Animations
    private Animation animDown, animUp, animLeft, animRight,animFireATT,animFireATTR,animFireATTU,animFireATTD;

    // Attack


    //Inventory
    private Inventory inventory;
    private Quest1 quest1;
    private Quest2 quest2;
    private Quest3 quest3;
    private QuestItems questItems;
    private SpellCastUI spellGUI;

    private int fcounter = 0;
    private Boolean fcactive=true;
    private Boolean FireBall=false;
    private Boolean LaunchedFireBall=false;
    private Boolean LaunchedFireBallL=false;
    private Boolean LaunchedFireBallR=false;
    private Boolean LaunchedFireBallU=false;
    private Boolean LaunchedFireBallD=false;
    private Boolean attacking=false;
    
    //Potion Variables
    protected boolean potionActive = false;
    protected int potionTime = 1200;
    protected int timePassed = -1;
    
    // Clock power up - Freezes boss for 10 seconds
    protected boolean clockPowerUpActive = false;
    
    private int animWalkingSpeed = 150;
    private int animFireSpeed = 250;
    private int FireSpeed = 2;
    private int FireMove = 0;
    private int movexp,moveyp,movexn,moveyn,tempmoveyp,tempmovexn,tempmoveyn,tempmovexp,fy,fx;

    //spells



    public Player(Handler handler, float x, float y) {
        super(handler, x, y, CreatureBase.DEFAULT_CREATURE_WIDTH, CreatureBase.DEFAULT_CREATURE_HEIGHT);

        bounds.x=8*2;
        bounds.y=18*2;
        bounds.width=16*2;
        bounds.height=14*2;
        health=75;
        attack=2;
        maxHealth = health;

        animDown = new Animation(animWalkingSpeed,Images.player_front);
        animLeft = new Animation(animWalkingSpeed,Images.player_left);
        animRight = new Animation(animWalkingSpeed,Images.player_right);
        animUp = new Animation(animWalkingSpeed,Images.player_back);
        animFireATT = new Animation(animFireSpeed,Images.FireBallLeft);
        animFireATTR = new Animation(animFireSpeed,Images.FireBallRight);
        animFireATTU = new Animation(animFireSpeed,Images.FireBallUp);
        animFireATTD = new Animation(animFireSpeed,Images.FireBallDown);

        inventory= new Inventory(handler);
        spellGUI= new SpellCastUI(handler);
        quest1 = new Quest1(handler);
        quest2 = new Quest2(handler);
        quest3 = new Quest3(handler);
        questItems = new QuestItems(handler);
    }

    @Override
    public void tick() {
    	if (potionActive) {
        	timePassed ++ ;
        	if(timePassed<=potionTime) {
        		attack = 4;
        	}else {
        		potionActive = !potionActive;
        		timePassed = -1;
        	}
        	
        }else {
        	attack = 2;
        }
        //Animations
        animDown.tick();
        animUp.tick();
        animRight.tick();
        animLeft.tick();
        animFireATT.tick();
        animFireATTR.tick();
        animFireATTU.tick();
        animFireATTD.tick();



        //Movement
        getInput();
        move();
        handler.getGameCamera().centerOnEntity(this);

        if(!fcactive){
            fcounter++;
        }
        if(fcounter>=60){
            fcounter=0;
            fcactive=true;
            FireBall=true;

        }

        if(FireBall){
            FireMove++;
        }


        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)){
            readyFireAttack();
        }

        // Attack
        if(handler.getKeyManager().attbut) {
            checkAttacks();
        }else if(handler.getKeyManager().fattbut){

            fireAttack();


        }


        //Inventory
        inventory.tick();
        
        //Quest Inventory
        questItems.tick();
        
        //Quests
        quest1.tick();
        quest2.tick();
        quest3.tick();
        
        //spellgui
        spellGUI.tick();
        
        if(handler.getKeyManager().lifebut && health < maxHealth) {
            this.health ++;
        }
        
    }
    
    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentAnimationFrame(animDown,animUp,animLeft,animRight,Images.player_front,Images.player_back,Images.player_left,Images.player_right), (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null);

        if(FireBall){
            FireBallAttack(g);

        }




        g.setColor(Color.BLACK);
        g.drawRect((int)(x-handler.getGameCamera().getxOffset()-1),(int)(y-handler.getGameCamera().getyOffset()-21),76,11);
        if(this.getHealth()>50){
            g.setColor(Color.GREEN);
            g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),getHealth(),10);

        }else if(this.getHealth()>=15 && getHealth()<=50){
            g.setColor(Color.YELLOW);
            g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),getHealth(),10);

        }else if(this.getHealth() < 15){
            g.setColor(Color.RED);
            g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),getHealth(),10);

        }
        g.setColor(Color.white);
        g.drawString("Health: " + getHealth(),(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-10));



    }

    public void readyFireAttack(){
        LaunchedFireBall=true;
        movexp =(int) (x - handler.getGameCamera().getxOffset()) + 48;
        moveyp =(int) (y - handler.getGameCamera().getyOffset()) + 64;
        movexn =(int) (x - handler.getGameCamera().getxOffset()) - 48;
        moveyn =(int) (y - handler.getGameCamera().getyOffset()) - 64;
        tempmovexp =(int) (x - handler.getGameCamera().getxOffset()) + 48;
        tempmoveyp =(int) (y - handler.getGameCamera().getyOffset()) + 64;
        tempmovexn =(int) (x - handler.getGameCamera().getxOffset()) - 48;
        tempmoveyn =(int) (y - handler.getGameCamera().getyOffset()) - 64;
        LaunchedFireBallL=false;
        LaunchedFireBallR=false;
        LaunchedFireBallU=false;
        LaunchedFireBallD=false;
        fy=(int) (y - handler.getGameCamera().getyOffset()) + (height / 2);
        fx=(int) (x - handler.getGameCamera().getxOffset()) + 16;
    }
    public void fireAttack() {

        for (Item i : getInventory().getInventoryItems()) {
            if (i.getName() == "Fire Rune"&&fcactive) {
                attacking=true;
                System.out.println("Burn");
                i.setCount(i.getCount() - 1);
                fcactive=false;
                return;

            }
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
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

        if(lookingUp && handler.getKeyManager().attbut){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y - arSize;
        }else if(lookingDown && handler.getKeyManager().attbut){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y + cb.height;
        }else if(lookingLeft && handler.getKeyManager().attbut){
            ar.x = cb.x - arSize;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else if(lookingRight && handler.getKeyManager().attbut){
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

    @Override
    public void die() {
        State.setState(handler.getGame().menuState);
        System.out.println("You lose");
        DisplayScreen display = handler.getGame().getDisplay();
        display.showWindow("You died! Better luck next time");
    }
    
    private void getInput() {
        xMove = 0;
        yMove = 0;

        if(handler.getKeyManager().up &&! attacking)
            yMove = -speed;
        if(handler.getKeyManager().down&&! attacking)
            yMove = speed;
        if(handler.getKeyManager().left&&! attacking)
            xMove = -speed;
        if(handler.getKeyManager().right&&! attacking)
            xMove = speed;
    }


    private void FireBallAttack(Graphics g) {


        if (lookingRight&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=false;
            LaunchedFireBallR=true;
            LaunchedFireBallU=false;
            LaunchedFireBallD=false;

        } else if (lookingDown&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=false;
            LaunchedFireBallR=false;
            LaunchedFireBallU=false;
            LaunchedFireBallD=true;

        } else if (lookingUp&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=false;
            LaunchedFireBallR=false;
            LaunchedFireBallU=true;
            LaunchedFireBallD=false;

        } else if(lookingLeft&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=true;
            LaunchedFireBallR=false;
            LaunchedFireBallU=false;
            LaunchedFireBallD=false;

        }
        if (LaunchedFireBallR) {
            movexp+=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), movexp, fy, 64, 32, null);
            if(movexp >= tempmovexp + 64*2){
                FireBall=false;
                attacking=false;
            }
        } else if (LaunchedFireBallD) {
            moveyp+=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), fx-6, moveyp, 32, 64, null);
            if(moveyp >= tempmoveyp + 64*2){
                FireBall=false;
                attacking=false;
            }
        } else if (LaunchedFireBallU) {
            moveyn-=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), fx, moveyn, 32, 64, null);
            if(moveyn <= tempmoveyn - 64*2){
                FireBall=false;
                attacking=false;
            }
        } else if(LaunchedFireBallL) {   //ll
            movexn-=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), movexn, fy, 64, 32, null);
            if(movexn <= tempmovexn - 64*2){
                FireBall=false;
                attacking=false;
            }
        }





    }

    private BufferedImage getCurrentFireAnimationFrame(){

        if(LaunchedFireBallR){

            return animFireATTR.getCurrentFrame();

        }else if(LaunchedFireBallD){

            return animFireATTD.getCurrentFrame();

        }else if(LaunchedFireBallU){

            return animFireATTU.getCurrentFrame();

        }else{  

            return animFireATT.getCurrentFrame();
        }


    }

    public Inventory getInventory() {
        return inventory;
    }
    public Quest1 getQuest1() {
        return quest1;
    }
    public Quest2 getQuest2() {
        return quest2;
    }
    public Quest3 getQuest3() {
        return quest3;
    }
    public QuestItems getQuestItems() {
        return questItems;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public SpellCastUI getSpellGUI() {
        return spellGUI;
    }
    
    public void setPotionActive(boolean status) {
    	this.potionActive = status;
    }
    
    public void setClockPowerUpActive(boolean status) {
        this.clockPowerUpActive = status;
    }
    
    public boolean isClockPowerUpActive() {
        return clockPowerUpActive;
    }
}