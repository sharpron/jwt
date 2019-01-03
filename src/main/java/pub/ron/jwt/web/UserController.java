package pub.ron.jwt.web;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.security.JwtPayload;
import pub.ron.jwt.security.JwtTokenizer;
import pub.ron.jwt.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/authc")
    public ResponseEntity<String> auth(@RequestParam String username,
                                       @RequestParam String password,
                                       HttpServletRequest request) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username is null");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("password is null");
        }
        try {
            return ResponseEntity.ok(userService.login(username, password, request.getRemoteHost()));
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}
