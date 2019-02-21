package org.smither.opwatch.utils.sharedDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
public class UpdateUserDTO {

    private Optional<String> username;

    private Optional<String> displayName;

    private Optional<String> password;

    private Optional<LocalDateTime> accountExpiry;

    private Optional<LocalDateTime> credentialsExpiry;

    private Optional<Boolean> locked;

    private Optional<Boolean> enabled;

    private Optional<List<UUID>> authority;
}
