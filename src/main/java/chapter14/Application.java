package chapter14;

import chapter14.args.Args;
import chapter14.args.ArgsException;

class Application {

    public static void main(String[] args) {
        try {
            Args arg = new Args("l,p#,d*", args);
            boolean logging = arg.getBoolean('l');
            int port = arg.getInt('p');
            String directory = arg.getString('d');
            executeApplication(logging, port, directory);
        } catch (ArgsException e) {
            System.out.printf("Argument error: %s\n", e.getMessage());
        }
    }

    // stub
    private static void executeApplication(boolean logging, int port, String directory) {
        System.out.printf("logging: %b, port: %d, directory: %s\n", logging, port, directory);
    }
}
