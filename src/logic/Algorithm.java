package logic;

import java.util.ArrayList;
import java.util.Collections;

public class Algorithm {

    private final AreaMap map;
    private final DistanceFunction heuristic;
    private final ArrayList<Node> closedList;
    private final SortedNodeList openList;

    Algorithm(AreaMap map, DistanceFunction heuristic) {
        this.map = map;
        this.heuristic = heuristic;
        closedList = new ArrayList<>();
        openList = new SortedNodeList();
    }

    public Path calcShortestPath(int startX, int startY, int goalX, int goalY) {
        System.out.println("calcShortestPath called");
        map.setStartLocation(startX, startY);
        map.setGoalLocation(goalX, goalY);
        if (map.getNode(goalX, goalY).isObstacle) {
            System.out.println("CoinView is an obstracle");
            return null;
        }

        map.getStartNode().setDistanceFromStart(0);
        closedList.clear();
        openList.clear();
        openList.add(map.getStartNode());
        System.out.println(openList.toString());


        while (openList.size() != 0) {
            Node current = openList.getFirst();

            if (current.getX() == map.getGoalLocationX() && current.getY() == map.getGoalLocationY()) {
                System.out.println("reconstruct");

                return reconstructPath(current);
            }
            openList.remove(current);
            closedList.add(current);

            for (Node neighbor : current.getNeighborList()) {
                boolean neighborIsBetter;

                if (closedList.contains(neighbor)) {
                    continue;
                }

                if (!neighbor.isObstacle) {

                    float neighborDistanceFromStart = (current.getDistanceFromStart() + map.getDistanceBetween(current, neighbor));

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                        neighborIsBetter = true;
                    } else {
                        neighborIsBetter = neighborDistanceFromStart < current.getDistanceFromStart();
                    }
                    if (neighborIsBetter) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(heuristic.getEstimatedDistanceToGoal(neighbor.getX(), neighbor.getY(), map.getGoalLocationX(), map.getGoalLocationY()));
                    }
                }

            }
        }
        return null;
    }

    private Path reconstructPath(Node node) {
        Path path = new Path();
        while (!(node.getPreviousNode() == null)) {
            path.prependWayPoint(node);
            node = node.getPreviousNode();
        }
        System.out.println("path reconstructed");
        return path;
    }

    private class SortedNodeList {

        private final ArrayList<Node> list = new ArrayList<>();

        public Node getFirst() {
            return list.get(0);
        }

        public void clear() {
            list.clear();
        }

        public void add(Node node) {
            list.add(node);
            Collections.sort(list);
        }

        public void remove(Node n) {
            list.remove(n);
        }

        public int size() {
            return list.size();
        }

        public boolean contains(Node n) {
            return list.contains(n);
        }
    }

}
