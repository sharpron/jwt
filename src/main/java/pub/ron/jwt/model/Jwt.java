package pub.ron.jwt.model;

public final class Jwt {

    private final String header;

    private final String payload;

    private final String signature;


    public Jwt(String header, String payload, String signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

//    public String represent() {
//
//    }
}
