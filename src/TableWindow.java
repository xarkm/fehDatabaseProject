package src;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class TableWindow {

    private Parser parser;
    private JTable statTable;
    private HashMap<String, String> descriptionMap;
    private JTextArea rowDescriptionLabel;

    public TableWindow() {
        parser = new Parser();
    }

    public JScrollPane createPanelForTable(String statFileName, String descriptionFileName, String[] statNameList) throws Exception {
        convertDescriptionFileToMap(descriptionFileName);
        statTable = new JTable(getFileContentsAsNestedArray(statFileName, statNameList), statNameList) {
            // Disable editing of the table
            public boolean isCellEditable(int row, int column) {
                return false;
            }

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
        statTable.setRowHeight(32);
        statTable.getColumnModel().setColumnMargin(0);
        statTable.getTableHeader().setOpaque(false);
        statTable.getTableHeader().setEnabled(false);
        statTable.getTableHeader().setBackground(new Color(180,180,180));
        statTable.getTableHeader().setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        statTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        resizeColumnWidth(statTable);
        JScrollPane scrollPane = new JScrollPane(statTable);
        return scrollPane;
    }

    public JPanel createPanelForSorting(String[] statNameList) {
        GridBagLayout sortingGrid = new GridBagLayout();
        JPanel tableSortingPanel = new JPanel(sortingGrid);

        GridBagConstraints gbc = new GridBagConstraints();
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
        gbc.fill = GridBagConstraints.NONE;

        // Create the descriptive label (that the panel is for sorting)
        JLabel tableSortingLabel = new JLabel("Sort by:");
        tableSortingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableSortingLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

        // Create empty JTextArea that will display info on currently clicked row (if any)
        rowDescriptionLabel = new JTextArea("Select a row to view its description");
        rowDescriptionLabel.setMargin( new Insets(10,10,10,10) );
        rowDescriptionLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        rowDescriptionLabel.setWrapStyleWord(true);
        rowDescriptionLabel.setLineWrap(true);
        rowDescriptionLabel.setEditable(false);
        rowDescriptionLabel.setFocusable(false);
        JScrollPane descriptionBox = new JScrollPane(rowDescriptionLabel);
        descriptionBox.setPreferredSize(new Dimension(290, 440));
        
        // Create the buttons for sorting and group them up in an array
        ArrayList<JButton> sortingButtons = new ArrayList<>();
        for (int i = 0; i < statNameList.length; i++) {
            JButton btn = new JButton(statNameList[i]);
            btn.setPreferredSize(new Dimension(150, 50));
            btn.setName(statNameList[i]);
            sortingButtons.add(btn);
        }

        // Add label to grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        sortingGrid.setConstraints(tableSortingLabel, gbc);
        tableSortingPanel.add(tableSortingLabel);

        // Add buttons to grid
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

        // Add empty JLabel
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        sortingGrid.setConstraints(descriptionBox, gbc);
        tableSortingPanel.add(descriptionBox);

        // Create the sorting element that will reorder the rows of the table
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(statTable.getModel());
        statTable.setRowSorter(sorter);

        // Add an action listener to each button
        for (JButton btn : sortingButtons) {
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
        }

        return tableSortingPanel;
    }

    private String[][] getFileContentsAsNestedArray(String fileName, String[] statNameList) throws Exception {
        return parser.parseStatFileIntoArray(fileName, statNameList);
    }

    private void convertDescriptionFileToMap(String fileName) {
        descriptionMap = parser.parseDescriptionFileIntoMap(fileName);
    } 

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
