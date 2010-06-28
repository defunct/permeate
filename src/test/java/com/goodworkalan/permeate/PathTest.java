package com.goodworkalan.permeate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Path} class.
 * 
 * @author Alan Gutierrez
 */
public class PathTest {
    /** Test parsing. */
    @Test
    public void test() throws ParseException {
        Path path = new Path("foo[1][2].bar", false);
        assertEquals(path.withoutIndexes(), "foo.bar");
    }
    
    /** Test path exception. */
    @Test
    public void pathException() {
        try {
            throw new ParseException(10001);
        } catch (ParseException e) {
        }
        try {
            throw new ParseException(10001, new IOException());
        }
        catch (ParseException e)
        {
        }
        assertEquals(new ParseException(999999).getMessage(), "999999");
    }

    /** Test a missing error format. */
    @Test(expectedExceptions = Error.class)
    public void errorBadFormat() {
        try {
            throw new ParseException(99999);
        } catch (ParseException e) {
            e.getMessage();
        }
    }
    
    /**  Test numeric index with alpha. */
    @Test(expectedExceptions = ParseException.class)
    public void badNumericIndexAlphaNum() throws ParseException {
        String part = "a[ 1i ] ";
        try {
            new Path(part, false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a[ 1i ] \". Invalid index specification at index 1.");
            throw e;
        }
    }
    
    /**  Test numeric index with non-alpha. */
    @Test(expectedExceptions=ParseException.class)
    public void badNumericIndexNonAlphaNum() throws ParseException
    {
        String part = "a[ 1i ["; 
        try
        {
            new Path(part, false);
        }
        catch (ParseException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a[ 1i [\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }
    
    /**  Test numeric index with non-alpha. */
    @Test(expectedExceptions=ParseException.class)
    public void badIndexValue() throws ParseException {
        String part = "a[@2][b]";
        try {
            new Path(part, false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a[@2][b]\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }
    
    /**  Test negative index index. */
    public void negativeInteger() throws ParseException {
        Path path = new Path("a[-2][b]", false);
        assertEquals(path.get(1).getName(), "-2"); 
        assertTrue(path.get(1).isInteger()); 

    }

    /** Test bad index with quote. */
    @Test(expectedExceptions = ParseException.class)
    public void badIndexQuote() throws ParseException {
        String part = "a \"";
        try {
            new Path(part, false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a \\\"\". Unexpected character '\"' at index 2.");
            throw e;
        }
    }
    
    /** Test bad index with non-alpha. */
    @Test(expectedExceptions = ParseException.class)
    public void badIndexNonAlphaNum() throws ParseException {
        String part = "a]";
        try {
            new Path(part, false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a]\". Unexpected character ']' at index 1.");
            assertEquals(e.getCode(), 129);
            throw e;
        }
    }

    /**
     * Assert that the path expression has the given name for the second part.
     * 
     * @param path
     *            The part.
     * @param name
     *            The name.
     * @throws ParseException
     *             For a parse error while evaluating the path.
     */
    private void assertName(String path, String name) throws ParseException {
        assertEquals(new Path(path, false).get(1).getName(), name);
    }

    /** Test string index parsing. */
    @Test
    public void stringIndex() throws ParseException {
        assertName("a ['a']", "a");
        assertName("a ['abcdef']", "abcdef");
        assertName("a ['\\'']", "'");
        assertName("a [\"\\\"\"]", "\"");
        assertName("a ['\\b']", "\b");
        assertName("a ['\\t']", "\t");
        assertName("a ['\\f']", "\f");
        assertName("a ['\\r']", "\r");
        assertName("a ['\\n']", "\n");
        assertName("a ['\\u0041']", "A");
        assertName("a ['\\x41']", "A");
    }

    /** Test matching the append index. */
    @Test
    public void appendIndex() throws ParseException {
        Path path = new Path("parameter[]", false);
        assertTrue(path.get(1).isAppend());
    }
    
    /** Test bad closing backets. */
    @Test(expectedExceptions = ParseException.class)
    public void stringIndexBadClosingBracket() throws ParseException {
        try {
            new Path("a['a'a", false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a['a'a\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }
    
    /** Test missing closing backets. */
    @Test(expectedExceptions = ParseException.class)
    public void stringIndexIncomplete() throws ParseException {
        try {
            new Path("a['a", true);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a['a\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }

    /** Test mismatched quote styles. */
    @Test(expectedExceptions = ParseException.class)
    public void stringIndexMismatchQuotes() throws ParseException {
        try {
            new Path("a['a\"]", true);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a['a\\\"]\". Invalid index specification at index 1.");
            throw e;
        }
    }

    /** Test an unknown escape sequence. */
    @Test(expectedExceptions = ParseException.class)
    public void stringIndexBadEscape() throws ParseException {
        try {
            new Path("a['\\a']", true);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a['\\a']\". Invalid index specification at index 1.");
            throw e;
        }
    }
    
    /** Test a string index with a zero character. */
    @Test(expectedExceptions = ParseException.class)
    public void stringIndexWithZero() throws ParseException {
        try {
            new Path("a['\0']", true);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a['\\0']\". Invalid character '\\0' in index specification \"'\\0'\" at index 1.");
            throw e;
        }
    }
    
    /** Test parsing a null string. */
    @Test(expectedExceptions = NullPointerException.class)
    public void nullString() throws ParseException {
        new Path((String) null, true);
    }

    /** Test parsing an empty string. */
    @Test(expectedExceptions = ParseException.class)
    public void emptyString() throws ParseException {
        try {
            new Path("", true);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"\". Invalid identifier specification at index 0.");
            throw e;
        }
    }
    
    /** Test parsing a Java identifier that does not start correctly. */
    @Test(expectedExceptions = ParseException.class)
    public void nonJavaIdentifierStart() throws ParseException {
        try {
            new Path("1", true);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"1\". Invalid identifier specification at index 0.");
            assertEquals(e.getCode(), 125);
            throw e;
        }
    }
    
    /** Test string escaping. */
    @Test
    public void stringEscape() {
        assertEquals(Messages.stringEscape("\b\f\n\r\t\0\1\2\3\4\5\6\7\""), "\"\\b\\f\\n\\r\\t\\0\\1\\2\\3\\4\\5\\6\\7\\\"\"");
    }
    
    /** Test character escaping. */
    @Test
    public void charEscape() {
        assertEquals(Messages.charEscape('\''), "'\\''");
        assertEquals(Messages.charEscape('\\'), "'\\\\'");
    }

    /** Test stripping the bracketed indexes. */
    @Test
    public void stripIndexes() throws ParseException {
        String path = " foo . bar [1] [   'Hello, World!\\n' ] [1] . baz [100] [  11 ]  ";
        assertEquals(new Path(path, false).withoutIndexes(), "foo.bar.baz");
    }
}
