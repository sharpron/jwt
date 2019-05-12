package pub.ron.jwt.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色实体
 * @author ron
 * 2019.01.03
 */
@Entity
@Table
public class Role extends BaseEntity {



    @ManyToMany
    @JoinTable(name="role_perm",
            joinColumns={ @JoinColumn(name="role_id") },
            inverseJoinColumns={@JoinColumn(name="perm_id")})
    private Set<Perm> perms;

    /**
     * 是否是系统角色
     */
    private boolean system;

    /**
     * 描述
     */
    @Column(nullable = false)
    private String description;


    public Set<Perm> getPerms() {
        return perms;
    }

    public void setPerms(Set<Perm> perms) {
        this.perms = perms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }
}
