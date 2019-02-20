package org.smither.opwatch.server.signs;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NotNull
@NonNull
public final class Sign {

    private String line1;
    private String line2;
    private String line3;
    private String line4;

    private LocalDateTime time;

    @Type(type = "uuid-char")
    private UUID placer;

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Type(type = "uuid-char")
    private UUID server;

    private int x;

    private int y;

    private int z;

    private String world;

    private boolean checked;

}