package pub.ron.jwt.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
