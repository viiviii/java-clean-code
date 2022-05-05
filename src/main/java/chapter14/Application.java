package chapter14;

public class Application {

    public static void main(String[] args) {
        Args arg = new Args("l", args);
        boolean logging = arg.getBoolean('l');
        executeApplication(logging);
    }

    // stub
    private static void executeApplication(boolean logging) {
        System.out.printf("logging: %b\n", logging);
    }
}
