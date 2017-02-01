package logic;

import java.util.ArrayList;

public class Node implements Comparable<Node> {

    AreaMap map;
    Node north;
    Node northEast;
    Node east;
    Node southEast;
    Node south;
    Node southWest;
    Node west;
    Node northWest;
    ArrayList<Node> neighborList;
    boolean visited;
    float distanceFromStart;
    float heuristicDistanceFromGoal;
    Node previousNode;
    int x;
    int y;
    boolean isObstacle;
    boolean isStart;
    boolean isGoal;

    Node(int x, int y) {
        neighborList = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.visited = false;
        this.distanceFromStart = Integer.MAX_VALUE;
        this.isObstacle = false;
        this.isStart = false;
        this.isGoal = false;
    }

    Node(int x, int y, boolean visited, int distanceFromStart,
            boolean isObstical, boolean isStart, boolean isGoal) {
        neighborList = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.visited = visited;
        this.distanceFromStart = distanceFromStart;
        this.isObstacle = isObstical;
        this.isStart = isStart;
        this.isGoal = isGoal;
    }

    public Node getNorth() {
        return north;
    }

    public void setNorth(Node north) {
        if (neighborList.contains(this.north)) {
            neighborList.remove(this.north);
        }
        neighborList.add(north);
        this.north = north;
    }

    public Node getNorthEast() {
        return northEast;
    }

    public void setNorthEast(Node northEast) {
        if (neighborList.contains(this.northEast)) {
            neighborList.remove(this.northEast);
        }
        neighborList.add(northEast);

        this.northEast = northEast;
    }

    public Node getEast() {
        return east;
    }

    public void setEast(Node east) {
        if (neighborList.contains(this.east)) {
            neighborList.remove(this.east);
        }
        neighborList.add(east);

        this.east = east;
    }

    public Node getSouthEast() {
        return southEast;
    }

    public Node getSouth() {
        return south;
    }

    public void setSouth(Node south) {
        if (neighborList.contains(this.south)) {
            neighborList.remove(this.south);
        }
        neighborList.add(south);
        this.south = south;
    }

    public Node getSouthWest() {
        return southWest;
    }

    public Node getWest() {
        return west;
    }

    public void setWest(Node west) {
        if (neighborList.contains(this.west)) {
            neighborList.remove(this.west);
        }
        neighborList.add(west);
        this.west = west;
    }

    public Node getNorthWest() {
        return northWest;
    }

    public ArrayList<Node> getNeighborList() {
        return neighborList;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public float getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(float f) {
        this.distanceFromStart = f;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public float getHeuristicDistanceFromGoal() {
        return heuristicDistanceFromGoal;
    }

    public void setHeuristicDistanceFromGoal(float heuristicDistanceFromGoal) {
        this.heuristicDistanceFromGoal = heuristicDistanceFromGoal;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isObstical() {
        return isObstacle;
    }

    public void setObstacle(boolean isObstical) {
        this.isObstacle = isObstical;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal(boolean isGoal) {
        this.isGoal = isGoal;
    }

    public boolean equals(Node node) {
        return (node.x == x) && (node.y == y);
    }

    @Override
    public int compareTo(Node otherNode) {
        float thisTotalDistanceFromGoal = heuristicDistanceFromGoal
                + distanceFromStart;
        float otherTotalDistanceFromGoal = otherNode
                .getHeuristicDistanceFromGoal()
                + otherNode.getDistanceFromStart();

        if (thisTotalDistanceFromGoal < otherTotalDistanceFromGoal) {
            return -1;
        } else if (thisTotalDistanceFromGoal > otherTotalDistanceFromGoal) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "<" + x + " , " + y + ">";
    }
}
