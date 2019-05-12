package pub.ron.jwt.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pub.ron.jwt.annotation.LogDesc;
import pub.ron.jwt.annotation.PermDefine;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.service.RoleService;

import javax.validation.Valid;

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

    @ApiOperation("新增角色")
    @LogDesc("新增角色")
    @PermDefine(name = "role:add", desc = "新增角色")
    @PostMapping
    public ResponseEntity<String> createRole(@Valid @RequestBody Role role) {
        roleService.add(role);
        return ResponseEntity.ok("新增角色成功");
    }

    @ApiOperation("修改角色")
    @LogDesc("修改角色")
    @PermDefine(name = "role:update", desc = "修改角色")
    @PutMapping
    public ResponseEntity<String> updateRole(@Valid @RequestBody Role role) {
        roleService.update(role);
        return ResponseEntity.ok("修改角色成功");
    }

    @ApiOperation("删除角色")
    @LogDesc("删除角色")
    @PermDefine(name = "role:delete", desc = "删除角色")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
        roleService.deleteIfNotSystem(id);
        return ResponseEntity.ok("删除角色成功");
    }

    @ApiOperation("获取角色")
    @PermDefine(name = "role:detail", desc = "获取角色")
    @GetMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.findById(id));
    }
}
