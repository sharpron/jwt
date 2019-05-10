package pub.ron.jwt.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.annotation.PermDefine;
import pub.ron.jwt.dto.MenuNode;
import pub.ron.jwt.service.MenuService;

import java.util.List;

/**
 * 菜单操作
 * @author ron
 * 2019.05.09
 */
@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation("获取所有菜单")
    @PermDefine(name = "menu:list", desc = "获取所有菜单")
    @GetMapping
    private ResponseEntity<List<MenuNode>> treeMenus() {
        return ResponseEntity.ok(menuService.treeMenu());
    }

}
