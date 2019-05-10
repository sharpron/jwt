package pub.ron.jwt.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 按钮类型
 * @author ron
 * 2019.05.09
 */
@Entity
@Table
public class Button extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Perm perm;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    public Perm getPerm() {
        return perm;
    }

    public void setPerm(Perm perm) {
        this.perm = perm;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
