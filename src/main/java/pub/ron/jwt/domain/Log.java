package pub.ron.jwt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 日志
 * @author ron
 * 2019.05.09
 */
@Entity
@Table
public class Log extends BaseEntity {


    private String operator;

    private String ip;

    @Column
    private Long spend;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getSpend() {
        return spend;
    }

    public void setSpend(Long spend) {
        this.spend = spend;
    }
}
