package pub.ron.jwt.service;

import pub.ron.jwt.domain.Button;
import pub.ron.jwt.domain.Menu;
import pub.ron.jwt.dto.MenuNode;

import java.util.List;

/**
 * 菜单服务
 * @author ron
 * 2019.05.10
 */
public interface MenuService extends BaseService<Menu> {
    /**
     * 获取树形菜单
     * @return 树形菜单
     */
    List<MenuNode> treeMenu();

    /**
     * 添加按钮到菜单
     * @param menuId 菜单id
     * @param button 按钮id
     */
    void addButtonToMenu(Integer menuId, Button button);

    /**
     * 从菜单中删除按钮
     * @param menuId 菜单id
     * @param buttonId 按钮id
     */
    void removeButtonFromMenu(Integer menuId, Integer buttonId);

    /**
     * 从菜单中更新按钮
     * @param menuId 菜单id
     * @param button 按钮
     */
    void updateButtonFromMenu(Integer menuId, Button button);
}
