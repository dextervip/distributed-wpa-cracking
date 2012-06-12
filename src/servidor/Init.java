package servidor;

import common.DI;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class Init {

    Logger logger;

    public Init() {
        this.logger = DI.getLogger();
    }

    public void initGUIMode() {
        logger.log(Level.INFO, "Initializing GUIMode");
        GUI gui = new GUI();
        gui.setVisible(true);
    }

    public void initTextMode() {
        logger.log(Level.INFO, "Initializing TextMode");
        TextMode text = new TextMode();
    }

    public void initServer(String args[]) {
        logger.log(Level.INFO, "Initializing server");
        if (args.length == 0) {
            if (GraphicsEnvironment.isHeadless()) {
                logger.log(Level.INFO, "System doesn't support GUI");
                initTextMode();
            } else {
                logger.log(Level.INFO, "Support GUI");
                initGUIMode();
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            initGUIMode();
        } else if (args[0].equalsIgnoreCase("text")) {
            initTextMode();
        }
    }

    public static void main(String[] args) {
        Init init = new Init();
        init.initServer(args);
    }
}
