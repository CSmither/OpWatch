package org.smither.opwatch.utils.sharedDTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthDTO {

    @NonNull
    private String authority;
}
