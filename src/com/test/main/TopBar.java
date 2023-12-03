package com.test.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.test.main.Game.HEIGHT;

public class TopBar extends MouseAdapter {

    private final Game game;
    private final Handler handler;

    private boolean fight;
    private boolean move;

    public enum ACTION {
        NONE,
        MOVE,
        FIGHT
    }

    public ACTION currentAction = ACTION.NONE;

    public TopBar(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
        this.fight = false;
        this.move = false;
    }

    @Override
    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        //fight button
        if(mouseOver(mx, my, 10, 8, 100, 50)){
            this.fight = !this.fight;
            this.move = false;
            if (this.currentAction == ACTION.FIGHT) {
                this.currentAction = ACTION.NONE;
            } else {
                this.currentAction = ACTION.FIGHT;
            }
            //System.out.println("Click");
            //handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            //handler.addObject(new BasicEnemy(r.nextInt(Game.SPAWN_WIDTH), r.nextInt(Game.SPAWN_HEIGHT), ID.BasicEnemy, handler));
        }

        //move button
        if(mouseOver(mx, my, 120, 8, 100, 50)){
            this.move = !this.move;
            this.fight = false;
            game.makeSelectable();
            if (this.currentAction == ACTION.MOVE) {
                this.currentAction = ACTION.NONE;
            } else {
                this.currentAction = ACTION.MOVE;
            }
            //System.out.println("Click");
            //handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            //handler.addObject(new BasicEnemy(r.nextInt(Game.SPAWN_WIDTH), r.nextInt(Game.SPAWN_HEIGHT), ID.BasicEnemy, handler));
        }

//        if(mouseOver(mx, my, 0, 0, 942, 5)){
//            this.fight = true;
//            System.out.println("Click");
//            //handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
//            //handler.addObject(new BasicEnemy(r.nextInt(Game.SPAWN_WIDTH), r.nextInt(Game.SPAWN_HEIGHT), ID.BasicEnemy, handler));
//        }
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if(game.gameState == Game.STATE.Game) {
            if (mx > x && mx < x + width) {
                if (my > y && my < y + height) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public void render(Graphics g) {
        Font fnt = new Font("arial", 1, 30);
        g.setFont(fnt);
        g.setColor(Color.WHITE);
        g.drawRect(941,0,1,672);
        //g.drawRect(0,0,1,672);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,942,108);

        //Button outlines
        g.setColor(Color.BLACK);
        g.drawRect(10,8,100,50);
        g.drawRect(120,8,100,50);

        //Fight button
        if (this.fight) {
            g.fillRect(10,8,100,50);
            g.setColor(Color.WHITE);
            g.drawString("Fight",24,45);
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Fight",24,45);
        }

        //Move button
        if (this.move) {
            g.fillRect(120,8,100,50);
            g.setColor(Color.WHITE);
            g.drawString("Move",134,45);
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Move",134,45);
        }
    }
}
