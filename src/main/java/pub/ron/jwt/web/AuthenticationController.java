package pub.ron.jwt.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pub.ron.jwt.dto.Token;
import pub.ron.jwt.security.JwtUtils;
import pub.ron.jwt.service.RefreshTokenService;
import pub.ron.jwt.service.UserService;

@RestController
@RequestMapping("/authentication")
@Api("认证")
public class AuthenticationController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthenticationController(UserService userService,
                                    RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @ApiOperation("申请JWT")
    @PostMapping("/apply-jwt")
    public ResponseEntity<Token> applyToken(@ApiParam @RequestParam String username,
                                            @ApiParam @RequestParam String password) {
        return ResponseEntity.ok(userService.sign(username, password));
    }


    @ApiOperation("重新申请JWT")
    @GetMapping("/reapply-jwt")
    public ResponseEntity<Token> reapplyToken(
            @RequestHeader(JwtUtils.JWT_AUTH_HEADER) String jwt,
            @ApiParam @RequestParam String refreshToken) {
        Token token = userService.reapplyToken(new Token(jwt, refreshToken));
        return ResponseEntity.ok(token);
    }
}
