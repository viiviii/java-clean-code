package chapter14.args;

import java.util.*;

public class Args {
    private String schema;
    private boolean valid = true;
    private Map<Character, ArgumentMarshaler> marshalers = new HashMap<>();
    private Set<Character> argsFound = new HashSet<>();
    private Iterator<String> currentArgument;
    private List<String> argsList;


    public Args(String schema, String[] args) throws ArgsException {
        this.schema = schema;
        argsList = Arrays.asList(args);
        valid = parse();
    }

    private boolean parse() throws ArgsException {
        if (schema.length() == 0 && argsList.size() == 0)
            return true;
        parseSchema();
        parseArguments();
        return valid;
    }

    private boolean parseSchema() throws ArgsException {
        for (String element : schema.split(",")) {
            if (element.length() > 0) {
                String trimmedElement = element.trim();
                parseSchemaElement(trimmedElement);
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ArgsException {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (elementTail.length() == 0) {
            marshalers.put(elementId, new BooleanArgumentMarshaler());
        } else if (elementTail.equals("*")) {
            marshalers.put(elementId, new StringArgumentMarshaler());
        } else if (elementTail.equals("#")) {
            marshalers.put(elementId, new IntegerArgumentMarshaler());
        } else if (elementTail.equals("##")) {
            marshalers.put(elementId, new DoubleArgumentMarshaler());
        } else {
            throw new ArgsException(
                    String.format("Argument: %c has invalid format: %s.",
                            elementId, elementTail));
        }
    }

    private void validateSchemaElementId(char elementId) throws ArgsException {
        if (!Character.isLetter(elementId)) {
            throw new ArgsException(
                    "Bad character: " + elementId + " in Args format: " + schema);
        }
    }

    private boolean parseArguments() throws ArgsException {
        for (currentArgument = argsList.iterator(); currentArgument.hasNext(); ) {
            String arg = currentArgument.next();
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) throws ArgsException {
        if (arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg) throws ArgsException {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) throws ArgsException {
        if (setArgument(argChar))
            argsFound.add(argChar);
        else {
            valid = false;
            throw new ArgsException(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT, argChar, null);
        }
    }

    private boolean setArgument(char argChar) throws ArgsException {
        ArgumentMarshaler m = marshalers.get(argChar);
        if (m == null)
            return false;
        try {
            m.set(currentArgument);
            return true;
        } catch (ArgsException e) {
            valid = false;
            e.setErrorArgumentId(argChar);
            throw e;
        }
    }

    public int cardinality() {
        return argsFound.size();
    }

    public String usage() {
        if (schema.length() > 0)
            return "-[" + schema + "]";
        else
            return "";
    }

    public String getString(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? "" : (String) am.get();
        } catch (ClassCastException e) {
            return "";
        }
    }

    public int getInt(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? 0 : (Integer) am.get();
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public boolean getBoolean(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        boolean b = false;
        try {
            b = am != null && (Boolean) am.get();
        } catch (ClassCastException e) {
            b = false;
        }
        return b;
    }

    public double getDouble(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? 0.0 : (Double) am.get();
        } catch (ClassCastException e) {
            return 0.0;
        }
    }

    public boolean has(char arg) {
        return argsFound.contains(arg);
    }

    public boolean isValid() {
        return valid;
    }

    public static class ArgsException extends Exception {
        private char errorArgumentId = '\0';
        private String errorParameter = "TILT";
        private ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.OK;

        public ArgsException() {
        }

        public ArgsException(String message) {
            super(message);
        }

        public ArgsException(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public ArgsException(ErrorCode errorCode, String errorParameter) {
            this.errorCode = errorCode;
            this.errorParameter = errorParameter;
        }

        public ArgsException(ErrorCode errorCode, char errorArgumentId, String errorParameter) {
            this.errorCode = errorCode;
            this.errorArgumentId = errorArgumentId;
            this.errorParameter = errorParameter;
        }

        public void setErrorArgumentId(char errorArgumentId) {
            this.errorArgumentId = errorArgumentId;
        }

        public String errorMessage() throws Exception {
            switch (errorCode) {
                case OK:
                    throw new Exception("TILT: Should not get here.");
                case UNEXPECTED_ARGUMENT:
                    return String.format("Argument(s) -%c unexpected.",
                            errorArgumentId);
                case MISSING_STRING:
                    return String.format("Could not find string parameter for -%c.",
                            errorArgumentId);
                case INVALID_INTEGER:
                    return String.format("Argument -%c expects an integer but was '%s'.",
                            errorArgumentId, errorParameter);
                case MISSING_INTEGER:
                    return String.format("Could not find integer parameter for -%c.",
                            errorArgumentId);
                case INVALID_DOUBLE:
                    return String.format("Argument -%c expects an double but was '%s'.",
                            errorArgumentId, errorParameter);
                case MISSING_DOUBLE:
                    return String.format("Could not find double parameter for -%c.",
                            errorArgumentId);
            }
            return "";
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public char getErrorArgumentId() {
            return errorArgumentId;
        }

        public String getErrorParameter() {
            return errorParameter;
        }

        public enum ErrorCode {
            OK, MISSING_STRING, MISSING_INTEGER, INVALID_INTEGER,
            UNEXPECTED_ARGUMENT, MISSING_DOUBLE, INVALID_DOUBLE
        }
    }

    private interface ArgumentMarshaler {
        void set(Iterator<String> currentArgument) throws ArgsException;

        Object get();
    }

    private class BooleanArgumentMarshaler implements ArgumentMarshaler {
        private boolean booleanValue = false;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            booleanValue = true;
        }

        @Override
        public Object get() {
            return booleanValue;
        }
    }

    private class StringArgumentMarshaler implements ArgumentMarshaler {
        private String stringValue = "";

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            try {
                stringValue = currentArgument.next();
            } catch (NoSuchElementException e) {
                throw new ArgsException(ArgsException.ErrorCode.MISSING_STRING);
            }
        }

        @Override
        public Object get() {
            return stringValue;
        }
    }

    private class IntegerArgumentMarshaler implements ArgumentMarshaler {
        private int intValue = 0;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;
            try {
                parameter = currentArgument.next();
                intValue = Integer.parseInt(parameter);
            } catch (NoSuchElementException e) {
                throw new ArgsException(ArgsException.ErrorCode.MISSING_INTEGER);
            } catch (NumberFormatException e) {
                throw new ArgsException(ArgsException.ErrorCode.INVALID_INTEGER, parameter);
            }
        }

        @Override
        public Object get() {
            return intValue;
        }
    }

    private class DoubleArgumentMarshaler implements ArgumentMarshaler {
        private double doubleValue = 0;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;
            try {
                parameter = currentArgument.next();
                doubleValue = Double.parseDouble(parameter);
            } catch (NoSuchElementException e) {
                throw new ArgsException(ArgsException.ErrorCode.MISSING_DOUBLE, parameter);
            } catch (NumberFormatException e) {
                throw new ArgsException(ArgsException.ErrorCode.INVALID_DOUBLE, parameter);
            }
        }

        @Override
        public Object get() {
            return doubleValue;
        }
    }
}
