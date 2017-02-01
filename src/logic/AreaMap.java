package logic;

import java.util.ArrayList;

public class AreaMap {

    private final int mapWith;
    private final int mapHeight;
    private ArrayList<ArrayList<Node>> map;
    private int startLocationX = 0;
    private int startLocationY = 0;
    private int goalLocationX = 0;
    private int goalLocationY = 0;
    private final int[][] obstacleMap;

    AreaMap(int mapWith, int mapHeight, int[][] obstacleMap) {
        this.mapWith = mapWith;
        this.mapHeight = mapHeight;
        this.obstacleMap = obstacleMap;

        createMap();
        registerEdges();
    }
    //creating node map according to obstacles
    private void createMap() {
        Node node;
        map = new ArrayList<>();
        for (int x = 0; x < mapWith; x++) {
            map.add(new ArrayList<>());
            for (int y = 0; y < mapHeight; y++) {
                node = new Node(x, y);
                if (obstacleMap[x][y] == 1) {
                    node.setObstacle(true);
                }
                map.get(x).add(node);
            }
        }
    }

    /**
     * Registers the nodes edges (connections to its neighbors).
     */
    //TODO add the nodes
    private void registerEdges() {
        for (int x = 0; x < mapWith; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Node node = map.get(x).get(y);
                if (!(y == 0)) {
                    node.setNorth(map.get(x).get(y - 1));
                }
                if (!(x == mapWith - 1)) {
                    node.setEast(map.get(x + 1).get(y));
                }
                if (!(y == mapHeight - 1)) {
                    node.setSouth(map.get(x).get(y + 1));
                }
                if (!(x == 0)) {
                    node.setWest(map.get(x - 1).get(y));
                }
            }
        }
    }

    public ArrayList<ArrayList<Node>> getNodes() {
        return map;
    }

    public void setObstacle(int x, int y, boolean isObstacle) {
        map.get(x).get(y).setObstacle(isObstacle);
    }

    public Node getNode(int x, int y) {
        return map.get(x).get(y);
    }

    public void setStartLocation(int x, int y) {
        map.get(startLocationX).get(startLocationY).setStart(false);
        map.get(x).get(y).setStart(true);
        startLocationX = x;
        startLocationY = y;
    }

    public void setGoalLocation(int x, int y) {
        map.get(goalLocationX).get(goalLocationY).setGoal(false);
        map.get(x).get(y).setGoal(true);
        goalLocationX = x;
        goalLocationY = y;
    }

    public int getStartLocationX() {
        return startLocationX;
    }

    public int getStartLocationY() {
        return startLocationY;
    }

    public Node getStartNode() {
        return map.get(startLocationX).get(startLocationY);
    }

    public int getGoalLocationX() {
        return goalLocationX;
    }

    public int getGoalLocationY() {
        return goalLocationY;
    }

    public Node getGoalLocation() {
        return map.get(goalLocationX).get(goalLocationY);
    }

    public float getDistanceBetween(Node node1, Node node2) {
        if (node1.getX() == node2.getX() || node1.getY() == node2.getY()) {
            return 1 * (mapHeight + mapWith);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public int getMapWith() {
        return mapWith;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void clear() {
        startLocationX = 0;
        startLocationY = 0;
        goalLocationX = 0;
        goalLocationY = 0;
        createMap();
        registerEdges();
    }
}
