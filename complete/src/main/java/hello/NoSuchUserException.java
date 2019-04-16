package hello;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(String s) {
        super(s);
    }
}