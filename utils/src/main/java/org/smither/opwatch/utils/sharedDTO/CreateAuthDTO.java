package org.smither.opwatch.utils.sharedDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAuthDTO {
    private String authority;
}
