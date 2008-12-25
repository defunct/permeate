package com.goodworkalan.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;

public class PublicTest
{
    @Test
    public void test() throws PathException 
    {
        PropertyPath path = new PropertyPath("foo[1][2].bar");
        assertEquals(path.withoutIndexes(), "foo.bar");
    }
}
