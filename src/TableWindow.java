package src;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.NullPointerException;
import java.lang.IllegalStateException;
import java.io.FileNotFoundException;

public class TableWindow {

    private Parser parser;
    private JTable statTable;
    private HashMap<String, String> descriptionMap;
    private JTextArea rowDescriptionLabel;
    private JPanel tableSortingPanel;
    private GridBagLayout sortingGrid;

    public TableWindow() {
        parser = new Parser();
    }

    /**
     * 
     * @param statFileName
     * @param descriptionFileName
     * @param statNameList
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IllegalStateException
     */
    private void initialiseTable(String statFileName, String descriptionFileName, String[] statNameList) throws NullPointerException, FileNotFoundException, IllegalStateException {
        statTable = new JTable(getFileContentsAsNestedArray(statFileName, statNameList), statNameList) {
            // Disable editing of the table
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            // Change renderer to have alternating colours for each row
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                Color color1 = new Color(230,230,230);
                Color color2 = Color.WHITE;
                if(!c.getBackground().equals(getSelectionBackground())) {
                    Color coleur = (row % 2 == 0 ? color1 : color2);
                    c.setBackground(coleur);
                    coleur = null;
                }
                return c;
            }
        };
        statTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            // Add event listener to display the description for the selected row in the small info window
            public void valueChanged(ListSelectionEvent event) {
                if (!statTable.getSelectionModel().isSelectionEmpty()) {
                    String name = (String) statTable.getValueAt(statTable.getSelectedRow(), 0);
                    // Get the value (description) of the selected key (name value of selected row)
                    rowDescriptionLabel.setText(name + "\n\n" + descriptionMap.get(name));
                    // Sets scroll pane to the top of the text
                    rowDescriptionLabel.setCaretPosition(0);
                }
            }
        });
    }

    /**
     * 
     */
    private void updateTableVisuals() {
        statTable.setRowHeight(32);
        statTable.getColumnModel().setColumnMargin(0);
        statTable.getTableHeader().setOpaque(false);
        statTable.getTableHeader().setEnabled(false);
        statTable.getTableHeader().setBackground(new Color(180,180,180));
        statTable.getTableHeader().setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        statTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        resizeColumnWidth(statTable);
    }

    /**
     * 
     * @return
     */
    public JScrollPane getPopulatedTablePanel(String statFileName, String descriptionFileName, String[] statNameList) throws NullPointerException, FileNotFoundException, IllegalStateException {
        initialiseTable(statFileName, descriptionFileName, statNameList);
        convertDescriptionFileToMap(descriptionFileName);
        updateTableVisuals();
        JScrollPane scrollPane = new JScrollPane(statTable);
        return scrollPane;
    }

    
    /**
     * 
     * @param statNameList
     * @return
     */
    public JPanel getSorterAndDescriptionPanel(String[] statNameList) {
        initialiseSortingAndDescriptionPanel();
        createTableSorter(statNameList);
        createDescriptionBox();
        return tableSortingPanel;
    }

    private void initialiseSortingAndDescriptionPanel() {
        sortingGrid = new GridBagLayout();
        tableSortingPanel = new JPanel(sortingGrid);
    }

    private void createTableSorter(String[] statNameList) {
        // Create base GridBagConstraints object that will be modified as needed for each aspect of the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        // Create dummy cells so there is correct sizing for each aspect of the grid
        createDummyCells(gbc);
        // Create the panel's label/header
        JLabel tableSortingLabel = createSortingPanelLabel();
        // Create the buttons for sorting and group them up in an array
        ArrayList<JButton> sortingButtons = createSortingPanelButtons(statNameList);;
        gbc.fill = GridBagConstraints.NONE;
        // Add label to grid
        addLabelToSortingPanel(gbc, tableSortingLabel);
        // Add buttons to grid
        addButtonsToSortingPanel(gbc, sortingButtons);
    }

    private void createDescriptionBox() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        // Create the panel's text area where descriptions will be displayed when the relevant row is clicked
        createSortingPanelTextArea();
        // Add the text box to a scroll pane for longer descriptions
        JScrollPane descriptionBox = new JScrollPane(rowDescriptionLabel);
        descriptionBox.setPreferredSize(new Dimension(290, 440));
        // Add text area to grid
        addTextAreaToSortingPanel(gbc, descriptionBox);
    }

    private void createDummyCells(GridBagConstraints gbc) {
        gbc = new GridBagConstraints();
        // Creating dummy panels so that the GridBagLayout actually spaces the cells correctly
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        for (int j = 0; j < 15; j++) {
            gbc.gridy = j;
            JLabel btn = new JLabel("");
            btn.setBackground(Color.GREEN);
            btn.setPreferredSize(new Dimension(150, 50));
            sortingGrid.setConstraints(btn, gbc);
            tableSortingPanel.add(btn, gbc);
        }
    }

    private JLabel createSortingPanelLabel() {
        // Create the descriptive label (that the panel is for sorting)
        JLabel tableSortingLabel = new JLabel("Sort by:");
        tableSortingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableSortingLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        return tableSortingLabel;
    }

    private void createSortingPanelTextArea() {
        // Create empty JTextArea that will display info on currently clicked row (if any)
        rowDescriptionLabel = new JTextArea("Select a row to view its description");
        rowDescriptionLabel.setMargin( new Insets(10,10,10,10) );
        rowDescriptionLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        rowDescriptionLabel.setWrapStyleWord(true);
        rowDescriptionLabel.setLineWrap(true);
        rowDescriptionLabel.setEditable(false);
        rowDescriptionLabel.setFocusable(false);
    }

    private ArrayList<JButton> createSortingPanelButtons(String[] statNameList) {
        // Create the sorting element that will reorder the rows of the table
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(statTable.getModel());
        statTable.setRowSorter(sorter);

        // Create the sorting buttons that will utilise the sorter
        ArrayList<JButton> sortingButtons = new ArrayList<>();
        for (int i = 0; i < statNameList.length; i++) {
            JButton btn = new JButton(statNameList[i]);
            btn.setPreferredSize(new Dimension(150, 50));
            btn.setName(statNameList[i]);
            // Add action listener to each button to sort correctly and update the text fields for the 
            // relevant buttons
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                    int i = 0;
                    boolean foundMatchingColumn = false;
                    // Check for the column that matches each button's name so each button sorts a different column
                    while (!foundMatchingColumn && i < statNameList.length) {
                        if (statNameList[i].equals(btn.getName())) {
                            foundMatchingColumn = true;
                            // If same button was the previous one clicked, then switch between ascending and descending sorting
                            if (btn.getText().equals(btn.getName() + " (Asc)")) {
                                sortKeys.add(new RowSorter.SortKey(i, SortOrder.DESCENDING));
                                btn.setText(btn.getName() + " (Des)");
                            }
                            else if (btn.getText().equals(btn.getName() + " (Des)")) {
                                sortKeys.add(new RowSorter.SortKey(i, SortOrder.ASCENDING));
                                btn.setText(btn.getName() + " (Asc)");
                            }
                            // If new button is clicked, reset the names of all the other buttons and update the name of the clicked button
                            else {
                                btn.setText(btn.getName() + " (Asc)");
                                sortKeys.add(new RowSorter.SortKey(i, SortOrder.ASCENDING));
                            }
                            for (JButton otherBtn : sortingButtons) {
                                if (otherBtn != btn) {
                                    otherBtn.setText(otherBtn.getName());
                                }
                            }
                            // By default, sort by first column (Name) as that is always unique
                            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                            sorter.setSortKeys(sortKeys);
                        }
                        else {
                            i++;
                        }
                    }
                }
            });
            sortingButtons.add(btn);
        }
        return sortingButtons;
    }

    private void addLabelToSortingPanel(GridBagConstraints gbc, JLabel tableSortingLabel) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        sortingGrid.setConstraints(tableSortingLabel, gbc);
        tableSortingPanel.add(tableSortingLabel);
    }

    private void addButtonsToSortingPanel(GridBagConstraints gbc, ArrayList<JButton> sortingButtons) {
        int x = 0;
        int y = 1;
        gbc.gridwidth = 1;
        for (int i = 0; i < sortingButtons.size(); i++) {
            gbc.gridx = x;
            gbc.gridy = y;
            sortingButtons.get(i).setFont(new Font("Lucida Grande", Font.PLAIN, 14));
            sortingGrid.setConstraints(sortingButtons.get(i), gbc);
            tableSortingPanel.add(sortingButtons.get(i));
            // increment the x position, wrapping round to 0 when needed
            x = (x + 1) % 2;
            // increment the y position, only if the x position has wrapped around
            if (x == 0) {
                y++;
            }
        }
    }

    private void addTextAreaToSortingPanel(GridBagConstraints gbc, JScrollPane descriptionBox) {
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        sortingGrid.setConstraints(descriptionBox, gbc);
        tableSortingPanel.add(descriptionBox);
    }
    /**
     * 
     * @param fileName
     * @param statNameList
     * @return
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IllegalStateException
     */
    private String[][] getFileContentsAsNestedArray(String fileName, String[] statNameList) throws NullPointerException, FileNotFoundException, IllegalStateException {
        return parser.parseStatFileIntoArray(fileName, statNameList);
    }

    /**
     * 
     * @param fileName
     */
    private void convertDescriptionFileToMap(String fileName) {
        descriptionMap = parser.parseDescriptionFileIntoMap(fileName);
    } 

    /**
     * 
     * @param table
     */
    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

}
