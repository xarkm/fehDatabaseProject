package src;

import java.io.File;
import java.util.Scanner;
import java.lang.NullPointerException;
import java.lang.IllegalStateException;
import java.io.FileNotFoundException;

public class Parser {

    /**
     * Takes file and creates an array list for each line in it.
     * @param fileName Name of the file to be parsed
     * @param statNames ArrayList of all stat names
     * @return String[][], with each entry corresponding to one line in the file
     * @throws NullPointerException Thrown when the file name parameter cannot be found
     * @throws FileNotFoundException Thrown when the file name given to the scanner is not found
     * @throws IllegalStateException Thrown if scanner is closed
     */
    public String[][] parseTextIntoArray(String fileName, String[] statNames) throws NullPointerException, FileNotFoundException, IllegalStateException {
        File heroFile = null;
        // Try to update heroFile with the file to be parsed
        try {
            heroFile = new File(fileName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Scanner scannerCounter = null;
        Scanner scanner = null;
        int numberOfLines = 0;
        // Try to scan the heroFile with the scanner
        try {
            scannerCounter = new Scanner(heroFile);
            scanner = new Scanner(heroFile);
            numberOfLines = getNumberOfLines(scannerCounter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[][] heroStatsArray = new String[numberOfLines][statNames.length];
        try {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String[] individualHeroStats = scanner.nextLine().split(",");
                for (int i = 0; i < statNames.length; i++) {
                    heroStatsArray[lineNumber][i] = individualHeroStats[i];
                }
                lineNumber++;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        return heroStatsArray;
    }

    /**
     * Count number of lines in the file.
     * @param scanner The scanner with the file whose lines will be checked
     * @return Number of lines in the file
     */
    public int getNumberOfLines(Scanner scanner) {
        int count = 0;
        while (scanner.hasNextLine()) {
            count++;
            scanner.nextLine();
        }
        return count;
    }

}
