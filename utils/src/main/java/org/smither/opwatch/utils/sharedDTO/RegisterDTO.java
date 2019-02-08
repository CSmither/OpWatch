package org.smither.opwatch.utils.sharedDTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    private String username;

    private String displayName;

    private String password;
}
