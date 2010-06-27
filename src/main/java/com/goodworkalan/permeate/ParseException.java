package com.goodworkalan.permeate;

// FIXME Rename to path parse exception.
public class ParseException extends PathException
{
    // TODO Document.
    private static final long serialVersionUID = 1L;

    // TODO Document.
    public ParseException(int code)
    {
        super(code);
    }
    
    // TODO Document.
    public ParseException(int code, Throwable cause)
    {
        super(code, cause);
    }
    
    // TODO Document.
    public ParseException add(Object argument)
    {
        arguments.add(argument);
        return this;
    }
}
