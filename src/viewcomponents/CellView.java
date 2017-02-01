package viewcomponents;

import control.Config;
import it.randomtower.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * @author Sugirjan
 */
public class CellView extends Entity {

    final static int gap = Config.gap;
    final static int startX = Config.startX;
    final static int startY = Config.startY;

    private final float posX;
    private final float posY;

    public CellView(float x, float y) {
        super(x, y);
        posX = x;
        posY = y;
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);

    }

    public float getPosX() {
        return x;
    }

    public float getPosY() {
        return y;
    }
}
