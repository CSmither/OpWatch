package org.smither.opwatch.server.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Authority implements GrantedAuthority {

    @Id
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID id;

    private String authority;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "authorities")
    @Setter
    private List<User> users;

    public Authority(String authority) {
        setAuthority(authority);
        this.users = new ArrayList<>();
        this.id=UUID.randomUUID();
    }

    public void setAuthority(String authority) {
        this.authority = authority.toUpperCase();
    }
}
