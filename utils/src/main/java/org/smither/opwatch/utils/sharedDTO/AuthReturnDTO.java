package org.smither.opwatch.utils.sharedDTO;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class AuthReturnDTO {

    @NonNull
    private UUID id;

    @NonNull
    private String authority;
}
