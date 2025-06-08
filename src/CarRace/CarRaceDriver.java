package CarRace;


import javax.swing.*;

public class CarRaceDriver {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            MainAppFrame frame = new MainAppFrame();
            frame.setVisible(true);
        });
    }
}