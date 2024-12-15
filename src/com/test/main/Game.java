package com.test.main;

import com.test.main.entities.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;


public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 960, HEIGHT = WIDTH / 12 * 9;
    public static final int SPAWN_WIDTH = WIDTH - 42, SPAWN_HEIGHT = HEIGHT - 65;
    public static final double FPS = 30.0;
    private Thread thread;
    private boolean running = false;
    private final Random r;
    private final Menu menu;
    public final Player player;
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
        topBar = new TopBar(this,handler);
        this.addMouseListener(menu);
        this.addMouseListener(topBar);
        new Window(WIDTH, HEIGHT, "Game", this);
        r = new Random();
        for(int i = 0; i < xaxis; i++){
            for(int j = 0; j < yaxis; j++){
                grid[i][j] = new Tiles(this,handler,(i * Tiles.size),(j * Tiles.size)+108,i,j);
                this.addMouseMotionListener(grid[i][j]);
                this.addMouseListener(grid[i][j]);
            }
        }
        spawnPlayer();
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
        // Add global variable and potential mutability
//        double amountOfTicks = 60.0;
        double ns = 1000000000 / FPS;
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
                if(running){
                    render();
                }
                frames++;
            }
//            if(running){
//                render();
//            }
//            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
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
            topBar.setRound(topBar.getRound() + 1);
            if (topBar.getRound() % 2 == 0) {
                spawnPowerUp();
            }
            if (topBar.getRound() == 6) {
                topBar.setRound(0);
                gameState = STATE.Menu;
            } else {
                spawnNewEnemy();
            }
        } else if (!handler.playerExists) {
            handler.clearEnemys();
            topBar.setRound(0);
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

        if(gameState == STATE.Game) {
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
        if (tile.contains.equals(Tiles.OBJECT.PowerUp)) {
            handler.removeObject(tile.getTempObject());
            tile.setTempObject(null);
            player.setMoveRange(player.getMoveRange()+1);
        }
        makeSelectable(player.getMoveRange(), false);
        prevTile.setTempObject(null);
//        if (tile.contains.)
        tile.setTempObject(player);

        moveEnemies();
    }

    public void spawnPlayer() {
        int playerRandX = r.nextInt(xaxis-1);
        int playerRandY = r.nextInt(yaxis-1);
        grid[playerRandX][playerRandY].setTempObject(player);
        handler.addObject(player);
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
                } else if (moveTile.getTempObject().getId().equals(ID.Player)) {
                    enemyTile.setTempObject(null);
                    handler.removeObject(player);
                    moveTile.setTempObject(null);
                    moveTile.setTempObject(enemy);
                }
            }
        }
    }

    public void spawnNewEnemy() {
        List<Enemy> enemies = new ArrayList<>();
        if (topBar.getRound() > 0) {
            enemies.add(new SlowEnemy(ID.Enemy, handler));
        }
        if (topBar.getRound() > 1) {
            enemies.add(new BasicEnemy(ID.Enemy, handler));
        }
        if (topBar.getRound() > 2) {
            enemies.add(new BasicEnemy(ID.Enemy, handler));
        }
        if (topBar.getRound() > 3) {
            enemies.add(new FastEnemy(ID.Enemy, handler));
        }
        if (topBar.getRound() > 4) {
            enemies.add(new FastEnemy(ID.Enemy, handler));
        }
        for (Enemy enemy : enemies) {
            boolean enemySet = false;
            while (!enemySet) {
                int spawnX = r.nextInt(xaxis-1);
                int spawnY = r.nextInt(yaxis-1);
                Tiles spawnTile = grid[spawnX][spawnY];
                try {
                    spawnTile.setTempObject(enemy);
                    handler.addObject(enemy);
                    enemySet = true;
                } catch (NullPointerException ignored) {

                }
            }
        }
    }

    public void destroyEnemy(Tiles selectedTile) {
        handler.removeObject(selectedTile.getTempObject());
        selectedTile.contains = Tiles.OBJECT.None;
        topBar.setCurrentAction(TopBar.ACTION.NONE);
        player.setSelected(false);
        makeSelectable(player.getMoveRange(), false);

        moveEnemies();
    }

    public void spawnPowerUp() {
        boolean powerUpSet = false;
        PowerUp powerUp = new PowerUp(ID.PowerUp,handler);
        while (!powerUpSet) {
            int spawnX = r.nextInt(xaxis-1);
            int spawnY = r.nextInt(yaxis-1);
            Tiles spawnTile = grid[spawnX][spawnY];
            try {
                spawnTile.setTempObject(powerUp);
                handler.addObject(powerUp);
                powerUpSet = true;
            } catch (NullPointerException ignored) {

            }
        }
    }

    public static void main(String[] args){
        new Game();
    }
}
