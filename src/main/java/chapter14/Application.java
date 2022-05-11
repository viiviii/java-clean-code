package chapter14;

import chapter14.args.Args;

import java.text.ParseException;

class Application {

    public static void main(String[] args) {
        try {
            Args arg = new Args("l,p#,d*", args);
            boolean logging = arg.getBoolean('l');
            int port = arg.getInt('p');
            String directory = arg.getString('d');
            executeApplication(logging, port, directory);
        } catch (ParseException e) {
            System.out.printf("Parse error: %s\n", e.getMessage());
        }
    }

    // stub
    private static void executeApplication(boolean logging, int port, String directory) {
        System.out.printf("logging: %b, port: %d, directory: %s\n", logging, port, directory);
    }
}
