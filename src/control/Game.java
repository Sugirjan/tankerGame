package control;

import viewcomponents.*;
import logic.*;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.World;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Sugirjan on 16/12/16.
 */
public class Game extends World {
    public static int GRIDSIZE = Config.GRIDSIZE;//arena size
    private Image background, arenaImage;//images for arena
    static public CellView[][] grid;//2d array for arena
    private NetworkTools networkTools;//connection
    private final ArrayList<PlayerView> players = new ArrayList<>();//players list
    private int AIplayerNo;//player no
    private ShortestPathFinder pathFinder;
    private final BlockingQueue<Collectable> queue = new ArrayBlockingQueue<>(150);
    private PlayerView player;
    private  Guard guard;

    public Game(int id, GameContainer gc) {
        super(id, gc);
    }

    private void setup(StateBasedGame game) throws IOException {
        networkTools = NetworkTools.getInstance();
        grid = new CellView[GRIDSIZE][GRIDSIZE];
        createArena();
        String reciveData;
        networkTools.sendMessage(Config.INITIALREQUEST);
        do {
            reciveData = networkTools.receiveMessage();
            if (reciveData.equals("PLAYERS_FULL") || reciveData.equals("GAME_ALREADY_STARTED")) {
                System.out.println("PLAYERS_FULL or GAME_ALREADY_STARTED");
                System.exit(0);
            }
            if ((!reciveData.equals("ALREADY_ADDED")) && (!(reciveData.charAt(0) == 'I')) && ((!reciveData.equals("GAME_ALREADY_STARTED")))) {
                networkTools.sendMessage(Config.INITIALREQUEST);
            }

        } while (!(reciveData.charAt(0) == 'I'));
//        guard = new Guard(networkTools);
//        guard.run();
//        shoot();//shooting started
        String[] section = reciveData.split(":");  //break into sections
        AIplayerNo = Integer.parseInt(section[1].charAt(1) + "");   //set AIplayer number
        setBricks(section[2].split(";")); //set bricks
        setStones(section[3].split(";")); //set stones
        setWater(section[4].split(";"));  //set water

        do {
            reciveData = networkTools.receiveMessage();
        } while (!(reciveData.charAt(0) == 'S'));
        setPlayers(reciveData.split(":")); //set players from the initiation message
        initializeAStar();
    }

    //function for getting map with details of situation of BrickView,water,stone
    //and by that it can be used to ShortestPathFinderClass for collecting coins,etc
    private void initializeAStar() {
        int[][] obstacleMap = new int[Config.GRIDSIZE][Config.GRIDSIZE];
        for (int i = 0; i < Config.GRIDSIZE; i++) {
            for (int j = 0; j < Config.GRIDSIZE; j++) {
                CellView e;
                e = grid[i][j];
                if (e instanceof BrickView || e instanceof WaterView
                        || e instanceof StoneView) {
                    obstacleMap[j][i] = 1;
                }
            }
        }
        pathFinder = new ShortestPathFinder(Config.GRIDSIZE, Config.GRIDSIZE, obstacleMap);
//        guard = null;
        collectCoin();
    }

    //setting bricks in places as provided by the server
    private void setBricks(String[] bricks) {
        for (String brick : bricks) {
            String[] coordinates = brick.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            grid[x][y] = new BrickView(grid[x][y].getPosX(), grid[x][y].getPosY());
            add(grid[x][y]);
        }
    }
    //setting up stones as provided by server
    private void setStones(String[] stones) {
        for (String stone : stones) {
            String[] coordinates = stone.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            grid[x][y] = new StoneView(grid[x][y].getPosX(), grid[x][y].getPosY());
            add(grid[x][y]);
        }

    }
    //setting up water as provided by server
    private void setWater(String[] waters) {
        for (String water : waters) {
            String[] coordinates = water.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            grid[x][y] = new WaterView(grid[x][y].getPosX(), grid[x][y].getPosY());
            add(grid[x][y]);
        }
    }
    ///method of World to load one or more entities into your World
    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        super.init(gc, game);
        gc.setAlwaysRender(true);
        gc.setUpdateOnlyWhenVisible(false);
        createBackground();
        container.setAlwaysRender(true);
        try {
            setup(game);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
    }


    //updating coins,lifepacks,players
    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        super.update(gc, game, delta);
        String reciveData = networkTools.receiveMessage();
        String[] section = reciveData.split(":");
        switch (section[0]) {
            case "C":
                setCoins(section);
                break;
            case "L":
                setLifePacks(section);
                break;
            case "G":
                updatePlayers(section);
                break;
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, 0, -130);
        g.drawImage(arenaImage, 20, 20);
        super.render(gc, game, g);
        setPointsTable(g);
    }
    //setting up coins provided by server and their life times inorder to remove them after their life period finishes
    private void setCoins(String[] section) {
        String[] coord = section[1].split(",");
        int x = Integer.parseInt(coord[0]);
        int y = Integer.parseInt(coord[1]);
        int lifetime = Integer.parseInt(section[2]);
        int value = Integer.parseInt(section[3]);
        CoinView c = new CoinView(x, y, grid[x][y].getPosX(), grid[x][y].getPosY(), value, lifetime);
        grid[x][y] = c;
        add(grid[x][y]);
        try {
            queue.put(c);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void setLifePacks(String[] section) {
        String[] coord = section[1].split(",");
        int x = Integer.parseInt(coord[0]);
        int y = Integer.parseInt(coord[1]);
        int lifetime = Integer.parseInt(section[2]);
        add(grid[x][y]);
        LifePackView lifePack = new LifePackView(x,y,grid[x][y].getPosX(), grid[x][y].getPosY(), lifetime);
        grid[x][y] = lifePack;
        try {
            queue.put(lifePack);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void createBackground() {
        background = ResourceManager.getImage("background");
        arenaImage = ResourceManager.getImage("grid");
    }

    private void setPointsTable(Graphics g) {
        String spaceBetColumns = Config.pointTableColumnSpacing;
        int spaceBetRows = Config.pointTableRowSpacing;
        int textPositionX = Config.textPositionX;
        int textPositionY = Config.textPositionY;
        g.setLineWidth(g.getLineWidth() * 3);
        g.setColor(Color.white);
        g.drawString(String.format("%10s", "PlayerView ID") + spaceBetColumns + String.format("%10s", "Points") + spaceBetColumns + String.format("%10s", "Coins") + spaceBetColumns + String.format("%10s", "Health"), textPositionX, textPositionY);
        for (int i = 0; i < players.size(); i++) {
            String pointsTableEntry = ((PlayerView) players.get(i)).getPointsTableEntry();
            g.drawString(pointsTableEntry, textPositionX - 15, textPositionY + spaceBetRows * (i + 1));
        }
    }

    private void setPlayers(String[] playerSection) {
        for (int i = 1; i < playerSection.length; i++) {
            int x, y, direction, no;
            String[] data = playerSection[i].split(";");
            no = Integer.parseInt(data[0].charAt(1) + "");
            String[] position = data[1].split(",");
            x = Integer.parseInt(position[0]);
            y = Integer.parseInt(position[1]);
            direction = Integer.parseInt(data[2]);
            PlayerView newPlayer = new PlayerView(x, y, grid[x][y].getPosX(), grid[x][y].getPosY(), no, direction);
            if (AIplayerNo == no) {
                this.player = newPlayer;
            }
            add(newPlayer);
            players.add(newPlayer);
        }
    }

    private void updatePlayers(String[] section) {
        for (int i = 0; i < players.size(); i++) {
            PlayerView get = (PlayerView) players.get(i);
            get.setGlobleUpdate(section[i + 1]);
        }

    }

    private void createArena() {
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                grid[i][j] = new CellView(Config.startX + Config.gap * i, Config.startY + Config.gap * j);
            }
        }
    }

    //for collecting coin if possible
    private void collectCoin() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("while loop");
                    try{
//                        queue.removeIf(c -> c.expired());
//                        if (queue.isEmpty()) {
//                            continue;
//                        }
                        Collectable collectable = queue.take();
                        System.out.println("got coin");
                        System.out.println(collectable.getB());
                        System.out.println(player.getB());
                        Path finalPath = pathFinder.getShortestPath(player.getB(), player.getA(), collectable.getB(), collectable.getA());
                        System.out.println(finalPath.toString());

                        if (finalPath == null) {
                            System.out.println("path null");
                            continue;
                        }
                        System.out.println("PlayerView: <" + player.getA() + " , " + player.getB() + ">  CoinView: <" + collectable.getA() + " , " + collectable.getB() + ">");
                        if (collectable.getA() < Config.GRIDSIZE && collectable.getB() < Config.GRIDSIZE && player.getA() < Config.GRIDSIZE && player.getB() < Config.GRIDSIZE) {
                            int tempX = player.getA();
                            int tempY = player.getB();
                            int point1;
                            int point2;
                            for (int i = 0; i < finalPath.getLength(); i++) {
                                System.out.println(finalPath.getY(i) + " : " + finalPath.getX(i));
                                point1 = finalPath.getX(i) - tempX;
                                point2 = finalPath.getY(i) - tempY;
                                //--------
                                if (point1 == 0 && point2 == 1) {// 0 North , 1 East , 2 South ,3 West
                                    System.out.println("east");
                                    if (player.getDirection() == 3) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 2) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 1) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
                                    } else {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.RIGHT);
                                        Thread.sleep(1100);
                                    }
                                } else if (point1 == 0 && point2 == -1) {
                                    System.out.println("west");// 0 North , 1 East , 2 South ,3 West
                                    if (player.getDirection() == 3) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 2) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 1) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
                                    } else {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.LEFT);
                                        Thread.sleep(1100);
                                    }
                                } else if (point1 == -1 && point2 == 0) {
                                    System.out.println("north");// 0 North , 1 East , 2 South ,3 West
                                    if (player.getDirection() == 3) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 2) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 1) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
                                    } else {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.UP);
                                        Thread.sleep(1100);
                                    }
                                } else {
                                    System.out.println("south");// 0 North , 1 East , 2 South ,3 West
                                    if (player.getDirection() == 3) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 2) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
                                    } else if (player.getDirection() == 1) {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
                                    } else {
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
//                                        networkTools.sendMessage(Config.SHOOT);
//                                        Thread.sleep(1100);
                                        networkTools.sendMessage(Config.DOWN);
                                        Thread.sleep(1100);
                                    }
                                }
                                tempX = finalPath.getX(i);
                                tempY = finalPath.getY(i);
                            }
                        }

                    }catch (InterruptedException e){
                        System.out.println(e.toString());
                    }
                }

            }
        }.start();
    }

}
