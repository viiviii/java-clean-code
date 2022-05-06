package chapter14;

import chapter14.args.Args;

import java.text.ParseException;

class Application {

    public static void main(String[] args) {
        try {
            Args arg = new Args("l,d*", args);
            boolean logging = arg.getBoolean('l');
            String directory = arg.getString('d');
            executeApplication(logging, directory);
        } catch (ParseException e) {
            System.out.printf("Parse error: %s\n", e.getMessage());
        }
    }

    // stub
    private static void executeApplication(boolean logging, String directory) {
        System.out.printf("logging: %b, directory: %s\n", logging, directory);
    }
}
