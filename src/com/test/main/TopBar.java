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
    public Integer round = 1;

    public enum ACTION {
        NONE,
        MOVE,
        FIGHT
    }

    private ACTION currentAction = ACTION.NONE;

    public TopBar(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
        this.fight = false;
        this.move = false;
    }

    public void setCurrentAction(ACTION action) {
        currentAction = action;
    }

    public ACTION getCurrentAction() {
        return currentAction;
    }

    @Override
    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        //fight button
        if(mouseOver(mx, my, 10, 8, 100, 50)){
            if (this.currentAction == ACTION.FIGHT) {
                this.currentAction = ACTION.NONE;
            } else {
                this.currentAction = ACTION.FIGHT;
            }
            game.makeSelectable(2,false);
        }

        //move button
        if(mouseOver(mx, my, 120, 8, 100, 50)){
            if (this.currentAction == ACTION.MOVE) {
                this.currentAction = ACTION.NONE;
            } else {
                this.currentAction = ACTION.MOVE;
            }
//            game.makeSelectable(2);
        }
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if(game.gameState == Game.STATE.Game) {
            if (mx > x && mx < x + width) {
                return my > y && my < y + height;
            }
        }
        return false;
    }

    public void render(Graphics g) {
        Font fnt = new Font("arial", Font.BOLD, 30);
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
        if (currentAction.equals(ACTION.FIGHT)) {
            g.fillRect(10,8,100,50);
            g.setColor(Color.WHITE);
            g.drawString("Fight",24,45);
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Fight",24,45);
        }

        //Move button
        if (currentAction.equals(ACTION.MOVE)) {
            g.fillRect(120,8,100,50);
            g.setColor(Color.WHITE);
            g.drawString("Move",134,45);
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Move",134,45);
        }

        //Round counter
        g.setColor(Color.BLACK);
        g.drawString("Round: " + round.toString(),800,45);
    }
}
