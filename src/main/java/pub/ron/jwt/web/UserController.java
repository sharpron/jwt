package pub.ron.jwt.web;


import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.annotation.LogDesc;
import pub.ron.jwt.security.jwt.JwtPayload;

@RestController
@RequestMapping("/user")
public class UserController {



    @LogDesc("获取个人信息")
    @ApiOperation("获取本身的信息")
    @GetMapping("/me")
    public ResponseEntity<JwtPayload> getSelf() {
        JwtPayload payload = JwtPayload.getAuthenticated();
        return ResponseEntity.ok(payload);
    }


}
