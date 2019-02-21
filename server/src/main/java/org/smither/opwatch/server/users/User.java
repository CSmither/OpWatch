package org.smither.opwatch.server.users;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class User implements UserDetails, Serializable {

    @Id
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private LocalDateTime accountExpiry;

    @NonNull
    private LocalDateTime credentialsExpiry;

    @NonNull
    private String displayName;

    private boolean locked;

    private boolean enabled;

    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority")
    private List<Authority> authorities;

    public User(String username) {
        this.username = username;
        this.id=UUID.randomUUID();
        setPassword("");
        setAccountExpiry(LocalDateTime.of(9999,12,31,23,59,59,999));
        setCredentialsExpiry(LocalDateTime.of(9999,12,31,23,59,59,999));
        setDisplayName(username);
        setEnabled(false);
        setLocked(false);
        setAuthorities(Collections.emptyList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiry.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiry.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }
}
