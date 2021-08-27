package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.NullPointerException;
import java.lang.IllegalStateException;
import java.io.FileNotFoundException;

public class MainWindow {

    TableWindow tableWindow;
    // Label, stat file name, description file name, and stat name list for the list of heroes
    private String heroViewLabel = "List of Heroes";
    private String heroFileName = "files/feh-heroes.txt";
    private String heroDescriptionFileName = "files/feh-herodescriptions.txt";
    private String[] heroStatList = {"Name", "Colour", "Weapon", "Movement", "HP", "Atk", "Spd", "Def", "Res", "Game"};
    // Label, stat file name, description file name, and stat name list for the list of weapons
    private String weaponViewLabel = "List of Weapons";
    private String weaponFileName = "files/feh-weapons.txt";
    private String weaponDescriptionFileName = "files/feh-weapondescriptions.txt";
    private String[] weaponStatList = {"Name", "Colour", "Type", "Might", "PRF", "Refinable", "Cost"};
    // Label, stat file name, description file name, and stat name list for the list of refines
    private String refineViewLabel = "List of Refines";
    private String refineFileName = "files/feh-refines.txt";
    private String refineDescriptionFileName = "files/feh-refinedescriptions.txt";
    private String[] refineStatList = {"Name", "Mt", "HP", "Spd", "Def", "Res", "Arena Medals", "Refining Stones", "Divine Dew", "SP"};
    // Arrays that contain the above in the same order 
    private String[] arrayOfLabels = {heroViewLabel, weaponViewLabel, refineViewLabel};
    private String[] arrayOfFileNames = {heroFileName, weaponFileName, refineFileName};
    private String[] arrayOfDescriptionFileNames = {heroDescriptionFileName, weaponDescriptionFileName, refineDescriptionFileName};
    private String[][] arrayOfStatLists = {heroStatList, weaponStatList, refineStatList};

    private JLabel header;                      // Label at the top that says what the table is displaying
    private JScrollPane tableViewPanel;         // Table that will contain data based on the files used
    private JPanel sortingAndDescriptionPanel;  // Panel that contains sorting buttons and text area to display description of selected row
    private int currentTableNumber;             // Current table number that is being displayed
    private JLabel tableNumberLabel;            // Label with the text of the current table number

    public static void main(String[] args) throws NullPointerException, FileNotFoundException, IllegalStateException  {
        MainWindow main = new MainWindow();
        main.createMainWindow();
    }

    public void createMainWindow() throws NullPointerException, FileNotFoundException, IllegalStateException  {
        // Initialising frame
        JFrame mainFrame = new JFrame();
        // Initialising base window header
        JPanel tableNamePanel = new JPanel();
        // Initialising main part of window, the table panel
        tableWindow = new TableWindow();
        tableViewPanel = tableWindow.getPopulatedTablePanel(arrayOfFileNames[0], arrayOfDescriptionFileNames[0], arrayOfStatLists[0]);
        // Initialising sorting panel for the table
        sortingAndDescriptionPanel = tableWindow.getSortingAndDescriptionPanel(arrayOfStatLists[0]);
        // Creating bottom part of window, to switch between table and sorting methods
        GridBagLayout tableSelectionPanelLayout = new GridBagLayout();
        JPanel tableSelectionPanel = new JPanel(tableSelectionPanelLayout);
        tableSelectionPanel.setPreferredSize(new Dimension(1500, 40));
        // Adding the above aspects to their respective areas
        mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, tableSelectionPanel);
        mainFrame.getContentPane().add(BorderLayout.EAST, sortingAndDescriptionPanel);
        mainFrame.getContentPane().add(BorderLayout.CENTER, tableViewPanel);

        // Components for tableNamePanel
        header = new JLabel(arrayOfLabels[0]);
        header.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        header.setVerticalAlignment(SwingConstants.CENTER);
        tableNamePanel.add(header);

        // Components for tableSelectionPanel
        JButton previousTableButton = new JButton("<");
        previousTableButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        previousTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().remove(tableNamePanel);
                mainFrame.getContentPane().remove(sortingAndDescriptionPanel);
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
                tableNumberLabel.setText(newTableNumber + 1 + "/" + arrayOfLabels.length);
                header.setText(arrayOfLabels[newTableNumber]);
                try {
                    tableViewPanel = tableWindow.getPopulatedTablePanel(arrayOfFileNames[newTableNumber], arrayOfDescriptionFileNames[newTableNumber], arrayOfStatLists[newTableNumber]);
                    sortingAndDescriptionPanel = tableWindow.getSortingAndDescriptionPanel(arrayOfStatLists[newTableNumber]);
                    mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
                    mainFrame.getContentPane().add(BorderLayout.EAST, sortingAndDescriptionPanel);
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
                mainFrame.getContentPane().remove(sortingAndDescriptionPanel);
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
                tableNumberLabel.setText(newTableNumber + 1 + "/" + arrayOfLabels.length);
                header.setText(arrayOfLabels[newTableNumber]);
                try {
                    tableViewPanel = tableWindow.getPopulatedTablePanel(arrayOfFileNames[newTableNumber], arrayOfDescriptionFileNames[newTableNumber], arrayOfStatLists[newTableNumber]);
                    sortingAndDescriptionPanel = tableWindow.getSortingAndDescriptionPanel(arrayOfStatLists[newTableNumber]);
                    mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
                    mainFrame.getContentPane().add(BorderLayout.EAST, sortingAndDescriptionPanel);
                    mainFrame.getContentPane().add(BorderLayout.CENTER, tableViewPanel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        tableNumberLabel = new JLabel("1/" + arrayOfLabels.length);
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
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }

}
