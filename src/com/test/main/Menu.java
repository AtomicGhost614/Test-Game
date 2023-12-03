package com.test.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import static com.test.main.Game.HEIGHT;

public class Menu extends MouseAdapter {

    private Game game;
    private Handler handler;
    private Random r = new Random();

    public Menu(Game game, Handler handler){
        this.game = game;
        this.handler = handler;
    }

    @Override
    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        //play button
        if(mouseOver(mx, my, Game.WIDTH/2 - 210, HEIGHT/2 - 150, 400, 128)){
            game.gameState = Game.STATE.Game;
            //handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            //handler.addObject(new BasicEnemy(r.nextInt(Game.SPAWN_WIDTH), r.nextInt(Game.SPAWN_HEIGHT), ID.BasicEnemy, handler));
        }

        //quit button
        if(mouseOver(mx, my, Game.WIDTH/2 - 210, HEIGHT/2 + 150, 400, 128)) {
            System.exit(1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if(game.gameState == Game.STATE.Menu) {
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

    public void tick(){

    }

    public void render(Graphics g){
        if(game.gameState == Game.STATE.Menu){
            Font fnt = new Font("arial", 1, 75);
            Font fnt2 = new Font("arial", 1, 60);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Menu", Game.WIDTH/2 - 115, 100);
            g.setFont(fnt2);
            g.setColor(Color.WHITE);
            g.drawString("Play", Game.WIDTH/2 - 75, HEIGHT/2 - 65);
            g.drawRect(Game.WIDTH/2 - 210, HEIGHT/2 - 150, 400, 128);
            g.setColor(Color.WHITE);
            g.drawString("Help", Game.WIDTH/2 - 75, HEIGHT/2 + 85);
            g.drawRect(Game.WIDTH/2 - 210, HEIGHT/2, 400, 128);
            g.setColor(Color.WHITE);
            g.drawString("Quit", Game.WIDTH/2 - 75, HEIGHT/2 + 235);
            g.drawRect(Game.WIDTH/2 - 210, HEIGHT/2 + 150, 400, 128);
        }
        else if(game.gameState == Game.STATE.Help){
            Font fnt = new Font("arial", 1, 75);
            Font fnt2 = new Font("arial", 1, 60);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
        }
    }
}

