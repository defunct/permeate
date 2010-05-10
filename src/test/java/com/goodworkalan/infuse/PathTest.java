package com.goodworkalan.infuse;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.testng.annotations.Test;

public class PathTest
{
    @Test
    public void pathException()
    {
        try
        {
            throw new PathException(10001);
        }
        catch (PathException e)
        {
        }
        try
        {
            throw new PathException(10001, new IOException());
        }
        catch (PathException e)
        {
        }
        assertEquals(new PathException(999999).getMessage(), "999999");
    }

    @Test(expectedExceptions=Error.class)
    public void errorBadFormat()
    {
        try
        {
            throw new PathException(99999);
        }
        catch (PathException e)
        {
            e.getMessage();
        }
    }
    
    @Test(expectedExceptions=ParseException.class)
    public void badNumericIndexAlphaNum() throws PathException
    {
        String part = "a[ 1i ] "; 
        try
        {
            new Path(part, false);
        }
        catch (ParseException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a[ 1i ] \". Invalid index specification at index 1.");
            throw e;
        }
    }
    
    @Test(expectedExceptions=ParseException.class)
    public void badNumericIndexNonAlphaNum() throws PathException
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
    
    @Test(expectedExceptions=ParseException.class)
    public void badIndexValue() throws PathException {
        String part = "a[@2][b]";
        try {
            new Path(part, false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Unable to parse path \"a[@2][b]\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }
    
    public void negativeInteger() throws PathException {
        Path path = new Path("a[-2][b]", false);
        assertEquals(path.get(1).getName(), "-2"); 
        assertTrue(path.get(1).isInteger()); 

    }
    
    @Test(expectedExceptions=PathException.class)
    public void badIndexAlphaNum() throws PathException
    {
        String part = "a \"";
        try
        {
            new Path(part, false);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a \\\"\". Unexpected character '\"' at index 2.");
            throw e;
        }
    }
    
    @Test(expectedExceptions=PathException.class)
    public void badIndexNonAlphaNum() throws PathException
    {
        String part = "a]";
        try
        {
            new Path(part, false);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a]\". Unexpected character ']' at index 1.");
            assertEquals(e.getCode(), 129);
            throw e;
        }
    }
    
    private void assertName(String part, String name) throws PathException
    {
        Path property = new Path(part, false);
        assertEquals(property.get(1).getName(), name);
    }
    
    @Test
    public void stringIndex() throws PathException
    {
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
    
    @Test
    public void appendIndex() throws PathException
    {
        Path path = new Path("parameter[]", false);
        assertTrue(path.get(1).isAppend());
    }
    
    @Test(expectedExceptions=PathException.class)
    public void stringIndexBadClosingBracket() throws PathException
    {
        try
        {
            new Path("a['a'a", false);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a['a'a\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }
    
    @Test(expectedExceptions=PathException.class)
    public void stringIndexIncomplete() throws PathException
    {
        try
        {
            new Path("a['a", true);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a['a\". Invalid index specification at index 1.");
            assertEquals(e.getCode(), 127);
            throw e;
        }
    }

    @Test(expectedExceptions=PathException.class)
    public void stringIndexMismatchQuotes() throws PathException
    {
        try
        {
            new Path("a['a\"]", true);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a['a\\\"]\". Invalid index specification at index 1.");
            throw e;
        }
    }
    
    @Test(expectedExceptions=PathException.class)
    public void stringIndexBadEscape() throws PathException
    {
        try
        {
            new Path("a['\\a']", true);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a['\\a']\". Invalid index specification at index 1.");
            throw e;
        }
    }
    
    @Test(expectedExceptions=PathException.class)
    public void stringIndexWithZero() throws PathException
    {
        try
        {
            new Path("a['\0']", true);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"a['\\0']\". Invalid character '\\0' in index specification \"'\\0'\" at index 1.");
            throw e;
        }
    }
    
    @Test(expectedExceptions=NullPointerException.class)
    public void nullString() throws PathException
    {
        new Path((String) null, true);
    }

    @Test(expectedExceptions=PathException.class)
    public void emptyString() throws PathException
    {
        try
        {
            new Path("", true);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"\". Invalid identifier specification at index 0.");
            throw e;
        }
    }
    
    @Test(expectedExceptions=PathException.class)
    public void nonJavaIdentifierStart() throws PathException
    {
        try
        {
            new Path("1", true);
        }
        catch (PathException e)
        {
            assertEquals(e.getMessage(), "Unable to parse path \"1\". Invalid identifier specification at index 0.");
            assertEquals(e.getCode(), 125);
            throw e;
        }
    }
    
    @Test
    public void stringEscape()
    {
        assertEquals(Messages.stringEscape("\b\f\n\r\t\0\1\2\3\4\5\6\7\""), "\"\\b\\f\\n\\r\\t\\0\\1\\2\\3\\4\\5\\6\\7\\\"\"");
    }
    
    @Test
    public void charEscape()
    {
        assertEquals(Messages.charEscape('\''), "'\\''");
        assertEquals(Messages.charEscape('\\'), "'\\\\'");
    }

    @Test
    public void stripIndexes() throws PathException
    {
        String path = " foo . bar [1] [   'Hello, World!\\n' ] [1] . baz [100] [  11 ]  ";
        assertEquals(new Path(path, false).withoutIndexes(), "foo.bar.baz");
    }
}
