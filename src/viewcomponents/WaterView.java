package viewcomponents;

import it.randomtower.engine.ResourceManager;
import org.newdawn.slick.Image;

/**
 *
 * @author Sugirjan
 */
public class WaterView extends CellView {
    public static String WATER = "water";
    private final Image water;

    public WaterView(float x, float y) {
        super(x, y);
        depth = 10;
        water = ResourceManager.getImage("water");
        setGraphic(water);
        setHitBox(0, 0, water.getWidth(), water.getHeight());
        addType(WATER);
    }


}
