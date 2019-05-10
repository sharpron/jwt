package pub.ron.jwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.ron.jwt.domain.Button;
import pub.ron.jwt.domain.Menu;
import pub.ron.jwt.dto.MenuNode;
import pub.ron.jwt.repository.MenuRepository;
import pub.ron.jwt.service.MenuService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ron
 * 2019.05.09
 */
@Service
class DefaultMenuService extends BaseServiceAdapter<Menu, MenuRepository> implements MenuService {

    @Autowired
    DefaultMenuService(MenuRepository menuRepository) {
        super(menuRepository);
    }

    @PostConstruct
    public void postConstruct() {
        List<MenuNode> menuNodes = new ArrayList<>();
        for (Menu menu : findAll()) {
            menuNodes.add(new MenuNode(menu));
        }
        //所有树节点将缓存到了TOP_MENU_NODE里面
        genTree(menuNodes, MenuNode.TOP_MENU_NODE);
    }

    /**
     * 获取树形菜单
     * @return 树形菜单
     */
    @Override
    public List<MenuNode> treeMenu() {
        return MenuNode.TOP_MENU_NODE.getChildren();
    }

    /**
     * 添加按钮到菜单
     * @param menuId 菜单id
     * @param button 按钮id
     */
    @Override
    public void addButtonToMenu(Integer menuId, Button button) {
        final Menu menu = findById(Objects.requireNonNull(menuId));
        menu.getButtons().add(button);
        update(menu);
    }

    /**
     * 从菜单中删除按钮
     * @param menuId 菜单id
     * @param buttonId 按钮id
     */
    @Override
    public void removeButtonFromMenu(Integer menuId, Integer buttonId) {
        final Menu menu = findById(Objects.requireNonNull(menuId));
        Button button = new Button();
        button.setId(Objects.requireNonNull(buttonId));
        menu.getButtons().remove(button);
        update(menu);
    }

    /**
     * 从菜单中更新按钮
     * @param menuId 菜单id
     * @param button 按钮
     */
    @Override
    public void updateButtonFromMenu(Integer menuId, Button button) {
        Objects.requireNonNull(menuId);
        Objects.requireNonNull(button);
        Menu menu = findById(menuId);
        for (Button menuButton : menu.getButtons()) {
            if (button.equals(menuButton)) {
                menuButton.setPerm(button.getPerm());
            }
        }
    }

    /**
     * 递归生成树
     * @param menuNodes 当前节点
     * @param parentNode 父节点
     */
    private void genTree(List<MenuNode> menuNodes,
                         MenuNode parentNode) {
        for (MenuNode node : menuNodes) {
            if (node.getParentId() == parentNode.getId()) {
                parentNode.getChildren().add(node);
                genTree(menuNodes, node);
            }
        }
    }

}
