import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow {

    TableWindow tableWindow;
    private String heroViewLabel = "List of Heroes";
    private String heroFileName = "src/feh-heroes.txt";
    private String[] heroStatList = {"Name", "Colour", "Weapon", "Movement", "HP", "Atk", "Spd", "Def", "Res", "Game"};

    private String weaponViewLabel = "List of Weapons";
    private String weaponFileName = "src/feh-weapons.txt";
    private String[] weaponStatList = {"Name", "Colour", "Type", "Might", "PRF", "Refinable", "Cost"};

    private String[] arrayOfLabels = {heroViewLabel, weaponViewLabel};
    private String[] arrayOfFileNames = {heroFileName, weaponFileName};
    private String[][] arrayOfStatLists = {heroStatList, weaponStatList};

    //private JFrame mainFrame;
    private JLabel tableNameLabel;
    private JScrollPane tableViewPanel;
    private JPanel tableSortingPanel;
    private int currentTableNumber;
    private JLabel tableNumberLabel;

    public static void main(String[] args) throws Exception {
        MainWindow main = new MainWindow();
        main.createMainWindow();
    }

    public void createMainWindow() throws Exception {
        tableWindow = new TableWindow();

        // Initialising frame
        JFrame mainFrame = new JFrame();
        // Initialising base window header
        JPanel tableNamePanel = new JPanel();
        tableNamePanel.setPreferredSize(new Dimension(1500, 25));
        // Initialising main part of window, the table panel
        tableViewPanel = tableWindow.createPanelForTable(arrayOfFileNames[0], arrayOfStatLists[0]);
        // Initialising sorting panel for the table
        tableSortingPanel = tableWindow.createPanelForSorting(arrayOfStatLists[0]);
        // Creating bottom part of window, to switch between table and sorting methods
        GridBagLayout tableSelectionPanelLayout = new GridBagLayout();
        JPanel tableSelectionPanel = new JPanel(tableSelectionPanelLayout);
        tableSelectionPanel.setPreferredSize(new Dimension(1500, 40));
        // Adding the above aspects to their respective areas
        mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, tableSelectionPanel);
        mainFrame.getContentPane().add(BorderLayout.EAST, tableSortingPanel);
        mainFrame.getContentPane().add(BorderLayout.CENTER, tableViewPanel);

        // Components for tableNamePanel
        tableNameLabel = new JLabel(arrayOfLabels[0]);
        tableNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        tableNamePanel.add(tableNameLabel);

        // Components for tableSelectionPanel
        JButton previousTableButton = new JButton("<");
        previousTableButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        previousTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().remove(tableNamePanel);
                mainFrame.getContentPane().remove(tableSortingPanel);
                mainFrame.getContentPane().remove(tableViewPanel);
                int newTableNumber = currentTableNumber;
                if (newTableNumber == 0) {
                    // Set newTableNumber to index of last element
                    newTableNumber = arrayOfStatLists.length - 1;
                }
                else {
                    newTableNumber -= 1;
                }
                currentTableNumber = newTableNumber;
                tableNumberLabel.setText(newTableNumber + 1 + "");
                tableNameLabel.setText(arrayOfLabels[newTableNumber]);
                try {
                    tableViewPanel = tableWindow.createPanelForTable(arrayOfFileNames[newTableNumber], arrayOfStatLists[newTableNumber]);
                    tableSortingPanel = tableWindow.createPanelForSorting(arrayOfStatLists[newTableNumber]);
                    mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
                    mainFrame.getContentPane().add(BorderLayout.EAST, tableSortingPanel);
                    mainFrame.getContentPane().add(BorderLayout.CENTER, tableViewPanel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        JButton nextTableButton = new JButton(">");
        nextTableButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        nextTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().remove(tableNamePanel);
                mainFrame.getContentPane().remove(tableSortingPanel);
                mainFrame.getContentPane().remove(tableViewPanel);
                int newTableNumber = currentTableNumber;
                if (newTableNumber == arrayOfStatLists.length - 1) {
                    // Set newTableNumber to index of last element
                    newTableNumber = 0;
                }
                else {
                    newTableNumber += 1;
                }
                currentTableNumber = newTableNumber;
                tableNumberLabel.setText(newTableNumber + 1 + "");
                tableNameLabel.setText(arrayOfLabels[newTableNumber]);
                try {
                    tableViewPanel = tableWindow.createPanelForTable(arrayOfFileNames[newTableNumber], arrayOfStatLists[newTableNumber]);
                    tableSortingPanel = tableWindow.createPanelForSorting(arrayOfStatLists[newTableNumber]);
                    mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
                    mainFrame.getContentPane().add(BorderLayout.EAST, tableSortingPanel);
                    mainFrame.getContentPane().add(BorderLayout.CENTER, tableViewPanel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        tableNumberLabel = new JLabel("1");
        tableNumberLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        tableNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentTableNumber = 0;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        tableSelectionPanelLayout.setConstraints(previousTableButton, gbc);
        tableSelectionPanel.add(previousTableButton);

        gbc.gridx = 1;
        gbc.gridwidth = 14;
        tableSelectionPanelLayout.setConstraints(tableNumberLabel, gbc);
        tableSelectionPanel.add(tableNumberLabel);

        gbc.gridx = 15;
        gbc.gridwidth = 1;
        tableSelectionPanelLayout.setConstraints(nextTableButton, gbc);
        tableSelectionPanel.add(nextTableButton);

        // Creating dummy panels so that the GridBagLayout actually spaces the cells correctly
        gbc.gridwidth = 1;
        gbc.gridheight = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        for (int i = 1; i < 15; i++) {
            gbc.gridx = i;
            tableSelectionPanelLayout.setConstraints(new JLabel(), gbc);
            tableSelectionPanel.add(new JLabel(), gbc);
        }
        
        // Finally setup for the main frame
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }

}
