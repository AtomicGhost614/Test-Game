package com.test.main;

import com.test.main.entities.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


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
    private final int yaxis = HEIGHT/Tiles.size - 1;
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
                grid[i][j] = new Tiles(this,handler,(i * Tiles.size),(j * Tiles.size)+108,i,j);
                this.addMouseMotionListener(grid[i][j]);
                this.addMouseListener(grid[i][j]);
            }
        }
        int playerRandX = r.nextInt(xaxis-1);
        int playerRandY = r.nextInt(yaxis-1);
        grid[playerRandX][playerRandY].setTempObject(player);
        int enemyRandX = r.nextInt(xaxis-1);
        int enemyRandY = r.nextInt(yaxis-1);
        Tiles enemyTile = grid[enemyRandX][enemyRandY];
        while (!enemyTile.contains.equals(Tiles.OBJECT.Enemy)) {
            if (enemyTile.contains.equals(Tiles.OBJECT.Player)) {
                enemyRandX = r.nextInt(xaxis-1);
                enemyRandY = r.nextInt(yaxis-1);
                enemyTile = grid[enemyRandX][enemyRandY];
            } else {
                enemyTile.setTempObject(enemy);
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
        if (!handler.enemiesExist) {
            gameState = STATE.Menu;
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

    public void makeSelectable(int range, boolean selectable) {
        Tiles centerTile = player.getCurrentTile();

        int i = centerTile.xUnit;
        int j = centerTile.yUnit;

        Set<Tiles> tilesSet = getTileList(range, i, j);
        tilesSet.add(centerTile);

        tilesSet.forEach(tile -> tile.setSelectable(selectable));
    }

    public Set<Tiles> getTileList(int range, int centerX, int centerY) {
        Set<Tiles> tileList = new HashSet<>();
        for (int main = 1; main <= range; main++) {
            for (int offset = 0; offset < main; offset++) {
                tileList.add(getTile(centerX,centerY,(main-offset),offset));
                tileList.add(getTile(centerX,centerY,offset,-(main-offset)));
                tileList.add(getTile(centerX,centerY,-(main-offset),-offset));
                tileList.add(getTile(centerX,centerY,-offset,(main-offset)));
            }
        }
        return tileList;
    }

    public Tiles getTile(int centerX, int centerY, int xMove, int yMove) {
        int newX = centerX + xMove;
        int newY = centerY + yMove;
        try {
            return grid[newX][newY];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return grid[centerX][centerY];
        }
    }

    public void movePlayer(Tiles tile) {
        topBar.setCurrentAction(TopBar.ACTION.NONE);
        Tiles prevTile = player.getCurrentTile();
        player.setSelected(false);
        makeSelectable(player.getMoveRange(), false);
        prevTile.setTempObject(null);
        tile.setTempObject(player);

        moveEnemies();
        spawnNewEnemy(5);
    }

    public void moveEnemies() {
        for (GameObject enemy : handler.object) {
            if (enemy.getId().equals(ID.Enemy)) {
                Tiles enemyTile = enemy.getCurrentTile();

                int i = enemyTile.xUnit;
                int j = enemyTile.yUnit;

                Set<Tiles> tilesSet = getTileList(enemy.getMoveRange(), i, j);

                int randomTile = r.nextInt(tilesSet.size());

                Tiles moveTile = (Tiles) tilesSet.toArray()[randomTile];

                if (moveTile.getTempObject() == null) {
                    enemyTile.setTempObject(null);
                    moveTile.setTempObject(enemy);
                }
            }
        }
    }

    public void spawnNewEnemy(int spawnRate) {
        int spawn = r.nextInt(spawnRate);
        int spawnX = r.nextInt(xaxis-1);
        int spawnY = r.nextInt(yaxis-1);
        int enemyType = r.nextInt(5);

        if (spawn == 0) {
            Tiles spawnTile = grid[spawnX][spawnY];
            if (spawnTile.getTempObject() == null) {
                GameObject enemy;
                if (enemyType == 0) {
                    enemy = new DeadlyEnemy(ID.Enemy, handler);
                } else if (enemyType < 3) {
                    enemy = new BasicEnemy(ID.Enemy, handler);
                } else {
                    enemy = new FastEnemy(ID.Enemy, handler);
                }
                handler.addObject(enemy);
                spawnTile.setTempObject(enemy);
            }
        }
    }

    public void destroyEnemy(Tiles selectedTile) {
        handler.removeObject(selectedTile.getTempObject());
        selectedTile.contains = Tiles.OBJECT.None;
        topBar.setCurrentAction(TopBar.ACTION.NONE);
        player.setSelected(false);
        makeSelectable(1,false);

        moveEnemies();
        spawnNewEnemy(2);
    }

    public static void main(String[] args){
        new Game();
    }
}
