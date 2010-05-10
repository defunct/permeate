package com.goodworkalan.infuse;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class PartTest
{
    @Test
    public void constructor() 
    {
        Part part = new Part("name");
        assertEquals(part.getName(), "name");
        assertEquals(part.getQuote(), '\0');
        assertFalse(part.isIndex());
        
    }
    
    @Test
    public void isGlob()
    {
        assertTrue(new Part("*", true, '\0').isGlob());
        assertFalse(new Part("name", true, '\0').isGlob());
        assertFalse(new Part("*", false, '\0').isGlob());
        assertFalse(new Part("*", true, '"').isGlob());
    }
    
    @Test
    public void isInteger() {
        assertTrue(new Part("0", true, '\0').isInteger());
        assertFalse(new Part("0", false, '\0').isInteger());
        assertFalse(new Part("0", true, '"').isInteger());
        assertFalse(new Part("a", true, '\0').isInteger());
        assertTrue(new Part("+1", true, '\0').isInteger());
        assertTrue(new Part("-1", true, '\0').isInteger());
    }
    
    @Test
    public void equals()
    {
        Part part = new Part("name", true, '"');
        
        assertEquals(part, part);
        assertEquals(part, new Part("name", true, '"'));
        assertFalse(part.equals(null));
        assertFalse(part.equals(new Part("name", true, '\'')));
        assertFalse(part.equals(new Part("name", false, '\0')));
        assertFalse(part.equals(new Part("eman", true, '"')));
    }
    
    @Test
    public void hash()
    {
        assertEquals(new Part("name", true, '"').hashCode(), new Part("name", true, '"').hashCode());
        assertEquals(new Part("name", false, '"').hashCode(), new Part("name", false, '"').hashCode());
    }
    
    @Test
    public void compare()
    {
        Part part = new Part("name", true, '"');
        
        assertEquals(part, part);
        assertEquals(part.compareTo(new Part("name", true, '"')), 0);
        assertTrue(part.compareTo(new Part("mame", true, '"')) > 0);
        assertTrue(part.compareTo(new Part("name", false, '\0')) < 0);
        assertTrue(new Part("name", false, '\0').compareTo(part) > 0);
        assertTrue(part.compareTo(new Part("name", true, '\'')) < 0);
    }
}
