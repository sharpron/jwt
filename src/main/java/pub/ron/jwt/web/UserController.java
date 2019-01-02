package pub.ron.jwt.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/authc")
    public ResponseEntity<String> auth(@RequestParam String username,
                                       @RequestParam String password) {
//        new JwtPayload()
//        JwtTokenizer.createToken()
        return ResponseEntity.ok("");
    }


}
