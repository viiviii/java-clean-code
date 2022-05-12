package chapter14.args;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static chapter14.args.ArgsException.ErrorCode.MISSING_STRING;

public class StringArgumentMarshaler implements ArgumentMarshaler {
    private String stringValue = "";

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        try {
            stringValue = currentArgument.next();
        } catch (NoSuchElementException e) {
            throw new ArgsException(MISSING_STRING);
        }
    }

    @Override
    public Object get() {
        return stringValue;
    }

    public static String getValue(ArgumentMarshaler am) {
        try {
            return am == null ? "" : (String) am.get();
        } catch (ClassCastException e) {
            return "";
        }
    }
}