package pub.ron.jwt.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.UserRepository;
import pub.ron.jwt.security.JwtPayload;
import pub.ron.jwt.security.JwtTokenizer;
import pub.ron.jwt.security.PasswordUtil;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return  userRepository.findUserByUsername(username);
    }

    @Transactional
    public String login(String username, String password, String host) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(usernamePasswordToken);
        Optional<User> optionalUser = findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new IllegalStateException("user is null");
        }
        return JwtTokenizer.createToken(new JwtPayload(optionalUser.get(), host));
    }


    public void register(User user) {
        if (existUsername(user.getUsername())) {
            throw new RuntimeException("username already existed");
        }
        String salt = PasswordUtil.randomSalt();
        String password = handlePassword(user.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);
        userRepository.save(user);
    }


    private String handlePassword(String password, String salt) {
        return PasswordUtil.encrypt(password, salt);
    }

    public boolean existUsername(String username) {
        return findByUsername(username).isPresent();
    }



}
