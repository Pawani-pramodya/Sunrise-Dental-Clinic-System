/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sunrisedental;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.LoginFrame;


/**
 *
 * @author pawan
 */
public class SunriseDental {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();

        // optional global styling
        UIManager.put("TextComponent.arc", 14);
        UIManager.put("Button.arc", 14);
        UIManager.put("Component.focusWidth", 1);

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));



    }
    
}
