package com.goodworkalan.permeate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities to display error messages that display the path string as it would
 * appear in Java, with Java escape sequences.
 * 
 * @author Alan Gutierrez
 */
class Messages {
    /**
     * Escape a character.
     * 
     * @param ch
     *            The character to escape.
     * @return An escaped sequence.
     */
    final static String charEscape(char ch) {
        return "'" + (ch == '\'' || ch == '\\' ? "\\" + ch : ch) + "'";
    }

    /**
     * Escape a Java string.
     * 
     * @param string
     *            The string.
     * @return The string as a Java escaped string.
     */
    final static String stringEscape(String string) {
        Pattern pattern = Pattern.compile("[\\\"\b\r\n\f\t\0\1\2\3\4\5\6\7]");
        Matcher matcher = pattern.matcher(string);
        StringBuffer newString = new StringBuffer();
        while (matcher.find()) {
            char ch = string.charAt(matcher.start());
            String replacement;
            if (ch < 8) {
                replacement = "\\\\" + (int) ch;
            } else {
                switch (ch) {
                case '\b':
                    replacement = "\\\\b";
                    break;
                case '\f':
                    replacement = "\\\\f";
                    break;
                case '\n':
                    replacement = "\\\\n";
                    break;
                case '\r':
                    replacement = "\\\\r";
                    break;
                case '\t':
                    replacement = "\\\\t";
                    break;
                default:
                    replacement = "\\\\" + ch;
                }
            }
            matcher.appendReplacement(newString, replacement);
        }
        matcher.appendTail(newString);
        return "\"" + newString.toString() + "\"";
    }
}
