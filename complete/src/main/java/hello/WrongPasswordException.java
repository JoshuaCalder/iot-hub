package hello;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String s) {
        super(s);
    }
}