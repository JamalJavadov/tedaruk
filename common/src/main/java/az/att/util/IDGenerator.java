package az.att.util;

public class IDGenerator {

    /**
     * Normalizes a string by:
     * - Removing all special characters (except spaces and alphanumerics)
     * - Replacing spaces with hyphens
     * - Lowercasing all characters
     * - Truncating to 128 characters
     *
     * @param input the input string
     * @return normalized string
     */
    public static String normalizeString(String input) {
        if (input == null) {
            return null;
        }

        // Remove all special characters except spaces and alphanumerics
        String cleaned = input.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Replace spaces with hyphens
        String hyphenated = cleaned.trim().replaceAll("\\s+", "-");

        // Convert to lowercase
        String lowercased = hyphenated.toLowerCase();

        // Truncate to 128 characters
        return lowercased.length() > 128
                ? lowercased.substring(0, 128)
                : lowercased;
    }
}
