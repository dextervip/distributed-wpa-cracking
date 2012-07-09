/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class Init {

    public static void main(String[] args) {
        Client c = new Client();
        try {
            c.connect();
            while(true){
                Thread.sleep(1000);
                String msg  = c.receive();
                c.process(msg);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Erro desconhecido", ex);
        }
    }
    private static final Logger LOG = Logger.getLogger(Init.class.getName());
}
