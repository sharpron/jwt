package pub.ron.jwt.dto;

public class Token {

    private final String jwt;

    private final String refreshToken;

    public Token(String jwt, String refreshToken) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
