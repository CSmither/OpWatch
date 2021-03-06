package org.smither.opwatch.utils.sharedDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class UserReturnDTO {
    private UUID id;
    private String username;
    private LocalDateTime accountExpiry;
    private LocalDateTime credentialsExpiry;
    private String displayName;
    private boolean locked;
    private boolean enabled;
    @Singular(value = "authority")
    private Map<UUID, String> authorities;
}
