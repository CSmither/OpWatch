package org.smither.opwatch.utils.sharedDTO;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ErrorDTO {
    private String message;
}
