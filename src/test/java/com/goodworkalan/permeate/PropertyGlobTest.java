package com.goodworkalan.permeate;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.regex.Pattern;

import org.testng.annotations.Test;

import com.goodworkalan.permeate.Patterns;

public class PropertyGlobTest
{
    @Test
    public void regex()
    {
        assertTrue("a".matches(Patterns.identifier(false)));
        assertFalse("1".matches(Patterns.identifier(false)));
        assertTrue("['foo']".matches(Patterns.stringIndex('\'', false)));
        assertTrue("['\\'']".matches(Patterns.stringIndex('\'', false)));
        assertTrue("['\\b']".matches(Patterns.stringIndex('\'', false)));
        assertFalse("[''']".matches(Patterns.stringIndex('\'', false)));
        assertTrue(Pattern.compile(Patterns.glob()).matcher("a[1]['\\''].b.c[ 12 ]").matches());
    }
}
