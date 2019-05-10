package pub.ron.jwt.domain;

import javax.persistence.*;

/**
 * 刷新token
 *
 * @author ron
 * 2019.01.17
 */
@Entity
@Table
public class RefreshToken extends BaseEntity {


    @Column
    private boolean mobile;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;


    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
