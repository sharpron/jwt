package pub.ron.jwt.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * 菜单
 * @author ron 
 * 2019.05.09
 */
@Entity
@Table
public class Menu extends BaseEntity {

    /**
     * 关联的权限
     */
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Perm perm;

    /**
     * 父菜单
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private Set<Button> buttons;

    public Perm getPerm() {
        return perm;
    }

    public void setPerm(Perm perm) {
        this.perm = perm;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Set<Button> getButtons() {
        return buttons;
    }

    public void setButtons(Set<Button> buttons) {
        this.buttons = buttons;
    }
}
