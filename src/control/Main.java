package control;

import it.randomtower.engine.ResourceManager;
import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Sugirjan
 */
//library used marteEngine and slick 2d
public class Main extends StateBasedGame {
    //according to the tutorial in https://github.com/Gornova/MarteEngine/wiki
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Main("Tank Game"));
        app.setDisplayMode(1280, 650, false);//screen size
        app.setTargetFrameRate(60);
        app.start();
    }

    public Main(String name) {
        super(name);
    }

    //adding resources such as images for resource manager
    //and adding states
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        try {
            ResourceManager.loadResources("res/resources.xml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        addState(new Game(1, gc));
        enterState(1);
    }

}
