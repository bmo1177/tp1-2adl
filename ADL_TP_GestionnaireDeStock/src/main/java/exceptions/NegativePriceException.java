package exceptions;

public class NegativePriceException extends Exception {
    public NegativePriceException(String msg) {
        super(msg);
    }
}