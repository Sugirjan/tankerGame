package logic;

public class DistanceFunction {

    public float getEstimatedDistanceToGoal(int startX, int startY, int goalX, int goalY) {
        float dx = goalX - startX;
        float dy = goalY - startY;
        float result =  (dx * dx) + (dy * dy);
        return result;
    }

}
