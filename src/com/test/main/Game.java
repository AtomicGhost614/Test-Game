package com.test.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;


public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 960, HEIGHT = WIDTH / 12 * 9;
    public static final int SPAWN_WIDTH = WIDTH - 42, SPAWN_HEIGHT = HEIGHT - 65;
    private Thread thread;
    private boolean running = false;
    private final Random r;
    //private HUD hud;
    //private Spawn spawn;
    private final Menu menu;
    private final Player player;
    private final BasicEnemy enemy;
    private final Handler handler;

    private final int xaxis = WIDTH/Tiles.size;
    private final int yaxis = (HEIGHT/Tiles.size);
    private final Tiles[][] grid = new Tiles[xaxis][yaxis];
    public final TopBar topBar;

    public enum STATE {
        Menu,
        Help,
        Game
    }

    public STATE gameState = STATE.Menu;

    public Game(){
        handler = new Handler();
        menu = new Menu(this, handler);
        player = new Player(ID.Player, handler);
        enemy = new BasicEnemy(ID.Enemy, handler);
        topBar = new TopBar(this,handler);
        //this.addKeyListener(new KeyInput(handler));
        this.addMouseListener(menu);
        new Window(WIDTH, HEIGHT, "Game", this);
        //hud = new HUD();
        //spawn = new Spawn(handler, hud);
        r = new Random();
        for(int i = 0; i < xaxis; i++){
            for(int j = 0; j < yaxis; j++){
                grid[i][j] = new Tiles(this,handler,(i * Tiles.size),(j * Tiles.size)+14,i,j);
                if (j == 2) {
                    if (i == 2) {
                        grid[i][j].setTempObject(player);
                        //player.setCurrentTile(grid[i][j]);
                    } else if (i == 5) {
                        grid[i][j].setTempObject(enemy);
                        //enemy.setCurrentTile(grid[i][j]);
                    }
                }
                this.addMouseMotionListener(grid[i][j]);
                this.addMouseListener(grid[i][j]);
            }
        }
//        topBar = new TopBar(this,handler);
        this.addMouseListener(topBar);
        //if(gameState == STATE.Game) {
//        player.setX(grid[2][2].tx + 31);
//        player.setY(grid[2][2].ty + 31);
        handler.addObject(player);
        handler.addObject(enemy);
        //}
    }

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running){
                //makeSelectable();
                render();
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
//                System.out.println(Tiles.size);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();
        if(gameState == STATE.Game){
            //hud.tick();
            //spawn.tick();
            for(int i = 0; i < xaxis; i++){
                for(int j = 0; j < yaxis; j++){
                    grid[i][j].tick();
                }
            }
        }
        else if(gameState == STATE.Menu){
            menu.tick();
        }
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //handler.render(g);

        if(gameState == STATE.Game) {
            //hud.render(g);
            grid[0][0].render(g);
            for(int i = 0; i < xaxis; i++){
                for(int j = 0; j < yaxis; j++){
                    grid[i][j].render(g);
                }
            }
            topBar.render(g);
            handler.render(g);

        }
        else if(gameState == STATE.Menu || gameState == STATE.Help){
            menu.render(g);
        }

        //handler.render(g);

        g.dispose();
        bs.show();
    }

    public void makeSelectable() {
        Tiles centerTile = player.getCurrentTile();

        int i = centerTile.xUnit;
        int j = centerTile.yUnit;

        int tx = grid[i][j].tx;
        int ty = grid[i][j].ty;

        int xmax = tx + Tiles.size;
        int ymax = ty + Tiles.size;

        if(player.getX() > tx && player.getX() < xmax && player.getY() > ty && player.getY() < ymax) {
            for(int k = 1; k < 3; k++) {
                grid[i][j].setSelectable(player.isSelected());
                if(i-k >= 0) {
                    if(!grid[i-k][j].contains.equals(Tiles.OBJECT.Enemy)){
                        grid[i-k][j].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i-k][j].setSelectable(false);
                    }
                }
                if(j-k >= 1) {
                    if(!grid[i][j-k].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i][j - k].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i][j - k].setSelectable(false);
                    }
                }
                if(i-(k/2) >= 0 && j-(k/2) >= 1) {
                    if(!grid[i-(k/2)][j-(k/2)].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i-(k/2)][j-(k/2)].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i-(k/2)][j-(k/2)].setSelectable(false);
                    }
                }
                if(i+k < xaxis) {
                    if(!grid[i+k][j].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i+k][j].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i+k][j].setSelectable(false);
                    }
                }
                if(j+k < yaxis) {
                    if(!grid[i][j+k].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i][j+k].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i][j+k].setSelectable(false);
                    }
                }
                if(i+(k/2) < xaxis && j+(k/2) < yaxis) {
                    if(!grid[i+(k/2)][j+(k/2)].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i+(k/2)][j+(k/2)].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i+(k/2)][j+(k/2)].setSelectable(false);
                    }
                }
                if(j-(k/2) >= 1 && i+(k/2) < xaxis) {
                    if(!grid[i + (k / 2)][j - (k / 2)].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i + (k / 2)][j - (k / 2)].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i + (k / 2)][j - (k / 2)].setSelectable(false);
                    }
                }
                if(i-(k/2) >= 0 && j+(k/2) < yaxis) {
                    if(!grid[i - (k / 2)][j + (k / 2)].contains.equals(Tiles.OBJECT.Enemy)) {
                        grid[i - (k / 2)][j + (k / 2)].setSelectable(true);
                    }
                    if (!player.isSelected()){
                        grid[i - (k / 2)][j + (k / 2)].setSelectable(false);
                    }
                }
            }
        }
    }

    public void movePlayer(Tiles tile) {
        topBar.setCurrentAction(TopBar.ACTION.NONE);
        Tiles prevTile = player.getCurrentTile();
        player.setSelected(false);
        makeSelectable();
        prevTile.setTempObject(null);
        tile.setTempObject(player);

        Tiles enemyTile = enemy.getCurrentTile();

        int i = enemyTile.xUnit;
        int j = enemyTile.yUnit;

//        int tx = grid[i][j].tx;
//        int ty = grid[i][j].ty;

        int eX = r.nextInt(3);
        if (eX == 2) {
            eX = -1;
        }

        int eY = r.nextInt(3);
        if (eY == 2) {
            eY = -1;
        }

        if ((i+eX) >= 0 && (i+eX) < xaxis && (j+eY) >= 1 && (j+eY) < yaxis) {
            if (grid[i+eX][j+eY].getTempObject() == null) {
                enemyTile.setTempObject(null);
                grid[i+eX][j+eY].setTempObject(enemy);
            }
        } else if ((i+eX) >= 0 && (i+eX) < xaxis) {
            eY = eY * (-1);
            if (grid[i+eX][j+eY].getTempObject() == null) {
                enemyTile.setTempObject(null);
                grid[i+eX][j+eY].setTempObject(enemy);
            }
        } else if ((j+eY) >= 1 && (j+eY) < yaxis) {
            eX = eX * (-1);
            if (grid[i+eX][j+eY].getTempObject() == null) {
                enemyTile.setTempObject(null);
                grid[i+eX][j+eY].setTempObject(enemy);
            }
        }
    }

//    public static float clamp(float var, int min, int max){
//        if(var >= max){
//            return  var =  max;
//        }
//        else if(var <= min){
//            return var = min;
//        }
//        else{
//            return var;
//        }
//    }

    public static void main(String[] args){
        new Game();
    }
}
