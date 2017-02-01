package logic;

public class ShortestPathFinder {

    private final AreaMap map;
    private final DistanceFunction heuristic;
    private final Algorithm pathFinder;

    public ShortestPathFinder(int mapWidth, int mapHeight, int[][] obstacleMap) {
        map = new AreaMap(mapWidth, mapHeight, obstacleMap);
        heuristic = new DistanceFunction();
        pathFinder = new Algorithm(map, heuristic);
    }

    public Path getShortestPath(int startX, int startY, int goalX, int goalY) {
        System.out.println("Shortest path finder called");
        return pathFinder.calcShortestPath(startX, startY, goalX, goalY);
    }
}
