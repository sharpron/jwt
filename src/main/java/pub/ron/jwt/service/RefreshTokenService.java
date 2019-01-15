package pub.ron.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import pub.ron.jwt.domain.RefreshToken;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.RefreshTokenRepository;

import java.util.Date;
import java.util.Optional;

@Component
public class RefreshTokenService {

    private static final int REFRESH_TOKEN_ACTIVE = 7 * 24 * 60 * 60 * 1000;

    private final RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    Optional<RefreshToken> findByTokenValue(String tokenValue) {
        return refreshTokenRepository.findByValue(tokenValue);
    }


    boolean isExpired(RefreshToken refreshToken) {
        return !inActive(refreshToken.getTime());
    }

    private boolean inActive(Date date) {
        return System.currentTimeMillis() - date.getTime() < REFRESH_TOKEN_ACTIVE;
    }

    RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setTime(new Date());
        refreshToken.setValue(genToken(refreshToken.getUser().getPassword()));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    RefreshToken createNewerRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        return updateRefreshToken(refreshToken);
    }

    private static String genToken(String source) {
        return DigestUtils.md5DigestAsHex((source + System.currentTimeMillis()).getBytes());
    }
}
