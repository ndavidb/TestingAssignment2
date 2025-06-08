package CarRace;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainAppFrame extends JFrame {

    // Car Customisation Components
    private JComboBox<Engine> engineComboBox;
    private JComboBox<Tyres> tyresComboBox;
    private JComboBox<AeroKit> aeroKitComboBox;
    private JTextArea carStatsTextArea;

    // Race Strategy Components
    private JComboBox<Track> trackComboBox;
    private JComboBox<Tyres.WeatherCondition> weatherComboBox;
    private JTextField lapsTextField;
    private JTextField fuelTankTextField;
    private JTextArea strategyResultTextArea;

    private Car currentCar;
    private final StrategyOptimiser optimiser;

    public MainAppFrame() {
        this.optimiser = new StrategyOptimiser();

        setTitle("Race Strategy Optimiser and Car Customisation Tool");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel carCustomisationPanel = createCarCustomisationPanel();
        tabbedPane.addTab("1. Car Customisation", carCustomisationPanel);

        JPanel raceStrategyPanel = createRaceStrategyPanel();
        tabbedPane.addTab("2. Race Strategy", raceStrategyPanel);

        add(tabbedPane);

        // Initialize the first car configuration
        updateCarConfiguration();
    }

    private JPanel createCarCustomisationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ----- Configuration Panel (West) -----
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(new TitledBorder("Select Components"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Engine
        configPanel.add(new JLabel("Engine:"), gbc);
        gbc.gridx = 1;
        engineComboBox = new JComboBox<>(CarComponentFactory.getAvailableEngines().toArray(new Engine[0]));
        configPanel.add(engineComboBox, gbc);

        // Tyres
        gbc.gridx = 0;
        gbc.gridy++;
        configPanel.add(new JLabel("Tyres:"), gbc);
        gbc.gridx = 1;
        tyresComboBox = new JComboBox<>(CarComponentFactory.getAvailableTyres().toArray(new Tyres[0]));
        configPanel.add(tyresComboBox, gbc);

        // AeroKit
        gbc.gridx = 0;
        gbc.gridy++;
        configPanel.add(new JLabel("Aerodynamic Kit:"), gbc);
        gbc.gridx = 1;
        aeroKitComboBox = new JComboBox<>(CarComponentFactory.getAvailableAeroKits().toArray(new AeroKit[0]));
        configPanel.add(aeroKitComboBox, gbc);

        // Action listeners to update stats when selection changes
        engineComboBox.addActionListener(e -> updateCarConfiguration());
        tyresComboBox.addActionListener(e -> updateCarConfiguration());
        aeroKitComboBox.addActionListener(e -> updateCarConfiguration());

        panel.add(configPanel, BorderLayout.WEST);

        // ----- Stats Display Panel (Center) -----
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(new TitledBorder("Car Performance Metrics"));
        carStatsTextArea = new JTextArea();
        carStatsTextArea.setEditable(false);
        carStatsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsPanel.add(new JScrollPane(carStatsTextArea), BorderLayout.CENTER);

        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRaceStrategyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ----- Inputs Panel (North) -----
        JPanel inputsPanel = new JPanel(new GridBagLayout());
        inputsPanel.setBorder(new TitledBorder("Race Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Track
        gbc.gridx = 0; gbc.gridy = 0;
        inputsPanel.add(new JLabel("Track:"), gbc);
        gbc.gridx = 1;
        trackComboBox = new JComboBox<>(CarComponentFactory.getAvailableTracks().toArray(new Track[0]));
        inputsPanel.add(trackComboBox, gbc);

        // Weather
        gbc.gridx = 0; gbc.gridy = 1;
        inputsPanel.add(new JLabel("Weather:"), gbc);
        gbc.gridx = 1;
        weatherComboBox = new JComboBox<>(Tyres.WeatherCondition.values());
        weatherComboBox.removeItem(Tyres.WeatherCondition.ANY); // User must select WET or DRY
        inputsPanel.add(weatherComboBox, gbc);

        // Laps
        gbc.gridx = 2; gbc.gridy = 0;
        inputsPanel.add(new JLabel("Total Laps:"), gbc);
        gbc.gridx = 3;
        lapsTextField = new JTextField("50", 5);
        inputsPanel.add(lapsTextField, gbc);

        // Fuel Tank
        gbc.gridx = 2; gbc.gridy = 1;
        inputsPanel.add(new JLabel("Fuel Tank (Liters):"), gbc);
        gbc.gridx = 3;
        fuelTankTextField = new JTextField("110", 5);
        inputsPanel.add(fuelTankTextField, gbc);

        // Generate Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        JButton generateButton = new JButton("Generate Optimal Strategy");
        generateButton.addActionListener(this::generateStrategyAction);
        inputsPanel.add(generateButton, gbc);

        panel.add(inputsPanel, BorderLayout.NORTH);

        // ----- Result Display -----
        strategyResultTextArea = new JTextArea();
        strategyResultTextArea.setEditable(false);
        strategyResultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        strategyResultTextArea.setText("Configure race details and click 'Generate' to see the optimal strategy.");
        panel.add(new JScrollPane(strategyResultTextArea), BorderLayout.CENTER);

        return panel;
    }

    private void updateCarConfiguration() {
        Engine selectedEngine = (Engine) engineComboBox.getSelectedItem();
        Tyres selectedTyres = (Tyres) tyresComboBox.getSelectedItem();
        AeroKit selectedAeroKit = (AeroKit) aeroKitComboBox.getSelectedItem();

        if (selectedEngine != null && selectedTyres != null && selectedAeroKit != null) {
            currentCar = new Car(selectedEngine, selectedTyres, selectedAeroKit);
            displayCarStats();
        }
    }

    private void displayCarStats() {
        if (currentCar == null) return;

        StringBuilder stats = new StringBuilder();
        stats.append(String.format("Top Speed         : %.1f km/h\n", currentCar.getCalculatedTopSpeed()));
        stats.append(String.format("Handling          : %.1f / 20.0\n", currentCar.getCalculatedHandling()));
        stats.append(String.format("Fuel Consumption  : %.2f Liters/Lap (Base)\n", currentCar.getCalculatedFuelConsumption()));
        stats.append("\n--- Component Details ---\n");
        stats.append(String.format("Engine: %s\n", currentCar.getEngine().getName()));
        stats.append(String.format("Tyres: %s (Optimal: %s)\n", currentCar.getTyres().getName(), currentCar.getTyres().getOptimalCondition()));
        stats.append(String.format("Aero Kit: %s\n", currentCar.getAeroKit().getName()));

        carStatsTextArea.setText(stats.toString());
    }

    private void generateStrategyAction(ActionEvent e) {
        // 1. Get current car
        updateCarConfiguration();
        if (currentCar == null) {
            JOptionPane.showMessageDialog(this, "Car configuration is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Get race details from inputs
        Track selectedTrack = (Track) trackComboBox.getSelectedItem();
        Tyres.WeatherCondition selectedWeather = (Tyres.WeatherCondition) weatherComboBox.getSelectedItem();
        int totalLaps;
        double fuelTankCapacity;

        try {
            totalLaps = Integer.parseInt(lapsTextField.getText().trim());
            fuelTankCapacity = Double.parseDouble(fuelTankTextField.getText().trim());
            if (totalLaps <= 0 || fuelTankCapacity <= 0) {
                throw new NumberFormatException("Values must be positive");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid positive numbers for Laps and Fuel Tank Capacity.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            strategyResultTextArea.setText("Error: Invalid input for Laps or Fuel Capacity. Please enter positive numbers.");
            return;
        }

        // 3. Call the optimiser
        RaceStrategy strategy = optimiser.generateOptimalStrategy(currentCar, selectedTrack, totalLaps, fuelTankCapacity, selectedWeather);

        // 4. Display the result
        strategyResultTextArea.setText(strategy.getSummary());
        strategyResultTextArea.setCaretPosition(0); // Scroll to top
    }
}