package chapter14.args;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static chapter14.args.ArgsException.ErrorCode.INVALID_DOUBLE;
import static chapter14.args.ArgsException.ErrorCode.MISSING_DOUBLE;

public class DoubleArgumentMarshaler implements ArgumentMarshaler {
    private double doubleValue = 0;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        String parameter = null;
        try {
            parameter = currentArgument.next();
            doubleValue = Double.parseDouble(parameter);
        } catch (NoSuchElementException e) {
            throw new ArgsException(MISSING_DOUBLE);
        } catch (NumberFormatException e) {
            throw new ArgsException(INVALID_DOUBLE, parameter);
        }
    }

    @Override
    public Object get() {
        return doubleValue;
    }

    public static double getValue(ArgumentMarshaler am) {
        try {
            return am == null ? 0.0 : (Double) am.get();
        } catch (ClassCastException e) {
            return 0.0;
        }
    }
}