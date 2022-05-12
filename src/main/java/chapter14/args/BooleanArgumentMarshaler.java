package chapter14.args;

import java.util.Iterator;

public class BooleanArgumentMarshaler implements ArgumentMarshaler {
    private boolean booleanValue = false;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        booleanValue = true;
    }

    @Override
    public Object get() {
        return booleanValue;
    }

    public static boolean getValue(ArgumentMarshaler am) {
        try {
            return am != null && (Boolean) am.get();
        } catch (ClassCastException e) {
            return false;
        }
    }
}