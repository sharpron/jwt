package pub.ron.jwt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pub.ron.jwt.domain.Menu;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单节点
 *
 * @author ron
 * 2019.05.09
 */
public final class MenuNode {

    private static final int TOP_NODE_ID = -1;

    public static final MenuNode TOP_MENU_NODE = new MenuNode(new Menu());

    private final int id;

    private final int parentId;

    private final String path;

    private final String name;

    private final List<MenuNode> children;

    private final Instant createTime;

    public MenuNode(Menu menu) {
        this.id = menu.getId() == null ? TOP_NODE_ID : menu.getId();
        this.parentId = menu.getParent() == null ? TOP_NODE_ID :
                menu.getParent().getId();
        this.path = menu.getPerm() == null ? null :
                menu.getPerm().getUriPatterns().toString();
        this.name = menu.getName();
        this.children = new ArrayList<>();
        this.createTime = menu.getCreateTime();
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public int getParentId() {
        return parentId;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public static boolean isTopNode(Menu menu) {
        return menu.getId() == TOP_NODE_ID;
    }
}
