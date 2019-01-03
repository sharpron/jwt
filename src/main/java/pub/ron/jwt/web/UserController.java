package pub.ron.jwt.web;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pub.ron.jwt.security.JwtPayload;
import pub.ron.jwt.service.UserService;

import javax.servlet.http.HttpServletRequest;

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
            return ResponseEntity.
                    ok(userService.login(username, password, request.getRemoteHost()));
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<JwtPayload> getSelf() {
        JwtPayload payload = JwtPayload.getAuthenticated();
        return ResponseEntity.ok(payload);
    }


}
