package com.goodworkalan.infuse.api;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.infuse.Path;
import com.goodworkalan.infuse.PathException;

public class PathTest
{
    @Test
    public void test() throws PathException 
    {
        Path path = new Path("foo[1][2].bar", false);
        assertEquals(path.withoutIndexes(), "foo.bar");
    }
}
