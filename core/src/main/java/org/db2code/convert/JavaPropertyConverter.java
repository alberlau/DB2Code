package org.db2code.convert;

public class JavaPropertyConverter {
    public static String camelCaseFromSnakeCaseInitCap(String columnName) {
        return camelCaseFromSnakeCase(columnName, true);
    }

    public static String camelCaseFromSnakeCaseInitLow(String columnName) {
        return camelCaseFromSnakeCase(columnName, false);
    }

    private static String camelCaseFromSnakeCase(String columnName, boolean initCap) {
        String[] words = columnName.split("[\\W_]");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word == null || word.isBlank()) {
                continue;
            }
            if (i == 0) {
                if (initCap) {
                    sb.append(Character.toUpperCase(word.charAt(0)));
                    sb.append(word.substring(1).toLowerCase());
                } else {
                    sb.append(word.toLowerCase());
                }
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
}
