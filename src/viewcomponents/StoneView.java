package viewcomponents;

import it.randomtower.engine.ResourceManager;
import org.newdawn.slick.Image;

/**
 * @author Sugirjan
 */
public class StoneView extends CellView {

    public static String SOLID_WALL = "solid";
    private final Image stone;

    public StoneView(float x, float y) {
        super(x, y);
        depth = 10;
        stone = ResourceManager.getImage("stone");
        setGraphic(stone);
        setHitBox(0, 0, stone.getWidth(), stone.getHeight());
        addType(SOLID_WALL);
    }

}
