import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TableWindow {

    private Parser parser;
    private JTable heroStatTable;

    public TableWindow() {
        parser = new Parser();
    }

    public JScrollPane createPanelForTable(String heroFileName, String[] statNameList) throws Exception {
        heroStatTable = new JTable(getFileContentsAsNestedArray(heroFileName, statNameList), statNameList);
        heroStatTable.setRowHeight(20);
        heroStatTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        resizeColumnWidth(heroStatTable);
        JScrollPane scrollPane = new JScrollPane(heroStatTable);
        return scrollPane;
    }

    public JPanel createPanelForSorting(String[] statNameList) {
        // Create the housing panel
        JPanel tableSortingPanel = new JPanel(new GridLayout(11, 1));
        tableSortingPanel.setPreferredSize(new Dimension(300, 800));
        // Create the descriptive label (that the panel is for sorting)
        JLabel tableSortingLabel = new JLabel("Sort by:");
        tableSortingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableSortingLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        // Create the buttons for sorting and group them up in an array
        ArrayList<JButton> sortingButtons = new ArrayList<>();
        for (int i = 0; i < statNameList.length; i++) {
            JButton btn = new JButton(statNameList[i]);
            btn.setName(statNameList[i]);
            sortingButtons.add(btn);
        }
        // Add the label and buttons to the panel
        tableSortingPanel.add(tableSortingLabel);
        for (JButton btn : sortingButtons) {
            btn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
            tableSortingPanel.add(btn);
        }
        // Create the sorting element that will reorder the rows of the table
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(heroStatTable.getModel());
        heroStatTable.setRowSorter(sorter);

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
        return parser.parseTextIntoArray(fileName, statNameList);
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
