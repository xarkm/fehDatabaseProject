package src;

import java.io.File;
import java.util.Scanner;
import java.lang.NullPointerException;
import java.lang.IllegalStateException;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Parser {

    /**
     * Takes file and creates an array list for each line in it. Splits each line using ','.
     * @param fileName Name of the file to be parsed
     * @param statNames ArrayList of all stat names
     * @return String[][], with each entry corresponding to one line in the file
     * @throws NullPointerException Thrown when the file name parameter cannot be found
     * @throws FileNotFoundException Thrown when the file name given to the scanner is not found
     * @throws IllegalStateException Thrown if scanner is closed
     */
    public String[][] parseStatFileIntoArray(String fileName, String[] statNames) {
        File file = null;
        // Try to update file with the file to be parsed
        try {
            file = new File(fileName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Scanner scannerCounter = null;
        Scanner scanner = null;
        int numberOfLines = 0;
        // Try to scan the file with the scanner
        try {
            scannerCounter = new Scanner(file);
            scanner = new Scanner(file);
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

    /**
     * Takes file and creates a hash map for each line in the file. Splits each line on the first ',', using the
     * first part as the key and the second part as the value.
     * @param fileName Name of the file to be converted into a hash map
     * @return hash map with name as the key and description as the value
     */
    public HashMap<String, String> parseDescriptionFileIntoMap(String fileName) {
        File file = null;
        // Try to update file with the file to be parsed
        try {
            file = new File(fileName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Scanner scanner = null;
        // Try to scan the file with the scanner
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, String> entryToDescription = new HashMap<>();
        try {
            while (scanner.hasNextLine()) {
                String[] individualHeroStats = scanner.nextLine().split(",", 2);
                entryToDescription.put(individualHeroStats[0], individualHeroStats[1]);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        return entryToDescription;
    }
}
