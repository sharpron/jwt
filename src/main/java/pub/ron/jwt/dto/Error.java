package pub.ron.jwt.dto;

public class Error {

    private final String message;

    private Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static Error from(String message) {
        return new Error(message);
    }

}
