package org.smither.opwatch.utils.sharedDTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String username;

    private String password;

    private String refresh;

    public boolean isValidRequest() {
        return ((username != null && password != null) || refresh != null) &&
                !((username != null && password != null) && refresh != null);
    }

}