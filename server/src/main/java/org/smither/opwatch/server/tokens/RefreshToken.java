package org.smither.opwatch.server.tokens;

import lombok.*;
import org.smither.opwatch.server.users.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    private UUID refresh;

    private User user;

    private Date expiry;

    private boolean used;

}
