package org.smither.opwatch.utils.sharedDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class SignPostDTO {

    private String line1;
    private String line2;
    private String line3;
    private String line4;

    private LocalDateTime time;

    private UUID placer;

    private UUID id;

    private UUID server;

    private int x;

    private int y;

    private int z;

    private String world;

    private boolean checked;


}