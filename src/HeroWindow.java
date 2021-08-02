import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class HeroWindow {

    private Parser parser;
    private String[] statNameList = {"Name", "Colour", "Weapon", "Movement", "HP", "Atk", "Spd", "Def", "Res", "Game"};
    private JTable heroStatTable;

    public HeroWindow() {
        parser = new Parser();
    }

    public JScrollPane createPanelForTable() throws Exception {
        heroStatTable = new JTable(getHeroStatArray("src/feh-heroes.txt"), statNameList);
        heroStatTable.setRowHeight(20);
        heroStatTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        resizeColumnWidth(heroStatTable);
        JScrollPane scrollPane = new JScrollPane(heroStatTable);
        return scrollPane;
    }

    public JPanel createPanelForSorting() {
        JPanel tableSortingPanel = new JPanel(new GridLayout(11, 1));
        tableSortingPanel.setPreferredSize(new Dimension(300, 800));
        tableSortingPanel.setBackground(Color.RED);

        JLabel tableSortingLabel = new JLabel("Sort by:");
        tableSortingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableSortingLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        // JButton sortByNameButton = new JButton("Name");
        // JButton sortByColourButton = new JButton("Colour");
        // JButton sortByWeaponButton = new JButton("Weapon");
        // JButton sortByMovementButton = new JButton("Movement");
        // JButton sortByHPButton = new JButton("HP");
        // JButton sortByAtkButton = new JButton("Atk");
        // JButton sortBySpdButton = new JButton("Spd");
        // JButton sortByDefButton = new JButton("Def");
        // JButton sortByResButton = new JButton("Res");
        // JButton sortByGameButton = new JButton("Game");

        ArrayList<JButton> sortingButtons = new ArrayList<>();
        for (int i = 0; i < statNameList.length; i++) {
            JButton btn = new JButton(statNameList[i]);
            btn.setName(statNameList[i]);
            sortingButtons.add(btn);
        }
        // sortingButtons.add(sortByNameButton);
        // sortingButtons.add(sortByColourButton);
        // sortingButtons.add(sortByWeaponButton);
        // sortingButtons.add(sortByMovementButton);
        // sortingButtons.add(sortByHPButton);
        // sortingButtons.add(sortByAtkButton);
        // sortingButtons.add(sortBySpdButton);
        // sortingButtons.add(sortByDefButton);
        // sortingButtons.add(sortByResButton);
        // sortingButtons.add(sortByGameButton);

        tableSortingPanel.add(tableSortingLabel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(heroStatTable.getModel());
        heroStatTable.setRowSorter(sorter);
        
        for (int i = 0; i < sortingButtons.size(); i++) {
            sortingButtons.get(i).setFont(new Font("Lucida Grande", Font.PLAIN, 16));
            tableSortingPanel.add(sortingButtons.get(i));
        }

        // sortByNameButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByColourButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByWeaponButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByMovementButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByHPButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByAtkButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortBySpdButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByDefButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(7, SortOrder.DESCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByResButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(8, SortOrder.DESCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        // sortByGameButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        //         sortKeys.add(new RowSorter.SortKey(9, SortOrder.ASCENDING));
        //         sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //         sorter.setSortKeys(sortKeys);
        //     }
        // });
        for (JButton btn : sortingButtons) {
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                    int i = 0;
                    boolean foundMatchingColumn = false;
                    while (!foundMatchingColumn && i < statNameList.length) {
                        System.out.println(statNameList[i]);
                        System.out.println(btn.getName());
                        if (statNameList[i].equals(btn.getName())) {
                            foundMatchingColumn = true;
                            sortKeys.add(new RowSorter.SortKey(i, SortOrder.ASCENDING));
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

    private String[][] getHeroStatArray(String fileName) throws Exception{
        return parser.parseTextIntoArray(fileName, statNameList);
    }

    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

}
