package com.goodworkalan.permeate;

/**
 * The regular expressions that define the graph language.
 * 
 * @author Alan Gutierrez
 */
public class Patterns {
    /**
     * Creates a regular expression that matches a Java identifier. If capture
     * is true, the regular expression surrounds the identifier with parenthesis
     * to capture the identifier.
     * 
     * @param capture
     *            Capture the identifier if true.
     * @return A regular expression that matches a DSPL/Java identifier.
     */
    public static String identifier(boolean capture) {
        return
            (capture ? "(" : "") +  
                "[$_\\w&&[^\\d]][$_\\w\\d]*" +
            (capture ? ")" : "");
    }

    /**
     * Create a regular expression that matches a numeric index.If capture is
     * true, the regular expression surrounds the value of the index with
     * parenthesis to capture the index value.
     * 
     * @param capture
     *            Capture the index value if true.
     * @return A regular expression that matches a numeric index.
     */
    public static String listIndex(boolean capture) {
        return
            "\\[\\s*" +
                (capture ? "(" : "") +
                    "-?\\d+" +
                (capture ? ")" : "") +
            "\\s*\\]";
    }

    /**
     * Create a regular expression that matches a wildcard index. If capture is
     * true, the regular expression surrounds the value of the index with
     * parenthesis to capture the index value.
     * 
     * @param capture
     *            Capture the index value if true.
     * @return A regular expression that matches a DSPL/Java numeric index.
     */
    public static String globIndex(boolean capture) {
        return
            "\\[\\s*" +
                (capture ? "(" : "") +
                    "\\*" +
                (capture ? ")": "") +
            "\\s*\\]";
    }

    /**
     * Create a regular expression that matches an append index, one that is an
     * empty index, used to indicate that values should be appended to an array
     * as in PHP. When you capture the string, you're capturing a blank string,
     * which is how you test for an append index.
     * 
     * @param capture
     *            If true, surround the append index contents with a capture in
     *            order to capture the blank string.
     * @return A regular expression that matches an append index.
     */
    public static String appendIndex(boolean capture) {
        return "\\[" + (capture ? "(" : "") + "\\s*" + (capture ? ")" : "") + "\\]";
    }

    /**
     * Create a regular expression that matches any index
     * 
     * @param capture
     *            If true, surround the string with a capture in order to
     *            capture the string with the surrounding quotes.
     * @return A regular expression that matches any type of bracket inclosed
     *         index.
     */
    public static String anyIndex(boolean capture) {
        return
            globIndex(capture) + "|" + listIndex(capture) + "|" +
                appendIndex(capture) + "|" +
                "\\[\\s*" + identifier(capture) + "\\s*\\]|" +
                stringIndex('\'', capture) + "|" + stringIndex('"', capture);
    }

    /**
     * Create an alternation that matches one of the special Java escape
     * characters that can be used in a string matching regular expression.
     * 
     * @param characters
     *            The special characters.
     * @return An alternation without the parenthesis.
     */
    private final static String escaped(char... characters) {
        StringBuffer newString = new StringBuffer();
        String separator = "";
        for (int i = 0; i < characters.length; i++) {
            newString.append(separator)
                     .append("\\\\")
                     .append(characters[i]);
            separator = "|";
        }
        return newString.toString();
    }

    // FIXME Add valid Java identifier, unquoted as a valid type.
    /**
     * Create a regular expression pattern that will match a JSON like quoted
     * string within index brackets.
     * 
     * @param quote
     *            The quote character, either a single or double quote.
     * @param capture
     *            If true, surround the string with a capture in order to
     *            capture the string with the surrounding quotes.
     */
    public static String stringIndex(char quote, boolean capture) {
        String escaped = escaped('b', 'f', 'n', 'r', 't');
        return "\\[" + "\\s*"
                     + (capture ? "(" : "")
                     + quote
                         + "(?:"
                             + "[^" + quote + "\\\\]"
                             + "|" 
                             + "(?:"
                                 + "\\\\\\\\"
                                 + "|"
                                 + "\\\\" + quote
                                 + "|"
                                 + "\\\\u[A-Fa-f0-9]{4}"
                                 + "|"
                                 + "\\\\x[A-Fa-f0-9]{2}"
                                 + "|"
                                 + escaped
                             + ")"
                         + ")*"
                     + quote
                     + (capture ? ")" : "")
                     + "\\s*" + "\\]"; 
    }
}
