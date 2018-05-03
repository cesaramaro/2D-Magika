package Worlds;

import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Creatures.ZombieEnemy;
import Game.Entities.Statics.*;
import Main.Handler;

public class DarkWorld extends BaseWorld    {
    
    private Handler handler;
    private Player player;
    
    public DarkWorld(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;

        entityManager.addEntity(new Rock(handler, 100, 250, "Rock"));
        entityManager.addEntity(new Tree(handler, 100, 450, "Tree"));
        entityManager.addEntity(new Rock(handler, 528, 276, "Rock"));
        entityManager.addEntity(new Tree(handler, 684, 1370, "Tree"));
        entityManager.addEntity(new Rock(handler, 765, 888, "Rock"));
        entityManager.addEntity(new Tree(handler, 88, 1345, "Tree"));
        entityManager.addEntity(new Rock(handler, 77, 700, "Rock"));
        entityManager.addEntity(new Tree(handler, 700, 83, "Tree"));
        entityManager.addEntity(new ZombieEnemy(handler, 300, 500));
        entityManager.addEntity(new SkelyEnemy(handler, 400, 300));

        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }

}