import javax.swing.*;
import java.awt.*;

public class MainWindow {

    HeroWindow heroTab;
    public static void main(String[] args) throws Exception {
        MainWindow main = new MainWindow();
        main.createMainWindow();
    }

    public void createMainWindow() throws Exception {
        heroTab = new HeroWindow();

        // Creating base components of frame
        JFrame mainFrame = new JFrame();
        JPanel tableNamePanel = new JPanel();
        tableNamePanel.setPreferredSize(new Dimension(1500, 25));
        JScrollPane tableViewPanel = heroTab.createPanelForTable();
        tableViewPanel.setBackground(Color.BLUE);
        JPanel tableSortingPanel = heroTab.createPanelForSorting();
        GridBagLayout tableSelectionPanelLayout = new GridBagLayout();
        JPanel tableSelectionPanel = new JPanel(tableSelectionPanelLayout);
        tableSelectionPanel.setPreferredSize(new Dimension(1500, 40));
        tableSelectionPanel.setBackground(Color.GREEN);
        mainFrame.getContentPane().add(BorderLayout.NORTH, tableNamePanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, tableSelectionPanel);
        mainFrame.getContentPane().add(BorderLayout.EAST, tableSortingPanel);
        mainFrame.getContentPane().add(BorderLayout.CENTER, tableViewPanel);

        // Components for tableNamePanel
        JLabel tableNameLabel = new JLabel("List of Heroes");
        tableNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        tableNamePanel.add(tableNameLabel);

        // Components for tableSelectionPanel
        JButton previousTableButton = new JButton("<");
        previousTableButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        JButton nextTableButton = new JButton(">");
        nextTableButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        JLabel tableNumber = new JLabel("1");
        tableNumber.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        tableNumber.setHorizontalAlignment(SwingConstants.CENTER);

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
        tableSelectionPanelLayout.setConstraints(tableNumber, gbc);
        tableSelectionPanel.add(tableNumber);

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
