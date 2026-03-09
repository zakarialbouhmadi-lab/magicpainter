import processing.core.PApplet;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            PApplet.main("windows.App");
        }catch (Exception e){
            JOptionPane.showMessageDialog(new JFrame(),
                    e.getMessage(),
                    "Error !",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
