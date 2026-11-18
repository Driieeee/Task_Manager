package services;

public class IdGenerator {
    private static int counter = 1;

    public static int next() {
        return counter++;
    }
}
