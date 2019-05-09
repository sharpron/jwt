package pub.ron.jwt.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    @JoinTable(name="role_permission",
            joinColumns={ @JoinColumn(name="permission_id") },
            inverseJoinColumns={@JoinColumn(name="role_id")})
    private Set<Permission> permissions;

    public Role() {
    }

    public Role(String name) {
        setName(name);
    }


    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "name", "permissions");
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, "name", "permissions");
    }
}
