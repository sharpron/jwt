package pub.ron.jwt.web;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.repository.UserRepository;

import java.util.Date;

@RestController
@RequestMapping("/session")
public class SessionController {

    private UserRepository userRepository;

    @Autowired
    public SessionController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/apply-token")
    public ResponseEntity auth(@RequestParam(name = "username") String username) {
        final String jwt = Jwts.builder()
                .setId("")
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor("fds1h24m1341n422".getBytes()))
                .compact();
        return ResponseEntity.ok(jwt);
    }
}
