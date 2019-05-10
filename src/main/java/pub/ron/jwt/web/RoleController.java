package pub.ron.jwt.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.annotation.LogDesc;
import pub.ron.jwt.annotation.PermDefine;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.service.RoleService;

/**
 * 角色操作
 *
 * @author ron
 * 2019.05.09
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @ApiOperation("查询角色列表")
    @LogDesc("查询角色列表")
    @PermDefine(name = "role:list", desc = "查询角色列表")
    @GetMapping
    public ResponseEntity<Page<Role>> getRoles(Pageable pageable) {
        return ResponseEntity.ok(roleService.findByPage(pageable));
    }
}
