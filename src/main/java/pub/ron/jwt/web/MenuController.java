package pub.ron.jwt.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pub.ron.jwt.annotation.PermDefine;
import pub.ron.jwt.domain.Menu;
import pub.ron.jwt.dto.MenuNode;
import pub.ron.jwt.service.MenuService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<MenuNode>> treeMenus() {
        return ResponseEntity.ok(menuService.treeMenu());
    }

    @ApiOperation("获取某个菜单")
    @PermDefine(name = "menu:detail", desc = "获取某个菜单")
    @GetMapping("/{id}")
    public ResponseEntity<Menu> findById(@PathVariable @NotNull Integer id) {
        return ResponseEntity.ok(menuService.findById(id));
    }

    @ApiOperation("添加菜单")
    @PermDefine(name = "menu:add", desc = "添加菜单")
    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody Menu menu) {
        if (MenuNode.isTopNode(menu.getParent())) {
            menu.setParent(null);
        }
        menuService.add(menu);
        return ResponseEntity.ok("添加菜单成功");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
