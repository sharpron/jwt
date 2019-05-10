package pub.ron.jwt.domain;


import javax.persistence.*;
import java.util.Set;

/**
 * 用户实体
 * @author ron
 * 2019.01.03
 */
@Entity
@Table
public class User extends BaseEntity {


    private String password;

    private String salt;

    private String realName;

    private String email;

    private boolean disabled;


    @ManyToMany
    @JoinTable(name="user_role",
            joinColumns={ @JoinColumn(name="user_id") },
            inverseJoinColumns={@JoinColumn(name="role_id")})
    private Set<Role> roles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
