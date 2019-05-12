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
import pub.ron.jwt.domain.Perm;
import pub.ron.jwt.service.PermService;

/**
 * 权限操作
 *
 * @author ron
 * 2019.05.09
 */
@RestController
@RequestMapping("/perms")
public class PermController {


    private final PermService permService;

    @Autowired
    public PermController(PermService permService) {
        this.permService = permService;
    }

    @ApiOperation("获取权限")
    @LogDesc("获取权限")
    @PermDefine(name = "perms:list", desc = "获取权限")
    @GetMapping
    private ResponseEntity<Page<Perm>> getPerms(Pageable pageable) {
        return ResponseEntity.ok(permService.findByPage(pageable));
    }

}
