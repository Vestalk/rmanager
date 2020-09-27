package rmanager.commons.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "user_token")
public class UserToken {

    public UserToken(User user) {
        this.user = user;
    }

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "user_id", updatable = false, insertable = false)
    private Integer userId;

    @Id
    @Column(name = "token")
    private String token;

}
