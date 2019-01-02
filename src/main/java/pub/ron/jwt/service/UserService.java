package pub.ron.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.UserRepository;
import pub.ron.jwt.security.JwtPayload;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public void register(User user) {
        if (existUsername(user.getUsername())) {
            throw new RuntimeException("username already existed");
        }
        String salt = randomSalt();
        String password = handlePassword(user.getPassword(), salt);
        user.setPassword(password);
        userRepository.save(user);
    }

    private static String randomSalt() {
        return UUID.randomUUID().toString();
    }

    private String handlePassword(String password, String salt) {
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }

    public boolean existUsername(String username) {
        return userRepository.findUserByUsername(username) != null;
    }



}
