package common;

import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class DI {

    public static Logger getLogger() {
        Logger logger = Logger.getLogger("distributed-wpa-cracking");
        return logger;
    }
}
