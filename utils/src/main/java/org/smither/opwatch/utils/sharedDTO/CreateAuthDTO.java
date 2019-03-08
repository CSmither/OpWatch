package org.smither.opwatch.utils.sharedDTO;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CreateAuthDTO {
    @NonNull
    private String authority;
}
