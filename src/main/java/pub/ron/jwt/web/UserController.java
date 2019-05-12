package pub.ron.jwt.web;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pub.ron.jwt.annotation.LogDesc;
import pub.ron.jwt.annotation.PermDefine;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.security.jwt.JwtPayload;
import pub.ron.jwt.service.JwtService;
import pub.ron.jwt.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("查询用户列表")
    @LogDesc("查询用户列表")
    @PermDefine(name = "user:list", desc = "查询用户列表")
    @GetMapping
    public ResponseEntity<Page<User>> getRoles(Pageable pageable) {
        return ResponseEntity.ok(userService.findByPage(pageable));
    }

    @LogDesc("获取个人信息")
    @ApiOperation("获取本身的信息")
    @GetMapping("/me")
    public ResponseEntity<JwtPayload> getSelf() {
        JwtPayload payload = JwtService.getAuthenticated();
        return ResponseEntity.ok(payload);
    }

    @LogDesc("创建用户")
    @ApiOperation("创建用户")
    @PermDefine(name = "user:add", desc = "创建用户")
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        userService.add(user);
        return ResponseEntity.ok("创建用户成功");
    }

    @LogDesc("修改用户")
    @ApiOperation("修改用户")
    @PermDefine(name = "user:update", desc = "修改用户")
    @PutMapping
    public ResponseEntity<String> updateUser(@Valid @RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok("修改用户成功");
    }

    @LogDesc("删除用户")
    @ApiOperation("删除用户")
    @PermDefine(name = "user:delete", desc = "删除用户")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok("删除用户成功");
    }



}
