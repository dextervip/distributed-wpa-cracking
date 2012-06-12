package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author Rafael
 */
public class Cliente {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Cliente() {
    }
}
