package logic;

import java.util.ArrayList;

public class Path {

    private final ArrayList<Node> waypoints = new ArrayList<>();

    public Path() {
    }

    public int getLength() {
        return waypoints.size();
    }

    public Node getWayPoint(int index) {
        return waypoints.get(index);
    }

    public int getX(int index) {
        return getWayPoint(index).getX();
    }

    public int getY(int index) {
        return getWayPoint(index).getY();
    }

    public void appendWayPoint(Node n) {
        waypoints.add(n);
    }

    public void prependWayPoint(Node n) {
        waypoints.add(0, n);
    }

    public boolean contains(int x, int y) {
        for (Node node : waypoints) {
            if (node.getX() == x && node.getY() == y) {
                return true;
            }
        }
        return false;
    }

}
