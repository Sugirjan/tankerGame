package viewcomponents;

import it.randomtower.engine.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author Sugirjan
 */
public class BrickView extends CellView {

    public static String SOLID_WALL = "solid";
    public static String BLANK_WALL = "blank";
    private final Image brick;

    public BrickView(float x, float y) {
        super(x, y);
        depth = 10;
        brick = ResourceManager.getImage("brick");
        setGraphic(brick);
        setHitBox(0, 0, brick.getWidth(), brick.getHeight());
        addType(SOLID_WALL);
    }


    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
    }

}
