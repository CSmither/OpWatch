package org.smither.opwatch.server.users;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority")
    private Set<Authority> authorities = new HashSet<>();

    User(String username) {
        this.username = username;
        this.id = UUID.randomUUID();
        setPassword("");
        setAccountExpiry(LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999));
        setCredentialsExpiry(LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999));
        setDisplayName(username);
        setEnabled(false);
        setLocked(false);
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

    private void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void addAuth(Authority authority) {
        authorities.add(authority);
        authority.getUsers().add(this);
    }

    public void removeAuth(Authority authority) {
        authorities.remove(authority);
        authority.getUsers().remove(this);
    }

    void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    void delAuthority(Authority authority) {
        authorities.remove(authority);
    }
}
