package servidor;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author Rafael
 */
public class Init {
    
    private static final Logger LOG = Logger.getLogger(Init.class.getName());

    public Init() {
    }
    /**
     * Init GUI mode interface
     */
    public void initGUIMode() {
        LOG.log(Level.INFO, "Initializing GUIMode");
        this.enableNimbusTheme();
        GUIMode gui = new GUIMode();
        gui.setVisible(true);
    }

    public void enableNimbusTheme() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    UIDefaults defaults = UIManager.getLookAndFeelDefaults();
                    UIManager.put("control", new Color(255, 255, 255));
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
    /**
     * Init text mode interface
     */
    public void initTextMode() {
        LOG.log(Level.INFO, "Initializing TextMode");
        TextMode text = new TextMode();
    }
    /**
     * Verify if user has chosen GUI or Text mode interface, otherwise test GUI support
     * @param args 
     */
    public void initServer(String args[]) {
        LOG.log(Level.INFO, "Initializing server");
        if (args.length == 0) {
            if (GraphicsEnvironment.isHeadless()) {
                LOG.log(Level.INFO, "System doesn't support GUI");
                initTextMode();
            } else {
                LOG.log(Level.INFO, "Support GUI");
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
