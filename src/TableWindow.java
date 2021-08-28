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
import javax.swing.border.*;

public class TableWindow {

    private Parser parser;                          // Parser to take in file and convert it to a different form
    private JTable statTable;                       // Used to contain population of data 
    private HashMap<String, String> descriptionMap; // Holds HashMap associating unique names to descriptions
    private JTextArea rowDescriptionBox;            // Text area to display description of selected row
    private JPanel tableSortingPanel;               // Will contain both sorting and description box elements
    private JTextField searchField;                 // Allow the user to enter in a string to search for names
    private GridBagLayout sortingGrid;              // Grid that will contain sorting and description box elements
    private TableRowSorter<TableModel> sorter;

    /**
     * Constructor that simply creates a parser for the files that will be used to create the table
     */
    public TableWindow() {
        parser = new Parser();
    }

    /**
     * Creates the populated table based on the stats and description file names and then returns it inside a JScrollPane
     * @param statFileName File name of the stats file
     * @param descriptionFileName File name of the description file
     * @param statNameList List of stat names that correspond 1:1 (in order) with the stat file
     * @return JScrollPane that contains the populated table
     * @throws NullPointerException Thrown when the file name parameter cannot be found
     * @throws FileNotFoundException Thrown when the file name given to the scanner is not found
     * @throws IllegalStateException Thrown if scanner is closed or tries reading a nonexistent line
     */
    public JScrollPane getPopulatedTablePanel(String statFileName, String descriptionFileName, String[] statNameList) throws NullPointerException, FileNotFoundException, IllegalStateException {
        initialiseTable(statFileName, descriptionFileName, statNameList);
        convertDescriptionFileToMap(descriptionFileName);
        updateTableVisuals();
        JScrollPane scrollPane = new JScrollPane(statTable);
        return scrollPane;
    }

    /**
     * Initialises the table with the converted file data (into an array); changes visual format of the 
     * table; add/disables some functionalities of the table
     * @param statFileName File name of the stats file
     * @param descriptionFileName File name of the description file
     * @param statNameList List of stat names that correspond 1:1 (in order) with the stat file
     * @throws NullPointerException Thrown when the file name parameter cannot be found
     * @throws FileNotFoundException Thrown when the file name given to the scanner is not found
     * @throws IllegalStateException Thrown if scanner is closed or tries reading a nonexistent line
     */
    private void initialiseTable(String statFileName, String descriptionFileName, String[] statNameList) throws NullPointerException, FileNotFoundException, IllegalStateException {
        // Creates a stat table, populated with an array created from the converted data of the stat file.
        // Also changes visuals of the table and disables editing
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
                // If there is a row in the table selected
                if (!statTable.getSelectionModel().isSelectionEmpty()) {
                    String name = (String) statTable.getValueAt(statTable.getSelectedRow(), 0);
                    // Get the value (description) of the selected key (name value of selected row)
                    rowDescriptionBox.setText(name + "\n\n" + descriptionMap.get(name));
                    // Sets scroll pane to the top of the text
                    rowDescriptionBox.setCaretPosition(0);
                }
            }
        });
    }

    /**
     * Updates the formatting of the table
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
     * Creates the sorting and description panel and returning it 
     * @param statNameList List of stat names that correspond 1:1 (in order) with the stat file
     * @return JPanel that contains sorting buttons and text area to display descriptions
     */
    public JPanel getSortDescriptionSearchPanel(String[] statNameList) {
        initialiseSortDescriptionSearchPanel();
        integrateTableSorter(statNameList);
        integrateDescriptionBox();
        integrateSearchField();
        return tableSortingPanel;
    }

    /**
     * Initialises the grid and panel, adding the grid to the panel
     */
    private void initialiseSortDescriptionSearchPanel() {
        sortingGrid = new GridBagLayout();
        tableSortingPanel = new JPanel(sortingGrid);
    }

    /**
     * Create and add the sorting portion of the sorting/description panel
     * @param statNameList List of stat names that correspond 1:1 (in order) with the stat file
     */
    private void integrateTableSorter(String[] statNameList) {
        // Create base GridBagConstraints object that will be modified as needed for each aspect of the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        // Create dummy cells so there is correct sizing for each aspect of the grid
        createDummyCells(gbc);
        // Create the panel's label/header
        JLabel tableSortingLabel = createSortDescriptionSearchPanelLabel();
        // Create the buttons for sorting and group them up in an array
        ArrayList<JButton> sortingButtons = createSortingPanelButtons(statNameList);;
        gbc.fill = GridBagConstraints.NONE;
        // Add label to grid
        addLabelToSortDescriptionSearchPanel(gbc, tableSortingLabel);
        // Add buttons to grid
        addButtonsToSortDescriptionSearchPanel(gbc, sortingButtons);
    }

    /**
     * Create and add text area for sorting/description panel
     */
    private void integrateDescriptionBox() {
        // Create GridBagConstraints to correctly place the text area in the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        // Create the panel's text area where descriptions will be displayed when the relevant row is clicked
        createSortDescriptionSearchPanelTextArea();
        // Add the text box to a scroll pane for longer descriptions
        JScrollPane descriptionBox = new JScrollPane(rowDescriptionBox);
        descriptionBox.setPreferredSize(new Dimension(290, 340));
        // Add text area to grid
        addTextAreaToSortDescriptionSearchPanel(gbc, descriptionBox);
    }

    /**
     * Create and add the search box for the sorting/description panel
     */
    private void integrateSearchField() {
        // Create GridBagConstraints to correctly place the search field in the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        // Create the panel's search field box 
        createSortDescriptionSearchPanelSearchField();
        // Add search field to grid
        addSearchFieldToSortDescriptionSearchPanel(gbc, searchField);
    }

    /**
     * Create empty dummy cells (JLabels) so that spacing of the sorting/description panel elements
     * is correct
     * @param gbc Constraints that will decide the location and size of the element within the grid
     */
    private void createDummyCells(GridBagConstraints gbc) {
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        // Create 15 dummy cells vertically (the horizontal will be covered by the buttons)
        for (int j = 0; j < 15; j++) {
            gbc.gridy = j;
            JLabel lbl = new JLabel("");
            lbl.setPreferredSize(new Dimension(150, 50));
            sortingGrid.setConstraints(lbl, gbc);
            tableSortingPanel.add(lbl, gbc);
        }
    }

    /**
     * Create label to signify the panel's function, and returns it
     * @return JLabel that says what the panel does
     */
    private JLabel createSortDescriptionSearchPanelLabel() {
        // Create the descriptive label (that the panel is for sorting)
        JLabel tableSortingLabel = new JLabel("Sort by:");
        tableSortingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableSortingLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        return tableSortingLabel;
    }

    /**
     * Create the text area for the sorting/description panel, with default text to guide the user
     */
    private void createSortDescriptionSearchPanelTextArea() {
        // Create empty JTextArea that will display info on currently clicked row (if any)
        rowDescriptionBox = new JTextArea("Select a row to view its description");
        rowDescriptionBox.setMargin(new Insets(10,10,10,10));
        rowDescriptionBox.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        rowDescriptionBox.setWrapStyleWord(true);
        rowDescriptionBox.setLineWrap(true);
        rowDescriptionBox.setEditable(false);
        rowDescriptionBox.setFocusable(false);
    }

    /**
     * Create sorting buttons with relevant functionality
     * @param statNameList List of stat names that correspond 1:1 (in order) with the stat file
     * @return ArrayList of the functional buttons
     */
    private ArrayList<JButton> createSortingPanelButtons(String[] statNameList) {
        // Create the sorting element that will reorder the rows of the table
        sorter = new TableRowSorter<TableModel>(statTable.getModel());
        statTable.setRowSorter(sorter);

        // Create the sorting buttons that will utilise the sorting
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
                        // If stat name and button name match, add the stat name index as a sort key and the direction (asc/des)
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
                            // By default, sort ascending by first column (Name) as that is always unique
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

    /**
     * Create the search box for the sorting/description panel, with placeholder text to guide the user
     */
    private void createSortDescriptionSearchPanelSearchField() {
        searchField = new JTextField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(Color.GRAY);
                    g2.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
                    g2.drawString("Search", 10, 25); 
                }
            }
        };
        searchField.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(290, 40));
        searchField.setBorder(new CompoundBorder(new LineBorder(new Color(153, 153, 153), 1), new EmptyBorder(10,10,10,10)));
        searchField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent d) {
                RowFilter<TableModel, Integer> rf = null;
                //If current expression doesn't parse, don't update.
                try {
                    rf = RowFilter.regexFilter("(?i)" + searchField.getText(), 0);
                } catch (java.util.regex.PatternSyntaxException e) {
                    return;
                }
                sorter.setRowFilter(rf);
            }
            @Override
            public void changedUpdate(DocumentEvent d) {}
            @Override
            public void removeUpdate(DocumentEvent d) {
                RowFilter<TableModel, Integer> rf = null;
                //If current expression doesn't parse, don't update.
                try {
                    rf = RowFilter.regexFilter("(?i)" + searchField.getText(), 0);
                } catch (java.util.regex.PatternSyntaxException e) {
                    return;
                }
                sorter.setRowFilter(rf);
            }
        });
    }
    
    /**
     * Adds the label to the grid of the sorting/description panel
     * @param gbc Constraints that will decide the location and size of the element within the grid
     * @param tableSortingLabel JLabel that describes the functionality of the buttons
     */
    private void addLabelToSortDescriptionSearchPanel(GridBagConstraints gbc, JLabel tableSortingLabel) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        sortingGrid.setConstraints(tableSortingLabel, gbc);
        tableSortingPanel.add(tableSortingLabel);
    }

    /**
     * Adds the sorting buttons to the grid of the sorting/description panel
     * @param gbc Constraints that will decide the location and size of the element within the grid
     * @param sortingButtons
     */
    private void addButtonsToSortDescriptionSearchPanel(GridBagConstraints gbc, ArrayList<JButton> sortingButtons) {
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

    /**
     * Adds the text area to the grid of the sorting/description panel
     * @param gbc Constraints that will decide the location and size of the element within the grid
     * @param descriptionBox JTextArea that will contain the description of the row clicked on by the user
     */
    private void addTextAreaToSortDescriptionSearchPanel(GridBagConstraints gbc, JScrollPane descriptionBox) {
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridheight = 7;
        sortingGrid.setConstraints(descriptionBox, gbc);
        tableSortingPanel.add(descriptionBox);
    }

    /**
     * Adds the search box to the grid of the sorting/description panel
     * @param gbc Constraints that will decide the location and size of the element within the grid
     * @param searchField JTextField that the user can type into to search through the table
     */
    private void addSearchFieldToSortDescriptionSearchPanel(GridBagConstraints gbc, JTextField searchField) {
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        sortingGrid.setConstraints(searchField, gbc);
        tableSortingPanel.add(searchField);
    }
    
    /**
     * Parses the given file into a String[][] where outer array is number of entries in file, and inner array is 
     * number of stat names (i.e., outer will be row, inner will be column)
     * @param statFileName File name of the stats file
     * @param statNameList List of stat names that correspond 1:1 (in order) with the stat file
     * @return String[][] that contains parsed file data 
     * @throws NullPointerException Thrown when the file name parameter cannot be found
     * @throws FileNotFoundException Thrown when the file name given to the scanner is not found
     * @throws IllegalStateException Thrown if scanner is closed or tries reading a nonexistent line
     */
    private String[][] getFileContentsAsNestedArray(String statFileName, String[] statNameList) throws NullPointerException, FileNotFoundException, IllegalStateException {
        return parser.parseStatFileIntoArray(statFileName, statNameList);
    }

    /**
     * Parses the given file into HashMap where key corresponds to unique name that can be associated with data 
     * from the other corresponding file, and value is the description
     * @param descriptionFileName File name of the description file
     */
    private void convertDescriptionFileToMap(String descriptionFileName) {
        descriptionMap = parser.parseDescriptionFileIntoMap(descriptionFileName);
    } 

    /**
     * Adjusts the table column widths to allow for each column to fit its data on the screen
     * Taken from internet (https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths)
     * @param table Table that needs its column widths to be adjusted.
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