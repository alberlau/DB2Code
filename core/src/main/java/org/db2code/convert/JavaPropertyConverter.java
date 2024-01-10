package org.db2code.convert;

import java.util.HashSet;
import java.util.Set;

public final class JavaPropertyConverter {

    private static final Set<String> reservedKeywords = new HashSet<>();

    static {
        // Add all reserved keywords to the set
        reservedKeywords.add("abstract");
        reservedKeywords.add("assert");
        reservedKeywords.add("boolean");
        reservedKeywords.add("break");
        reservedKeywords.add("byte");
        reservedKeywords.add("case");
        reservedKeywords.add("catch");
        reservedKeywords.add("char");
        reservedKeywords.add("class");
        // 'const' is a reserved word, but not used
        reservedKeywords.add("continue");
        reservedKeywords.add("default");
        reservedKeywords.add("do");
        reservedKeywords.add("double");
        reservedKeywords.add("else");
        reservedKeywords.add("enum");
        reservedKeywords.add("exports"); // Added in Java 9
        reservedKeywords.add("extends");
        reservedKeywords.add("final");
        reservedKeywords.add("finally");
        reservedKeywords.add("float");
        reservedKeywords.add("for");
        // 'goto' is a reserved word, but not used
        reservedKeywords.add("if");
        reservedKeywords.add("implements");
        reservedKeywords.add("import");
        reservedKeywords.add("instanceof");
        reservedKeywords.add("int");
        reservedKeywords.add("interface");
        reservedKeywords.add("long");
        reservedKeywords.add("module"); // Added in Java 9
        reservedKeywords.add("native");
        reservedKeywords.add("new");
        reservedKeywords.add("open"); // Added in Java 9
        reservedKeywords.add("package");
        reservedKeywords.add("private");
        reservedKeywords.add("protected");
        reservedKeywords.add("public");
        reservedKeywords.add("requires"); // Added in Java 9
        reservedKeywords.add("return");
        reservedKeywords.add("short");
        reservedKeywords.add("static");
        reservedKeywords.add("strictfp");
        reservedKeywords.add("super");
        reservedKeywords.add("switch");
        reservedKeywords.add("synchronized");
        reservedKeywords.add("this");
        reservedKeywords.add("throw");
        reservedKeywords.add("throws");
        reservedKeywords.add("transient");
        reservedKeywords.add("try");
        reservedKeywords.add("var"); // Added in Java 10
        reservedKeywords.add("void");
        reservedKeywords.add("volatile");
        reservedKeywords.add("while");
        reservedKeywords.add("yield"); // Added in Java 14
        reservedKeywords.add("record"); // Added in Java 16
        reservedKeywords.add("sealed"); // Added in Java 17
        reservedKeywords.add("non-sealed"); // Added in Java 17
        reservedKeywords.add("permits"); // Added in Java 17
    }

    private JavaPropertyConverter() {
        // Utility class
    }

    public static String camelCaseFromSnakeCaseInitCap(String columnName) {
        return camelCaseFromSnakeCase(columnName, true);
    }

    public static String camelCaseFromSnakeCaseInitLow(String columnName) {
        return camelCaseFromSnakeCase(columnName, false);
    }

    private static String camelCaseFromSnakeCase(String columnName, boolean initCap) {
        if (reservedKeywords.contains(columnName.toLowerCase())) {
            columnName = columnName + "0";
        }
        if (!isValidStartingCharacter(columnName)) {
            columnName = "x_" + columnName;
        }
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

    private static boolean isValidStartingCharacter(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        Character firstChar = name.charAt(0);
        return Character.isLetter(firstChar) || firstChar.equals('_') || firstChar.equals('$');
    }
}
