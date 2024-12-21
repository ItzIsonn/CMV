package me.itzisonn_.meazy;

import me.itzisonn_.meazy.parser.ast.DataType;
import me.itzisonn_.meazy.registry.Registries;
import me.itzisonn_.meazy.registry.RegistryEntry;
import me.itzisonn_.meazy.runtime.values.clazz.ClassValue;
import me.itzisonn_.meazy.runtime.values.clazz.DefaultClassValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Utility class
 */
public class Utils {
    private Utils() {}

    /**
     * Regex used by all identifiers
     */
    public static final String IDENTIFIER_REGEX = "[a-zA-Z_][a-zA-Z0-9_]*";

    /**
     * System.in Scanner
     */
    public static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Counts number of target's matches in given string
     *
     * @param string The string to count in
     * @param target The target to match
     * @return Number of target's matches in given string
     */
    public static int countMatches(String string, String target) {
        if (string == null || target == null) throw new NullPointerException("Neither of strings can't be null!");
        return (string.length() - string.replace(target, "").length()) / target.length();
    }

    /**
     * Returns extension of given file
     *
     * @param file Target file
     * @return Extension of file
     * @throws NullPointerException When given file is null
     */
    public static String getExtension(File file) throws NullPointerException {
        if (file == null) throw new NullPointerException("File can't be null!");
        String name = file.getName();

        int i = name.lastIndexOf('.');
        if (i > 0) {
            return name.substring(i + 1);
        }
        return "";
    }

    /**
     * Returns lines of given file
     *
     * @param file Target file
     * @return Lines of file
     * @throws NullPointerException When given file is null
     */
    public static String getLines(File file) throws NullPointerException {
        if (file == null) throw new NullPointerException("File can't be null!");

        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();

            while (line != null) {
                lines.add(line.trim());
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder string = new StringBuilder();
        for (String line : lines) {
            string.append(line).append("\n");
        }
        return string.toString();
    }

    /**
     * Compares to versions in format 'a.b.c...'
     *
     * @param version1 Version to compare
     * @param version2 Version to compare
     * @return True if version2 was released after version1, otherwise false
     * @throws NullPointerException When either of versions is null
     * @throws NumberFormatException When either of version have non-integer part
     */
    public static boolean isVersionAfter(String version1, String version2) throws NullPointerException, NumberFormatException {
        if (version1 == null || version2 == null) throw new NullPointerException("Neither of versions can't be null.");

        String[] split1 = version1.split("\\.");
        String[] split2 = version2.split("\\.");
        for (int i = 0; i < Math.max(split1.length, split2.length); i++) {
            int part1, part2;
            try {
                part1 = i < split1.length ? Integer.parseInt(split1[i]) : 0;
                part2 = i < split2.length ? Integer.parseInt(split2[i]) : 0;
            }
            catch (NumberFormatException ignore) {
                throw new IllegalArgumentException("Version parts must be integers");
            }
            if (part1 < part2) return true;
            if (part1 > part2) return false;
        }
        return false;
    }

    /**
     * Returns offset represented by a string
     *
     * @param offset Number of offsets
     * @return String offset
     * @throws IllegalArgumentException When given offset is negative
     */
    public static String getOffset(int offset) throws IllegalArgumentException {
        if (offset < 0) throw new IllegalArgumentException("Offset can't be negative.");

        return "\t".repeat(offset);
    }

    /**
     * Returns existing DataType or creates new that matches classes with given name
     *
     * @param dataType DataType's name
     * @return Existing or created DataType
     * @throws NullPointerException When given DataType's name is null
     */
    public static DataType parseDataType(String dataType) throws NullPointerException {
        if (dataType == null) throw new NullPointerException("DataType's name can't be null");

        for (RegistryEntry<DataType> entry : Registries.DATA_TYPE.getEntries()) {
            if (entry.getValue().getName().equals(dataType)) {
                return entry.getValue();
            }
        }

        return new DataType(dataType) {
            public boolean isMatches(Object value) {
                if (value instanceof ClassValue classValue) return classValue.getId().equals(dataType);
                if (value instanceof DefaultClassValue defaultClassValue) return defaultClassValue.getId().equals(dataType);
                return false;
            }
        };
    }
}