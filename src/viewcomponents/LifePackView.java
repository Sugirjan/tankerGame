package viewcomponents;

import control.Collectable;
import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;
import org.newdawn.slick.*;

/**
 * @author Sugirjan
 */
public class LifePackView extends CellView implements Collectable {

    int life;
    private final Image lifeImage;
    private int timeLived = 0;
    public static String LIFE = "LIFE";
    private int a;
    private int b;

    public LifePackView(int a, int b, float x, float y, int life) {
        super(x, y);
        this.a = a;
        this.b = b;
        this.life = life;
        lifeImage = ResourceManager.getImage("lifepack");
        setGraphic(lifeImage);
        setHitBox(0, 0, lifeImage.getWidth(), lifeImage.getHeight());
        addType(LIFE);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        g.setColor(Color.black);
        g.drawString(life + "", x + 5, y + 20);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        timeLived += delta;
        if (timeLived >= life) {
            ME.world.remove(this);
        }
    }

    public boolean expired() {
        return timeLived >= life;
    }

    @Override
    public void collisionResponse(Entity other) {
        ME.world.remove(this);
    }


    @Override
    public int getA() {
        return a;
    }

    @Override
    public int getB() {
        return b;
    }
}
